package org.ff4j.cassandra.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ff4j.cassandra.driver.CassandraSchema;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

/**
 * Mapping entity.
 * 
 * CREATE TABLE IF NOT EXISTS ff4j_feature (
 *   uid                text,
 *   created            timestamp,
 *   last_modified      timestamp,
 *   owner              text,
 *   description        text,
 *   enable             boolean,
 *   group_name         text,
 *   toggle_strategies  list<frozen<ff4j_toggle_strategy>>,
 *   properties         map<text, frozen<ff4j_param>>,
 *   permissions_roles  map<text, frozen< set<text>> >,
 *   permissions_users  map<text, frozen< set<text>> >,
 *   PRIMARY KEY ((uid))
 * );
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Entity
@CqlName(CassandraSchema.TABLE_FEATURE)
public class FeatureEntity implements CassandraSchema, Serializable {

    /** Serial. */
    private static final long serialVersionUID = 7614576038272418567L;

    @PartitionKey
    @CqlName(TABLE_FEATURE_UID)
    private String uid;
    
    @CqlName(TABLE_FEATURE_CREATED)
    private Instant created;
    
    @CqlName(TABLE_FEATURE_LASTMODIFIED)
    private Instant lastModified;
    
    @CqlName(TABLE_FEATURE_OWNER)
    private String owner;
    
    @CqlName(TABLE_FEATURE_DESCRIPTION)
    private String description;
    
    @CqlName(TABLE_FEATURE_ENABLE)
    private boolean enable;
    
    @CqlName(TABLE_FEATURE_GROUPNAME)
    private String groupName;
    
    @CqlName(TABLE_FEATURE_TOGGLE_STRATEGIES)
    private List <UdtToggleStrategy > listOfToggleStrategies = new ArrayList<>();
    
    @CqlName(TABLE_FEATURE_PROPERTIES)
    private Map <String, UdtParam > properties = new HashMap<>();
    
    @CqlName(TABLE_FEATURE_PERM_USERS)
    private Map <String, Set<String> > permissionsUsers;
    
    @CqlName(TABLE_FEATURE_PERM_ROLES)
    private Map <String, Set<String> > permissionsRoles;
    
    /**
     * Default constructor.
     */
    public FeatureEntity() {}

    /**
     * Getter accessor for attribute 'uid'.
     *
     * @return
     *       current value of 'uid'
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter accessor for attribute 'uid'.
     * @param uid
     * 		new value for 'uid '
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter accessor for attribute 'created'.
     *
     * @return
     *       current value of 'created'
     */
    public Instant getCreated() {
        return created;
    }

    /**
     * Setter accessor for attribute 'created'.
     * @param created
     * 		new value for 'created '
     */
    public void setCreated(Instant created) {
        this.created = created;
    }

    /**
     * Getter accessor for attribute 'lastModified'.
     *
     * @return
     *       current value of 'lastModified'
     */
    public Instant getLastModified() {
        return lastModified;
    }

    /**
     * Setter accessor for attribute 'lastModified'.
     * @param lastModified
     * 		new value for 'lastModified '
     */
    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Getter accessor for attribute 'owner'.
     *
     * @return
     *       current value of 'owner'
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Setter accessor for attribute 'owner'.
     * @param owner
     * 		new value for 'owner '
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Getter accessor for attribute 'description'.
     *
     * @return
     *       current value of 'description'
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter accessor for attribute 'description'.
     * @param description
     * 		new value for 'description '
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter accessor for attribute 'enable'.
     *
     * @return
     *       current value of 'enable'
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Setter accessor for attribute 'enable'.
     * @param enable
     * 		new value for 'enable '
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Getter accessor for attribute 'groupName'.
     *
     * @return
     *       current value of 'groupName'
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Setter accessor for attribute 'groupName'.
     * @param groupName
     * 		new value for 'groupName '
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Getter accessor for attribute 'listOfToggleStrategies'.
     *
     * @return
     *       current value of 'listOfToggleStrategies'
     */
    public List<UdtToggleStrategy> getListOfToggleStrategies() {
        return listOfToggleStrategies;
    }

    /**
     * Setter accessor for attribute 'listOfToggleStrategies'.
     * @param listOfToggleStrategies
     * 		new value for 'listOfToggleStrategies '
     */
    public void setListOfToggleStrategies(List<UdtToggleStrategy> listOfToggleStrategies) {
        this.listOfToggleStrategies = listOfToggleStrategies;
    }

    /**
     * Getter accessor for attribute 'properties'.
     *
     * @return
     *       current value of 'properties'
     */
    public Map<String, UdtParam> getProperties() {
        return properties;
    }

    /**
     * Setter accessor for attribute 'properties'.
     * @param properties
     * 		new value for 'properties '
     */
    public void setProperties(Map<String, UdtParam> properties) {
        this.properties = properties;
    }

    /**
     * Getter accessor for attribute 'permissionsUsers'.
     *
     * @return
     *       current value of 'permissionsUsers'
     */
    public Map<String, Set<String>> getPermissionsUsers() {
        return permissionsUsers;
    }

    /**
     * Setter accessor for attribute 'permissionsUsers'.
     * @param permissionsUsers
     * 		new value for 'permissionsUsers '
     */
    public void setPermissionsUsers(Map<String, Set<String>> permissionsUsers) {
        this.permissionsUsers = permissionsUsers;
    }

    /**
     * Getter accessor for attribute 'permissionsRoles'.
     *
     * @return
     *       current value of 'permissionsRoles'
     */
    public Map<String, Set<String>> getPermissionsRoles() {
        return permissionsRoles;
    }

    /**
     * Setter accessor for attribute 'permissionsRoles'.
     * @param permissionsRoles
     * 		new value for 'permissionsRoles '
     */
    public void setPermissionsRoles(Map<String, Set<String>> permissionsRoles) {
        this.permissionsRoles = permissionsRoles;
    }
    
}
