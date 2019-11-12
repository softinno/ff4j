package org.ff4j.core.jdbc;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ff4j.core.utils.Util;

/**
 * Schema
 * @author Cedrick Lunven (@clunven)
 */
public interface JdbcSchema {
   
    /** Column Names. */
    String COLUMN_UID           = "UID";
    String COLUMN_CREATED       = "CREATED";
    String COLUMN_LASTMODIFIED  = "LASTMODIFIED";
    String COLUMN_OWNER         = "OWNER";
    String COLUMN_DESCRIPTION   = "DESCRIPTION";
    String COLUMN_CLASSNAME     = "CLASSNAME";
    String COLUMN_VALUE         = "VAL";
    String COLUMN_FIXEDVALUES   = "FIXEDVALUES";
    String COLUMN_REF_FEATURE   = "FEATURE_UID";
    String COLUMN_TIMESTAMP     = "TICK";
    
    
   
    /**
     * Representation of the JDBC Table FEATURES.
     */
    enum SQLTypes { VARCHAR, DATE, DATETIME, INTEGER, TIMESTAMP; }
    
    /**
     * Template to defined a column in a SQL Table
     * 
     * @author Cedrick LUNVEN  (@clunven)
     */
    interface SqlTableColumns {
        
        /** identifier for the column. */
        String colname();
        
        /** sql type. */
        SQLTypes type();
        
        /** size of column or null. */
        int size();
        
        /** if nullable. */
        boolean nullable();
        
        /** underlying table name. */
        String tableName();
        
        /** Table primary key definition. */
        List < SqlTableColumns > primaryKey();
        
        /**  Table foreign keys definitions. */
        Optional <Map < SqlTableColumns,SqlTableColumns >> foreignKey();
    }
    
    // ----------------------------------
    // ------- TABLE FEATURES -----------
    // ----------------------------------
    
    /**
     * Representation of the JDBC Table FEATURES.
     */
    enum FeaturesColumns implements SqlTableColumns {
        
        // Columns shared by all entities
        UID(COLUMN_UID, SQLTypes.VARCHAR, 100, true),
        CREATED(COLUMN_CREATED, SQLTypes.DATETIME, 0, false),
        LASTMODIFIED(COLUMN_LASTMODIFIED, SQLTypes.DATETIME, 0, false),
        OWNER(COLUMN_OWNER, SQLTypes.VARCHAR,   100, false),
        DESCRIPTION(COLUMN_DESCRIPTION, SQLTypes.VARCHAR, 255, false),
        
        // Specialization for the Feature
        ENABLE("ENABLE", SQLTypes.INTEGER, 0, true),
        GROUPNAME("GROUPNAME", SQLTypes.VARCHAR, 100, false);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private FeaturesColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "FEATURE"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { return Util.listOf(UID); }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns,SqlTableColumns >> foreignKey() { return Optional.empty(); }
    }

    // ---------------------------------
    // ----- TABLE PROPERTIES ----------
    // ---------------------------------
   
    /** Representation of the JDBC Table FEATURES. */
    enum PropertyColumns implements SqlTableColumns {
        
        // Columns shared by all entities
        UID(COLUMN_UID, SQLTypes.VARCHAR, 100, true),
        CREATED(COLUMN_CREATED, SQLTypes.DATETIME, 0, false),
        LASTMODIFIED(COLUMN_LASTMODIFIED, SQLTypes.DATETIME, 0, false),
        OWNER(COLUMN_OWNER, SQLTypes.VARCHAR,   100, false),
        DESCRIPTION(COLUMN_DESCRIPTION, SQLTypes.VARCHAR, 255, false),

