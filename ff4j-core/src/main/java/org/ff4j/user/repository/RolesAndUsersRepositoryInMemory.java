package org.ff4j.user.repository;

import static org.ff4j.core.test.AssertUtils.assertHasLength;
import static org.ff4j.core.test.AssertUtils.assertNotNull;

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

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ff4j.core.config.FF4jConfiguration;
import org.ff4j.core.config.FF4jConfigurationParser;
import org.ff4j.core.test.AssertUtils;
import org.ff4j.user.FF4jRole;
import org.ff4j.user.FF4jUser;
import org.ff4j.user.exception.RoleNotFoundException;

/**
 * Implementation to handle Users in memory.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class RolesAndUsersRepositoryInMemory extends RolesAndUsersRepositorySupport {

    /** serialVersionUID. */
    private static final long serialVersionUID = 2514550814475735765L;
    
    /** InMemory Feature Map */
    private Map<String, FF4jUser > mapOfUsers = new LinkedHashMap<String, FF4jUser>();
    
    /** InMemory Feature Map */
    private Map < String, FF4jRole > mapOfRoles = new LinkedHashMap<String, FF4jRole >();
    
    /**
     * Default constructor.
     */
    public RolesAndUsersRepositoryInMemory() {}
    
    /**
     * Load data with a Parser and a fileName.
     *
     * @param parser
     *      target parser
     * @param fileName
     *      target file name
     */
    public RolesAndUsersRepositoryInMemory(FF4jConfigurationParser parser, String fileName) {
        AssertUtils.assertHasLength(fileName, "fileName");
        AssertUtils.assertNotNull(parser,     "parser");
        initWithConfig(parser.parse(fileName));
    }
    
    /**
     * Load data with a Parser and a fileName.
     *
     * @param parser
     *      target parser
     * @param fileName
     *      target file name
     */
    public RolesAndUsersRepositoryInMemory(FF4jConfigurationParser parser, InputStream in) {
        AssertUtils.assertNotNull(parser,  "parser");
        AssertUtils.assertNotNull(in, "inputStream");
        initWithConfig(parser.parse(in));
    }
    
    /**
     * Constructor with inputstream fileName.
     * 
     * @param fileName
     *            fileName present in classPath or on fileSystem.
     */
    public RolesAndUsersRepositoryInMemory(FF4jConfiguration ff4jConfig) {
        initWithConfig(ff4jConfig);
    }

    /**
     * Constructor with features to be imported immediately.
     * 
     * @param features
     *      collection of features to be created
     */
    public RolesAndUsersRepositoryInMemory(Collection<FF4jUser> users) {
        initWithUsers(users);
    }
    
    /**
     * Initialization of features and groups.
     * 
     * @param features
     */
    private void initWithConfig(FF4jConfiguration ff4jConfig) {
        AssertUtils.assertNotNull(ff4jConfig);
        AssertUtils.assertNotNull(ff4jConfig.getUsers());
        initWithUsers(ff4jConfig.getUsers().values());
        initWithRoles(ff4jConfig.getRoles().values());
    }
    
    /**
     * Initialization of features and groups.
     * 
     * @param features
     */
    private void initWithUsers(Collection<FF4jUser> users) {
        createSchema();
        if (null != users) {
            this.mapOfUsers = users.stream()
                    .collect(Collectors.toMap(FF4jUser::getUid, Function.identity()));
        }
    }
    
    /**
     * Initialization of features and groups.
     * 
     * @param features
     */
    private void initWithRoles(Collection<FF4jRole> roles) {
        createSchema();
        if (null != roles) {
            this.mapOfRoles = roles.stream()
                    .collect(Collectors.toMap(FF4jRole::getUid, Function.identity()));
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public Stream<String> findAllIds() {
        if (mapOfUsers ==  null) return null;
        return mapOfUsers.keySet().stream();
    }

    /** {@inheritDoc} */
    @Override
    public long countRoles() {
        return findAllRoles().count();
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsRole(String roleName) {
        assertHasLength(roleName);
        return mapOfRoles.containsKey(roleName);
    }
    
    // Role [DELETE]
    
    /** {@inheritDoc} */
    @Override
    public void deleteRole(String roleName) {
        assertNotNull(roleName);
        if (!existsRole(roleName)) {
            throw new RoleNotFoundException(roleName);
        }
        mapOfRoles.remove(roleName);
    }
    
    /** {@inheritDoc} */
    @Override
    public void deleteAllRoles() {
        mapOfRoles.clear();
    }
    
    /** {@inheritDoc} */
    @Override
    public void deleteAll() {
        mapOfUsers.clear();
    }
    
    /** {@inheritDoc} */
    @Override
    public Optional<FF4jRole> findRole(String roleName) {
        return existsRole(roleName) ? 
                Optional.ofNullable(mapOfRoles.get(roleName)) : 
                Optional.empty();
    }
    
    /** {@inheritDoc} */
    @Override
    public void saveRole(FF4jRole entity) {
        assertNotNull(entity);
        assertHasLength(entity.getUid());
        if (!existsRole(entity.getUid())) {
            entity.setCreationDate(LocalDateTime.now());
        }
        entity.setLastModified(LocalDateTime.now());
        mapOfRoles.put(entity.getUid(), entity);
    }
   
    /** {@inheritDoc} */
    @Override
    public boolean exists(String userId) {
        assertHasLength(userId);
        return mapOfUsers.containsKey(userId);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<FF4jUser> find(String userid) {
        return exists(userid) ? 
                Optional.ofNullable(mapOfUsers.get(userid)) : 
                Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    public Stream<FF4jUser> findAll() {
        return mapOfUsers.values().stream();
    }
  
    /** {@inheritDoc} */
    @Override
    protected void saveUser(FF4jUser user) {
        assertNotNull(user);
        assertHasLength(user.getUid());
        user.setLastModified(LocalDateTime.now());
        mapOfUsers.put(user.getUid(), user);
    }

    /** {@inheritDoc} */
    @Override
    protected  void deleteUser(String userId) {
        assertNotNull(userId);
        assertItemExist(userId);
        mapOfUsers.remove(userId);
    }

    /** {@inheritDoc} */
    @Override
    public Stream<FF4jRole> findAllRoles() {
        return mapOfRoles.values().stream();
    }

    /** {@inheritDoc} */
    @Override
    public void save(Iterable<FF4jUser> entities) {
       if (entities != null) {
           entities.forEach(this::saveUser);
       }
    }

    /** {@inheritDoc} */
    @Override
    public void delete(Iterable<String> entities) {
        if (entities != null) {
            entities.forEach(this::deleteUser);
        }
    }

}