package org.ff4j.event;

import static org.ff4j.utils.Util.inetAddressHostName;

/*
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2014 Ff4J
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
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.ff4j.FF4jEntity;
import org.ff4j.utils.JsonUtils;

/**
 * Audit information relevant to features.
 * 
 * @author Cedrick Lunven (@clunven)
 */
public class Event extends FF4jEntity<Event> implements Serializable, Comparable < Event > {

    /** Serial. */
    private static final long serialVersionUID = 6490780530212257217L;
    
    /** Time of event creation. */
    private long timestamp = 0;
   
    /** feature or property. */
    private Scope scope = Scope.GENERAL;
    
    /** Action performed. */
    private Action action = Action.UNKNOWN;
    
    /** Source. */
    private Source source = Source.UNKNOWN;
    
    /** feature or property name. */
    private String targetUid;
   
    /** HostName. */
    private String hostName;
    
    /** Duration of action. */
    private Optional < Long > duration = Optional.empty();
    
    /** Common element. */
    private Optional < String > value = Optional.empty();
    
    /** Specific parameters. */
    private Optional < Map < String, String > > customKeys = Optional.empty();
    
    public enum Action {
        UNKNOWN, ADD, REMOVE, CONNECT, DISCONNECT, 
        TOGGLE_ON, TOGGLE_OFF, CREATE, DELETE, UPDATE, 
        CLEAR, CREATE_SCHEMA, HIT, ADD_TO_GROUP, 
        REMOVE_FROM_GROUP, UPDATE_ACL;
    }
    
    public enum Scope {
        GENERAL, FF4J, FEATURE, FEATURE_GROUP, PROPERTY, USER, ROLE,
        FEATURESTORE, PROPERTYSTORE, AUDIT_TRAIL, FEATURE_USAGE_STORE, 
        USERSTORE, UNKNOWN;
    }
    
    public enum Source {
        UNKNOWN, JAVA_API, WEB_CONSOLE, WEB_API, JMX, SSH;
    }
    
    /**
     * Default constructor.
     */
    public Event() {
        this(UUID.randomUUID().toString());
    }
    
    /**
     * Default constructor.
     * 
     */
    public Event(String uid) {
        super(uid);
        timestamp    = creationDate.get().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        hostName     = inetAddressHostName();
    }
    
    /**
     * Add custom key to event.
     *
     * @param key
     *      custom key
     * @param value
     *      current value
     */
    public Event put(String key, String value) {
        if (!getCustomKeys().isPresent()) {
          setCustomKeys(new HashMap<String, String>());
        }
        getCustomKeys().get().put(key, value);
        return this;
    }
    
    /** {@inheritDoc} */
    public String toString() {
        String start = super.toString();
        start = start.substring(0, start.length()-1);
        StringBuilder sbEvent = new StringBuilder(start);
        sbEvent.append(JsonUtils.attributeAsJson("hostname", hostName));
        sbEvent.append(JsonUtils.attributeAsJson("source", source));
        sbEvent.append(JsonUtils.attributeAsJson("action", action));
        sbEvent.append(JsonUtils.attributeAsJson("scope", scope));
        sbEvent.append(JsonUtils.attributeAsJson("targetUid", targetUid));
        if (!duration.isEmpty()) {
            sbEvent.append(JsonUtils.attributeAsJson("duration", duration.get()));
        }
        if (!value.isEmpty()) {
            sbEvent.append(JsonUtils.attributeAsJson("value", value.get()));
        }
        if (!customKeys.isEmpty()) {
            sbEvent.append(",\"customKeys\":" +  JsonUtils.mapAsJson(customKeys.get()));
        }
        sbEvent.append("}");
        return sbEvent.toString();
    }
    
    /**
     * Read key value (if present).
     */
    public Optional<String> getKey(String key) {
        if (getCustomKeys().isPresent()) {
            return Optional.ofNullable(getCustomKeys().get().get(key));
        }
        return Optional.empty();
    }
    
    /**
     * Getter accessor for attribute 'timestamp'.
     * 
     * @return current value of 'timestamp'
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Getter accessor for attribute 'timestamp'.
     * 
     * @return current value of 'timestamp'
     */
    public Date getDate() {
        return new Date(getTimestamp());
    }

    /**
     * Getter accessor for attribute 'hostName'.
     *
     * @return
     *       current value of 'hostName'
     */
    public String getHostName() {
        return hostName;
    }
    
    public Event hostName(String host) {
        this.hostName = host;
        return this;
    }

    /**
     * Getter accessor for attribute 'source'.
     *
     * @return
     *       current value of 'source'
     */
    public Source getSource() {
        return source;
    }   
    
    public Event source(Source source) {
        this.source = source;
        return this;
    }
    
    /**
     * Getter accessor for attribute 'targetUid'.
     *
     * @return
     *       current value of 'targetUid'
     */
    public String getTargetUid() {
        return targetUid;
    }

    public Event targetUid(String uid) {
        this.targetUid = uid;
        return this;
    }
    
    /**
     * Getter accessor for attribute 'scope'.
     *
     * @return
     *       current value of 'type'
     */
    public Scope getScope() {
        return scope;
    }

    public Event scope(Scope scope) {
        this.scope = scope;
        return this;
    }
    
    /**
     * Getter accessor for attribute 'action'.
     *
     * @return
     *       current value of 'action'
     */
    public Action getAction() {
        return action;
    }

    public Event action(Action action) {
        this.action = action;
        return this;
    }
    
    /**
     * Getter accessor for attribute 'customKeys'.
     *
     * @return
     *       current value of 'customKeys'
     */
    public Optional <Map<String, String>> getCustomKeys() {
        return customKeys;
    }

    /**
     * Setter accessor for attribute 'customKeys'.
     * @param customKeys
     * 		new value for 'customKeys '
     */
    public Event setCustomKeys(Map<String, String> customKeys) {
        this.customKeys = Optional.ofNullable(customKeys);
        return this;
    }

    /**
     * Setter accessor for attribute 'timestamp'.
     * @param timestamp
     * 		new value for 'timestamp '
     */
    public Event setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Getter accessor for attribute 'duration'.
     *
     * @return
     *       current value of 'duration'
     */
    public Optional < Long > getDuration() {
        return duration;
    }

    /**
     * Setter accessor for attribute 'duration'.
     * @param duration
     * 		new value for 'duration '
     */
    public Event duration(long duration) {
        this.duration = Optional.ofNullable(duration);
        return this;
    }

    /**
     * Getter accessor for attribute 'value'.
     *
     * @return
     *       current value of 'value'
     */
    public Optional < String > getValue() {
        return value;
    }

    /**
     * Setter accessor for attribute 'value'.
     * @param value
     * 		new value for 'value '
     */
    public Event value(String value) {
        this.value = Optional.ofNullable(value);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Event evt) {
        int myTime = Long.valueOf(this.getTimestamp() - evt.getTimestamp()).intValue();
        // Timestamp alone does not ensure unicity, adding UIDs
        return (myTime != 0) ? myTime : evt.getUid().compareTo(getUid());
    }
    
    /**
     * Utility.
     *
     * @param evt
     *      current evenement
     * @param startTime
     *      begin time
     * @param endTime
     *      end time
     * @return
     *      if the event is between dates
     */
    public boolean isInInterval(long startTime, long endTime) {
        return (getTimestamp() >= startTime) && (getTimestamp() <= endTime);
    }   

    

}
