package org.ff4j.event.repository.audit;

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

import static org.ff4j.core.jdbc.JdbcUtils.executeUpdate;
import static org.ff4j.core.jdbc.JdbcUtils.isTableExist;
import static org.ff4j.core.utils.Util.validateEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.ff4j.core.jdbc.JdbcQueryBuilder;
import org.ff4j.core.jdbc.JdbcSchema;
import org.ff4j.core.test.AssertUtils;
import org.ff4j.event.Event;
import org.ff4j.event.EventSeries;
import org.ff4j.event.repository.hit.FeatureHitRepository;
import org.ff4j.feature.exception.AuditTrailAccessException;

/**
 * Implementation of in memory {@link FeatureHitRepository} with limited events.
 * 
 * @author Cedrick Lunven (@clunven)
 */
public class AuditTrailRepositoryJdbc implements AuditTrailRepository, JdbcSchema {
    
    /** Error message 1. */
    public static final String CANNOT_READ_AUDITTABLE =  "Cannot read audit table from DB";
 
    /** Access to storage. */
    private DataSource dataSource;
    
    /** Query builder. */
    private JdbcQueryBuilder queryBuilder;
    
    /**
     * Constructor from DataSource.
     * 
     * @param jdbcDS
     *            native jdbc datasource
     */
    public AuditTrailRepositoryJdbc(DataSource jdbcDS) {
        this.dataSource = jdbcDS;
    }
    
    @Override
    /** {@inheritDoc} */
    public void createSchema() {
        DataSource       ds = getDataSource();
        JdbcQueryBuilder qb = getQueryBuilder();
        if (!isTableExist(ds, qb.getTableNameAuditTrail())) {
            executeUpdate(ds, qb.sqlCreateTable(AuditTrailColumns.values()));
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void log(Event evt) {
        validateEvent(evt);
        try (Connection sqlConn = dataSource.getConnection()) {
            JdbcEventAuditTrailMapper mapper = new JdbcEventAuditTrailMapper(sqlConn, getQueryBuilder());
            try (PreparedStatement ps1 = mapper.mapToRepository(evt)) {
                ps1.executeUpdate();
            }
        } catch (SQLException sqlEX) {
            throw new AuditTrailAccessException("Cannot insert event into audit trail", sqlEX);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void purge(AuditTrailQuery query) {
        AssertUtils.assertNotNull(query, "Audit Query cannot be null nor empty");
        try (Connection sqlConn = dataSource.getConnection()) {
            JdbcEventAuditTrailMapper mapper = new JdbcEventAuditTrailMapper(sqlConn, getQueryBuilder());
            try (PreparedStatement ps1 = mapper.buildPurgeStatement(query)) {
                ps1.executeUpdate();  
            }
        } catch (SQLException sqlEX) {
            throw new AuditTrailAccessException("Cannot purge audit trail based on query :", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    public EventSeries search(AuditTrailQuery query) {
        AssertUtils.assertNotNull(query, "Audit Query cannot be null nor empty");
        EventSeries resEvents = new EventSeries();
        try (Connection sqlConn = dataSource.getConnection()) {
            JdbcEventAuditTrailMapper mapper = new JdbcEventAuditTrailMapper(sqlConn, getQueryBuilder());
            try (PreparedStatement ps1 = mapper.buildSelectStatement(query)) {
                try(ResultSet rs = ps1.executeQuery()) {
                    while (rs.next()) {
                        resEvents.add(mapper.mapFromRepository(rs));
                    }
                }
            }
        } catch (SQLException sqlEX) {
            throw new AuditTrailAccessException("Cannot purge audit trail based on query :", sqlEX);
        }
        return resEvents;
    }
    
    /**
     * Getter accessor for attribute 'dataSource'.
     *
     * @return current value of 'dataSource'
     */
    public DataSource getDataSource() {
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
