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

import org.ff4j.event.repository.audit.AuditTrailRepository;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel( value = "AuditTrailSettings", description = "Describe informations related to Audit Trail" )
public class AuditTrailSettings {
    
    /** Implementation class for feature repository. */
    @JsonProperty("className")
    @ApiModelProperty( value = "Classname of implementation", required = true )
    private String className;

    /**
     * Default constructor.
     */
    public AuditTrailSettings() {}
    
    /**
     * Constructor.
     */
    public AuditTrailSettings(AuditTrailRepository repo) {
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
     *      new value for 'className '
     */
    public void setClassName(String className) {
        this.className = className;
    }

}
