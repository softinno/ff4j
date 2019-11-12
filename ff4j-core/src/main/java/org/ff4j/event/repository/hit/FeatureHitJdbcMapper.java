package org.ff4j.event.repository.hit;

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
import java.util.Set;

import org.ff4j.core.jdbc.JdbcMapper;
import org.ff4j.core.jdbc.JdbcQueryBuilder;
import org.ff4j.core.utils.TimeUtils;
import org.ff4j.event.Event;
import org.ff4j.event.EventMapper;
import org.ff4j.event.Event.Source;
import org.ff4j.event.exception.AuditAccessException;

/**
 * Map resultset into {@link Event}
 *
 * @author Cedrick Lunven (@clunven)
 */
public class FeatureHitJdbcMapper extends JdbcMapper implements EventMapper < PreparedStatement, ResultSet> {
  
    public FeatureHitJdbcMapper(Connection sqlConn, JdbcQueryBuilder qbd) {
        super(sqlConn, qbd);
    }
    
    public PreparedStatement buildSelectStatement(FeatureHitQuery query) {
        try {
            StringBuilder sqlQuery = new StringBuilder(queryBuilder.sqlSelectAllFeatureHit())
                    .append(" WHERE ")
                    .append(AuditTrailColumns.TIMESTAMP.colname() + " > ? AND ")
                    .append(AuditTrailColumns.TIMESTAMP.colname() + " < ? ");
            if (!query.getFilteredEntityUids().isEmpty()) {
                sqlQuery.append(" AND ")
                        .append(AuditTrailColumns.NAME.colname() + " IN")
                        .append(toStringListWithComma(query.getFilteredEntityUids()));
            }
            if (!query.getFilteredHostNames().isEmpty()) {
                sqlQuery.append(" AND ")
                        .append(AuditTrailColumns.HOSTNAME.colname() + " IN")
                        .append(toStringListWithComma(query.getFilteredHostNames()));
            }
            if (!query.getFilteredSources().isEmpty()) {
                sqlQuery.append(" AND ")
                        .append(AuditTrailColumns.SOURCE.colname() + " IN")
                        .append(toStringListWithComma(query.getFilteredSources()));
            }
            PreparedStatement ps = sqlConn.prepareStatement(sqlQuery.toString());
            ps.setTimestamp(1, new java.sql.Timestamp(query.getFrom()));
            ps.setTimestamp(2, new java.sql.Timestamp(query.getTo()));
            return ps;
        } catch(SQLException sqlEx) {
            throw new AuditAccessException("Cannot purge audit trail", sqlEx);
        }
    }
    
    public PreparedStatement buildPurgeStatement(FeatureHitQuery query) {
        try {
            StringBuilder sqlQuery = new StringBuilder(queryBuilder.sqlDeleteAllFeatureHit())
                    .append(" WHERE ")
                    .append(AuditTrailColumns.TIMESTAMP.colname() + " >? AND ")
                    .append(AuditTrailColumns.TIMESTAMP.colname() + " <? ");
            if (!query.getFilteredEntityUids().isEmpty()) {
                sqlQuery.append(" AND ")
                        .append(AuditTrailColumns.NAME.colname() + " IN")
                        .append(toStringListWithComma(query.getFilteredEntityUids()));
            }
            if (!query.getFilteredHostNames().isEmpty()) {
                sqlQuery.append(" AND ")
                        .append(AuditTrailColumns.HOSTNAME.colname() + " IN")
                        .append(toStringListWithComma(query.getFilteredHostNames()));
            }
            if (!query.getFilteredSources().isEmpty()) {
                sqlQuery.append(" AND ")
                        .append(AuditTrailColumns.SOURCE.colname() + " IN")
                        .append(toStringListWithComma(query.getFilteredSources()));
            }
            PreparedStatement ps = sqlConn.prepareStatement(sqlQuery.toString());
            ps.setTimestamp(1, new java.sql.Timestamp(query.getFrom()));
            ps.setTimestamp(2, new java.sql.Timestamp(query.getTo()));
            return ps;
        } catch(SQLException sqlEx) {
            throw new AuditAccessException("Cannot purge audit trail", sqlEx);
        }
    }
    
    public static String toStringListWithComma(Set<String> items) {
        return new StringBuilder("('").append(String.join("','", items)).append("')").toString();
    }
    
    /** {@inheritDoc} */
    @Override
    public PreparedStatement mapToRepository(Event evt) {
        PreparedStatement stmt = null;
        try {
            stmt = sqlConn.prepareStatement(queryBuilder.sqlInsertFeatureHit());
            populateEntity(stmt, evt);
            stmt.setTimestamp(6, new java.sql.Timestamp(evt.getTimestamp()));
            stmt.setString(7, evt.getRefEntityUid());
            stmt.setString(8, evt.getHostName());
            stmt.setString(9, evt.getSource());
        } catch(SQLException sqlEx) {
            throw new AuditAccessException("Cannot create statement to create event", sqlEx);
        }
        return stmt;
    }

    /**
     * Unmarshall a resultset to Event.
     *
     * @param rs
     *      current line
     * @return
     *      bean populated
     * @throws SQLException
     *      cannot read SQL result
     */
    @Override
    public Event mapFromRepository(ResultSet rs) {
        try {
            Event.Builder eventBuilder = Event.builder();
            // Core FF4jEntity
            eventBuilder.uid(rs.getString(FeatureHitColumns.UID.colname()));
            eventBuilder.owner(rs.getString(FeatureHitColumns.OWNER.colname()));
            eventBuilder.description(rs.getString(FeatureHitColumns.DESCRIPTION.colname()));
            eventBuilder.creationDate(TimeUtils.asLocalDateTime(rs.getTimestamp(FeatureHitColumns.CREATED.colname())));

            // Specialization for featureHit
            eventBuilder.refEntityUid(rs.getString(FeatureHitColumns.FEATURE_UID.colname()));
            eventBuilder.hostName(rs.getString(FeatureHitColumns.HOSTNAME.colname()));
            eventBuilder.source(Source.valueOf(rs.getString(FeatureHitColumns.SOURCE.colname())));
            
            Event evt = eventBuilder.build();
            mapEntity(rs, evt);
            return evt;
        } catch(SQLException sqlEx) {
            throw new AuditAccessException("Cannot map result to Event", sqlEx);
        }
    }
}
