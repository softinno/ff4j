package org.ff4j.user.repository;

import static org.ff4j.jdbc.JdbcUtils.executeUpdate;
import static org.ff4j.jdbc.JdbcUtils.isTableExist;

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

import org.ff4j.jdbc.JdbcQueryBuilder;
import org.ff4j.user.FF4jRole;
import org.ff4j.user.FF4jUser;

public class RolesAndUsersRepositoryJdbc extends RolesAndUsersRepositorySupport {

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
    
    @Override
    public long countRoles() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deleteRole(String roleName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteRoles(Iterable<FF4jRole> roles) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteRole(FF4jRole entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteAllRoles() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean existsRole(String roleName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<FF4jRole> findRole(String roleName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stream<FF4jRole> findRoles(Iterable<String> roleNames) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stream<FF4jRole> findAllRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stream<String> listRoleNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FF4jRole readRole(String roleName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createRole(FF4jRole entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateRole(FF4jRole entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveRoles(Collection<FF4jRole> entities) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isRoleEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean exists(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void save(Iterable<FF4jUser> entities) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Stream<String> findAllIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<FF4jUser> find(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Iterable<String> entities) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void saveUser(FF4jUser user) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void deleteUser(String userId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void deleteAllUsers() {
        // TODO Auto-generated method stub
        
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
