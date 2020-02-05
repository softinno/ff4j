package org.ff4j.cassandra.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ff4j.cassandra.driver.CassandraSchema;
import org.ff4j.core.security.FF4jAcl;
import org.ff4j.core.security.FF4jGrantees;
import org.ff4j.core.security.FF4jPermission;
import org.ff4j.core.utils.TimeUtils;
import org.ff4j.feature.Feature;
import org.ff4j.feature.ToggleStrategy;
import org.ff4j.feature.mapper.FeatureMapper;
import org.ff4j.property.Property;
import org.ff4j.property.PropertyFactory;

/**
 * Helper marshalling and unmarshalling data from Cassandra.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class FeatureEntityMapper implements CassandraSchema, FeatureMapper<FeatureEntity, FeatureEntity> {

    /** {@inheritDoc} */
    @Override
    public FeatureEntity mapToRepository(Feature f) {
        FeatureEntity fe = new FeatureEntity();
        fe.setUid(f.getUid());
        fe.setEnable(f.isEnabled());
        f.getGroup().ifPresent(fe::setGroupName);
        f.getDescription().ifPresent(fe::setDescription);
        f.getOwner().ifPresent(fe::setOwner);
        f.getCreationDate().ifPresent(d -> fe.setCreated(TimeUtils.toInstant(d)));
        f.getLastModifiedDate().ifPresent(d -> fe.setLastModified(TimeUtils.toInstant(d)));
        // ACL
        Map <String, Set<String>> pUsers = new HashMap<String, Set<String>>();
        Map <String, Set<String>> pRoles = new HashMap<String, Set<String>>();
        for (Entry<FF4jPermission, FF4jGrantees> p : f.getAccessControlList().getPermissions().entrySet()) {
            if (!p.getValue().getUsers().isEmpty()) {
                pUsers.put(p.getKey().toString(), p.getValue().getUsers());
            }
            if (!p.getValue().getRoles().isEmpty()) {
                pRoles.put(p.getKey().toString(), p.getValue().getRoles());
            }
        }
        fe.setPermissionsUsers(pUsers);
        fe.setPermissionsRoles(pRoles);
        fe.setProperties(f
                .getProperties().values()
                .stream().map(this::mapProperty)
                .collect(Collectors.toMap(UdtParam::getUid, Function.identity())));
        fe.setListOfToggleStrategies(f
                .getToggleStrategies().stream()
                .map(this::mapToggleStrategy)
                .collect(Collectors.toList()));
        return fe;
    }

    /** {@inheritDoc} */
    @Override
    public Feature mapFromRepository(FeatureEntity fe) {
        Feature f = new Feature(fe.getUid());
        f.setCreationDate(TimeUtils.asLocalDateTime(fe.getCreated()));
        f.setDescription(fe.getDescription());
        f.setEnable(fe.isEnable());
        f.setGroup(fe.getGroupName());
        f.setLastModified(TimeUtils.asLocalDateTime(fe.getLastModified()));
        f.setOwner(fe.getOwner());
        f.setProperties(fe.getProperties().values().stream().map(this::mapParam).toArray(Property[]::new));
        f.setToggleStrategies(fe.getListOfToggleStrategies().stream().map(udt -> this.mapUdtToggleStrategy(fe.getUid(), udt))
                .collect(Collectors.toList()));
        FF4jAcl acl = new FF4jAcl();
        for (Map.Entry<String, Set<String>> permissionRoles : fe.getPermissionsRoles().entrySet()) {
            permissionRoles.getValue().stream()
                    .forEach(role -> acl.grantRole(role, FF4jPermission.valueOf(permissionRoles.getKey())));
        }
        for (Map.Entry<String, Set<String>> permissionUsers : fe.getPermissionsUsers().entrySet()) {
            permissionUsers.getValue().stream()
                    .forEach(user -> acl.grantUser(user, FF4jPermission.valueOf(permissionUsers.getKey())));
        }
        f.setAccessControlList(acl);
        return f;
    }

    public Property<?> mapParam(UdtParam udt) {
        Optional<String> propertyClass = Property.mapFromSimple2PropertyType(udt.getClassName());
        Property<?> p = PropertyFactory.createProperty(
                udt.getUid(), propertyClass.orElse(udt.getClassName()), udt.getValue());
        udt.getFixedValues().forEach(p::add2FixedValueFromString);
        return p;
    }
    
    public UdtParam mapProperty(Property<?> p ) {
        UdtParam udt = new UdtParam();
        udt.setUid(p.getUid());
        udt.setClassName(p.getClassName());
        udt.setValue(p.getValueAsString());
        p.getFixedValues().ifPresent(vals -> vals
                .stream().map(Object::toString)
                .forEach(udt::addFixedValue));
        p.getDescription().ifPresent(udt::setDescription);
        return udt;
    }

    public ToggleStrategy mapUdtToggleStrategy(String uid, UdtToggleStrategy udt) {
        return ToggleStrategy.of(uid, udt.getClassname(), udt
                           .getParams().values().stream()
                           .map(this::mapParam)
                           .collect(Collectors.toSet()));
    }
    
    public UdtToggleStrategy mapToggleStrategy(ToggleStrategy ts) {
        UdtToggleStrategy udt = new UdtToggleStrategy();
        udt.setClassname(ts.getClassName());
        udt.setParams(ts.getProperties()
                .map(this::mapProperty)
                .collect(Collectors.toMap(UdtParam::getUid, Function.identity())));
        return udt;
    }

}
