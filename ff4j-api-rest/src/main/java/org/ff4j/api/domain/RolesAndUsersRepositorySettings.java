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

import org.ff4j.user.repository.RolesAndUsersRepository;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Class to TODO
 *
 * @author Cedrick LUNVEN (@clunven)
 *
 */
@ApiModel( value = "RolesAndUsersRepositorySettings", description = "Describe informations related to Features" )
public class RolesAndUsersRepositorySettings {
    
    /** type. */
    @JsonProperty("className")
    @ApiModelProperty( value = "Classname of implementation", required = true )
    private String className;
    
    /**
     * Default constructor.
     */
    public RolesAndUsersRepositorySettings() {}
    
    /**
     * Constructor from its feature store.
     *
     * @param featureStore
     *      cuurent fature store
     */
    public RolesAndUsersRepositorySettings(RolesAndUsersRepository repo) {
        this.className = repo.getClass().getName();
    }
            

}
