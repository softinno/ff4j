package org.ff4j.cassandra.driver;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createType;
import static com.datastax.oss.driver.api.querybuilder.relation.Relation.column;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.UserDefinedType;

/**
 * Grouping all constants related to FF4j data model in Cassandra in a 
 * single place to easy renaming or schema evolutions.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public interface CassandraSchema {
    
    String DEFAULT_KEYSPACE = "ff4j";
    
    String UDT_PARAM                     = "ff4j_param";
    String UDT_PARAM_UID                 = "uid";
    String UDT_PARAM_CLASSNAME           = "class_name";
    String UDT_PARAM_VALUE               = "value";
    
    String UDT_PARAM_FIXEDVALUES         = "fixed_values";
    String UDT_PARAM_DESCRIPTION         = "description";
    
    String UDT_TOGGLESTRATEGY            = "ff4j_toggle_strategy";
    String UDT_TOGGLESTRATEGY_CLASSNAME  = "class_name";
    String UDT_TOGGLESTRATEGY_PARAMS     = "params";
    
    String TABLE_SETTINGS                = "ff4j_settings";
    String TABLE_SETTINGS_PARAMNAME      = "param_name";
    String TABLE_SETTINGS_PARAM          = "param";
    
    String TABLE_ROLE                    = "ff4j_role";
    String TABLE_ROLE_NAME               = "role_name";
    String TABLE_ROLE_PERMISSIONS        = "permissions";
    
    String TABLE_USER                    = "ff4j_user";
    String TABLE_USER_UID                = "uid";
    String TABLE_USER_CREATED            = "created";
    String TABLE_USER_LASTMODIFIED       = "last_modified";
    String TABLE_USER_OWNER              = "owner";
    String TABLE_USER_DESCRIPTION        = "description";
    String TABLE_USER_FIRSTNAME          = "firstname";
    String TABLE_USER_LASTNAME           = "lastname";
    String TABLE_USER_PERMISSIONS        = "permissions";
    String TABLE_USER_ROLES              = "roles";
    
    String TABLE_FEATURE                    = "ff4j_feature";
    String TABLE_FEATURE_UID                = "uid";
    String TABLE_FEATURE_CREATED            = "created";
    String TABLE_FEATURE_LASTMODIFIED       = "last_modified";
    String TABLE_FEATURE_OWNER              = "owner";
    String TABLE_FEATURE_DESCRIPTION        = "description";
    String TABLE_FEATURE_ENABLE             = "enable";
    String TABLE_FEATURE_GROUPNAME          = "group_name";
    String TABLE_FEATURE_TOGGLE_STRATEGIES  = "toggle_strategies";
    String TABLE_FEATURE_PROPERTIES         = "properties";
    String TABLE_FEATURE_PERM_USERS         = "permissions_roles";
    String TABLE_FEATURE_PERM_ROLES         = "permissions_users";
    
    String TABLE_PROPERTY                    = "ff4j_property";
    String TABLE_PROPERTY_UID                = "uid";
    String TABLE_PROPERTY_CREATED            = "created";
    String TABLE_PROPERTY_LASTMODIFIED       = "last_modified";
    String TABLE_PROPERTY_OWNER              = "owner";
    String TABLE_PROPERTY_DESCRIPTION        = "description";
    String TABLE_PROPERTY_PARAM              = "param";
    String TABLE_PROPERTY_PERM_USERS         = "permissions_roles";
    String TABLE_PROPERTY_PERM_ROLES         = "permissions_users";
    
    String IDX_FEATURE_GROUP = "ff4j_feature_groupname_idx";
    
    default SimpleStatement schemaCreateUdtToggleStrategy(UserDefinedType udtParam) {
        return createType(UDT_TOGGLESTRATEGY).ifNotExists()
                  .withField(UDT_TOGGLESTRATEGY_CLASSNAME, DataTypes.TEXT)
                  .withField(UDT_TOGGLESTRATEGY_PARAMS, DataTypes.mapOf(DataTypes.TEXT, udtParam, true))
                  .build();
    }
    
    default SimpleStatement schemaCreateUdtParam() {
        return createType(UDT_PARAM).ifNotExists()
                  .withField(UDT_PARAM_UID, DataTypes.TEXT)
                  .withField(UDT_PARAM_CLASSNAME, DataTypes.TEXT)
                  .withField(UDT_PARAM_FIXEDVALUES, DataTypes.setOf(DataTypes.TEXT, true))
                  .withField(UDT_PARAM_DESCRIPTION, DataTypes.TEXT)
                  .build();
    }
    
    default SimpleStatement schemaCreateTableSettings(UserDefinedType udtParam) {
       return createTable(TABLE_SETTINGS)
                .ifNotExists()
                .withPartitionKey(TABLE_SETTINGS_PARAMNAME, DataTypes.TEXT)
                .withColumn(TABLE_SETTINGS_PARAM, udtParam)
                .build();
    }
    
    default SimpleStatement schemaCreateTableRole() {
        return createTable(TABLE_ROLE)
                .ifNotExists()
                .withPartitionKey(TABLE_ROLE_NAME, DataTypes.TEXT)
                .withColumn(TABLE_ROLE_PERMISSIONS, DataTypes.setOf(DataTypes.TEXT))
                .build();
    }
    
    default SimpleStatement schemaCreateTableUser() {
       return createTable(TABLE_USER)
                .ifNotExists()
                .withPartitionKey(TABLE_USER_UID, DataTypes.TEXT)
                .withColumn(TABLE_USER_CREATED, DataTypes.TIMESTAMP)
                .withColumn(TABLE_USER_LASTMODIFIED, DataTypes.TIMESTAMP)
                .withColumn(TABLE_USER_DESCRIPTION, DataTypes.TEXT)
                .withColumn(TABLE_USER_FIRSTNAME, DataTypes.TEXT)
                .withColumn(TABLE_USER_LASTNAME, DataTypes.TEXT)
                .withColumn(TABLE_USER_PERMISSIONS, DataTypes.setOf(DataTypes.TEXT))
                .withColumn(TABLE_USER_ROLES, DataTypes.setOf(DataTypes.TEXT))
                .build();
    }
    
    default SimpleStatement schemaCreateTableFeature(UserDefinedType udtParam,  UserDefinedType udtToggleStrategy) {
        return createTable(TABLE_FEATURE)
                .ifNotExists()
                .withPartitionKey(TABLE_FEATURE_UID,    DataTypes.TEXT)
                .withColumn(TABLE_FEATURE_CREATED,      DataTypes.TIMESTAMP)
                .withColumn(TABLE_FEATURE_LASTMODIFIED, DataTypes.TIMESTAMP)
                .withColumn(TABLE_FEATURE_OWNER,        DataTypes.TEXT)
                .withColumn(TABLE_FEATURE_DESCRIPTION,  DataTypes.TEXT)
                .withColumn(TABLE_FEATURE_ENABLE,       DataTypes.BOOLEAN)
                .withColumn(TABLE_FEATURE_GROUPNAME,    DataTypes.TEXT)
                .withColumn(TABLE_FEATURE_TOGGLE_STRATEGIES, DataTypes.listOf(udtToggleStrategy, true))
                .withColumn(TABLE_FEATURE_PROPERTIES, DataTypes.mapOf(DataTypes.TEXT, udtParam, true))
                .withColumn(TABLE_FEATURE_PERM_USERS, DataTypes.mapOf(DataTypes.TEXT, DataTypes.setOf(DataTypes.TEXT, true)))
                .withColumn(TABLE_FEATURE_PERM_ROLES, DataTypes.mapOf(DataTypes.TEXT, DataTypes.setOf(DataTypes.TEXT, true)))
                .build();
    }
    
    default SimpleStatement schemaCreateSecondaryIndices() {
        return createIndex(IDX_FEATURE_GROUP).ifNotExists()
                .onTable(TABLE_FEATURE)
                .andColumn(TABLE_FEATURE_GROUPNAME)
                .build();
    }
    
    default SimpleStatement schemaCreateTableProperty(UserDefinedType udtParam) {
        return createTable(TABLE_PROPERTY)
                .ifNotExists()
                .withPartitionKey(TABLE_PROPERTY_UID,    DataTypes.TEXT)
                .withColumn(TABLE_PROPERTY_CREATED,      DataTypes.TIMESTAMP)
                .withColumn(TABLE_PROPERTY_LASTMODIFIED, DataTypes.TIMESTAMP)
                .withColumn(TABLE_PROPERTY_OWNER,        DataTypes.TEXT)
                .withColumn(TABLE_PROPERTY_DESCRIPTION,  DataTypes.TEXT)
                .withColumn(TABLE_PROPERTY_PARAM,        udtParam)
                .withColumn(TABLE_PROPERTY_PERM_USERS, DataTypes.mapOf(DataTypes.TEXT, DataTypes.setOf(DataTypes.TEXT, true)))
                .withColumn(TABLE_PROPERTY_PERM_ROLES, DataTypes.mapOf(DataTypes.TEXT, DataTypes.setOf(DataTypes.TEXT, true)))
                .build();
    }
    
    /** SELECT group_name from ff4j_feature where group_name=:group_name */
    default SimpleStatement queryGroupExist() {
        return selectFrom(TABLE_FEATURE)
                .column(TABLE_FEATURE_GROUPNAME)
                .where(column(TABLE_FEATURE_GROUPNAME)
                .isEqualTo(bindMarker(TABLE_FEATURE_GROUPNAME)))
                .build();
    }
    
    /** SELECT uid from ff4j_feature where group_name=:group_name */
    default SimpleStatement queryGroupFind() {
        return selectFrom(TABLE_FEATURE)
                .column(TABLE_FEATURE_UID)
                .where(column(TABLE_FEATURE_GROUPNAME)
                .isEqualTo(bindMarker(TABLE_FEATURE_GROUPNAME)))
                .build();
    }
    
   default SimpleStatement queryGroupListNames() {
        return selectFrom(TABLE_FEATURE)
                .column(TABLE_FEATURE_GROUPNAME).build();
   }
   
   /** SELECT group_name from ff4j_feature where group_name=:group_name */
   default SimpleStatement queryFeatureExist() {
       return selectFrom(TABLE_FEATURE)
               .column(TABLE_FEATURE_UID)
               .where(column(TABLE_FEATURE_UID)
               .isEqualTo(bindMarker(TABLE_FEATURE_UID)))
               .build();
   }
   
   default SimpleStatement queryFeatureListIds() {
       return selectFrom(TABLE_FEATURE).column(TABLE_FEATURE_UID).build();
   }
   
   default SimpleStatement queryUpdateGroup() {
       return update(TABLE_FEATURE)
               .setColumn(TABLE_FEATURE_GROUPNAME, bindMarker())
               .setColumn(TABLE_FEATURE_LASTMODIFIED, bindMarker())
               .whereColumn(TABLE_FEATURE_UID).isEqualTo(bindMarker())
               .ifColumn(TABLE_FEATURE_GROUPNAME).isEqualTo(bindMarker())
               .build();
   }
    
    /**
     * Create schema for the different ff4j repositories.
     * 
     * @param cqlSession
     *      cqlSession
     * @param keyspaceName
     *      target keyspace name
     */
    default void schemaCreateAll(CqlSession cqlSession, String keyspaceName) {
        KeyspaceMetadata metaData = cqlSession.getMetadata().getKeyspace(keyspaceName).get();
        cqlSession.execute(schemaCreateUdtParam());
        UserDefinedType udtParam = metaData.getUserDefinedType(UDT_PARAM).get();
        cqlSession.execute(schemaCreateUdtToggleStrategy(udtParam));
        UserDefinedType udtToggleStrategy = metaData.getUserDefinedType(UDT_TOGGLESTRATEGY).get();
        cqlSession.execute(schemaCreateTableSettings(udtParam));
        cqlSession.execute(schemaCreateTableRole());
        cqlSession.execute(schemaCreateTableUser());
        cqlSession.execute(schemaCreateTableFeature(udtParam, udtToggleStrategy));
        cqlSession.execute(schemaCreateTableProperty(udtParam));
        cqlSession.execute(schemaCreateSecondaryIndices());
    }
    
}
