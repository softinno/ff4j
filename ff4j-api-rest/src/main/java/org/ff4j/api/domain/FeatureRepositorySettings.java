package org.ff4j.api.domain;

/*-
 * #%L
 * ff4j-api-rest
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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.ff4j.feature.repository.FeatureRepository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Meta Information for Feature
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@ApiModel( value = "FeatureRepositorySettings", description = "Describe informations related to Features" )
public class FeatureRepositorySettings {
    
    /** Implementation class for feature repository. */
    @JsonProperty("className")
    @ApiModelProperty( value = "Classname of implementation", required = true )
    private String className;
    
    /** numberOfFeatures. */
    @JsonProperty("count")
    @ApiModelProperty(value = "Count for features", required = true )
    private int count = 0;
    
    /** numberOfGroups. */
    @JsonProperty("numberOfGroups")
    @ApiModelProperty( value = "Count for group", required = true )
    private int groupCount = 0;

    /** features. */
    @JsonProperty("featureNames")
    @ApiModelProperty( value = "List of feature names", required = true )
    private Set < String > featureNames = new HashSet<>();
    
    /** groups. */
    @JsonProperty("groupNames")
    @ApiModelProperty( value = "List of group names", required = true )
    private Set < String > groupNames = new HashSet<>();
    
    /** cached. */
    @JsonProperty("cache")
    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty( value = "if a cachestore is defined", required = true )
    private CacheProxyFeaturesDefinition cache = null;
    
    /**
     * Default constructor.
     */
    public FeatureRepositorySettings() {}
            
    /**
     * Constructor from its feature store.
     *
     * @param featureStore
     *      cuurent fature store
     */
    public FeatureRepositorySettings(FeatureRepository repo) {
        this.className    = repo.getClass().getName();
        this.featureNames = repo.findAllIds().collect(Collectors.toSet());
        this.groupNames   = repo.listGroupNames().collect(Collectors.toSet());
        this.count        = featureNames.size();
        this.groupCount   = groupNames.size();
        CacheProxyFeaturesDefinition tmpCache = new CacheProxyFeaturesDefinition(repo);
        if (tmpCache.isFeatureRepositoryCached()) {
            this.cache = tmpCache;
        }
    }

    /**
     * Getter accessor for attribute 'className'.
     *
     * @return
     *       current value of 'className'
     */
    public String getClassName() {
        return className;
    }

    /**
     * Setter accessor for attribute 'className'.
     * @param className
     * 		new value for 'className '
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Getter accessor for attribute 'count'.
     *
     * @return
     *       current value of 'count'
     */
    public int getCount() {
        return count;
    }

    /**
     * Setter accessor for attribute 'count'.
     * @param count
     * 		new value for 'count '
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Getter accessor for attribute 'groupCount'.
     *
     * @return
     *       current value of 'groupCount'
     */
    public int getGroupCount() {
        return groupCount;
    }

    /**
     * Setter accessor for attribute 'groupCount'.
     * @param groupCount
     * 		new value for 'groupCount '
     */
    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    /**
     * Getter accessor for attribute 'featureNames'.
     *
     * @return
     *       current value of 'featureNames'
     */
    public Set<String> getFeatureNames() {
        return featureNames;
    }

    /**
     * Setter accessor for attribute 'featureNames'.
     * @param featureNames
     * 		new value for 'featureNames '
     */
    public void setFeatureNames(Set<String> featureNames) {
        this.featureNames = featureNames;
    }

    /**
     * Getter accessor for attribute 'groupNames'.
     *
     * @return
     *       current value of 'groupNames'
     */
    public Set<String> getGroupNames() {
        return groupNames;
    }

    /**
     * Setter accessor for attribute 'groupNames'.
     * @param groupNames
     * 		new value for 'groupNames '
     */
    public void setGroupNames(Set<String> groupNames) {
        this.groupNames = groupNames;
    }

    /**
     * Getter accessor for attribute 'cache'.
     *
     * @return
     *       current value of 'cache'
     */
    public CacheProxyFeaturesDefinition getCache() {
        return cache;
    }

    /**
     * Setter accessor for attribute 'cache'.
     * @param cache
     * 		new value for 'cache '
     */
    public void setCache(CacheProxyFeaturesDefinition cache) {
        this.cache = cache;
    }

}
