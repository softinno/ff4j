package org.ff4j.event.repository.audit;

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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.ff4j.core.FF4j;
import org.ff4j.core.config.FF4jConfigurationParser;
import org.ff4j.core.exception.AssertionViolationException;
import org.ff4j.core.test.AssertFF4j;
import org.ff4j.core.test.FF4jTestDataSet;
import org.ff4j.event.Event;
import org.ff4j.event.Event.Action;
import org.ff4j.event.Event.Scope;
import org.ff4j.event.repository.audit.AuditTrailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractTestSupportAuditTrail implements FF4jTestDataSet {
    
    /** Initialize */
    protected FF4j ff4j = null;

    /** Tested Store. */
    protected AuditTrailRepository auditTrail;

    /** Test Values */
    protected AssertFF4j assertFF4j;
    
    /** {@inheritDoc} */
    @BeforeEach
    public void setUp() throws Exception {
        FF4jConfigurationParser.clearCache();
        ff4j = new FF4j(expectConfig()).withAudit(initAuditTrailRepository());
        auditTrail = ff4j.getAuditTrail().get();
    }
    
    protected abstract AuditTrailRepository initAuditTrailRepository();
    
    protected static Event genEventCreatureFeature(String featureName) {
        return Event.builder().action(Action.CREATE)
                          .scope(Scope.FEATURE)
                          .refEntityUid(featureName)
                          .build();
    }
    
    protected static Event genEventToggleOnFeature(String featureName) {
        return Event.builder().action(Action.TOGGLE_ON)
                          .scope(Scope.FEATURE)
                          .refEntityUid(featureName).build();
    }
    
    @Test
    public void should_throw_AssertionViolationException_when_invalid_event() {
        assertThrows(AssertionViolationException.class, () -> { auditTrail.log(null); });
        Event e1 = Event.builder().scope((Scope) null).action((Action)null).refEntityUid(null).build();
        assertThrows(AssertionViolationException.class, () -> { auditTrail.log(e1); });
        Event e2 = Event.builder().scope(Scope.FEATURE).action((Action)null).refEntityUid(null).build();
        assertThrows(AssertionViolationException.class, () -> { auditTrail.log(e2); });
        Event e3 = Event.builder().scope(Scope.FEATURE).action(Action.ADD).refEntityUid(null).build();
        assertThrows(AssertionViolationException.class, () -> { auditTrail.log(e3); });
        Event e4 = Event.builder().scope(Scope.FEATURE).action(Action.ADD).refEntityUid("f1").build();
        auditTrail.log(e4);
    }
    
    @Test
    public void should_create_record_when_log_event() {
        // Given an empty repo
        AuditTrailQuery last5Days = new AuditTrailQuery.Builder()
                .from(System.currentTimeMillis() - 1000*3600*24*5)
                .to(System.currentTimeMillis() + 10)
                .filterOnScopes(Scope.FEATURE)
                .filterOnEntityUid("f1")
                .build();
        Assertions.assertTrue(auditTrail.search(last5Days).isEmpty());
        // When creating event
        auditTrail.log(genEventCreatureFeature("f1"));
        // All events
        Assertions.assertFalse(auditTrail.search(last5Days).isEmpty());
    }
    
    @Test
    public void should_delete_entry_when_purge_event() throws InterruptedException {
        AuditTrailQuery last5Days = new AuditTrailQuery.Builder()
                .from(System.currentTimeMillis() - 1000*3600*24*5)
                .to(System.currentTimeMillis() + 10000)
                .filterOnScopes(Scope.FEATURE)
                .filterOnEntityUid("f1")
                .build();
        Assertions.assertTrue(auditTrail.search(last5Days).isEmpty());
        auditTrail.log(genEventCreatureFeature("f1"));
        Assertions.assertFalse(auditTrail.search(last5Days).isEmpty());
        auditTrail.purge(last5Days);
        Assertions.assertTrue(auditTrail.search(last5Days).isEmpty());
    }
}
