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

import org.ff4j.event.repository.hit.FeatureHitRepository;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel( value = "FeatureUsageRepositorySettings", description = "Describe informations related to Features usage" )
public class FeatureHitRepositorySettings {
    
    /** type. */
    @JsonProperty("className")
    @ApiModelProperty( value = "Classname of implementation", required = true )
    private String className;
    
    /**
     * Default constructor.
     */
    public FeatureHitRepositorySettings() {}
    
    /**
     * Constructor.
     */
    public FeatureHitRepositorySettings(FeatureHitRepository repo) {
        this.className = repo.getClass().getName();
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

}
