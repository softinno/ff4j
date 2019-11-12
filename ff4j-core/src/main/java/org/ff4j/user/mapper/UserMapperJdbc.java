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
import org.ff4j.user.FF4jUser;
import org.ff4j.user.exception.RolesAndUserException;

/**
 * Map resultset into {@link Feature}
 *
 * @author Cedrick Lunven (@clunven)
 */
public class UserMapperJdbc extends JdbcMapper implements UserMapper< PreparedStatement, ResultSet > {
    
    /**
     * Constructor with parameters.
     *
     * @param sqlConn
     *      connection sql
     * @param qbd
     *      query builder
     */
    public UserMapperJdbc(Connection sqlConn, JdbcQueryBuilder qbd) {
        super(sqlConn, qbd);
    }
    
    public PreparedStatement inserUserPermission(String userName, String permissionName) {
        PreparedStatement stmt;
        try {
            stmt = sqlConn.prepareStatement(queryBuilder.sqlInsertUserPermission());
            stmt.setString(1, userName);
            stmt.setString(2, permissionName);
            return stmt;
        } catch (SQLException sqlEx) {
            throw new RolesAndUserException("Cannot create statement to create user permission", sqlEx);
        }
    }
    
    public PreparedStatement inserUserRole(String userName, String roleName) {
        PreparedStatement stmt;
        try {
            stmt = sqlConn.prepareStatement(queryBuilder.sqlInsertRolesUser());
            stmt.setString(1, userName);
            stmt.setString(2, roleName);
            return stmt;
        } catch (SQLException sqlEx) {
            throw new RolesAndUserException("Cannot create statement to create user permission", sqlEx);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public PreparedStatement mapToRepository(FF4jUser role) {
        PreparedStatement ps;
        try {
            ps = sqlConn.prepareStatement(queryBuilder.sqlInsertUser());
            populateEntity(ps, role);
        } catch (SQLException sqlEx) {
            throw new RolesAndUserException("Cannot create statement to create role", sqlEx);
        }
        return ps;
    }

    /** {@inheritDoc} */
    @Override
    public FF4jUser mapFromRepository(ResultSet rs) {
        try {
            FF4jUser myUser = new FF4jUser(rs.getString(UsersColumns.UID.colname()));
            mapEntity(rs, myUser);
            return myUser;
       } catch(SQLException sqlEx) {
           throw new FeatureAccessException("Cannot create statement to create role", sqlEx);
       }
    }

}
