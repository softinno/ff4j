package org.ff4j.audit;

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

import org.ff4j.FF4j;
import org.ff4j.event.Event;
import org.ff4j.event.Event.Action;
import org.ff4j.event.Event.Scope;
import org.ff4j.exception.AssertionViolationException;
import org.ff4j.parser.ConfigurationFileParser;
import org.ff4j.test.AssertFF4j;
import org.ff4j.test.FF4jTestDataSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AuditTrailTestSupport implements FF4jTestDataSet {
    
    /** Initialize */
    protected FF4j ff4j = null;

    /** Tested Store. */
    protected AuditTrailRepository auditTrail;

    /** Test Values */
    protected AssertFF4j assertFF4j;
    
    /** {@inheritDoc} */
    @BeforeEach
    public void setUp() throws Exception {
        ConfigurationFileParser.clearCache();
        ff4j = new FF4j(expectConfig()).withAudit(initAuditTrailRepository());
        auditTrail = ff4j.getAuditTrail().get();
    }
    
    protected abstract AuditTrailRepository initAuditTrailRepository();
    
    protected static Event genEventCreatureFeature(String featureName) {
        return new Event().action(Action.CREATE)
                          .scope(Scope.FEATURE)
                          .targetUid(featureName);
    }
    
    protected static Event genEventToggleOnFeature(String featureName) {
        return new Event().action(Action.TOGGLE_ON)
                          .scope(Scope.FEATURE)
                          .targetUid(featureName);
    }
    
    @Test
    public void should_throw_AssertionViolationException_when_invalid_event() {
        assertThrows(AssertionViolationException.class, () -> { auditTrail.log(null); });
        Event e = new Event().scope(null).action(null).targetUid(null);
        assertThrows(AssertionViolationException.class, () -> { auditTrail.log(e); });
        e.scope(Scope.FEATURE);
        assertThrows(AssertionViolationException.class, () -> { auditTrail.log(e); });
        e.action(Action.ADD);
        assertThrows(AssertionViolationException.class, () -> { auditTrail.log(e); });
        e.targetUid("f1");
        // No Exception anymore as event is scorrect
        auditTrail.log(e);
    }
    
    @Test
    public void should_create_record_when_log_event() {
        // Given an empty repo
        AuditTrailQuery last5Days = new AuditTrailQuery(
                System.currentTimeMillis() - 1000*3600*24*5, 
                System.currentTimeMillis() + 10);
        Assertions.assertTrue(auditTrail.search(last5Days).findFirst().isEmpty());
        // When creating event
        auditTrail.log(genEventCreatureFeature("f1"));
        // All events
        Assertions.assertFalse(auditTrail.search(last5Days).findFirst().isEmpty());
        // Adding filter
        last5Days.scope(Scope.FEATURE);
        Assertions.assertFalse(auditTrail.search(last5Days).findFirst().isEmpty());
        // Adding filter
        last5Days.uid("f1");
        Assertions.assertFalse(auditTrail.search(last5Days).findFirst().isEmpty());
    }
    
    @Test
    public void should_delete_entry_when_purge_event() {
        AuditTrailQuery last5Days = new AuditTrailQuery()
                .from(System.currentTimeMillis() - 1000*3600*24*5)
                .to(System.currentTimeMillis() + 10);
        last5Days.scope(Scope.FEATURE);
        last5Days.uid("f1");
        Assertions.assertTrue(auditTrail.search(last5Days).findFirst().isEmpty());
        auditTrail.log(genEventCreatureFeature("f1"));
        Assertions.assertFalse(auditTrail.search(last5Days).findFirst().isEmpty());
        auditTrail.purge(last5Days);
        Assertions.assertTrue(auditTrail.search(last5Days).findFirst().isEmpty());
    }
    
    @Test
    public void should_create_resources_when_createSchema() {
        // Given no info
        AuditTrailQuery last5Days = new AuditTrailQuery()
                .from(System.currentTimeMillis() - 1000*3600*24*5)
                .to(System.currentTimeMillis() + 10);
        last5Days.scope(Scope.AUDIT_TRAIL);
        last5Days.uid("createSchema");
        Assertions.assertTrue(auditTrail.search(last5Days).findFirst().isEmpty());
        auditTrail.createSchema();
        Assertions.assertFalse(auditTrail.search(last5Days).findFirst().isEmpty());
    }

}
