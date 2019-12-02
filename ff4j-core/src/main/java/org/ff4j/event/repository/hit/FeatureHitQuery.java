package org.ff4j.event.repository.hit;

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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.ff4j.core.utils.JsonUtils;
import org.ff4j.event.Event;
import org.ff4j.event.EventSeries;

public class FeatureHitQuery implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = 3216301807651444710L;

    /** Bounds */
    private Long from;
    private Long to;
    
    /** Filtered criteria. */
    private Set<String> filteredEntityUids = new HashSet<>();
    private Set<String> filteredHostNames  = new HashSet<>();
    private Set<String> filteredSources    = new HashSet<>();
    
    private FeatureHitQuery() {
        this.from = System.currentTimeMillis() - 1000*3600*24*100;
        this.to = System.currentTimeMillis();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private FeatureHitQuery q = null;
        public Builder() {
            this.q = new FeatureHitQuery();
        }
        public Builder from(long from) {
            this.q.from = from;
            return this;
        }
        public Builder to(long to) {
            this.q.to = to;
            return this;
        }
        public Builder filterOnEntityUid(String... uid) {
            this.q.filteredEntityUids.addAll(Arrays.asList(uid));
            return this;
        }
        public Builder filterOnHostNames(String... hostnames) {
            this.q.filteredSources.addAll(Arrays.asList(hostnames));
            return this;
        }
        // -- Sources
        public Builder filterOnSources(Set<String> sources) {
            this.q.filteredSources.addAll(sources);
            return this;
        }
        public Builder filterOnSources(String... sources) {
            return this.filterOnSources(new HashSet<>(Arrays.asList(sources)));
        }
        public Builder filterOnSources(Event.Source... sources) {
            return this.filterOnSources(Arrays.stream(sources).map(Event.Source::name).collect(Collectors.toSet()));
        }
        public FeatureHitQuery build() {
            return q;
        }
    }
    
    /**
     * Lisibility, lisibility
     */
    public boolean match(Event evt) {
        boolean ok = 
               // from < X < to
               evt.getTimestamp() >= from && evt.getTimestamp() <= to &&
               // Uid
               (filteredEntityUids.isEmpty() || filteredEntityUids.contains(evt.getRefEntityUid())) &&
               // Source
               (filteredSources.isEmpty() || filteredSources.contains(evt.getSource())) &&
               // HourName
               (filteredHostNames.isEmpty() || filteredHostNames.contains(evt.getHostName()));
       return ok;
    }
    
    public boolean matchNames(Event evt) {
        return filteredEntityUids.isEmpty() || filteredEntityUids.contains(evt.getRefEntityUid());
    }
    
    public Collection < Event > filter(EventSeries es) {
        return es.stream().filter(this::match).collect(Collectors.toSet());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder sbEvent = new StringBuilder();
        sbEvent.append(JsonUtils.attributeAsJson("from", from));
        sbEvent.append(JsonUtils.attributeAsJson("to", to));
        sbEvent.append(",\"filteredEntityUids\":" + JsonUtils.collectionAsJson(filteredEntityUids));
        sbEvent.append(",\"filteredHostNames\":" + JsonUtils.collectionAsJson(filteredHostNames));
        sbEvent.append(",\"filteredSources\":" + JsonUtils.collectionAsJson(filteredSources));
        return sbEvent.toString();
    }

    /**
     * Getter accessor for attribute 'from'.
     *
     * @return
     *       current value of 'from'
     */
    public long getFrom() {
        return from;
    }

    /**
     * Getter accessor for attribute 'to'.
     *
     * @return
     *       current value of 'to'
     */
    public long getTo() {
        return to;
    }

    /**
     * Getter accessor for attribute 'filteredHostName'.
     *
     * @return
     *       current value of 'filteredHostName'
     */
    public Set<String> getFilteredHostNames() {
        return filteredHostNames;
    }
    
    /**
     * Getter accessor for attribute 'filteredHostName'.
     *
     * @return
     *       current value of 'filteredHostName'
     */
    public Set<String> getFilteredEntityUids() {
        return filteredEntityUids;
    }

    /**
     * Getter accessor for attribute 'filteredSource'.
     *
     * @return
     *       current value of 'filteredSource'
     */
    public Set<String> getFilteredSources() {
        return filteredSources;
    }
}
