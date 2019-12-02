package org.ff4j.user.repository;

/*-
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2018 FF4J
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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.stream.Stream;

import org.ff4j.core.FF4j;
import org.ff4j.core.config.FF4jConfiguration;
import org.ff4j.core.config.FF4jConfigurationParser;
import org.ff4j.core.exception.AssertionViolationException;
import org.ff4j.core.test.AssertFF4j;
import org.ff4j.core.test.FF4jTestDataSet;
import org.ff4j.core.utils.Util;
import org.ff4j.user.FF4jUser;
import org.ff4j.user.exception.RoleNotFoundException;
import org.ff4j.user.exception.UserNotFoundException;
import org.ff4j.user.repository.RolesAndUsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Users and roles.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class AbstractTestSupportRolesAndUsersRepository implements FF4jTestDataSet {
    
    /** Initialize */
    protected FF4j ff4j = null;

    /** Tested Store. */
    protected RolesAndUsersRepository testedStore;

    /** Test Values */
    protected AssertFF4j assertFF4j;
    
    /** DataSet. **/
    protected FF4jConfiguration testDataSet;
    
    /** {@inheritDoc} */
    @BeforeEach
    public void setUp() throws Exception {
        FF4jConfigurationParser.clearCache();
        ff4j        = new FF4j().withRepositoryUsersRoles(initStore());
        assertFF4j  = new AssertFF4j(ff4j);
        testedStore = ff4j.getRepositoryUsersRoles();
        testDataSet = expectConfig();
    }
    
    /**
     * Any store test will declare its store through this callback.
     * 
     * @return working feature store
     * @throws Exception
     *          Hi guys, just let you know I did the update in the presentation : changing instructors names to put the 2 of you    error during building feature store
     */
    protected abstract RolesAndUsersRepository initStore();
    
    @Test
    @DisplayName("When test roles existence with null param, expecting AssertionViolationException")
    public void existWithNullShouldThrowViolationException() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.exists(null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.exists(""); });
    }
    
    @Test
    @DisplayName("When creating properties with null param, expecting AssertionViolationException")
    public void createWithNullShouldThrowViolationException() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.save((FF4jUser)null); });
    }
    
    @Test
    public void should_createRecord_when_save() throws Exception {
        // Given
        assertFF4j.assertThatUserStoreHasSize(testDataSet.getUsers().size());
        assertFF4j.assertThatUserDoesNotExist(USER_FOR_TEST);
        // When
        testedStore.save(new FF4jUser(USER_FOR_TEST));
        // Then
        assertFF4j.assertThatUserStoreHasSize(testDataSet.getUsers().size() + 1);
        assertFF4j.assertThatUserExist(USER_FOR_TEST);
    }
    
    @Test
    public void should_throw_AssertionViolationException_when_read_invalid() {
        assertThrows(AssertionViolationException.class, () -> { testedStore.read(null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.read(""); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.save((FF4jUser) null); });
    }
    
    @Test
    public void should_match_configurationFile_when_parse() {
        // Given
        assertFF4j.assertThatUserStoreHasSize(testDataSet.getUsers().size());
        // When
        Map < String, FF4jUser > users = Util.toMap(testedStore.findAll());
        // Then
        Assertions.assertEquals(testDataSet.getUsers().size(), users.size());
        Assertions.assertTrue(users.containsKey(USER_JOHN));
        Assertions.assertTrue(users.get(USER_JOHN).getRoles().contains(ROLE_ADMIN));
        Assertions.assertTrue(users.containsKey(USER_SARAH));
    }
    
    // --- update ---
    
    @Test
    public void updateUserWithNullShouldThrowViolationException() throws Exception {
        
    }
    
    @Test
    @DisplayName("When updating user, all attributes should be updated")
    public void testUpdateUserProperties() {
        // Givens
        String newDescription = "new-description";
        assertFF4j.assertThatUserExist(USER_JOHN);
        FF4jUser john = testedStore.read(USER_JOHN);
        Assertions.assertTrue(john.getDescription().isPresent());
        Assertions.assertNotEquals(newDescription, john.getDescription().get());
        Assertions.assertFalse(john.getRoles().contains(ROLE_NEW));
        // When
        john.setDescription(newDescription);
        john.addRole(ROLE_SUPERADMIN);
        testedStore.save(john);
        // Then
        assertFF4j.assertThatUserExist(USER_JOHN);
        FF4jUser updatedUser = testedStore.read(USER_JOHN);
        Assertions.assertEquals(newDescription, updatedUser.getDescription().get());
        Assertions.assertEquals(updatedUser.getLastName(),  john.getLastName());
        Assertions.assertEquals(updatedUser.getFirstName(), john.getFirstName());
        Assertions.assertTrue(updatedUser.getRoles().contains(ROLE_SUPERADMIN));
    }
    
    // --- delete ---
    
    @Test
    @DisplayName("When deleting new user, it is not available anymore")
    public void deleteShouldRemoveExistence() throws Exception {
        assertFF4j.assertThatUserStoreHasSize(testDataSet.getUsers().size());
        assertFF4j.assertThatUserExist(USER_JOHN);
        testedStore.delete(USER_JOHN);
        assertFF4j.assertThatUserStoreHasSize(testDataSet.getUsers().size() - 1 );
        assertFF4j.assertThatUserDoesNotExist(USER_JOHN);
    }
    
    @Test
    @DisplayName("When deleting new role, it is not available anymore")
    public void deleteRoleShouldRemoveExistence() throws Exception {
        assertFF4j.assertThatRoleStoreHasSize(testDataSet.getRoles().size());
        assertFF4j.assertThatRoleExist(ROLE_ADMIN);
        testedStore.deleteRole(ROLE_ADMIN);
        assertFF4j.assertThatRoleStoreHasSize(testDataSet.getRoles().size() - 1 );
        assertFF4j.assertThatRoleDoesNotExist(ROLE_ADMIN);
    }

    @Test
    @DisplayName("When deleting user with null param, expecting AssertionViolationException")
    public void deleteUserWithNullShouldThrowViolationException() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.delete((String) null); });
    }
    
    @Test
    @DisplayName("When deleting role with null param, expecting AssertionViolationException")
    public void deleteRoleWithNullShouldThrowViolationException() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.deleteRole((String) null); });
    }

    @Test
    @DisplayName("When deleting user with null param, expecting UserNotFoundException")
    public void deleteUnknownuserShouldThrowUserNotFound() throws Exception {
        assertFF4j.assertThatUserDoesNotExist(USER_FOR_TEST);
        assertThrows(UserNotFoundException.class, () -> { 
            testedStore.delete(USER_FOR_TEST); 
        });
    }
    
    @Test
    public void should_throw_RoleNotFoundException_if_role_not_found() throws Exception {
        assertFF4j.assertThatRoleDoesNotExist(ROLE_NEW);
        assertThrows(RoleNotFoundException.class, () -> { 
            testedStore.deleteRole(ROLE_NEW); 
        });
    }
    
    @Test
    public void should_clear_all_users_when_deleteAll() {
        // Given
        assertFF4j.assertThatUserExist(USER_JOHN);
        assertFF4j.assertThatUserExist(USER_SARAH);
        // When
        testedStore.deleteAll();
        // Then
        Assertions.assertEquals(0, testedStore.findAll().count());
    }
    
    @Test
    public void should_empty_repo_when_deleteAllRole() {
        // Given
        assertFF4j.assertThatRoleExist(ROLE_ADMIN);
        assertFF4j.assertThatRoleExist(ROLE_USER);
        // When
        testedStore.deleteAllRoles();
        // Then
        Assertions.assertEquals(0, testedStore.countRoles());
    }
    
    @Test
    public void should_listUserNames() {
        Stream< String> userNames = testedStore.listUsersNames();
        Stream< String> roleNames = testedStore.findAllRoleNames();
        Assertions.assertNotNull(userNames);
        Assertions.assertNotNull(roleNames);
        
        Assertions.assertEquals(testDataSet.getUsers().size(), userNames.count());
        Assertions.assertEquals(testDataSet.getUsers().size(),  testedStore.count());
        
        Assertions.assertEquals(testDataSet.getRoles().size(), roleNames.count());
        Assertions.assertEquals(testDataSet.getRoles().size(),  testedStore.countRoles());
    }
    
    @Test
    public void should_throw_UserNotFoundException_when_delete_invalid_user() {
        // Given
        assertFF4j.assertThatUserDoesNotExist("dummy");
        // Then
        assertThrows(UserNotFoundException.class, () -> { testedStore.delete("dummy"); });
    }
}
