package org.ff4j.property.repository;

import static org.ff4j.test.AssertUtils.assertNotNull;
import static org.ff4j.utils.JsonUtils.attributeAsJson;
import static org.ff4j.utils.JsonUtils.collectionAsJson;
import static org.ff4j.utils.JsonUtils.objectAsJson;
import static org.ff4j.utils.Util.setOf;

import java.util.Set;

import org.ff4j.FF4jRepositoryListener;
import org.ff4j.FF4jRepositorySupport;
import org.ff4j.audit.AuditTrailRepository;
import org.ff4j.exception.ItemNotFoundException;
import org.ff4j.property.Property;
import org.ff4j.property.exception.PropertyNotFoundException;


/*
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2015 Ff4J
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
 * Superclass for any property store.
 *
 * @author Cedrick Lunven (@clunven)
 */
public abstract class PropertyRepositorySupport 
    extends FF4jRepositorySupport<Property<?>, PropertyRepositoryListener> 
    implements PropertyRepository {
    
    /** serialVersionUID. */
    private static final long serialVersionUID = -5638535944745337074L;
    
    /** Listener Name. */
    private static final String LISTENERNAME_AUDIT = "PropertyStoreAuditListener";
    
    /** Json Attribute. */
    public static final String JSON_ATTRIBUTE_PROPERTIESCOUNT = "propertiesCount";
    public static final String JSON_ATTRIBUTE_PROPERTIESNAMES = "propertiesNames";
    
    // -- Assertion --
    
    protected void assertPropertyExist(String uid) {
        try {
            assertItemExist(uid);
        } catch(ItemNotFoundException infEx) {
            throw new PropertyNotFoundException(uid, infEx);
        }
    }
    
    protected void assertPropertyNotNull(Property<?> property) {
        assertNotNull(property);
    }
    
    /** {@inheritDoc} */
    @Override
    public void createSchema() {
        /* 
         * In most of cases there is nothing to do. The feature and properties are createdat runtime.
         * But not always (JDBC, Mongo, Cassandra)... this is the reason why the dedicated store must 
         * override this method. It a default implementation (Pattern Adapter).
         */
        return;
    }
    
    /** {@inheritDoc} */
    @Override
    public void save(Iterable<Property<?>> iterProperties) {
        assertNotNull(iterProperties);
        iterProperties.forEach(prop -> {
            preUpdate(prop);
            saveProperty(prop);
            // Notify all listeners registered (like AuditTrail)
            this.notify(l -> l.onUpdate(prop));
        });
    }
    
    /** {@inheritDoc} */
    @Override
    public void delete(Iterable<String> iterIds) {
        assertNotNull(iterIds);
        iterIds.forEach(uid -> {
            assertPropertyExist(uid);
            deleteProperty(uid);
            this.notify(l -> l.onDelete(uid));
        });
    }
    
    /** {@inheritDoc} */
    @Override
    public void deleteAll() {
        delete(findAllIds());
        this.notify(l -> l.onDeleteAll());
    }
    
    // --------------------------
    //         Listeners
    // --------------------------
    
    /** {@inheritDoc} */
    @Override
    public void registerListener(String name, FF4jRepositoryListener<Property<?>> listener) {
        registerListener(name, (PropertyRepositoryListener) listener);
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerAuditListener(AuditTrailRepository auditTrail) {
        this.registerListener(LISTENERNAME_AUDIT, new PropertyRepositoryListenerAudit(auditTrail));
    }
    
    /** {@inheritDoc} */
    @Override
    public void unRegisterAuditListener() {
        this.unregisterListener(LISTENERNAME_AUDIT);
    }
    
    /** {@inheritDoc} */
    protected String customToString() {
        StringBuilder sb = new StringBuilder();
        Set<String> myProperties = setOf(findAll().map(Property::getUid));
        sb.append(attributeAsJson(JSON_ATTRIBUTE_PROPERTIESCOUNT, myProperties.size()));
        sb.append(objectAsJson(JSON_ATTRIBUTE_PROPERTIESNAMES, collectionAsJson(myProperties)));
        return sb.toString();
    }  
    
}
