package org.ff4j.feature.usage.repository;

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

import static org.ff4j.jdbc.JdbcUtils.executeUpdate;
import static org.ff4j.jdbc.JdbcUtils.isTableExist;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.ff4j.event.Event;
import org.ff4j.event.EventQueryDefinition;
import org.ff4j.event.EventSeries;
import org.ff4j.event.HitCount;
import org.ff4j.event.TimeSeries;
import org.ff4j.jdbc.JdbcConstants.FeatureUsageColumns;
import org.ff4j.jdbc.JdbcQueryBuilder;

/**
 * Implementation of in memory {@link FeatureUsageRepository} with limited events.
 * 
 * @author Cedrick Lunven (@clunven)
 */
public class FeatureUsageRepositoryJdbc extends EventRepositorySupport {
   
    /** Serial */
    private static final long serialVersionUID = -5636652530816030318L;

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
    public FeatureUsageRepositoryJdbc(DataSource jdbcDS) {
        this.dataSource = jdbcDS;
    }
    
    /** {@inheritDoc} */
    @Override
    public void createSchema() {
        DataSource       ds = getDataSource();
        JdbcQueryBuilder qb = getQueryBuilder();
        if (!isTableExist(ds, qb.getTableNameFeatureUsage())) {
            executeUpdate(ds, qb.sqlCreateTable(FeatureUsageColumns.values()));
        }
    }
    
    @Override
    /** {@inheritDoc} */
    public EventSeries search(EventQueryDefinition query) {
        notImplementedYet();
        return null;
    }

    @Override
    /** {@inheritDoc} */
    public void purge(EventQueryDefinition query) {
        notImplementedYet();
    }

    @Override
    /** {@inheritDoc} */
    public Map<String, HitCount> getHitCount(EventQueryDefinition query) {
        notImplementedYet();
        return null;
    }

    @Override
    /** {@inheritDoc} */
    public Map<String, HitCount> getHostHitCount(EventQueryDefinition query) {
        notImplementedYet();
        return null;
    }

    @Override
    /** {@inheritDoc} */
    public Map<String, HitCount> getUserHitCount(EventQueryDefinition query) {
        notImplementedYet();
        return null;
    }

    @Override
    /** {@inheritDoc} */
    public Map<String, HitCount> getSourceHitCount(EventQueryDefinition query) {
        notImplementedYet();
        return null;
    }

    @Override
    /** {@inheritDoc} */
    public TimeSeries getFeatureUsageHistory(EventQueryDefinition query, TimeUnit units) {
        notImplementedYet();
        return null;
    }

    @Override
    /** {@inheritDoc} */
    public boolean exists(String id) {
        notImplementedYet();
        return false;
    }

    @Override
    /** {@inheritDoc} */
    public void save(Iterable<Event> entities) {
        notImplementedYet();
    }

    @Override
    /** {@inheritDoc} */
    public Stream<String> findAllIds() {
        notImplementedYet();
        return null;
    }

    @Override
    /** {@inheritDoc} */
    public Optional<Event> find(String id) {
        notImplementedYet();
        return null;
    }

    @Override
    /** {@inheritDoc} */
    public void delete(Iterable<String> entities) {
        notImplementedYet();
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
