package org.ff4j.event.repository.metrics;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ff4j.core.FF4j;
import org.ff4j.core.config.FF4jConfiguration;
import org.ff4j.core.config.FF4jConfigurationParser;
import org.ff4j.core.exception.AssertionViolationException;
import org.ff4j.core.test.AssertFF4j;
import org.ff4j.core.test.FF4jTestDataSet;
import org.ff4j.core.utils.Util;
import org.ff4j.event.Event;
import org.ff4j.event.HitCount;
import org.ff4j.event.repository.hit.FeatureHitQuery;
import org.ff4j.event.repository.hit.FeatureHitRepository;
import org.ff4j.event.repository.hit.FeatureHitRepositorySupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Superclass to test {@link FeatureHitRepository}.
 * 
 * @author Cedrick Lunven (@clunven)
 */
public abstract class AbstractTestSupportMetricsRepository implements FF4jTestDataSet {
   
    /** Initialize */
    protected FF4j ff4j = null;
    
    /** Tested Store. */
    protected FeatureHitRepository testedStore;

    /** Test Values */
    protected AssertFF4j assertFF4j;
    
    /** Cached feature names. */
    protected List <String > featureNames = new ArrayList<>();
    
    /** DataSet. **/
    protected FF4jConfiguration testDataSet;
    
    /**
     * I need listener cannot get EventFeatureUsageRepository only.
     */
    protected abstract FeatureHitRepositorySupport initRepository();
    
    /** {@inheritDoc} */
    @BeforeEach
    public void setUp() throws Exception {
        FF4jConfigurationParser.clearCache();
        ff4j        = new FF4j().withRepositoryEventFeaturesUsage(initRepository());
        assertFF4j  = new AssertFF4j(ff4j);
        testedStore = ff4j.getRepositoryEventFeaturesUsage();
        testDataSet = expectConfig();
    }
    
    /**
     * Generate feature event.
     */
    protected Event generateFeatureUsageEvent(String uid) {
        return Event.builder()
                .refEntityUid(uid)
                .source(Event.Source.TEST)
                .scope(Event.Scope.FEATURE)
                .action(Event.Action.HIT).build();
    }
    
    /**
     * Generate feature event.
     */
    protected Event generateFeatureUsageEvent(String uid, long timestamp) {
        return Event.builder()
                .refEntityUid(uid)
                .source(Event.Source.TEST)
                .scope(Event.Scope.FEATURE)
                .action(Event.Action.HIT)
                .timestamp(timestamp)
                .build();
    }
    
    /**
     * Generating event.
     *
     * @param uid
     *      feature unique id
     * @param from
     *      start date
     * @param to
     *      end date
     * @return
     *      event generated
     */
    protected Event generateFeatureUsageEvent(String uid, long from, long to) {
        return generateFeatureUsageEvent(uid, from + (long) (Math.random() * (to-from)));
    }
    
    /**
     * Pick a feature randomly.
     *
     * @return
     *      pick one of the name  
     */
    protected String getRandomFeatureName() {
        if (featureNames == null) {
            featureNames = new ArrayList<>(testDataSet.getFeatures().keySet());
        }
        return Util.getRandomElement(featureNames);
    }
    
    /**
     * Generating event.
     *
     * @param from
     *      start date
     * @param to
     *      end date
     * @return
     *      event generated
     */
    protected Event generateRandomFeatureUsageEvent(long from, long to) {
        return generateFeatureUsageEvent(getRandomFeatureName(), from , to);
    }
    
    /**
     * Populate repository.
     *
     * @param from
     *      start date
     * @param to
     *      end date
     * @param totalEvent
     *      number of events to generate
     */
    protected void populateRepository(long from, long to, int totalEvent) {
        for (int i = 0; i < totalEvent; i++) {
            testedStore.save(generateRandomFeatureUsageEvent(from, to));
        }
    }
    
    @Test
    @DisplayName("When saving event unitary, record is stored in the repository")
    public void should_create_event_when_save() throws InterruptedException {
        Event evt1 = generateFeatureUsageEvent("f1");
        Assertions.assertFalse(testedStore.find(evt1.getUid()).isPresent());
        testedStore.save(evt1);
        Thread.sleep(100);
        Assertions.assertTrue(testedStore.find(evt1.getUid()).isPresent());
    }

    @Test
    @DisplayName("When creating event with null param, expecting AssertionViolationException")
    public void should_throw_AssertionViolationException_when_event_null() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.save((Event) null); });
    }
    
    @Test
    public void should_compute_hitcount_feature() throws InterruptedException {
        Event evt1 = Event.builder().refEntityUid("f1").scope(Event.Scope.FEATURE)
                          .action(Event.Action.HIT).source(Event.Source.TEST).build();
        Event evt2 = Event.builder().refEntityUid("f1").scope(Event.Scope.FEATURE)
                .action(Event.Action.HIT).source(Event.Source.TEST).build();
        Event evt3 = Event.builder().refEntityUid("f2").scope(Event.Scope.FEATURE)
                .action(Event.Action.HIT).source(Event.Source.TEST).build();
        testedStore.save(evt1, evt2, evt3);
        Thread.sleep(200);
        // No Filter, FROM and TO as current
        Map < String, HitCount> maps = testedStore.getHitCount(
                FeatureHitQuery.builder()
                .from(System.currentTimeMillis()-1000)
                .to(System.currentTimeMillis()+1000).build());
        Assertions.assertNotNull(maps);
        Assertions.assertTrue(maps.containsKey("f1"));
        Assertions.assertTrue(maps.containsKey("f2"));
        Assertions.assertEquals(maps.get("f2").get(), 1);
        Assertions.assertEquals(maps.get("f1").get(), 2);
    }
    
    @Test
    public void should_compute_hitcount_source() throws InterruptedException {
        Event evt1 = Event.builder().refEntityUid("f1").scope(Event.Scope.FEATURE)
                          .action(Event.Action.HIT).source(Event.Source.JAVA_API).build();
        Event evt2 = Event.builder().refEntityUid("f1").scope(Event.Scope.FEATURE)
                .action(Event.Action.HIT).source(Event.Source.WEB_API).build();
        Event evt3 = Event.builder().refEntityUid("f2").scope(Event.Scope.FEATURE)
                .action(Event.Action.HIT).source(Event.Source.TEST).build();
        testedStore.save(evt1, evt2, evt3);
        Thread.sleep(200);
        // No Filter, FROM and TO as current
        Map < String, HitCount> maps = testedStore.getSourceHitCount(
                FeatureHitQuery.builder()
                .from(System.currentTimeMillis()-1000)
                .to(System.currentTimeMillis()+1000).build());
        Assertions.assertNotNull(maps);
        Assertions.assertTrue(maps.containsKey("WEB_API"));
        Assertions.assertTrue(maps.containsKey("TEST"));
        Assertions.assertEquals(maps.get("TEST").get(), 1);
    }
    
}

