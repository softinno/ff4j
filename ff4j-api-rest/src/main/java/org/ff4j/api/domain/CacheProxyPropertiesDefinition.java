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

import org.ff4j.core.cache.CacheProxyProperties;
import org.ff4j.property.repository.PropertyRepository;

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
@ApiModel( value = "CacheProxyPropertiesDefinition", description = "cache resource representation" )
public class CacheProxyPropertiesDefinition {
    
    /** cacheProvider. */
    @JsonProperty("propertyRepositoryCached")
    @ApiModelProperty(value = "Flag enabled if a cache has been provided for properties", required = false )
    private boolean propertyRepositoryCached = false;
    
    /** cacheProvider. */
    @JsonProperty("cacheManagerClassName")
    @ApiModelProperty( value = "ClassName for the cacheManager implementation", required = false )
    private String cacheManagerClassName = null;
    
    @JsonProperty("cacheProviderClassname")
    @ApiModelProperty( value = "ClassName for the cacheProvider implementation", required = false )
    private String cacheProviderClassname = null;
    
    @JsonProperty("propertyRepositoryClassname")
    @ApiModelProperty( value = "ClassName for the propertyRepository implementation", required = false )
    private String propertyRepositoryClassname = null;
    
    @JsonProperty("cachedPropertiesNames")
    @ApiModelProperty( value = "List of properties names in cache", required = false )
    private Set < String > cachedPropertiesNames = new HashSet<String>();

    /**
     * Constructor from its feature store.
     *
     * @param featureStore
     *      cuurent fature store
     */
    public CacheProxyPropertiesDefinition(PropertyRepository fRepo) {
        if (fRepo instanceof CacheProxyProperties) {
            propertyRepositoryCached = true;
            CacheProxyProperties cacheProxy = (CacheProxyProperties) fRepo;
            cacheManagerClassName         = cacheProxy.getCacheManager().getNativeCache().getClass().getName();
            cacheProviderClassname        = cacheProxy.getCacheManager().getCacheProviderName();
            propertyRepositoryClassname   = cacheProxy.getTargetPropertyStore().getClass().getName();
            cachedPropertiesNames         = cacheProxy.findAllIds().collect(Collectors.toSet());
        }
    }

    /**
     * Getter accessor for attribute 'propertyRepositoryCached'.
     *
     * @return
     *       current value of 'propertyRepositoryCached'
     */
    public boolean isPropertyRepositoryCached() {
        return propertyRepositoryCached;
    }

    /**
     * Setter accessor for attribute 'propertyRepositoryCached'.
     * @param propertyRepositoryCached
     * 		new value for 'propertyRepositoryCached '
     */
    public void setPropertyRepositoryCached(boolean propertyRepositoryCached) {
        this.propertyRepositoryCached = propertyRepositoryCached;
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
     * Getter accessor for attribute 'propertyRepositoryClassname'.
     *
     * @return
     *       current value of 'propertyRepositoryClassname'
     */
    public String getPropertyRepositoryClassname() {
        return propertyRepositoryClassname;
    }

    /**
     * Setter accessor for attribute 'propertyRepositoryClassname'.
     * @param propertyRepositoryClassname
     * 		new value for 'propertyRepositoryClassname '
     */
    public void setPropertyRepositoryClassname(String propertyRepositoryClassname) {
        this.propertyRepositoryClassname = propertyRepositoryClassname;
    }

    /**
     * Getter accessor for attribute 'cachedPropertiesNames'.
     *
     * @return
     *       current value of 'cachedPropertiesNames'
     */
    public Set<String> getCachedPropertiesNames() {
        return cachedPropertiesNames;
    }

    /**
     * Setter accessor for attribute 'cachedPropertiesNames'.
     * @param cachedPropertiesNames
     * 		new value for 'cachedPropertiesNames '
     */
    public void setCachedPropertiesNames(Set<String> cachedPropertiesNames) {
        this.cachedPropertiesNames = cachedPropertiesNames;
    }

}
