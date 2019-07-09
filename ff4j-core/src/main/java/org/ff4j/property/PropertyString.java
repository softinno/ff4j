package org.ff4j.property;

/*-
 * #%L
 * ff4j-core
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

/**
 * Default implementation of {@link Property} as Simple string property.
 *
 * @author Cedrick Lunven (@clunven)
 */
public class PropertyString extends Property<String> {
    
    /** serial. */
    private static final long serialVersionUID = -7894832435341748278L;
    
    /**
     * Constructor by property name.
     *
     * @param name
     *      property name
     */
    public PropertyString(String name) {
        super(name);
    }
    
    /**
     * Constructor by property value.
     * 
     * @param name
     *      current name
     * @param value
     *      current value
     */
    public PropertyString(String name, String value) {
        super(name);
        this.value = value;
    }
    
    /**
     * Copy constructor. Create a property based on an existing one.
     *
     * @param name
     *      new property name
     * @param value
     *      existing property
     */
    public PropertyString(String name, PropertyString value) {
        super(name, value);
    }
    
    /** {@inheritDoc} */
    @Override
    public String fromString(String v) {
        assertStringValueIsNotNull(v);
        return v;
    }

}
