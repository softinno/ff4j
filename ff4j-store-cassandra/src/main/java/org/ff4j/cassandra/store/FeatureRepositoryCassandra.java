package org.ff4j.cassandra.store;

import static org.ff4j.core.test.AssertUtils.assertHasLength;

import java.util.Optional;
import java.util.stream.Stream;

import org.ff4j.cassandra.driver.CassandraDriverMapper;
import org.ff4j.cassandra.driver.CassandraDriverMapperBuilder;
import org.ff4j.cassandra.driver.CassandraSchema;
import org.ff4j.cassandra.entity.FeatureDao;
import org.ff4j.cassandra.entity.FeatureEntity;
import org.ff4j.cassandra.entity.FeatureEntityMapper;
import org.ff4j.feature.Feature;
import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.feature.repository.FeatureRepositorySupport;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Implementation of {@link FeatureRepository} to persist data into Apache Cassandra.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class FeatureRepositoryCassandra extends FeatureRepositorySupport implements CassandraSchema {

    /** Serial. */
    private static final long serialVersionUID = -8831825890084850860L;

    /** Mappers for tables. */
    private static final FeatureEntityMapper MAPPER_FEATURE = new FeatureEntityMapper();
    
    /** CqlSession holding metadata to interact with Cassandra. */
    private FeatureDao featureDao;
   
    public FeatureRepositoryCassandra(CqlSession cqlSession) {
        this(cqlSession, cqlSession.getKeyspace().get().asInternal());
    }
    
    /**
     * Constructor with `CqlSession` and `keyspace` already defined.
     *
     * @param cqlSession
     *      current connection to Cassandra Cluster
     * @param keyspaceName
     *      applicative keyspace where to find ff4j_* tables
     */
    public FeatureRepositoryCassandra(CqlSession cqlSession, String keyspaceName) {
        // Table is required for mapper, creating table if required
        schemaCreateAll(cqlSession, keyspaceName);
        // Use entity mapper from driver
        CassandraDriverMapper mapper = new CassandraDriverMapperBuilder(cqlSession).build();
        featureDao = mapper.featureDao(CqlIdentifier.fromCql(keyspaceName));
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean existGroup(String groupName) {
        assertHasLength(groupName);
        return featureDao.existGroup(groupName);
    }

    /** {@inheritDoc} */
    @Override
    public Stream<Feature> readGroup(String groupName) {
        assertGroupExist(groupName);
        return featureDao.readGroup(groupName)
                         .all().stream().map(MAPPER_FEATURE::mapFromRepository);
    }

    /** {@inheritDoc} */
    @Override
    public Stream<String> listGroupNames() {
        return featureDao.listGroupNames().stream();
    }

    /** {@inheritDoc} */
    @Override
    public Stream<String> findAllIds() {
        return featureDao.findAllIds().stream();
    }
    
    /** {@inheritDoc} */
    @Override
    public void saveFeature(Feature feature) {
        assertFeatureNotNull(feature);
        assertHasLength(feature.getUid());
        FeatureEntity fe = MAPPER_FEATURE.mapToRepository(feature);
        featureDao.upsert(fe);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteFeature(String uid) {
        assertFeatureExist(uid);
        featureDao.delete(uid);
    }

    /** {@inheritDoc} */
    @Override
    public boolean exists(String id) {
        assertHasLength(id);
        return featureDao.existFeature(id);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Feature> find(String id) {
        assertHasLength(id);
        Optional<FeatureEntity> fe = featureDao.findById(id);
        return fe.isPresent() ? 
                Optional.ofNullable(MAPPER_FEATURE.mapFromRepository(fe.get())) : 
                Optional.empty();
    } 
    
    /** {@inheritDoc} */
    @Override
    public void removeFromGroup(String uid, String groupName) {
        assertFeatureExist(uid);
        assertGroupExist(groupName);
        this.notify(l -> l.onRemoveFeatureFromGroup(uid, groupName));
        featureDao.removeFromGroup(uid, groupName);
    }

}
