package org.ff4j.property;

/*-
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2017 FF4J
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
import static org.ff4j.test.AssertUtils.assertHasLength;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * Create {@link Property} from name type and value.
 *
 * @author Cedrick Lunven (@clunven)
 */
public class PropertyFactory {
    
    /**
     * Hide constructor.
     */
    private PropertyFactory() {}
    
    /**
     * Factory method to create property.
     *
     * @param pName
     *            property name.
     * @param pType
     *            property type
     * @param pValue
     *            property value
     * @return
     */
    public static Property<?> createProperty(Property<?> from) {
        Property<?> to = PropertyFactory.createProperty(from.getUid(), 
                from.getClassName(), from.getValueAsString(), 
                from.getDescription().orElse(""), null);
        if (from.getFixedValues().isPresent()) {
            for (Object fixedValue : from.getFixedValues().get()) {
                to.add2FixedValueFromString(fixedValue.toString());
            }
        }
        return to;
    }
    
    /**
     * Factory method to create property.
     *
     * @param pName
     *            property name.
     * @param className
     *            property type
     * @param pValue
     *            property value
     * @return
     */
    public static Property<?> createProperty(String pName, String className, String pValue) {
        return PropertyFactory.createProperty(pName, className, pValue, null, null);
    }

    /**
     * Factory method to create property.
     *
     * @param pName
     *            property name.
     * @param className
     *            property type
     * @param pValue
     *            property value
     * @return
     */
    public static Property<?> createProperty(String pName, String className, String pValue, String desc, Set < String > fixedValues) {
        assertHasLength(pName);
        assertHasLength(className);
        try {
            Constructor<?> constr = Class.forName(className).getConstructor(String.class, String.class);
            final Property<?> ap = (Property<?>) constr.newInstance(pName, pValue);
            ap.setDescription(desc);
            ap.addFixedValues(fixedValues);
            return ap;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot instantiate '" + className + "' check default constructor : " + e.getMessage(), e);
        }
    }
}
