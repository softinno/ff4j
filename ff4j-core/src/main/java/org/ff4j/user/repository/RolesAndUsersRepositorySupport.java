package org.ff4j.user.repository;

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

import static org.ff4j.core.test.AssertUtils.assertNotEmpty;
import static org.ff4j.core.test.AssertUtils.assertNotNull;
import static org.ff4j.core.utils.JsonUtils.attributeAsJson;
import static org.ff4j.core.utils.JsonUtils.collectionAsJson;
import static org.ff4j.core.utils.JsonUtils.objectAsJson;
import static org.ff4j.core.utils.Util.setOf;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.ff4j.core.FF4jRepositoryListener;
import org.ff4j.core.FF4jRepositorySupport;
import org.ff4j.core.exception.NotFoundException;
import org.ff4j.event.repository.audit.AuditTrailRepository;
import org.ff4j.user.FF4jUser;
import org.ff4j.user.exception.UserNotFoundException;

/**
 * Superclass as helper to implements user repository.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class RolesAndUsersRepositorySupport
    extends FF4jRepositorySupport < FF4jUser , FF4jRepositoryListener< FF4jUser >> 
    implements RolesAndUsersRepository {

    /** serialVersionUID. */
    private static final long serialVersionUID = 2472380934533153376L;
    
    /** Listener Name. */
    private static final String LISTENERNAME_AUDIT = "RepositoryUserAuditListener";
    
    /** Listener Name. */
    protected static final String LISTENERNAME_AUDIT_ROLE = "RepositoryRoleAuditListener";
    
    /** Json Attribute. */
    public static final String JSON_ATTRIBUTE_USERS_COUNT = "userCount";
    public static final String JSON_ATTRIBUTE_USERS_NAMES = "userNames";
    
    /** Specialized method for users : CREATE */
    protected abstract void saveUser(FF4jUser user);
    
    /** Specialized method for users : DELETE */
    protected abstract void deleteUser(String userId);
     
    protected void assertUserExist(String uid) {
        try {
            assertItemExist(uid);
        } catch(NotFoundException infEx) {
            throw new UserNotFoundException(uid, infEx);
        }
    }
    
    protected void assertUserNotNull(FF4jUser user) {
        assertNotNull(user);
    }
    
    /** {@inheritDoc} */
    @Override
    public Stream<String> findAllIds() {
        return findAll().map(FF4jUser::getUid);
    }
    
    /** {@inheritDoc} */
    @Override
    public void delete(String... uids) {
        assertNotEmpty(uids);
        delete(Arrays.stream(uids));
    }
    
    public void delete(Stream<String> entities) {
        assertNotNull(entities);
        entities.forEach(uid -> {
            assertUserExist(uid);
            deleteUser(uid);
            this.notify(l -> l.onDelete(uid));
        });
    }
    
    /** {@inheritDoc} */
    @Override
    public void delete(Iterable<String> entities) {
        assertNotNull(entities);
        delete(StreamSupport.stream(entities.spliterator(), true));
    }
    
    /** {@inheritDoc} */
    @Override
    public void deleteAll() {
        deleteAllUsers();
        this.notify(l -> l.onDeleteAll());
    }

    /** {@inheritDoc} */
    @Override
    public void save(FF4jUser... entities) {
        assertNotNull(entities);
        save(Arrays.stream(entities));
    }
    
    /** {@inheritDoc} */
    @Override
    public void save(Stream<FF4jUser> entities) {
        assertNotNull(entities);
        entities.forEach(entity -> {
            preUpdate(entity);
            saveUser(entity);
            this.notify(l -> l.onUpdate(entity));
        });
    }
    /** {@inheritDoc} */
    @Override
    public void save(Iterable<FF4jUser> entities) {
        assertNotNull(entities);
        save(StreamSupport.stream(entities.spliterator(), true));
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerListener(String name, FF4jRepositoryListener<FF4jUser> listener) {
        registerListener(name, (RepositoryUserListenerAudit) listener);
    }
    
    /** {@inheritDoc} */
    @Override
    public void registerAuditListener(AuditTrailRepository auditTrail) {
        this.registerListener(LISTENERNAME_AUDIT, new RepositoryUserListenerAudit(auditTrail));
    }
    
    /** {@inheritDoc} */
    @Override
    public void unRegisterAuditListener() {
        this.unregisterListener(LISTENERNAME_AUDIT);
    }
    
    /** {@inheritDoc} */
    @Override
    public Stream<String> listListenerNames() {
        return this.listeners.keySet().stream();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<FF4jRepositoryListener<FF4jUser>> readListener(String listenerName) {
        return Optional.ofNullable(this.listeners.get(listenerName));
    }

    /** {@inheritDoc} */
    @Override
    public Optional<RepositoryUserListenerAudit> readAuditListener() {
        Optional<FF4jRepositoryListener<FF4jUser>> current = readListener(LISTENERNAME_AUDIT);
        // Enforcing type for audit listener
        if (current.isPresent()) {
            return Optional.ofNullable( (RepositoryUserListenerAudit) current.get());
        }
        return Optional.empty();
    }
    
    
    /** {@inheritDoc} */
    protected String customToString() {
        StringBuilder sb = new StringBuilder();
        Set<String> myProperties = setOf(findAll().map(FF4jUser::getUid));
        sb.append(attributeAsJson(JSON_ATTRIBUTE_USERS_COUNT, myProperties.size()));
        sb.append(objectAsJson(JSON_ATTRIBUTE_USERS_NAMES, collectionAsJson(myProperties)));
        return sb.toString();
    }

}
