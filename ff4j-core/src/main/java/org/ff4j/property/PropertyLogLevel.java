package org.ff4j.property;

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

import org.ff4j.property.PropertyLogLevel.LogLevel;

/**
 * Custom property to code a logLevel.
 *
 * @author Cedrick Lunven (@clunven)
 */
public class PropertyLogLevel extends Property<LogLevel> {

    /** Serial. */
    private static final long serialVersionUID = 1792311055570779010L;

    /** Expected Log Levels. */
    public static enum LogLevel {TRACE, DEBUG, INFO, WARN, ERROR, FATAL}
    
    /**
     * Constructor by string expression.
     *
     * @param uid
     *      unique name
     * @param lvl
     *      current log level
     */
    public PropertyLogLevel(String uid) {
       super(uid);
    }
    
    /**
     * Constructor by string expression.
     *
     * @param uid
     *      unique name
     * @param lvl
     *      current log level
     */
    public PropertyLogLevel(String uid, String lvl) {
       this(uid, LogLevel.valueOf(lvl));
    }
    
    /**
     * Constructor by enum expression.
     *
     * @param uid
     *      unique name
     * @param lvlv
     *      current log level
     */
    public PropertyLogLevel(String uid, LogLevel lvl) {
        super(uid, lvl);
        setFixedValues(LogLevel.values());
    }
    
    /** {@inheritDoc} */
    @Override
    public LogLevel fromString(String v) {
        assertStringValueIsNotNull(v);
        return LogLevel.valueOf(v);
    } 
    
    /**
     * update to trace
     */
    public PropertyLogLevel trace() {
        setValue(LogLevel.TRACE);
        return this;
    }

    /**
     * update to debug
     */
    public PropertyLogLevel debug() {
        setValue(LogLevel.DEBUG);
        return this;
    }  

    /**
     * update to ingo
     */
    public PropertyLogLevel info() {
        setValue(LogLevel.INFO);
        return this;
    }  

    /**
     * update to warn
     */
    public PropertyLogLevel warn() {
        setValue(LogLevel.WARN);
        return this;
    }  

    /**
     * update to error
     */
    public PropertyLogLevel error() {
        setValue(LogLevel.ERROR);
        return this;
    }  

    /**
     * update to fatal
     */
    public PropertyLogLevel fatal() {
        setValue(LogLevel.FATAL);
        return this;
    }
       
}
