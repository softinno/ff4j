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

import org.ff4j.property.repository.PropertyRepository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Representation of the store.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@ApiModel( value = "FeatureRepositoryDefinition", description = "store resource representation" )
public class PropertyRepositorySettings {
    
    /** type. */
    @JsonProperty("className")
    @ApiModelProperty( value = "Classname of implementation", required = true )
    private String className;
    
    /** numberOfFeatures. */
    @JsonProperty("count")
    @ApiModelProperty(value = "Count for properties", required = true )
    private int count = 0;
    
    /** features. */
    @JsonProperty("propertiesNames")
    @ApiModelProperty( value = "List of properties names", required = true )
    private Set < String > propertiesNames = new HashSet<>();
    
    /** cached. */
    @JsonProperty("cache")
    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty( value = "if a cachestore is defined", required = true )
    private CacheProxyPropertiesDefinition cache = null;
    
    /**
     * Default constructor.
     */
    public PropertyRepositorySettings() {}
            
    /**
     * Constructor from its feature store.
     *
     * @param featureStore
     *      cuurent fature store
     */
    public PropertyRepositorySettings(PropertyRepository repo) {
        this.className       = repo.getClass().getName();
        this.propertiesNames = repo.findAllIds().collect(Collectors.toSet());
        this.count        = propertiesNames.size();
        CacheProxyPropertiesDefinition tmpCache = new CacheProxyPropertiesDefinition(repo);
        if (tmpCache.isPropertyRepositoryCached()) {
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
     * Getter accessor for attribute 'propertiesNames'.
     *
     * @return
     *       current value of 'propertiesNames'
     */
    public Set<String> getPropertiesNames() {
        return propertiesNames;
    }

    /**
     * Setter accessor for attribute 'propertiesNames'.
     * @param propertiesNames
     * 		new value for 'propertiesNames '
     */
    public void setPropertiesNames(Set<String> propertiesNames) {
        this.propertiesNames = propertiesNames;
    }

    /**
     * Getter accessor for attribute 'cache'.
     *
     * @return
     *       current value of 'cache'
     */
    public CacheProxyPropertiesDefinition getCache() {
        return cache;
    }

    /**
     * Setter accessor for attribute 'cache'.
     * @param cache
     * 		new value for 'cache '
     */
    public void setCache(CacheProxyPropertiesDefinition cache) {
        this.cache = cache;
    }

}
