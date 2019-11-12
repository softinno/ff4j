package org.ff4j.user.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

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

import java.util.stream.Stream;

import org.ff4j.core.FF4jEntity;
import org.ff4j.core.FF4jRepository;
import org.ff4j.user.FF4jRole;
import org.ff4j.user.FF4jUser;
import org.ff4j.user.exception.RoleNotFoundException;

/**
 * Common operations with Users.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public interface RolesAndUsersRepository extends FF4jRepository<String, FF4jUser > {
   
    /**
     * Count number of elements in the repository
     *
     * @return target
     */
    long countRoles();

    // -----------------------
    // -- ROLES,  [DELETE]  --
    // -----------------------
    
    /**
     * Delete target entity by its id.
     *
     * @param entityId
     *            target entity
     */
    void deleteRole(String roleName);
     
    /**
     * Syntax sugar to delete roles
     */
    default void deleteRoles(Stream<String> rolesNames) {
        if (rolesNames != null) {
            rolesNames.forEach(this::deleteRole);
        }
    }
    
    /**
     * Syntax sugar to delete roles
     */
    default void deleteRoles(Collection<String> rolesNames) {
        if (rolesNames != null) {
            deleteRoles(rolesNames.stream());
        }
    }
    
    /**
     * Syntax sugar to delete roles
     */
    default void deleteRoles(String... roleNames) {
        if (roleNames != null) {
            deleteRoles(Arrays.stream(roleNames));
        }
    }
    
    /**
     * Empty the target repository.
     */
    default void deleteAllRoles() {
        findAllRoleNames().forEach(this::deleteRole);
    }
    
    default void deleteAllUsers() {
        findAllIds().forEach(this::delete);
    }
    
    // -----------------------
    // -- ROLES,  [FIND]  --
    // -----------------------
    
    /**
     * Check if an entity exist or not.
     * 
     * @param id
     *            unique identifier of the id
     * @return
     */
    boolean existsRole(String roleName);

    /**
     * Find One entity by its id.
     * 
     * @param id
     *            target identifier
     * @return entity if exist
     */
    Optional<FF4jRole> findRole(String roleName);
    
    /**
     * Find One entity by its id.
     * 
     * @param id
     *            target identifier
     * @return entity if exist
     */
    default FF4jRole readRole(String roleName) {
        return findRole(roleName).orElseThrow(RoleNotFoundException::new);
    }
    
    /**
     * Syntax sugar to find roles
     */
    default Stream<FF4jRole> findRoles(Stream<String> rolesNames) {
        if (rolesNames != null) {
            return rolesNames.map(this::findRole)
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        }
        return Stream.empty();
    }
    
    /**
     * Syntax sugar to find roles
     */
    default Stream<FF4jRole> findRoles(Collection<String> rolesNames) {
        if (rolesNames != null) {
            return findRoles(rolesNames.stream());
        }
        return Stream.empty();
    }
    
    /**
     * Syntax sugar to find roles
     */
    default Stream<FF4jRole> findRoles(String... roleNames) {
        if (roleNames != null) {
            return findRoles(Arrays.stream(roleNames));
        }
        return Stream.empty();
    }
    
    /**
     * Retrieve all entities of the stores as a collection.
     *
     * @return entities as an {@link Iterable}
     */
    Stream<FF4jRole> findAllRoles();
    
    /**
     * Retriev all keys for a repository.
     *
     * @return
     *      retrieve key for a repository
     */
    default Stream <String> findAllRoleNames() {
        return findAllRoles().map(FF4jEntity::getUid);
    }
    
    // -----------------------
    // -- ROLES,  [UPSERT]  --
    // -----------------------s
    
    /**
     * Import Item into target repository (override if exist).
     *
     * @param entities
     *            target entities
     */
    void saveRole(FF4jRole role);
    
    /**
     * Syntax sugar to save roles
     */
    default void saveRoles(Stream<FF4jRole> roles) {
        if (roles != null) {
            roles.forEach(this::saveRole);
        }
    }
    
    /**
     * Syntax sugar to save roles
     */
    default void saveRoles(Collection<FF4jRole> roles) {
        if (roles != null) {
            saveRoles(roles.stream());
        }
    }
    
    /**
     * Syntax sugar to save roles
     */
    default void saveRoles(FF4jRole... roles) {
        if (roles != null) {
            saveRoles(Arrays.stream(roles));
        }
    }
    
    /**
     * Syntax sugar.
     *
     * @return
     *      list of properties names.
     */
    default Stream < String > listUsersNames() {
        return findAllIds();
    }
}
