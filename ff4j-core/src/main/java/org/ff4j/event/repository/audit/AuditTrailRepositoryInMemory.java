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

import static org.ff4j.core.test.AssertUtils.assertNotNull;
import static org.ff4j.core.utils.Util.validateEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.ff4j.core.utils.Util;
import org.ff4j.event.Event;
import org.ff4j.event.Event.Action;
import org.ff4j.event.Event.Scope;
import org.ff4j.event.EventSeries;

/**
 * Store AuditLog for all informations related to features and properties.
 *
 * @author Cedrick LUNVEN  (@clunven)
 */
public class AuditTrailRepositoryInMemory implements AuditTrailRepository {

    /** Default Event retention, in memory */
    private final int DEFAULT_QUEUE_CAPACITY = 100000;

    /** current capacity. */
    private int queueCapacity = DEFAULT_QUEUE_CAPACITY;
    
    /** Event 
     * <SCOPE> ; @see {@link Scope}
     * <ID> : Object ID
     * List Event related to user action in console. 
     */
    private Map< String , Map < String, EventSeries>> auditTrail = new ConcurrentHashMap<>();
    
    /** Default constructor. */
    public AuditTrailRepositoryInMemory() {}
    
    /** {@inheritDoc} */
    @Override
    public void createSchema() {
        // We may send a dedicated event
        log(Event.builder()
                 .action(Action.CREATE)
                 .scope(Scope.AUDIT_TRAIL)
                 .refEntityUid("createSchema")
                 .build());
    }
    
    /** {@inheritDoc} */
    @Override
    public void log(Event evt) {
        validateEvent(evt);
        String scope = evt.getScope();
        if (!auditTrail.containsKey(scope)) {
            auditTrail.put(scope, new ConcurrentHashMap<>());
        }
        // Some event do not point a specific feature (featureStore..) so reuse the scope
        String uid = Util.hasLength(evt.getRefEntityUid()) ? evt.getRefEntityUid() : scope;
        if (!auditTrail.get(scope).containsKey(uid)) {
            auditTrail.get(scope).put(uid, new EventSeries(queueCapacity));
        }
        auditTrail.get(scope).get(uid).add(evt);
    }

    /** {@inheritDoc} */
    @Override
    public EventSeries search(AuditTrailQuery query) {
        assertNotNull(query);
        EventSeries results = new EventSeries();
        if (!query.getFilteredScopes().isEmpty()) {
            query.getFilteredScopes()
                 .stream().map(auditTrail::get)
                 .map(Optional::ofNullable)
                 .filter(Optional::isPresent)
                 .map(Optional::get)
                 .forEach(p -> results.addAll(searchInMapOfEventSeries(query, p)));
        } else {
            auditTrail
                 .values().stream()
                 .forEach(p -> results.addAll(searchInMapOfEventSeries(query, p)));
        }
        return results;
    }
    
    /**
     * Utility to fetch a map of eventSeries.
     *
     * @param query
     *      current audit query (date, scope)
     * @param mapOfEventSeries
     *      map of events to perform filter
     * @return
     *      the events matching query in the target event series
     */
    private Collection < Event > searchInMapOfEventSeries(AuditTrailQuery query, Map < String, EventSeries > mapOfEventSeries) {
        assertNotNull(query);
        Collection < Event > results = new ArrayList<>();
        // Map of EventSeries
        if (!query.getFilteredEntityUids().isEmpty()) {
            query.getFilteredEntityUids().stream()
                 .map(mapOfEventSeries::get)
                 .map(Optional::ofNullable)
                 .filter(Optional::isPresent)
                 .map(Optional::get)
                 .map(query::filter)
                 .forEach(results::addAll);
        // No single EventSerie so will have to loop on each key
        } else {
            mapOfEventSeries.values().stream()
                            .map(query::filter)
                            .forEach(results::addAll);
        }
        return results;
    }

    /** {@inheritDoc} */
    @Override
    public void purge(AuditTrailQuery query) {
        // Will get a stream of event to remove
        search(query).forEach(evt -> {
            // Get correct scope (Feature, Properties, ...)
            auditTrail.get(evt.getScope())
                      // Get correct event series
                      .get(evt.getRefEntityUid())
                      // Remove from the Event Series
                      .remove(evt);
        });
    }      
    
}
