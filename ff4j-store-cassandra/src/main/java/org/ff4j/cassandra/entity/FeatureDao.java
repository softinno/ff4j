package org.ff4j.cassandra.entity;

import java.util.List;
import java.util.Optional;

import org.ff4j.cassandra.driver.CassandraSchema;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.Select;

/**
 * Operations on `ff4j_feature` table.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Dao
public interface FeatureDao extends CassandraSchema {
    
    @QueryProvider(providerClass = FeatureDaoQueryProvider.class)
    boolean existGroup(String groupName);
    
    @QueryProvider(providerClass = FeatureDaoQueryProvider.class)
    boolean existFeature(String featureName);
    
    @QueryProvider(providerClass = FeatureDaoQueryProvider.class)
    List<String> listGroupNames();
    
    @QueryProvider(providerClass = FeatureDaoQueryProvider.class)
    List<String> findAllIds();
    
    @Select(customWhereClause = TABLE_FEATURE_GROUPNAME + "= :groupName")
    PagingIterable<FeatureEntity> readGroup(@CqlName("groupName") String groupName);
    
    @Select
    PagingIterable<FeatureEntity> findAll();
    
    @Select
    Optional<FeatureEntity> findById(String uid);
    
    @Insert
    void upsert(FeatureEntity feature);
    
    @Delete(entityClass = FeatureEntity.class)
    void delete(String uid);
    
    @QueryProvider(providerClass=FeatureDaoQueryProvider.class)
    void removeFromGroup(String uid, String groupName);
    
}
