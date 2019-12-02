package org.ff4j.json.domain;

/*-
 * #%L
 * ff4j-json
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

import java.io.Serializable;

import org.ff4j.json.utils.FF4jObjectMapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Superclass, helper for serialization.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@JsonInclude(Include.NON_NULL)
public abstract class AbstractJsonBean implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = 2918624168764760000L;
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
       return toJson();
    }
    
    /** Serialize as Json. */
    public String toJson() {
        return FF4jObjectMapper.toJson(this);
    }
    
}
