package org.ff4j.event.repository.hit;

import static org.ff4j.core.jdbc.JdbcUtils.buildStatement;

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
import static org.ff4j.core.test.AssertUtils.assertHasLength;
import static org.ff4j.core.utils.Util.validateEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.ff4j.core.jdbc.JdbcQueryBuilder;
import org.ff4j.core.jdbc.JdbcSchema;
import org.ff4j.core.jdbc.JdbcUtils;
import org.ff4j.core.test.AssertUtils;
import org.ff4j.event.Event;
import org.ff4j.event.EventSeries;
import org.ff4j.event.HitCount;
import org.ff4j.event.TimeSeries;
import org.ff4j.feature.exception.AuditTrailAccessException;
import org.ff4j.feature.exception.FeatureAccessException;

/**
 * Implementation of in memory {@link FeatureHitRepository} with limited events.
 * 
 * @author Cedrick Lunven (@clunven)
 */
public class FeatureHitRepositoryJdbc extends FeatureHitRepositorySupport implements JdbcSchema {
   
    /** Serial */
    private static final long serialVersionUID = -5636652530816030318L;
    
    /** error message. */
    public static final String CANNOT_BUILD_PIE_CHART_FROM_REPOSITORY = "Cannot build PieChart from repository, ";

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
    public FeatureHitRepositoryJdbc(DataSource jdbcDS) {
        this.dataSource = jdbcDS;
    }
    
