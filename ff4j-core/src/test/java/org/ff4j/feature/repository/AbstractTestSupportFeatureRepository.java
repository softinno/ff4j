package org.ff4j.feature.repository;

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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.ff4j.core.FF4j;
import org.ff4j.core.config.FF4jConfiguration;
import org.ff4j.core.config.FF4jConfigurationParser;
import org.ff4j.core.exception.AssertionViolationException;
import org.ff4j.core.security.FF4jPermission;
import org.ff4j.core.test.AssertFF4j;
import org.ff4j.core.test.FF4jTestDataSet;
import org.ff4j.core.utils.Util;
import org.ff4j.event.repository.audit.AuditTrailRepositoryInMemory;
import org.ff4j.feature.Feature;
import org.ff4j.feature.FeatureRepositoryListenerConsole;
import org.ff4j.feature.ToggleStrategy;
import org.ff4j.feature.exception.FeatureNotFoundException;
import org.ff4j.feature.exception.GroupNotFoundException;
import org.ff4j.feature.togglestrategy.PonderationToggleStrategy;
import org.ff4j.property.Property;
import org.ff4j.property.PropertyBoolean;
import org.ff4j.property.PropertyDouble;
import org.ff4j.property.PropertyInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Mutualization of unit tests for every implementation of {@link FeatureRepository}.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public abstract class AbstractTestSupportFeatureRepository implements FF4jTestDataSet {

    /** Initialize */
	protected FF4j ff4j = null;

	/** Tested Store. */
	protected FeatureRepository testedStore;

	/** Test Values */
	protected AssertFF4j assertFF4j;
	
	/** DataSet. **/
	protected FF4jConfiguration testDataSet;
	
	/** {@inheritDoc} */
	@BeforeEach
	public void setUp() throws Exception {
	    FF4jConfigurationParser.clearCache();
	    testedStore = initStore();
	    ff4j = new FF4j().withRepositoryFeatures(testedStore)
	                     .withAudit(new AuditTrailRepositoryInMemory());
	    assertFF4j  = new AssertFF4j(ff4j);
        testDataSet = expectConfig();
	}

	/**
	 * Any store test will declare its store through this callback.
	 * 
	 * @return working feature store
	 * @throws Exception
	 *          Hi guys, just let you know I did the update in the presentation : changing instructors names to put the 2 of you    error during building feature store
	 */
	protected abstract FeatureRepository initStore();

	@Test
	public void should_throw_AssertionViolationException_with_null_filename() {
	    assertThrows(AssertionViolationException.class, () -> { testedStore.read(null); });
	}

	@Test
	public void should_throw_AssertionViolationException_with_empty_filename() {
        assertThrows(AssertionViolationException.class, () -> { testedStore.read(""); });
	}
	
	@Test
    public void should_match_expected_features_when_findIds() {
        // Given
        assertFF4j.assertThatStoreHasSize(testDataSet.getFeatures().size());
        Set < String > featuresNames = Util.asSet(testedStore.findAllIds());
        Assertions.assertEquals(testDataSet.getFeatures().size(), featuresNames.size());
        Assertions.assertTrue(featuresNames.contains(F1));
        Assertions.assertTrue(featuresNames.contains(F2));
        Assertions.assertTrue(featuresNames.contains(F3));
        Assertions.assertTrue(featuresNames.contains(F4));
	}
	
	@Test
    public void should_match_expected_features_when_findAll() {
        // Given
        assertFF4j.assertThatStoreHasSize(testDataSet.getFeatures().size());
        assertFF4j.assertThatFeatureExist(F1);
        assertFF4j.assertThatFeatureExist(F2);
        assertFF4j.assertThatFeatureExist(F3);
        assertFF4j.assertThatFeatureExist(F4);
        // When
        Map < String, Feature > features = Util.toMap(testedStore.findAll());
        // Then
        Assertions.assertEquals(testDataSet.getFeatures().size(), features.size());
        Feature f4 = testedStore.read(F4);
        Assertions.assertEquals(F4, f4.getUid());
        Assertions.assertTrue(f4.getDescription().isPresent() && !"".equals(f4.getDescription().get()));
        assertFF4j.assertThatFeatureIsInGroup(F4, GRP1);
      
        Feature f2 = features.get(F2);
        Assertions.assertNotNull(f2);
        Assertions.assertNotNull(f2.getUid());
        // Features -- Properties
        Assertions.assertNotNull(f2.getProperties());
        Assertions.assertNotNull(f2.getProperties().get("ppint"));
        Assertions.assertEquals(12, f2.getProperties().get("ppint").getValueAsInt());
        Assertions.assertEquals(12.5, f2.getProperties().get("ppdouble").getValueAsDouble());
        Assertions.assertEquals(true, f2.getProperties().get("ppboolean").getValueAsBoolean());
        Assertions.assertEquals("hello", f2.getProperties().get("ppstring").getValueAsString(), "hello");
        Assertions.assertEquals("NA",    f2.getProperties().get("regionIdentifier").getValueAsString());
        Assertions.assertTrue(f2.getProperties().get("regionIdentifier").getFixedValues().isPresent());
        // Features -- ToggleStrategies
        Assertions.assertFalse(f2.getToggleStrategies().isEmpty());
        ToggleStrategy tp = f2.getToggleStrategies().get(0);
        Assertions.assertEquals(PonderationToggleStrategy.class.getName(), tp.getClass().getName());
        PonderationToggleStrategy pts = (PonderationToggleStrategy) tp;
        Assertions.assertEquals(Double.valueOf(1), 
                pts.getPropertiesAsMap().get(PonderationToggleStrategy.PARAM_WEIGHT).getValue());
        // Features -- Permissions
        Assertions.assertFalse(f2.getAccessControlList().getPermissions().isEmpty());
        Assertions.assertTrue(f2.getAccessControlList().getPermissions().containsKey(FF4jPermission.FEATURE_TOGGLE));
    }
	
	@Test
	public void should_throw_FeatureNotFoundException_when_read_invalid_feature() {
        assertThrows(FeatureNotFoundException.class, () -> { testedStore.read("do-not-exist"); });
	}

	@Test
	public void should_match_dataset_when_read_f2() {
		// Given
	    Feature expectedF2 = testDataSet.getFeatures().get(F2);
	    // When
	    assertFF4j.assertThatFeatureExist(F2);
		Feature f2 = testedStore.read(F2);
		// uid
		Assertions.assertNotNull(f2);
		Assertions.assertEquals(f2.getUid(), expectedF2.getUid());
		// description
        Assertions.assertTrue(f2.getDescription().isPresent());
		Assertions.assertEquals(f2.getDescription().get(), f2.getDescription().get());
		// groupName
		Assertions.assertTrue(f2.getGroup().isPresent());
		Assertions.assertEquals(f2.getGroup().get(), f2.getGroup().get());
		// permissions
		Assertions.assertTrue(f2.getAccessControlList().getPermissions() != null && 
                !f2.getAccessControlList().getPermissions().isEmpty());
        // strategies
		Assertions.assertFalse(f2.getToggleStrategies().isEmpty());
		Assertions.assertEquals(
		            expectedF2.getToggleStrategies().get(0).getClass(), 
		            f2.getToggleStrategies().get(0).getClass());
		Assertions.assertEquals(
		   expectedF2.getToggleStrategies().get(0).getPropertiesAsMap().get(PonderationToggleStrategy.PARAM_WEIGHT).getValueAsString(), 
		           f2.getToggleStrategies().get(0).getPropertiesAsMap().get(PonderationToggleStrategy.PARAM_WEIGHT).getValueAsString());
        // properties
		Assertions.assertFalse(f2.getProperties().isEmpty());
		Assertions.assertEquals(expectedF2.getProperties().size(), f2.getProperties().size());
		Assertions.assertTrue(f2.getProperties().containsKey(P_F2_PPINT));
		Assertions.assertTrue(f2.getProperties().containsKey(P_F2_PDOUBLE));
		assertFF4j.assertThatFeatureIsInGroup(F2, GRP1);
	}

	@Test
	public void should_throw_AssertionViolationException_when_toggle_null() {
	    assertThrows(AssertionViolationException.class, () -> { testedStore.toggleOn(null); });
	    assertThrows(AssertionViolationException.class, () -> { testedStore.toggleOff(null); });
	}
	
	@Test
	public void should_throw_AssertionViolationException_when_toggle_empty() {
	    assertThrows(AssertionViolationException.class, () -> { testedStore.toggleOn(""); });
	    assertThrows(AssertionViolationException.class, () -> { testedStore.toggleOff(""); });
	}
	
	@Test
	public void should_throw_FeatureNotFoundException_when_toggle_unexisting_feature() {
		assertThrows(FeatureNotFoundException.class, () -> { testedStore.toggleOn("does-not-exist"); });
		assertThrows(FeatureNotFoundException.class, () -> { testedStore.toggleOff("does-not-exist"); });
	}
	
	@Test
	public void should_change_status_when_toggle_feature() {
		// Given
		assertFF4j.assertThatFeatureExist(F1);
		assertFF4j.assertThatFeatureIsDisabled(F1);
		// When (1)
		testedStore.toggleOn(F1);
		assertFF4j.assertThatFeatureIsEnabled(F1);
		// When (2)
		testedStore.toggleOff(F1);
		assertFF4j.assertThatFeatureIsDisabled(F1);
	}
	
	@Test
	public void should_throw_AssertionViolationException_when_saveFeature_null() throws Exception {
	    assertThrows(AssertionViolationException.class, () -> { testedStore.saveFeature(null); });
	}
	
	@Test
	public void should_create_feature_when_save() throws Exception {
		assertFF4j.assertThatFeatureDoesNotExist(FEATURE_FOR_TEST);
		Feature fp = new Feature(FEATURE_FOR_TEST).toggleOn().description("description").group(GRP1);
		testedStore.save(fp);
		assertFF4j.assertThatStoreHasSize(testDataSet.getFeatures().size() + 1);
		assertFF4j.assertThatFeatureExist(FEATURE_FOR_TEST);
		assertFF4j.assertThatFeatureIsInGroup(FEATURE_FOR_TEST, GRP1);
	}
	
    @Test
    public void should_remove_feature_when_delete() throws Exception {
        assertFF4j.assertThatFeatureExist(F1);
        testedStore.delete(F1);
        assertFF4j.assertThatStoreHasSize(testDataSet.getFeatures().size() - 1);
        assertFF4j.assertThatFeatureDoesNotExist(F1);
    }
	
	@Test
	public void should_throw_AssertionViolationException_when_delete_null() throws Exception {
	    assertThrows(AssertionViolationException.class, () -> { testedStore.deleteFeature((String) null); });
	}
	
	@Test
	public void should_throw_FeatureNotFoundException_when_delete_unexisting_feature() throws Exception {
	    assertFF4j.assertThatFeatureDoesNotExist(FEATURE_FOR_TEST);
	    assertThrows(FeatureNotFoundException.class, () -> { 
	        testedStore.delete(FEATURE_FOR_TEST); 
	    });
	}

	@Test
    public void should_throw_AssertionViolationException_when_save_null() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { 
            testedStore.save((Feature) null); 
        });
    }
	
	@Test
    public void should_create_feature_when_save_new() throws Exception {
        assertFF4j.assertThatFeatureDoesNotExist(FEATURE_FOR_TEST);
        testedStore.save(new Feature(FEATURE_FOR_TEST));
        assertFF4j.assertThatFeatureExist(FEATURE_FOR_TEST);
    }
	
	@Test
	public void should_udpate_feature_when_save_existing() {
	    // Givens
	    String newDescription = "new-description";
        assertFF4j.assertThatFeatureExist(F1);
	    Assertions.assertFalse(newDescription.equals(testedStore.read(F1).getDescription().get()));
	    Assertions.assertTrue(testedStore.read(F1).getToggleStrategies().isEmpty());
		// When
		Feature fpBis = testedStore.read(F1);
		fpBis.setDescription(newDescription);
		fpBis.getToggleStrategies().add(ToggleStrategy.of(F1,
		        PonderationToggleStrategy.class.getName(), 
		        Util.setOf(new PropertyDouble(PonderationToggleStrategy.PARAM_WEIGHT, Double.valueOf(0.12)))));
		testedStore.save(fpBis);
		// Then
		Feature updatedFeature = testedStore.read(F1);
		Assertions.assertTrue(newDescription.equals(updatedFeature.getDescription().get()));
		Assertions.assertFalse(testedStore.read(F1).getToggleStrategies().isEmpty());
		Assertions.assertEquals(
		        testedStore.read(F1).getToggleStrategies().get(0).getPropertiesAsMap().get(PonderationToggleStrategy.PARAM_WEIGHT).getValueAsDouble(), 
		        updatedFeature.getToggleStrategies().get(0).getPropertiesAsMap().get(PonderationToggleStrategy.PARAM_WEIGHT).getValueAsDouble());
	}

	@Test
    public void should_throw_AssertionViolationException_when_exist_with_null() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.exists(null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.exists(""); });
    }
	
	@Test
    public void should_throw_AssertionViolationException_when_existGroup_with_null() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.existGroup(null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.existGroup(""); });
    }
	
	@Test
	public void should_return_correct_flag_when_existGroup() {
		// Given
	    assertFF4j.assertThatGroupExist(GRP1);
	    assertFF4j.assertThatGroupDoesNotExist("GRPY");
		// Then
		Assertions.assertTrue(testedStore.existGroup(GRP1));
		Assertions.assertFalse(testedStore.existGroup("GRPY"));
	}
	
	@Test
	public void should_throw_AssertionViolationException_when_nullorEmpty() throws Exception {
        assertThrows(AssertionViolationException.class, () -> { testedStore.toggleOnGroup(""); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.toggleOnGroup(null); });
        
        assertThrows(AssertionViolationException.class, () -> { testedStore.toggleOffGroup(""); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.toggleOffGroup(null); });
        
        assertThrows(AssertionViolationException.class, () -> { testedStore.readGroup(null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.readGroup(""); });
        
        assertThrows(AssertionViolationException.class, () -> { testedStore.addToGroup(null, GRP1); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.addToGroup("", GRP1); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.addToGroup(F1, null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.addToGroup(F1, ""); });
        
        assertThrows(AssertionViolationException.class, () -> { testedStore.removeFromGroup(null, GRP1); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.removeFromGroup("", GRP1); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.removeFromGroup(F1, null); });
        assertThrows(AssertionViolationException.class, () -> { testedStore.removeFromGroup(F1, ""); });
    }

	@Test
    public void should_toggle_all_related_features_when_toggle_group() {
		// Given
	    assertFF4j.assertThatGroupExist(GRP1);
	    assertFF4j.assertThatFeatureIsInGroup(F2, GRP1);
	    assertFF4j.assertThatFeatureIsInGroup(F4, GRP1);
	    assertFF4j.assertThatFeatureIsEnabled(F2);
	    assertFF4j.assertThatFeatureIsEnabled(F4);
	    // When
		testedStore.toggleOffGroup(GRP1);
		// Then
		assertFF4j.assertThatFeatureIsDisabled(F2);
	    assertFF4j.assertThatFeatureIsDisabled(F4);
	    // WHen
	    testedStore.toggleOnGroup(GRP1);
	    // Then
	    assertFF4j.assertThatFeatureIsEnabled(F2);
        assertFF4j.assertThatFeatureIsEnabled(F4);
	}
	
	@Test
    public void should_throw_GroupNotFoundExceptionn_when_group_does_not_exist() {
	    assertFF4j.assertThatGroupDoesNotExist(GRPX);
	    assertThrows(GroupNotFoundException.class, () -> { testedStore.toggleOnGroup(GRPX); });
	    assertThrows(GroupNotFoundException.class, () -> { testedStore.toggleOffGroup(GRPX); });
	}
	
	@Test
    public void should_return_expected_features_when_read_group() {
		// Given
	    assertFF4j.assertThatGroupExist(GRP1);
	    assertFF4j.assertThatFeatureIsInGroup(F2, GRP1);
        assertFF4j.assertThatFeatureIsInGroup(F4, GRP1);
		// When
		List <Feature> group = testedStore.readGroup(GRP1).collect(Collectors.toList());
		// Then
		Assertions.assertEquals(2, group.size());
		Assertions.assertTrue(group.stream().anyMatch(f -> F2.equals(f.getUid())));
		Assertions.assertTrue(group.stream().anyMatch(f -> F4.equals(f.getUid())));
	}
	
	@Test
    public void should_throw_GroupNotFoundException_when_readGroup_doesnotexist() {
	    assertFF4j.assertThatGroupDoesNotExist(GRPX);
        assertThrows(GroupNotFoundException.class, () -> { testedStore.readGroup(GRPX); });
    }
	
	@Test
    public void should_throw_FeatureNotFoundException_when_addToGroup_feature_doesnotexist() throws Exception {
	    assertThrows(FeatureNotFoundException.class, () -> { testedStore.addToGroup(FEATURE_FOR_TEST, GRP1); });
	}
	
	@Test
    public void should_create_group_when_addToGroup_in_group_doesnotexist() throws Exception {
	    // Given
        assertFF4j.assertThatGroupDoesNotExist(GRPX);
        // When
        testedStore.addToGroup(F1, GRPX);
        // Then
        assertFF4j.assertThatGroupExist(GRPX);
        assertFF4j.assertThatFeatureIsInGroup(F1, GRPX);
        assertFF4j.assertThatGroupHasSize(1, GRPX);
    }
	
	@Test
    public void should_reduce_group_size_when_removeFromGroup() throws Exception {
	    // Given
	    assertFF4j.assertThatGroupExist(GRP1);
	    assertFF4j.assertThatGroupHasSize(2, GRP1);
	    assertFF4j.assertThatFeatureIsInGroup(F2, GRP1);
	    // When
	    testedStore.removeFromGroup(F2, GRP1);
	    // Then
	    assertFF4j.assertThatGroupHasSize(1, GRP1);
	    Assertions.assertFalse(testedStore.read(F2).getGroup().isPresent());
	}

	@Test
    public void should_delete_group_when_removeFromGroup_last_feature() {
		// Given
	    assertFF4j.assertThatGroupExist(GRP0);
        assertFF4j.assertThatGroupHasSize(1, GRP0);
        assertFF4j.assertThatFeatureIsInGroup(F3, GRP0);
        // When
        testedStore.removeFromGroup(F3, GRP0);
        // Then
        assertFF4j.assertThatGroupDoesNotExist(GRP0);
        Assertions.assertFalse(testedStore.read(F3).getGroup().isPresent());
	}

	@Test
    public void should_throw_FeatureNotFoundException_when_removeFromGroup_feature_doesnotexist() {
		// Given
	    assertFF4j.assertThatGroupExist(GRP0);
	    assertFF4j.assertThatFeatureDoesNotExist(FEATURE_FOR_TEST);
	    assertThrows(FeatureNotFoundException.class, () -> { 
	        testedStore.removeFromGroup(FEATURE_FOR_TEST, GRP0); });
	}
	
	@Test
    public void should_throw_GroupNotFoundException_when_removeFromGroup_group_doesnotexist() {
        assertFF4j.assertThatGroupDoesNotExist(GRPX);
        assertFF4j.assertThatFeatureExist(F1);
        assertThrows(GroupNotFoundException.class, () -> { 
            testedStore.removeFromGroup(F1, GRPX); });
    }
	
	@Test
    public void should_pass_when_removeFromGroup_invalid_group() {
        // Given
	    assertFF4j.assertThatGroupExist(GRP0);
        assertFF4j.assertThatGroupExist(GRP1);
        assertFF4j.assertThatFeatureExist(F2);
        assertFF4j.assertThatFeatureIsInGroup(F2, GRP1);
        // When
        testedStore.removeFromGroup(F2, GRP0);
        // Then
        assertFF4j.assertThatFeatureIsInGroup(F2, GRP1);
	}

	@Test
    public void should_match_expected_dataset_when_listGroupNames() {
		// Given
	    assertFF4j.assertThatStoreHasNumberOfGroups(2);
	    assertFF4j.assertThatGroupExist(GRP0);
	    assertFF4j.assertThatGroupExist(GRP1);
		// When
		Set<String> groups = testedStore.listGroupNames()
		                                .collect(Collectors.toSet());
		// Then
		Assertions.assertEquals(2, groups.size());
		Assertions.assertTrue(groups.contains(GRP0));
		Assertions.assertTrue(groups.contains(GRP1));
	}

	@Test
    public void should_update_feature_when_add_toggleStrategy() {
		// Given
	    assertFF4j.assertThatFeatureExist(F3);
	    Feature startFeature = testedStore.read(F3);
	    Assertions.assertTrue(startFeature.getToggleStrategies().isEmpty());
		// When
	    startFeature.addToggleStrategy(ToggleStrategy.of(F3,
                PonderationToggleStrategy.class.getName(), 
                Util.setOf(new PropertyDouble(PonderationToggleStrategy.PARAM_WEIGHT, Double.valueOf(1)))));
	    
	    testedStore.save(startFeature);
		// Then
		assertFF4j.assertThatFeatureHasFlippingStrategy(F3);
		Feature endFeature = testedStore.read(F3);
		Assertions.assertFalse(startFeature.getToggleStrategies().isEmpty());
		Assertions.assertEquals(PonderationToggleStrategy.class, 
		        endFeature.getToggleStrategies().get(0).getClass());
	}

	@Test
    public void should_update_feature_when_remove_toggleStrategy() {
		// Given
	    assertFF4j.assertThatFeatureExist(F4);
        Feature startFeature = testedStore.read(F4);
        Assertions.assertFalse(startFeature.getToggleStrategies().isEmpty());
        // When
        startFeature.getToggleStrategies().remove(0);
        testedStore.save(startFeature);
        // Then
        Feature endFeature = testedStore.read(F3);
        Assertions.assertTrue(endFeature.getToggleStrategies().isEmpty());
	}

	@Test
    public void should_empty_store_when_deleteAll() {
	    // Given
	    Assertions.assertNotEquals(0, testedStore.findAll().count());
	    // When
		testedStore.deleteAll();
		// Then
		Assertions.assertEquals(0, testedStore.findAll().count());
	}

	@Test
    public void should_update_feature_when_add_property() {
        // Given
        assertFF4j.assertThatFeatureExist(F3);
        Feature startFeature = testedStore.read(F3);
        Assertions.assertTrue(startFeature.getProperties().isEmpty());
        // When
        startFeature.addProperty(new PropertyBoolean(PROP_FLAG, true));
        testedStore.save(startFeature);
        // Then
        assertFF4j.assertThatFeatureHasProperties(F3);
        assertFF4j.assertThatFeatureHasProperty(F3, PROP_FLAG);
        Feature endFeature = testedStore.read(F3);
        Assertions.assertFalse(startFeature.getProperties().isEmpty());
        Assertions.assertEquals(PropertyBoolean.class, 
                endFeature.getProperties().get(PROP_FLAG).getClass());
	}
	
	@Test
    public void should_update_feature_when_remove_property() {
        // Given
        assertFF4j.assertThatFeatureExist(F2);
        Feature startFeature = testedStore.read(F2);
        Assertions.assertFalse(startFeature.getProperties().isEmpty());
        // When
        startFeature.getProperties().clear();
        testedStore.save(startFeature);
        // Then
        Feature endFeature = testedStore.read(F2);
        Assertions.assertTrue(endFeature.getProperties().isEmpty());
    }
	
	@Test
    public void should_update_feature_when_update_property() {
        // Given
        assertFF4j.assertThatFeatureExist(F2);
        Feature startFeature = testedStore.read(F2);
        Assertions.assertFalse(startFeature.getProperties().isEmpty());
        assertFF4j.assertThatFeatureHasProperty(F2, P_F2_PPINT);
        Assertions.assertEquals(12,startFeature.getProperties().get(P_F2_PPINT).getValueAsInt());
        // When
        startFeature.getProperties().get(P_F2_PPINT).setValueFromString("20");
        testedStore.save(startFeature);
        // Then
        Feature endFeature = testedStore.read(F2);
        Assertions.assertEquals(20, endFeature.getProperties().get(P_F2_PPINT).getValueAsInt());        
    }

    @Test
    public void should_update_feature_when_addFixedValue_property() {
        // Given
        assertFF4j.assertThatFeatureExist(F2);
        Feature startFeature    = testedStore.read(F2);
        assertFF4j.assertThatFeatureHasProperty(F2, P_F2_DIGITVALUE);
        Optional<Property<?>> opDigitValue = startFeature.getProperty(P_F2_DIGITVALUE);
        Assertions.assertTrue(opDigitValue.isPresent());
        PropertyInteger pDigitValue = (PropertyInteger) opDigitValue.get();
        Assertions.assertEquals(4, pDigitValue.getFixedValues().get().size()); 
        // WHen
        pDigitValue.addFixedValue(4);
        testedStore.save(startFeature);
        // Then
        Feature endFeature = testedStore.read(F2);
        Optional<Property<?>> opt2 = endFeature.getProperty(P_F2_DIGITVALUE);
        PropertyInteger digit2 = (PropertyInteger) opt2.get();
        Assertions.assertEquals(5, digit2.getFixedValues().get().size()); 
	}

    @Test
    public void should_update_feature_when_removeFixedValue_property() {
        // Given
        assertFF4j.assertThatFeatureExist(F2);
        Feature startFeature    = testedStore.read(F2);
        assertFF4j.assertThatFeatureHasProperty(F2, P_F2_DIGITVALUE);
        Optional<Property<?>> opDigitValue = startFeature.getProperty(P_F2_DIGITVALUE);
        Assertions.assertTrue(opDigitValue.isPresent());
        PropertyInteger pDigitValue = (PropertyInteger) opDigitValue.get();
        Assertions.assertEquals(4, pDigitValue.getFixedValues().get().size()); 
        // WHen
        pDigitValue.getFixedValues().get().remove(0);
        pDigitValue.getFixedValues().get().remove(1);
        pDigitValue.getFixedValues().get().remove(2);
        testedStore.save(startFeature);
        // Then
        Feature endFeature = testedStore.read(F2);
        Optional<Property<?>> opt2 = endFeature.getProperty(P_F2_DIGITVALUE);
        PropertyInteger digit2 = (PropertyInteger) opt2.get();
        Assertions.assertEquals(1, digit2.getFixedValues().get().size()); 
    }
    
    @Test
    public void should_generate_json_when_toString() {
        Assertions.assertNotNull(testedStore.toString());
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void should_have_auditListener_and_remove_if_unregister() {
        // Given
        Optional<FeatureRepositoryListenerAudit> auditListener = 
                (Optional<FeatureRepositoryListenerAudit>) testedStore.readAuditListener();
        Assertions.assertTrue(auditListener.isPresent());
        Assertions.assertEquals(1, testedStore.listListenerNames().count());
        Assertions.assertEquals(auditListener, 
                testedStore.readListener(FeatureRepositorySupport.LISTENERNAME_AUDIT));
        // When
        testedStore.unRegisterAuditListener();
        // Then
        Assertions.assertEquals(0, testedStore.listListenerNames().count());
        Assertions.assertFalse(testedStore.readAuditListener().isPresent());
    }
    
    @Test
    public void should_add_Listener_when_register() throws InterruptedException {
        // Given
        Assertions.assertEquals(1, testedStore.listListenerNames().count());
        // When
        testedStore.registerListener("Sample", new FeatureRepositoryListenerConsole());
        // Then
        Assertions.assertEquals(2, testedStore.listListenerNames().count());
        testedStore.createSchema();
        // Do not close test before logging of console
        Thread.sleep(500);
    }
    
    @Test
    public void should_create_schema_when_createSchema() 
    throws InterruptedException {
        testedStore.createSchema();
        // Do not close test before logging of console
        Thread.sleep(500);
    }
}
