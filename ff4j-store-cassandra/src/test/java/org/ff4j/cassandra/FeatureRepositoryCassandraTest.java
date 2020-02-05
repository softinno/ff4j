package org.ff4j.cassandra;

import org.ff4j.cassandra.store.FeatureRepositoryCassandra;
import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.test.features.FeatureRepositoryTestSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Full Fledge {@link FeatureRepository} backed by Cassandra.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class FeatureRepositoryCassandraTest extends FeatureRepositoryTestSupport {

    private static CqlSession        cqlSession;
    private static FeatureRepository featureRepo;
    
    @BeforeAll
    public static void init() {
        cqlSession  = CqlSession.builder().build();
        featureRepo = new FeatureRepositoryCassandra(cqlSession);
    }
    
    /** {@inheritDoc} */
    public FeatureRepository initStore() {
        cqlSession.execute("TRUNCATE TABLE ff4j_feature");
        featureRepo.save(testDataSet.getFeatures().values().stream());
        return featureRepo;
    }
    
    @AfterAll
    public static void closeRepo() {
        cqlSession.close();
    }
    
}