    /** {@inheritDoc} */
    @Override
    public void createSchema() {
        DataSource       ds = getDataSource();
        JdbcQueryBuilder qb = getQueryBuilder();
        if (!isTableExist(ds, qb.getTableNameFeatureHit())) {
            executeUpdate(ds, qb.sqlCreateTable(FeatureHitColumns.values()));
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void save(Iterable<Event> entities) {
        if (null != entities) {
            try (Connection sqlConn = dataSource.getConnection()) {
                sqlConn.setAutoCommit(false);
                FeatureHitJdbcMapper mapper = new FeatureHitJdbcMapper(sqlConn, getQueryBuilder());
                for (Event evt : entities) {
                    validateEvent(evt);
                    try (PreparedStatement ps1 = mapper.mapToRepository(evt)) {
                        ps1.executeUpdate();
                    }
                }
                sqlConn.commit();
                sqlConn.setAutoCommit(true);
            } catch (SQLException sqlEX) {
                throw new AuditTrailAccessException("Cannot insert event into audit trail", sqlEX);
            }
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public EventSeries search(FeatureHitQuery query) {
        AssertUtils.assertNotNull(query, "Audit Query cannot be null nor empty");
        EventSeries resEvents = new EventSeries();
        try (Connection sqlConn = dataSource.getConnection()) {
            FeatureHitJdbcMapper mapper = new FeatureHitJdbcMapper(sqlConn, getQueryBuilder());
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
    
    /** {@inheritDoc} */
    @Override
    public boolean exists(String id) {
        AssertUtils.assertHasLength(id, "Event Id should not be null not empty");
        try (Connection sqlConn = dataSource.getConnection()) {
            try (PreparedStatement ps1 = JdbcUtils.buildStatement(sqlConn, getQueryBuilder().sqlExistFeatureHit(), id)) {
                try(ResultSet rs = ps1.executeQuery()) {
                    rs.next();
                    return 1 == rs.getInt(1);
                }
            }
        } catch (SQLException sqlEX) {
            throw new AuditTrailAccessException("Cannot read event: ", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void purge(FeatureHitQuery query) {
        AssertUtils.assertNotNull(query, "Feature Usage Query cannot be null nor empty");
        try (Connection sqlConn = dataSource.getConnection()) {
            FeatureHitJdbcMapper mapper = new FeatureHitJdbcMapper(sqlConn, getQueryBuilder());
            try (PreparedStatement ps1 = mapper.buildPurgeStatement(query)) {
                ps1.executeUpdate();  
            }
        } catch (SQLException sqlEX) {
            throw new AuditTrailAccessException("Cannot purge audit trail based on query :", sqlEX);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, HitCount> getHitCount(FeatureHitQuery query) {
        return computeHitCount(FeatureHitColumns.FEATURE_UID, query);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, HitCount> getHostHitCount(FeatureHitQuery query) {
        return computeHitCount(FeatureHitColumns.HOSTNAME, query);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, HitCount> getUserHitCount(FeatureHitQuery query) {
        return computeHitCount(FeatureHitColumns.OWNER, query);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, HitCount> getSourceHitCount(FeatureHitQuery query) {
        return computeHitCount(FeatureHitColumns.SOURCE, query);
    }
    
    /** {@inheritDoc} */
    public Stream<String> findAllIds() {
        Set<String> setOFIds = new HashSet<String>();
        try (Connection sqlConn = getDataSource().getConnection()) {
            try(PreparedStatement ps1 = sqlConn.prepareStatement(getQueryBuilder().sqlFindAllFeatureHitId())) {
                try (ResultSet rs1 = ps1.executeQuery()) {
                    while (rs1.next()) {
                        setOFIds.add(rs1.getString(FeaturesColumns.UID.colname()));
                    }
                }
            }
            setOFIds.remove(null);
            setOFIds.remove("");
            return setOFIds.stream();
        } catch (SQLException sqlEX) {
            throw new FeatureAccessException("Cannot list groups, error related to database", sqlEX);
        }
    }

    @Override
    /** {@inheritDoc} */
    public Optional<Event> find(String uid) {
        assertHasLength(uid);
        try (Connection sqlConn = getDataSource().getConnection()) {
            FeatureHitJdbcMapper mapper = new FeatureHitJdbcMapper(sqlConn, getQueryBuilder());
            try (PreparedStatement ps1 = sqlConn.prepareStatement(getQueryBuilder().sqlFindFeatureHitById())) {
                ps1.setString(1, uid);
                try (ResultSet rs1 = ps1.executeQuery()) {
                    if (!rs1.next()) {
                        return Optional.empty();
                    } else {
                       return Optional.ofNullable(mapper.mapFromRepository(rs1));
                    }
                }
            }
        } catch (SQLException sqlEX) {
            throw new AuditTrailAccessException("Cannot purge audit trail based on query :", sqlEX);
        }
    }

    @Override
    /** {@inheritDoc} */
    public void delete(Iterable<String> listOfId) {
        if (null != listOfId) {
            try (Connection sqlConn = dataSource.getConnection()) {
                sqlConn.setAutoCommit(false);
                for (String uid : listOfId) {
                    try (PreparedStatement ps = buildStatement(sqlConn, getQueryBuilder().sqlDeleteFeatureHit(), uid)) {
                        ps.executeUpdate();
                    }
                }
                sqlConn.commit();
                sqlConn.setAutoCommit(true);
            } catch (SQLException sqlEX) {
                throw new AuditTrailAccessException("Cannot insert event into audit trail", sqlEX);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public TimeSeries getFeatureUsageHistory(FeatureHitQuery query, TimeUnit units) {
        // Create the interval depending on units
        TimeSeries tsc = new TimeSeries(query.getFrom(), query.getTo(), units);
        // Search All events
        Iterator<Event> iterEvent = search(query).iterator();
        // Dispatch events into time slots
        while (iterEvent.hasNext()) {
            tsc.addEvent(iterEvent.next());
        }
        return tsc;
    }
    
    /** {@inheritDoc} */
    protected  Map<String, HitCount> computeHitCount(FeatureHitColumns columnName, FeatureHitQuery q) {
        Connection          sqlConn = null;
        PreparedStatement   ps = null;
        ResultSet           rs = null;
        Map<String, HitCount>  hitCount = new HashMap<String, HitCount>();
        try {
            // Returns features
            sqlConn = dataSource.getConnection();
            ps = sqlConn.prepareStatement(getQueryBuilder().sqlHitCount(columnName));
            ps.setTimestamp(1, new Timestamp(q.getFrom()));
            ps.setTimestamp(2, new Timestamp(q.getTo()));
            rs = ps.executeQuery();
            while (rs.next()) {
                hitCount.put(rs.getString(columnName.colname()), new HitCount(rs.getInt("NB")));
            } 
        } catch (SQLException sqlEX) {
            throw new FeatureAccessException(CANNOT_BUILD_PIE_CHART_FROM_REPOSITORY, sqlEX);
        }
        return hitCount;
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
