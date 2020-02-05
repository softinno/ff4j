package org.ff4j.cassandra.driver;

import org.ff4j.cassandra.entity.FeatureDao;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * Annotated as {@link Mapper} will generate working {@link Dao}.
 */
@Mapper
public interface CassandraDriverMapper {

    /**
     * Initialization of Dao {@link FeatureDao}
     *
     * @param keyspace
     *      working keyspace name
     * @return
     *      instanciation with the mappers
     */
    @DaoFactory
    FeatureDao featureDao(@DaoKeyspace CqlIdentifier keyspace);
    
}
