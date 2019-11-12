package org.ff4j.feature;

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

import org.ff4j.feature.repository.FeatureRepositoryListener;

/**
 * Custom listener to listener feature. Could be use to externalize events
 * to dedicated third party like Prometheus | Grafana..
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class FeatureRepositoryListenerConsole implements FeatureRepositoryListener {

    /** {@inheritDoc} */
    @Override
    public void onCreate(Feature feature) {
        System.out.println("Feature " + feature.getUid() + " has been created");
    }

    /** {@inheritDoc} */
    @Override
    public void onDelete(String featureUid) {
        System.out.println("Feature " + featureUid + " has been created");
    }

    /** {@inheritDoc} */
    @Override
    public void onDeleteAll() {
        System.out.println("Empty feature store");
    }

    /** {@inheritDoc} */
    @Override
    public void onUpdate(Feature feature) {
        System.out.println("Feature " + feature.getUid() + " has been updated");
    }

    /** {@inheritDoc} */
    @Override
    public void onCreateSchema() {
        System.out.println("FeatureRepositoryListenerConsole_ Create schema");
    }

    /** {@inheritDoc} */
    @Override
    public void onToggleOnFeature(String uid) {
        System.out.println("Toggle On feature " + uid);
    }

    /** {@inheritDoc} */
    @Override
    public void onToggleOffFeature(String uid) {
        System.out.println("Toggle Off feature " + uid);
    }

    /** {@inheritDoc} */
    @Override
    public void onToggleOnGroup(String groupName) {
        System.out.println("Toggle On Group " + groupName);
    }

    /** {@inheritDoc} */
    @Override
    public void onToggleOffGroup(String groupname) {
        System.out.println("Toggle Off group " + groupname);
    }

    /** {@inheritDoc} */
    @Override
    public void onAddFeatureToGroup(String uid, String groupName) {}

    /** {@inheritDoc} */
    @Override
    public void onRemoveFeatureFromGroup(String uid, String groupName) {}

}
