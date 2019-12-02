package org.ff4j.user.repository;

import static org.ff4j.core.jdbc.JdbcUtils.buildStatement;
import static org.ff4j.core.jdbc.JdbcUtils.executeUpdate;
import static org.ff4j.core.jdbc.JdbcUtils.isTableExist;
import static org.ff4j.core.test.AssertUtils.assertHasLength;
import static org.ff4j.core.test.AssertUtils.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/*-
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2019 FF4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.ff4j.core.jdbc.JdbcQueryBuilder;
import org.ff4j.core.jdbc.JdbcSchema;
import org.ff4j.core.jdbc.JdbcUtils;
import org.ff4j.core.security.FF4jPermission;
import org.ff4j.user.FF4jRole;
import org.ff4j.user.FF4jUser;
import org.ff4j.user.exception.RoleNotFoundException;
import org.ff4j.user.exception.RolesAndUserException;
import org.ff4j.user.exception.UserNotFoundException;
import org.ff4j.user.mapper.RoleMapperJdbc;
import org.ff4j.user.mapper.UserMapperJdbc;

public class RolesAndUsersRepositoryJdbc extends RolesAndUsersRepositorySupport implements JdbcSchema {

    /** Serial. */
    private static final long serialVersionUID = -4023058752112026854L;

    /** Access to storage. */
    private DataSource dataSource;

    /** Query builder. */
    private JdbcQueryBuilder queryBuilder;

    /** Default Constructor. */
    public RolesAndUsersRepositoryJdbc() {}
    
    /** {@inheritDoc} */
    @Override
    public void createSchema() {
        DataSource       ds = getDataSource();
        JdbcQueryBuilder qb = getQueryBuilder();
        if (!isTableExist(ds, qb.getTableNameUser())) {
            executeUpdate(ds, qb.sqlCreateTableUser());
        }
        if (!isTableExist(ds, qb.getTableNameUserPermissions())) {
            executeUpdate(ds, qb.sqlCreateTableUserPermissions());
        }
        if (!isTableExist(ds, qb.getTableNameRoles())) {
            executeUpdate(ds, qb.sqlCreateTableRoles());
        }
        if (!isTableExist(ds, qb.getTableNameRolesPermissions())) {
            executeUpdate(ds, qb.sqlCreateTableRolesPermissions());
        }
        if (!isTableExist(ds, qb.getTableNameRolesUsers())) {
            executeUpdate(ds, qb.sqlCreateTableRolesUsers());
        }
    }
    
    /**
     * Constructor from DataSource.
     *
     * @param jdbcDS
     *            native jdbc datasource
     */
    public RolesAndUsersRepositoryJdbc(DataSource jdbcDS) {
        this.dataSource = jdbcDS;
    }
    
    /** {@inheritDoc} */
    @Override
    public long countRoles() {
        try (Connection sqlConn = getDataSource().getConnection()) {
            try (PreparedStatement ps = JdbcUtils.buildStatement(sqlConn, getQueryBuilder().sqlCountRoles())) {
                try (ResultSet rs = ps.executeQuery()) {
                    //Query count always have return
                    rs.next();
                    return rs.getInt(1);
                }
            }
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot count roles", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void deleteRole(String roleName) {
        if (!existsRole(roleName)) {
            throw new RoleNotFoundException("Cannot delete role '" + roleName + "' as it does not exists");
        }
        try (Connection sqlConn = getDataSource().getConnection()) {
            sqlConn.setAutoCommit(false);
            deleteRole(sqlConn, roleName);
            sqlConn.commit();
            sqlConn.setAutoCommit(true);
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot delete role in database, SQL ERROR", sqlEX);
        }
    }
    
    protected void deleteRole(Connection sqlConn, String roleName) throws SQLException {
        // Delete Roles Permissions
        try (PreparedStatement ps = buildStatement(sqlConn, getQueryBuilder().sqlDeleteAllPermissionsForRole(), roleName)) {
            ps.executeUpdate();
        }
        // Delete Roles <-> User associations
        try (PreparedStatement ps = buildStatement(sqlConn, getQueryBuilder().sqlDeleteRoleInRoleUsers(), roleName)) {
            ps.executeUpdate();
        }
        // Delete Roles
        try (PreparedStatement ps = buildStatement(sqlConn, getQueryBuilder().sqlDeleteRole(), roleName)) {
            ps.executeUpdate();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void deleteAllRoles() {
        try (Connection   sqlConn = getDataSource().getConnection()) {
            sqlConn.setAutoCommit(false);
            // Remove all item in Role/User association (due to foreign key)
            try (PreparedStatement ps = sqlConn.prepareStatement(getQueryBuilder().sqlDeleteAllRolesUsers())) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = sqlConn.prepareStatement(getQueryBuilder().sqlDeleteAllRolePermissions())) {
                ps.executeUpdate();
            }
            // Remove all roles
            try(PreparedStatement ps = buildStatement(sqlConn, getQueryBuilder().sqlDeleteAllRoles())) {
                ps.executeUpdate();
            }
            // Commit
            sqlConn.commit();
            sqlConn.setAutoCommit(true);
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot clear roles table, SQL ERROR", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsRole(String roleName) {
        assertHasLength(roleName);
        try (Connection sqlConn = getDataSource().getConnection()) {
            try(PreparedStatement ps1 = sqlConn.prepareStatement(getQueryBuilder().sqlExistRole())) {
                ps1.setString(1, roleName);
                try (ResultSet rs1 = ps1.executeQuery()) {
                    rs1.next();
                    return 1 == rs1.getInt(1);
                }
            }
        } catch (SQLException sqlEX) {
           throw new RolesAndUserException("Cannot check role existence, error related to database", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Optional<FF4jRole> findRole(String roleName) {
        assertHasLength(roleName);
        try (Connection sqlConn = getDataSource().getConnection()) {
            RoleMapperJdbc roleMapper = new RoleMapperJdbc(sqlConn, getQueryBuilder());
            try(PreparedStatement ps1 = sqlConn.prepareStatement(getQueryBuilder().sqlSelectRoleByName())) {
                ps1.setString(1, roleName);
                try (ResultSet rs1 = ps1.executeQuery()) {
                    Optional<FF4jRole> res = Optional.empty();
                    if (rs1.next()) {
                        FF4jRole myRole = roleMapper.mapFromRepository(rs1);
                        populateRolePermissions(sqlConn, myRole);
                        res = Optional.of(myRole);
                    }
                    return res;
                }
            }
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot check property existence, error related to database", sqlEX);
        }
    }
    
    private void populateRolePermissions(Connection sqlConn, FF4jRole myRole) throws SQLException {
        try(PreparedStatement ps2 = sqlConn.prepareStatement(getQueryBuilder().sqlSelectPermissionsOfRole())) {
            ps2.setString(1, myRole.getUid());
            try (ResultSet rs2 = ps2.executeQuery()) {
                while(rs2.next()) {
                    myRole.getPermissions().add(
                            FF4jPermission.valueOf(rs2.getString(
                                    RolesPermissionColumns.PERMISSION.colname())));
                }
            }
        }
    }
    
    private void populateUserPermissions(Connection sqlConn, FF4jUser myUser) throws SQLException {
        try(PreparedStatement ps2 = sqlConn.prepareStatement(getQueryBuilder().sqlSelectPermissionsOfUser())) {
            ps2.setString(1, myUser.getUid());
            try (ResultSet rs2 = ps2.executeQuery()) {
                while(rs2.next()) {
                    myUser.getPermissions().add(
                            FF4jPermission.valueOf(rs2.getString(
                                    RolesPermissionColumns.PERMISSION.colname())));
                }
            }
        }
    }
    
    private void populateUserRoles(Connection sqlConn, FF4jUser myUser) throws SQLException {
        try(PreparedStatement ps2 = sqlConn.prepareStatement(getQueryBuilder().sqlSelectAllRolesForUser())) {
            ps2.setString(1, myUser.getUid());
            try (ResultSet rs2 = ps2.executeQuery()) {
                while(rs2.next()) {
                    myUser.getRoles().add(rs2.getString(UsersRolesColumns.REF_ROLE.colname()));
                }
            }
        }
    }
   
    /** {@inheritDoc} */
    @Override
    public Stream<FF4jRole> findAllRoles() {
        Collection <FF4jRole> roles = new ArrayList<>();
        try (Connection sqlConn = getDataSource().getConnection()) {
            RoleMapperJdbc roleMapper = new RoleMapperJdbc(sqlConn, getQueryBuilder());
            try(PreparedStatement ps1 = sqlConn.prepareStatement(getQueryBuilder().sqlSelectAllRoles())) {
                try (ResultSet rs1 = ps1.executeQuery()) {
                    while (rs1.next()) {
                        FF4jRole myRole = roleMapper.mapFromRepository(rs1);
                        populateRolePermissions(sqlConn, myRole);
                        roles.add(myRole);
                    }
                }
            }
            return roles.stream();
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot check property existence, error related to database", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void saveRole(FF4jRole entity) {
        assertNotNull(entity);
        Optional<FF4jRole> existingRole = findRole(entity.getUid());
        try (Connection sqlConn = getDataSource().getConnection()) {
            sqlConn.setAutoCommit(false);
            // May failed due to foreign key but expected
            if (existingRole.isPresent()) {
                // Do not commit this delete until everything is performed but cancel and replace
                deleteRole(sqlConn, entity.getUid());
                entity.setCreationDate(existingRole.get().getCreationDate().get());
            }
            entity.setLastModified(LocalDateTime.now());
            
            // Saving Role
            RoleMapperJdbc pmapper = new RoleMapperJdbc(sqlConn, getQueryBuilder());
            try(PreparedStatement ps1 = pmapper.mapToRepository(entity)) {
                ps1.executeUpdate();
            }
            // Saving Role Permissions
            if (null != entity.getPermissions() && !entity.getPermissions().isEmpty()) {
                for (FF4jPermission permission : entity.getPermissions()) {
                    try(PreparedStatement ps2 = pmapper.inserRolePermissions(entity.getUid(), permission.name())) {
                        ps2.executeUpdate();
                    }
                }
            }
            sqlConn.commit();
            sqlConn.setAutoCommit(true);
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot update role in database, SQL ERROR", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean exists(String userId) {
        assertHasLength(userId);
        try (Connection sqlConn = getDataSource().getConnection()) {
            try(PreparedStatement ps1 = sqlConn.prepareStatement(getQueryBuilder().sqlExistUser())) {
                ps1.setString(1, userId);
                try (ResultSet rs1 = ps1.executeQuery()) {
                    rs1.next();
                    return 1 == rs1.getInt(1);
                }
            }
        } catch (SQLException sqlEX) {
           throw new RolesAndUserException("Cannot check use existence, error related to database", sqlEX);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Optional<FF4jUser> find(String userId) {
        assertHasLength(userId);
        Optional<FF4jUser> res = Optional.empty();
        try (Connection sqlConn = getDataSource().getConnection()) {
            UserMapperJdbc userMapper = new UserMapperJdbc(sqlConn, getQueryBuilder());
            try(PreparedStatement ps1 = sqlConn.prepareStatement(getQueryBuilder().sqlSelectUserById())) {
                ps1.setString(1, userId);
                try (ResultSet rs1 = ps1.executeQuery()) {
                    if (rs1.next()) {
                        FF4jUser myUser = userMapper.mapFromRepository(rs1);
                        populateUserPermissions(sqlConn, myUser);
                        populateUserRoles(sqlConn, myUser);
                        res = Optional.of(myUser);
                    }
                }
            }
            return res;
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot check userO existence, error related to database", sqlEX);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Stream<FF4jUser> findAll() {
        Collection <FF4jUser> users = new ArrayList<>();
        try (Connection sqlConn = getDataSource().getConnection()) {
            UserMapperJdbc userMapper = new UserMapperJdbc(sqlConn, getQueryBuilder());
            try(PreparedStatement ps1 = sqlConn.prepareStatement(getQueryBuilder().sqlSelectAllUsers())) {
                try (ResultSet rs1 = ps1.executeQuery()) {
                    while (rs1.next()) {
                        FF4jUser myUser = userMapper.mapFromRepository(rs1);
                        populateUserPermissions(sqlConn, myUser);
                        populateUserRoles(sqlConn, myUser);
                        users.add(myUser);
                    }
                }
            }
            return users.stream();
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot check property existence, error related to database", sqlEX);
        }
    }
   
    /** {@inheritDoc} */
    @Override
    protected void saveUser(FF4jUser user) {
        assertNotNull(user);
        Optional<FF4jUser> existingUser = find(user.getUid());
        try (Connection sqlConn = getDataSource().getConnection()) {
            sqlConn.setAutoCommit(false);
            if (existingUser.isPresent()) {
                // Do not commit this delete until everything is performed but cancel and replace
                deleteUser(sqlConn, user.getUid());
                user.setCreationDate(existingUser.get().getCreationDate().get());
            }
            user.setLastModified(LocalDateTime.now());
            
            // Saving Core User
            UserMapperJdbc pmapper = new UserMapperJdbc(sqlConn, getQueryBuilder());
            try(PreparedStatement ps1 = pmapper.mapToRepository(user)) {
                ps1.executeUpdate();
            }
            
            // Saving Role Permissions
            if (null != user.getPermissions() && !user.getPermissions().isEmpty()) {
                for (FF4jPermission permission : user.getPermissions()) {
                    try(PreparedStatement ps2 = pmapper.inserUserPermission(user.getUid(), permission.name())) {
                        ps2.executeUpdate();
                    }
                }
            }
            // Saving User/Role Association
            if (null != user.getRoles() && !user.getRoles().isEmpty()) {
                // Here we do not Create ROLE, we expect them to be there already
                for (String roleName : user.getRoles()) {
                    try(PreparedStatement ps2 = pmapper.inserUserRole(user.getUid(), roleName)) {
                        ps2.executeUpdate();
                    }
                }
            }
            
            sqlConn.commit();
            sqlConn.setAutoCommit(true);
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot update user in database, SQL ERROR", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void deleteUser(String userId) {
        if (!exists(userId)) {
            throw new UserNotFoundException("Cannot delete user '" + userId + "' as it does not exists");
        }
        try (Connection sqlConn = getDataSource().getConnection()) {
            sqlConn.setAutoCommit(false);
            deleteUser(sqlConn, userId);
            sqlConn.commit();
            sqlConn.setAutoCommit(true);
        } catch (SQLException sqlEX) {
            throw new RolesAndUserException("Cannot delete user in database, SQL ERROR", sqlEX);
        }
    }  
    
    protected void deleteUser(Connection sqlConn, String userName) throws SQLException {
        // Delete Roles Permissions
        try (PreparedStatement ps = buildStatement(sqlConn, getQueryBuilder().sqlDeleteAllPermissionsForUser(), userName)) {
            ps.executeUpdate();
        }
        // Delete Roles <-> User associations
        try (PreparedStatement ps = buildStatement(sqlConn, getQueryBuilder().sqlDeleteUserInRoleUsers(), userName)) {
            ps.executeUpdate();
        }
        // Delete User
        try (PreparedStatement ps = buildStatement(sqlConn, getQueryBuilder().sqlDeleteUser(), userName)) {
            ps.executeUpdate();
        }
    }
    
    /**
     * Getter accessor for attribute 'dataSource'.
     *
     * @return current value of 'dataSource'
     */
    public DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized");
        }
        return dataSource;
    }

    /**
     * Setter accessor for attribute 'dataSource'.
     *
     * @param dataSource
     *            new value for 'dataSource '
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the queryBuilder
     */
    public JdbcQueryBuilder getQueryBuilder() {
        if (queryBuilder == null) {
            queryBuilder = new JdbcQueryBuilder();
        }
        return queryBuilder;
    }

    /**
     * @param queryBuilder the queryBuilder to set
     */
    public void setQueryBuilder(JdbcQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }
}
