package org.ff4j.cassandra.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.ff4j.cassandra.driver.CassandraSchema;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;

/**
 * Dedicated Queries do not matching {@link FeatureEntity}.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class FeatureDaoQueryProvider implements CassandraSchema {
    
    /** Cassandra connectivity. */ 
    private CqlSession        cqlSession;
    private PreparedStatement psListFeatureNames;
    private PreparedStatement psListGroupNames;
    private PreparedStatement psExistGroup;
    private PreparedStatement psExistFeature;
    private PreparedStatement psUpdateGroup;
    
    /**
     * Constructor invoked by the DataStax driver based on Annotation {@link QueryProvider} 
     * set on class {@link FeatureDao}.
     * 
     * @param context
     *      context to extrat dse session
     * @param helperFeature
     *      entity helper to interact with bean {@link FeatureEntity}
     */
    public FeatureDaoQueryProvider(MapperContext context) {
        this.cqlSession     = context.getSession();
        psListFeatureNames  = cqlSession.prepare(queryFeatureListIds());        
        psListGroupNames    = cqlSession.prepare(queryGroupListNames());
        psExistGroup        = cqlSession.prepare(queryGroupExist());
        psExistFeature      = cqlSession.prepare(queryFeatureExist());
        psUpdateGroup       = cqlSession.prepare(queryUpdateGroup());
    }
    
    public boolean existGroup(String groupName) {
        return cqlSession.execute(psExistGroup.bind(groupName))
                         .getAvailableWithoutFetching() > 0;
    }
    
    public boolean existFeature(String featureName) {
        return cqlSession.execute(psExistFeature.bind(featureName))
                .getAvailableWithoutFetching() > 0;
    }
    
    public List<String> listGroupNames() {
        Set <String> result = cqlSession.execute(psListGroupNames.bind())
                .all().stream()
                .map(row -> row.getString(TABLE_FEATURE_GROUPNAME))
                .collect(Collectors.toSet());
        result.remove(null);
        List<String> groupNames = new ArrayList<String>();
        groupNames.addAll(result);
        Collections.sort(groupNames, Comparator.naturalOrder());
        return groupNames;
    }
    
    public List<String> findAllIds() {
        List<String> featureNames = cqlSession.execute(psListFeatureNames.bind())
                .all().stream()
                .map(row -> row.getString(TABLE_FEATURE_UID))
                .collect(Collectors.toList());
        Collections.sort(featureNames, Comparator.naturalOrder());
        return featureNames;
    }
    
    public void removeFromGroup(String uid, String groupName) {
        cqlSession.execute(psUpdateGroup.bind(null, Instant.now(), uid, groupName));
    }
    

}
