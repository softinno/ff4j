package org.ff4j.property;

import java.util.Iterator;

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
 * Load property as list of {@link Class}.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class PropertyListClass extends PropertyList<Class<?> , PropertyClass > {
     
    /** Serial Number. */
    private static final long serialVersionUID = 3268988106433496994L;
    
    public PropertyListClass(String uid, String valueAsString) {
        super(uid, valueAsString);
    }
   
    public PropertyListClass(String uid, Class<?> ... value) {
        super(uid, value);
    }
    
    /** {@inheritDoc} */
    @Override
    public String getValueAsString() {
        if (get() == null) return null;
        Iterator<Class<?>> it = get().iterator();
        StringBuilder sb = new StringBuilder(it.next().getCanonicalName());
        while (it.hasNext()) {
            sb.append(listDelimiter);
            sb.append(it.next().getCanonicalName());
        }
        return sb.toString();
    }
  

}