        /// Specific property value
        CLASSNAME(COLUMN_CLASSNAME, SQLTypes.VARCHAR, 255, true),
        VALUE(COLUMN_VALUE, SQLTypes.VARCHAR, 255, true),
        FIXEDVALUES(COLUMN_FIXEDVALUES, SQLTypes.VARCHAR, 1000, false);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private PropertyColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }

        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "PROPERTY"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(UID); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            return Optional.empty(); 
        }
    }
    
    // ---------------------------------
    // --- TABLE CUSTOM PROPERTIES  ----
    // ---------------------------------
    
    /**
     * Representation of the JDBC Table FEATURES.
     */
    enum FeaturePropertyColumns implements SqlTableColumns {
        UID(COLUMN_UID,                     SQLTypes.VARCHAR, 100, true),
        CLASSNAME(COLUMN_CLASSNAME,         SQLTypes.VARCHAR, 255, true),
        CREATED(COLUMN_CREATED,             SQLTypes.DATETIME, 0, false),
        LASTMODIFIED(COLUMN_LASTMODIFIED,   SQLTypes.DATETIME, 0, false),
        OWNER(COLUMN_OWNER,                 SQLTypes.VARCHAR,   100, false),
        
        VALUE(COLUMN_VALUE, SQLTypes.VARCHAR, 255, true),
        FIXEDVALUES(COLUMN_FIXEDVALUES, SQLTypes.VARCHAR, 1000, false),
        FEATURE_UID(COLUMN_REF_FEATURE, SQLTypes.VARCHAR, 100, true);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private FeaturePropertyColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }

        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "FEATURE_PROP"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(UID, FEATURE_UID); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            return Optional.of(Util.mapOf(FEATURE_UID, FeaturesColumns.UID)); 
        }
    }
    
    // ---------------------------------
    // ------- TABLE AUDIT -------------
    // ---------------------------------
    
    /**
     * Representation of the JDBC Table FEATURES.
     */
    enum AuditTrailColumns implements SqlTableColumns {
        
        // Columns shared by all entities
        UID(COLUMN_UID, SQLTypes.VARCHAR, 100, true),
        CREATED(COLUMN_CREATED, SQLTypes.DATETIME, 0, true),
        LASTMODIFIED(COLUMN_LASTMODIFIED, SQLTypes.DATETIME, 0, true),
        OWNER(COLUMN_OWNER, SQLTypes.VARCHAR,   100, false),
        DESCRIPTION(COLUMN_DESCRIPTION, SQLTypes.VARCHAR, 255, false),
        
        TIMESTAMP(COLUMN_TIMESTAMP, SQLTypes.TIMESTAMP, 0, true),
        SCOPE("SCOPE",      SQLTypes.VARCHAR, 30, true),
        NAME("NAME",          SQLTypes.VARCHAR, 30, true),
        ACTION("ACTION",      SQLTypes.VARCHAR, 30, true),
        HOSTNAME("HOSTNAME",  SQLTypes.VARCHAR, 100, false),
        SOURCE("SOURCE",      SQLTypes.VARCHAR, 100, false),
        DURATION("DURATION",  SQLTypes.INTEGER, 0, true),
        VALUE(COLUMN_VALUE,   SQLTypes.VARCHAR, 100, false),
        KEYS("EVT_KEYS",      SQLTypes.VARCHAR, 1000, true);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private AuditTrailColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "AUDIT_TRAIL"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(UID, TIMESTAMP); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            return Optional.empty(); 
        }
    }

    // ---------------------------------
    // ------- TABLE METRICS -----------
    // ---------------------------------
    
    /**
     * Representation of the JDBC Table FEATURES.
     */
    enum FeatureHitColumns implements SqlTableColumns {
        
        // Columns shared by all entities
        UID(          COLUMN_UID,           SQLTypes.VARCHAR, 100, true),
        CREATED(      COLUMN_CREATED,       SQLTypes.DATETIME, 0, true),
        LASTMODIFIED( COLUMN_LASTMODIFIED,  SQLTypes.DATETIME, 0, true),
        OWNER(        COLUMN_OWNER,         SQLTypes.VARCHAR,   100, false),
        DESCRIPTION(  COLUMN_DESCRIPTION,   SQLTypes.VARCHAR, 255, false),
        
        TIMESTAMP(    COLUMN_TIMESTAMP,     SQLTypes.TIMESTAMP, 0, true),
        FEATURE_UID(COLUMN_REF_FEATURE,     SQLTypes.VARCHAR, 100,  true),
        HOSTNAME(     "HOSTNAME",           SQLTypes.VARCHAR, 100, false),
        SOURCE(       "SOURCE",             SQLTypes.VARCHAR, 100, false);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private FeatureHitColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "FEATURE_HIT"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(UID); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            return Optional.of(Util.mapOf(FEATURE_UID, FeaturesColumns.UID)); 
        }
    }
    
    // ---------------------------------------------
    // ------- TABLE PERMISSIONS (ACL) -------------
    // ---------------------------------------------
    
    /**
     * Representation of the JDBC Table ROLE.
     */
    enum FeaturePermissionColumns  implements SqlTableColumns {
        FEATURE_UID(COLUMN_REF_FEATURE, SQLTypes.VARCHAR, 100,  true),
        PERMISSION("PERMISSION",        SQLTypes.VARCHAR, 100,  true),
        USERS("USERS",                  SQLTypes.VARCHAR, 1000, false),
        ROLES("ROLES",                  SQLTypes.VARCHAR, 1000, false);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private FeaturePermissionColumns(String pname, SQLTypes ptype, int psize, boolean require) {
            name = pname;
            type = ptype;
            size = psize;
            required = require;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "FEATURE_PERM"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(FEATURE_UID, PERMISSION); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            return Optional.of(Util.mapOf(FEATURE_UID, FeaturesColumns.UID)); 
        }
    }
    
    /**
     * Representation of the JDBC Table ROLE.
     */
    enum FeatureToggleStrategyColumns  implements SqlTableColumns {
        FEATURE_UID(COLUMN_REF_FEATURE, SQLTypes.VARCHAR, 100, true),
        TOGGLE_CLASS(COLUMN_CLASSNAME,  SQLTypes.VARCHAR, 200, true);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private FeatureToggleStrategyColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "FEATURE_STRAT"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(FEATURE_UID, TOGGLE_CLASS); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            return Optional.of(Util.mapOf(FEATURE_UID, FeaturesColumns.UID)); 
        }
    }
    
    /**
     * Representation of the JDBC Table ROLE.
     */
    enum FeatureToggleStrategyPropertiesColumns  implements SqlTableColumns {
        // -- Core Property
        UID(COLUMN_UID, SQLTypes.VARCHAR, 100, true),
        CLASSNAME(COLUMN_CLASSNAME, SQLTypes.VARCHAR, 255, true),
        VALUE(COLUMN_VALUE, SQLTypes.VARCHAR, 255, true),
        FIXEDVALUES(COLUMN_FIXEDVALUES, SQLTypes.VARCHAR, 1000, false),
        
        // -- Relation to ToggleStrategy
        FEATURE_UID(COLUMN_REF_FEATURE, SQLTypes.VARCHAR, 100, true),
        TOGGLESTRATEGY_CLASSNAME("STRAT_CLASS", SQLTypes.VARCHAR, 200, true);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private FeatureToggleStrategyPropertiesColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "FEATURE_STRAT_P"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(UID, TOGGLESTRATEGY_CLASSNAME, FEATURE_UID); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            Map < SqlTableColumns, SqlTableColumns > foreignKey = new HashMap<>();
            foreignKey.put(TOGGLESTRATEGY_CLASSNAME, FeatureToggleStrategyColumns.TOGGLE_CLASS);
            foreignKey.put(FEATURE_UID, FeatureToggleStrategyColumns.FEATURE_UID);
            return Optional.of(foreignKey); 
        }
    }
    
    // ---------------------------------------------
    // ------- TABLE USER              -------------
    // ---------------------------------------------
    
    /**
     * Representation of the JDBC Table ROLE.
     */
    enum UsersColumns  implements SqlTableColumns {
        UID(COLUMN_UID, SQLTypes.VARCHAR, 100, true),
        CREATED(COLUMN_CREATED, SQLTypes.DATETIME, 0, false),
        LASTMODIFIED(COLUMN_LASTMODIFIED, SQLTypes.DATETIME, 0, false),
        OWNER(COLUMN_OWNER, SQLTypes.VARCHAR,   100, false),
        DESCRIPTION(COLUMN_DESCRIPTION, SQLTypes.VARCHAR, 255, false),
        
        PASSWORD("PASSWORD", SQLTypes.VARCHAR, 255, true),
        LASTNAME("LASTNAME", SQLTypes.VARCHAR, 100, false),
        FIRSTNAME("FIRSTNAME", SQLTypes.VARCHAR, 100, false);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private UsersColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "USER"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(UID); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() {
            return Optional.empty(); 
        }
    }

    /**
     * Representation of the JDBC Table ROLE.
     */
    enum RolesColumns  implements SqlTableColumns {
        UID(COLUMN_UID, SQLTypes.VARCHAR, 100, true),
        CREATED(COLUMN_CREATED, SQLTypes.DATETIME, 0, false),
        LASTMODIFIED(COLUMN_LASTMODIFIED, SQLTypes.DATETIME, 0, false),
        OWNER(COLUMN_OWNER, SQLTypes.VARCHAR,   100, false),
        DESCRIPTION(COLUMN_DESCRIPTION, SQLTypes.VARCHAR, 255, false);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private RolesColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "ROLE"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(UID); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() {
            return Optional.empty(); 
        }
    }
    
    /**
     * Representation of the JDBC Table ROLE.
     */
    enum RolesPermissionColumns  implements SqlTableColumns {
        ROLE_NAME("ROLE_NAME",   SQLTypes.VARCHAR, 100, true),
        PERMISSION("PERMISSION", SQLTypes.VARCHAR, 100, true);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private RolesPermissionColumns(String pname, SQLTypes ptype, int psize, boolean require) {
            name = pname;
            type = ptype;
            size = psize;
            required = require;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "ROLE_PERM"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(ROLE_NAME, PERMISSION); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            return Optional.of(Util.mapOf(ROLE_NAME, RolesColumns.UID)); 
        }
    }
    
    /**
     * Representation of the JDBC Table ROLE.
     */
    enum UsersPermissionColumns  implements SqlTableColumns {
        USER_UID("USER_UID",   SQLTypes.VARCHAR, 100, true),
        PERMISSION("PERMISSION", SQLTypes.VARCHAR, 100, true);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private UsersPermissionColumns(String pname, SQLTypes ptype, int psize, boolean require) {
            name = pname;
            type = ptype;
            size = psize;
            required = require;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "USER_PERM"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(USER_UID, PERMISSION); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() { 
            return Optional.of(Util.mapOf(USER_UID, UsersColumns.UID)); 
        }
    }
    
    /**
     * Representation of the JDBC Table ROLE.
     */
    enum UsersRolesColumns  implements SqlTableColumns {
        REF_USER("REF_USER", SQLTypes.VARCHAR, 100, true),
        REF_ROLE("REF_ROLE", SQLTypes.VARCHAR, 100, true);
        
        /** Column attribute */
        private final String name;
        
        /** Column attribute */
        private final SQLTypes type;
        
        /** Column attribute */
        private final int size;
        
        /** Column attribute */
        private final boolean required;
        
        /**
         * Private constructor.
         *
         * @param pname
         *      column name
         * @param ptype
         *      column type (depends on underlying JDBC DB NUMBER, INTEGER, but still useful 
         * @param psize
         *      column size
         * @param pnullabz
         */
        private UsersRolesColumns(String pname, SQLTypes ptype, int psize, boolean pnullable) {
            name = pname;
            type = ptype;
            size = psize;
            required = pnullable;
        }
        
        /** {@inheritDoc} */
        public String colname() { return name; }
        
        /** {@inheritDoc} */
        public SQLTypes type()  { return type; }
        
        /** {@inheritDoc} */
        public int size()       { return size; }
        
        /** {@inheritDoc} */
        public boolean nullable()  { return !required; }
        
        /** {@inheritDoc} */
        public String tableName() { return "USER_ROLE_A"; }
        
        /** {@inheritDoc} */
        public List < SqlTableColumns > primaryKey() { 
            return Util.listOf(REF_USER, REF_ROLE); 
        }
        
        /** {@inheritDoc} */
        public Optional <Map < SqlTableColumns, SqlTableColumns >> foreignKey() {
            Map < SqlTableColumns, SqlTableColumns > foreignKeysMap = new HashMap<>();
            foreignKeysMap.put(REF_USER, UsersColumns.UID);
            foreignKeysMap.put(REF_ROLE, RolesColumns.UID);
            return Optional.of(foreignKeysMap);
        }
    }
    
}
