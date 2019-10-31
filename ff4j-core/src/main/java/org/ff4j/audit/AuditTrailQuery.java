package org.ff4j.audit;

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

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ff4j.event.Event;
import org.ff4j.event.EventSeries;
import org.ff4j.utils.JsonUtils;

public class AuditTrailQuery {
    
    private Long from;
    
    private Long to;
    
    private Event.Scope scope;
    
    private String uid;
    
    public AuditTrailQuery() {
    }
    
    public AuditTrailQuery(long from, long to) {
        this.from = Long.valueOf(from);
        this.to   = Long.valueOf(to);
    }
    
    public AuditTrailQuery from(long from) {
        this.from = Long.valueOf(from);
        return this;
    }
    
    public AuditTrailQuery to(long to) {
        this.to = Long.valueOf(to);
        return this;
    }
    
    public AuditTrailQuery scope(Event.Scope scope) {
        this.scope = scope;
        return this;
    }
    
    public AuditTrailQuery uid(String uid) {
        this.uid = uid;
        return this;
    }
    
    public Optional < Long > getFrom() {
        return Optional.ofNullable(from);
    }
    
    public Optional < Long > getTo() {
        return Optional.ofNullable(to);
    }
    
    public Optional < String > getUid() {
        return Optional.ofNullable(uid);
    }
    
    public Optional < Event.Scope > getScope() {
        return Optional.ofNullable(scope);
    }
    
    /**
     * Lisibility, lisibility
     */
    public boolean match(Event evt) {
               // LowerBound
        return ((getFrom() == null) || (getFrom() != null && evt.getTimestamp() >= from)) &&
               // UpperBound
               ((getTo() == null) || (to != null && evt.getTimestamp() <= to)) &&
               // Scope
               ((scope == null) || scope.name().equalsIgnoreCase(evt.getScope().name())) &&
               // Uid
               ((uid == null) || uid.equalsIgnoreCase(evt.getTargetUid()));
    }
    
    public Collection < Event > filter(EventSeries es) {
        return es.stream().filter(this::match).collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder sbEvent = new StringBuilder();
        sbEvent.append(JsonUtils.attributeAsJson("from", from));
        sbEvent.append(JsonUtils.attributeAsJson("to", to));
        sbEvent.append(JsonUtils.attributeAsJson("scope", scope));
        sbEvent.append(JsonUtils.attributeAsJson("uid", uid));
        return sbEvent.toString();
    }

}
