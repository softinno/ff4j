package org.ff4j.feature.repository;

import org.ff4j.audit.AuditTrailListenerSupport;
import org.ff4j.audit.AuditTrailRepository;
import org.ff4j.event.Event;

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

import org.ff4j.event.Event.Action;
import org.ff4j.event.Event.Scope;
import org.ff4j.feature.Feature;

/**
 * Proposition of superclass to allow audit trail trackings.
 * 
 * @author Cedrick LUNVEN  (@clunven)
 */
public class FeatureRepositoryListenerAudit extends AuditTrailListenerSupport < Feature > 
                                             implements FeatureRepositoryListener {
    
    public FeatureRepositoryListenerAudit(AuditTrailRepository auditTrail) {
        super(auditTrail, Scope.FEATURE, Scope.FEATURESTORE);
    }
    
    /** {@inheritDoc} */
    @Override
    public void onToggleOnFeature(String uid) {
        log(Event.builder().source(source)
                 .action(Action.TOGGLE_ON)
                 .scope(Scope.FEATURE)
                 .refEntityUid(uid).build());
    }

    /** {@inheritDoc} */
    @Override
    public void onToggleOffFeature(String uid) {
        log(Event.builder().source(source)
                .action(Action.TOGGLE_OFF)
                .scope(Scope.FEATURE)
                .refEntityUid(uid).build());
    }

    /** {@inheritDoc} */
    @Override
    public void onToggleOnGroup(String groupName) {
        log(Event.builder().source(source)
                .action(Action.TOGGLE_ON)
                .scope(Scope.FEATURE_GROUP)
                .refEntityUid(groupName).build());
    }

    /** {@inheritDoc} */
    @Override
    public void onToggleOffGroup(String groupName) {
        log(Event.builder().source(source)
                .action(Action.TOGGLE_OFF)
                .scope(Scope.FEATURE_GROUP)
                .refEntityUid(groupName).build());
    }

    /** {@inheritDoc} */
    @Override
    public void onAddFeatureToGroup(String uid, String groupName) {
        log(Event.builder().source(source)
                .action(Action.ADD_TO_GROUP)
                .scope(Scope.FEATURE)
                .refEntityUid(groupName)
                .customKey("targetGroup", groupName)
                .build());
    }

    /** {@inheritDoc} */
    @Override
    public void onRemoveFeatureFromGroup(String uid, String groupName) {
        log(Event.builder().source(source)
                .action(Action.REMOVE_FROM_GROUP)
                .scope(Scope.FEATURE)
                .refEntityUid(groupName)
                .customKey("targetGroup", groupName)
                .build());
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(Feature bo) {
        log(Event.builder().source(source)
                .action(Action.CREATE)
                .scope(Scope.FEATURE)
                .refEntityUid(bo.getUid())
                .build());
    }

}
