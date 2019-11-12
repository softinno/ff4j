package org.ff4j.user.mapper;

/*
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2016 FF4J
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ff4j.core.jdbc.JdbcMapper;
import org.ff4j.core.jdbc.JdbcQueryBuilder;
import org.ff4j.feature.Feature;
import org.ff4j.feature.exception.FeatureAccessException;
import org.ff4j.user.FF4jRole;
import org.ff4j.user.exception.RolesAndUserException;

/**
 * Map resultset into {@link Feature}
 *
 * @author Cedrick Lunven (@clunven)
 */
public class RoleMapperJdbc extends JdbcMapper implements RoleMapper< PreparedStatement, ResultSet > {
    
    /**
     * Constructor with parameters.
     *
     * @param sqlConn
     *      connection sql
     * @param qbd
     *      query builder
     */
    public RoleMapperJdbc(Connection sqlConn, JdbcQueryBuilder qbd) {
        super(sqlConn, qbd);
    }
    
    public PreparedStatement inserRolePermissions(String roleName, String permissionName) {
        PreparedStatement stmt;
        try {
            stmt = sqlConn.prepareStatement(queryBuilder.sqlInsertRolePermission());
            stmt.setString(1, roleName);
            stmt.setString(2, permissionName);
            return stmt;
        } catch (SQLException sqlEx) {
            throw new RolesAndUserException("Cannot create statement to create role", sqlEx);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public PreparedStatement mapToRepository(FF4jRole role) {
        PreparedStatement ps;
        try {
            ps = sqlConn.prepareStatement(queryBuilder.sqlInsertRole());
            populateEntity(ps, role);
        } catch (SQLException sqlEx) {
            throw new RolesAndUserException("Cannot create statement to create role", sqlEx);
        }
        return ps;
    }

    /** {@inheritDoc} */
    @Override
    public FF4jRole mapFromRepository(ResultSet rs) {
        try {
            FF4jRole myRole = new FF4jRole(rs.getString(RolesColumns.UID.colname()));
            mapEntity(rs, myRole);
            
            return myRole;
       } catch(SQLException sqlEx) {
           throw new FeatureAccessException("Cannot create statement to create role", sqlEx);
       }
    }

}
