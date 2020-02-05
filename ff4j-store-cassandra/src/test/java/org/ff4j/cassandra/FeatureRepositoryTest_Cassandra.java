package org.ff4j.cassandra;

import java.util.List;
import java.util.stream.Collectors;

import org.ff4j.cassandra.store.FeatureRepositoryCassandra;
import org.ff4j.feature.repository.FeatureRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.datastax.oss.driver.api.core.CqlSession;

public class FeatureRepositoryTest_Cassandra {

    private static CqlSession cqlSession;
    private static FeatureRepository featureRepo;
    
    @BeforeAll
    public static void initRepo() {
        cqlSession  = CqlSession.builder().build();
        featureRepo = new FeatureRepositoryCassandra(cqlSession);
    }
    
    @AfterAll
    public static void closeRepo() {
        cqlSession.close();
    }
    
    @Test
    public void test() {
        List<String> features = featureRepo.findAllIds().collect(Collectors.toList());
        System.out.println(features);
        
    }
}
