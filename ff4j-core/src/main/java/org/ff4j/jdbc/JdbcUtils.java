package org.ff4j.jdbc;

/*-
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2017 FF4J
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

import static org.ff4j.test.AssertUtils.assertHasLength;
import static org.ff4j.test.AssertUtils.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.ff4j.feature.exception.FeatureAccessException;
import org.ff4j.feature.exception.RepositoryAccessException;

/**
 * Group utilities methods to work with low-level JDBC.
 *
 * @author Cedrick Lunven (@clunven)
 */
public class JdbcUtils {
    
    /**
     * Hide default constructor. 
     **/
    private JdbcUtils() {}
    
    /**
     * Check if target Table exist.
     *
     * @param tableName
     *      table to create
     * @return
     *      if the table exist or not
     */
    public static boolean isTableExist(DataSource ds, String tableName) {
        assertNotNull(ds);
        assertHasLength(tableName);
        try (Connection sqlConn = ds.getConnection()) {
            try (ResultSet rs = sqlConn.getMetaData().getTables(null, null, tableName, new String[] {"TABLE"})) {
                return rs.next();
            }
        } catch (SQLException sqlEX) {
            throw new FeatureAccessException("Cannot check table existence", sqlEX);
        }
    }
    
    /**
     * Create table based on SQL.
     *
     * @param sqlQuery
     *      sql query
     */
    public static void executeUpdate(DataSource ds, String sqlQuery) {
        assertNotNull(ds);
        assertHasLength(sqlQuery);
        try (Connection sqlConn = ds.getConnection()) {
            try(Statement stmt = sqlConn.createStatement()) {
                stmt.executeUpdate(sqlQuery);
            }
        } catch (SQLException sqlEX) {
            throw new RepositoryAccessException("Cannot execute SQL " + sqlQuery, sqlEX);
        }
    }
    
    /**
     * Build {@link PreparedStatement} from parameters
     * 
     * @param query
     *            query template
     * @param params
     *            current parameters
     * @return working {@link PreparedStatement}
     * @throws SQLException
     *             sql error when working with statement
     */
    public static PreparedStatement buildStatement(Connection sqlConn, String query, Object... params)
    throws SQLException {
        PreparedStatement ps = sqlConn.prepareStatement(query);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
        return ps;
    }
}
