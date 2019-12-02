package org.ff4j.api.domain;

/*
 * #%L
 * ff4j-web
 * %%
 * Copyright (C) 2013 - 2014 Ff4J
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

import org.ff4j.core.cache.CacheProxyFeatures;
import org.ff4j.feature.repository.FeatureRepository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Information of Cache.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@JsonInclude(Include.NON_NULL)
@ApiModel( value = "CacheProxyFeaturesDefinition", description = "cache resource representation" )
public class CacheProxyFeaturesDefinition {
    
    /** cacheProvider. */
    @JsonProperty("featureRepositoryCached")
    @ApiModelProperty(value = "Flag enabled if a cache has been provided", required = false )
    private boolean featureRepositoryCached = false;
    
    /** cacheProvider. */
    @JsonProperty("cacheManagerClassName")
    @ApiModelProperty( value = "ClassName for the cacheManager implementation", required = false )
    private String cacheManagerClassName = null;
    
    @JsonProperty("cacheProviderClassname")
    @ApiModelProperty( value = "ClassName for the cacheProvider implementation", required = false )
    private String cacheProviderClassname = null;
    
    @JsonProperty("featureRepositoryClassname")
    @ApiModelProperty( value = "ClassName for the featureRepository implementation", required = false )
    private String featureRepositoryClassname = null;
    
    @JsonProperty("cachedfeatureNames")
    @ApiModelProperty( value = "List of features names in cache", required = false )
    private Set < String > cachedfeatureNames = new HashSet<String>();

    /**
     * Constructor from its feature store.
     *
     * @param featureStore
     *      cuurent fature store
     */
    public CacheProxyFeaturesDefinition(FeatureRepository fRepo) {
        if (fRepo instanceof CacheProxyFeatures) {
            featureRepositoryCached = true;
            CacheProxyFeatures cacheProxy = (CacheProxyFeatures) fRepo;
            cacheManagerClassName         = cacheProxy.getCacheManager().getNativeCache().getClass().getName();
            cacheProviderClassname        = cacheProxy.getCacheManager().getCacheProviderName();
            featureRepositoryClassname    = cacheProxy.getTargetFeatureStore().getClass().getName();
            cachedfeatureNames            = cacheProxy.findAllIds().collect(Collectors.toSet());
        }
    }

    /**
     * Getter accessor for attribute 'featureRepositoryCached'.
     *
     * @return
     *       current value of 'featureRepositoryCached'
     */
    public boolean isFeatureRepositoryCached() {
        return featureRepositoryCached;
    }

    /**
     * Setter accessor for attribute 'featureRepositoryCached'.
     * @param featureRepositoryCached
     * 		new value for 'featureRepositoryCached '
     */
    public void setFeatureRepositoryCached(boolean featureRepositoryCached) {
        this.featureRepositoryCached = featureRepositoryCached;
    }

    /**
     * Getter accessor for attribute 'cacheManagerClassName'.
     *
     * @return
     *       current value of 'cacheManagerClassName'
     */
    public String getCacheManagerClassName() {
        return cacheManagerClassName;
    }

    /**
     * Setter accessor for attribute 'cacheManagerClassName'.
     * @param cacheManagerClassName
     * 		new value for 'cacheManagerClassName '
     */
    public void setCacheManagerClassName(String cacheManagerClassName) {
        this.cacheManagerClassName = cacheManagerClassName;
    }

    /**
     * Getter accessor for attribute 'cacheProviderClassname'.
     *
     * @return
     *       current value of 'cacheProviderClassname'
     */
    public String getCacheProviderClassname() {
        return cacheProviderClassname;
    }

    /**
     * Setter accessor for attribute 'cacheProviderClassname'.
     * @param cacheProviderClassname
     * 		new value for 'cacheProviderClassname '
     */
    public void setCacheProviderClassname(String cacheProviderClassname) {
        this.cacheProviderClassname = cacheProviderClassname;
    }

    /**
     * Getter accessor for attribute 'featureRepositoryClassname'.
     *
     * @return
     *       current value of 'featureRepositoryClassname'
     */
    public String getFeatureRepositoryClassname() {
        return featureRepositoryClassname;
    }

    /**
     * Setter accessor for attribute 'featureRepositoryClassname'.
     * @param featureRepositoryClassname
     * 		new value for 'featureRepositoryClassname '
     */
    public void setFeatureRepositoryClassname(String featureRepositoryClassname) {
        this.featureRepositoryClassname = featureRepositoryClassname;
    }

    /**
     * Getter accessor for attribute 'cachedfeatureNames'.
     *
     * @return
     *       current value of 'cachedfeatureNames'
     */
    public Set<String> getCachedfeatureNames() {
        return cachedfeatureNames;
    }

    /**
     * Setter accessor for attribute 'cachedfeatureNames'.
     * @param cachedfeatureNames
     * 		new value for 'cachedfeatureNames '
     */
    public void setCachedfeatureNames(Set<String> cachedfeatureNames) {
        this.cachedfeatureNames = cachedfeatureNames;
    }
   

}
