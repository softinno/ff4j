package org.ff4j.core.jdbc;

import static org.ff4j.core.utils.TimeUtils.asLocalDateTime;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ff4j.core.FF4jEntity;
import org.ff4j.core.utils.TimeUtils;

/**
 * Mapper for JDBC object.
 *
 * @author Cedrick LUNVEN  (@clunven)
 */
public abstract class JdbcMapper implements JdbcSchema {
    
    /** sql Connection. */
    protected Connection sqlConn = null;
    
    /** helper to access JDBC queries and constants. */
    protected JdbcQueryBuilder queryBuilder = null;
    
    /**
     * Constructor with connection.
     *
     * @param sqlConn
     *      sql conenction
     * @param qbd
     *      sql query builder
     */
    public JdbcMapper(Connection sqlConn, JdbcQueryBuilder qbd) {
        this.sqlConn      = sqlConn;
        this.queryBuilder = qbd;
    }
    
    public void mapEntity(ResultSet rs, FF4jEntity<?> e) {
        try {
            e.setCreationDate(asLocalDateTime(rs.getTimestamp(JdbcSchema.COLUMN_CREATED)));
            e.setLastModified(asLocalDateTime(rs.getTimestamp(JdbcSchema.COLUMN_LASTMODIFIED)));
            e.setOwner(rs.getString(JdbcSchema.COLUMN_OWNER));
            e.setDescription(rs.getString(JdbcSchema.COLUMN_DESCRIPTION));
        } catch (SQLException sqlEx) {
            throw new IllegalArgumentException("Cannot map entity ", sqlEx);
        }
    }
    
    protected void populateEntity(PreparedStatement stmt, FF4jEntity<?> ent) {
        try {
            // Feature uid
            stmt.setString(1, ent.getUid());
            // Creation Date
            stmt.setTimestamp(2, TimeUtils.asSqlTimeStamp(ent.getCreationDate().get()));
            // Last Modified Date
            stmt.setTimestamp(3, TimeUtils.asSqlTimeStamp(ent.getLastModifiedDate().get()));
            // Owner
            stmt.setString(4, ent.getOwner().orElse(null));
            // Description
            stmt.setString(5, ent.getDescription().orElse(null));
        } catch (SQLException sqlEx) {
            throw new IllegalArgumentException("Cannot populate entity ", sqlEx);
        }
    }

}
