package org.ff4j.cassandra;

import java.nio.file.Paths;
import java.util.List;

import org.ff4j.cassandra.driver.CassandraDriverMapperBuilder;
import org.ff4j.cassandra.entity.FeatureDao;
import org.ff4j.cassandra.entity.FeatureEntity;
import org.ff4j.cassandra.entity.UdtParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Connectivity Test.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class CassandraConnectivityTest {
    
    @Test
    public void testConnection() {
        try (CqlSession cqlSession = CqlSession.builder()
            .withCloudSecureConnectBundle(Paths.get("/Users/cedricklunven/dev/WORKSPACES/apollo.zip"))
            .withAuthCredentials("KVUser","KVPassword")
            .withKeyspace("killrvideo")
            .build()) {
            Assertions.assertEquals("killrvideo", cqlSession.getKeyspace().get().asInternal());
        }
    }
    
    @Test
    public void testConnectionWithApplicationConf() {
        try (CqlSession cqlSession = CqlSession.builder().build()) {
            Assertions.assertEquals("killrvideo", cqlSession.getKeyspace().get().asInternal());
        }
    }
    
    @Test
    public void testListFeatures() {
        try (CqlSession cqlSession = CqlSession.builder().build()) {
            FeatureDao featureDao = new CassandraDriverMapperBuilder(cqlSession).build()
                    .featureDao(CqlIdentifier.fromCql("killrvideo"));
            List<FeatureEntity> features = featureDao.findAll().all();
            System.out.println(features.size());
            System.out.println(features.get(2).getUid());
            FeatureEntity f1 = features.get(2);
            f1.setDescription("Cedrick Seems to be OK");
            UdtParam ok = new UdtParam();
            ok.setClassName("String");
            ok.setUid("ok");
            ok.setValue("COOL");
            f1.getProperties().put(ok.getUid(), ok);
            featureDao.upsert(f1);
        }
        
    }

}
