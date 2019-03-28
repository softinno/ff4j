package org.ff4j.web.controller;

/*
 * #%L
 * ff4j-sample-web
 * %%
 * Copyright (C) 2013 - 2016 FF4J
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

import static org.ff4j.web.bean.WebConstants.DESCRIPTION;
import static org.ff4j.web.bean.WebConstants.GROUPNAME;
import static org.ff4j.web.bean.WebConstants.NEW_NAME;
import static org.ff4j.web.bean.WebConstants.OP_ADD_PERMISSION;
import static org.ff4j.web.bean.WebConstants.OP_CLEAR_PERMISSIONS;
import static org.ff4j.web.bean.WebConstants.OP_COPY_FEATURE;
import static org.ff4j.web.bean.WebConstants.OP_CREATE_FEATURE;
import static org.ff4j.web.bean.WebConstants.OP_DISABLE;
import static org.ff4j.web.bean.WebConstants.OP_EDIT_FEATURE;
import static org.ff4j.web.bean.WebConstants.OP_ENABLE;
import static org.ff4j.web.bean.WebConstants.OP_RENAME_FEATURE;
import static org.ff4j.web.bean.WebConstants.OP_RMV_FEATURE;
import static org.ff4j.web.bean.WebConstants.OP_RMV_PERMISSION;
import static org.ff4j.web.bean.WebConstants.OP_RMV_PROPERTY;
import static org.ff4j.web.bean.WebConstants.OP_TOGGLE_GROUP;
import static org.ff4j.web.bean.WebConstants.SUBOPERATION;
import static org.ff4j.web.embedded.ConsoleRenderer.msg;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ff4j.FF4j;
import org.ff4j.FF4jEntity;
import org.ff4j.feature.Feature;
import org.ff4j.security.FF4jGrantees;
import org.ff4j.security.FF4jPermission;
import org.ff4j.utils.Util;
import org.ff4j.web.bean.WebConstants;
import org.ff4j.web.embedded.ConsoleOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 * Controller for main class
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class FeaturesController extends AbstractController {

	/** Logger for this class. */
    public static final Logger LOGGER = LoggerFactory.getLogger(FeaturesController.class);

    /** View name. */
	private static final String VIEW_FEATURES = "features";

	/** {@inheritDoc} */
	public FeaturesController(FF4j ff4j, TemplateEngine te) {
		super(ff4j, VIEW_FEATURES, te);
	}
	
    /** {@inheritDoc} */
    public void get(HttpServletRequest req, HttpServletResponse res, WebContext ctx) throws IOException {
        String operation = req.getParameter(WebConstants.OPERATION);
        String featureId = req.getParameter(WebConstants.FEATID);
        String msgType = "success";
        String msg = null;
        if (Util.hasLength(operation) && Util.hasLength(featureId)) {
            if (getFeatureRepository().exists(featureId)) {
                // Read feature from store
                Feature feature = getFeatureRepository().read(featureId);
                if (OP_ENABLE.equalsIgnoreCase(operation)) {
                    feature.toggleOn();
                    msg = msg(featureId, "ENABLED");
                    LOGGER.info(featureId + " has been enabled");
                }
                if (OP_DISABLE.equalsIgnoreCase(operation)) {
                   feature.toggleOff();
                    msg = msg(featureId, "DISABLED");
                    LOGGER.info(featureId + " has been disabled");
                }
                if (OP_ADD_PERMISSION.equalsIgnoreCase(operation)) {
                   actionAddPermissionToUser(req, feature, featureId);
                }
                if (OP_RMV_PERMISSION.equalsIgnoreCase(operation)) {
                   actionRemovePermissionFromUser(req, feature, featureId);
                }
                if (OP_CLEAR_PERMISSIONS.equalsIgnoreCase(operation)) {
                    feature.getAccessControlList().getPermissions().clear();
                    LOGGER.info("Clear permissions for " + featureId);
                }
                if (OP_RMV_PROPERTY.equalsIgnoreCase(operation)) {
                    String propertyName = req.getParameter(WebConstants.NAME);
                    feature.getProperties().remove(propertyName);
                    LOGGER.info("Remove Property " + propertyName + " to " + featureId );
                }
                getFeatureRepository().save(feature);
                
            } else {
                msgType = "warning";
                msg = "The feature '" + featureId + "' does not exist";
            }
        }
        ctx.setVariable("msgType", msgType);
        ctx.setVariable("msgInfo", msg);
        renderPage(ctx);
    }

	/** {@inheritDoc} */
    public void post(HttpServletRequest req, HttpServletResponse res, WebContext ctx)
    throws IOException {
        String msg       = null;
        String msgType   = "success";
        String operation = req.getParameter(WebConstants.OPERATION);
        String featureId = req.getParameter(WebConstants.FEATID);
        
        if (OP_EDIT_FEATURE.equalsIgnoreCase(operation)) {
            this.updateFeature(req, featureId);
            msg = featureId + " has been UPDATED";
            
        } else if (OP_CREATE_FEATURE.equalsIgnoreCase(operation)) {
            ConsoleOperations.createFeature(getFf4j(), req);
            msg = featureId + " has been CREATED";
            
        } else if (OP_RMV_FEATURE.equalsIgnoreCase(operation)) {
            getFf4j().getRepositoryFeatures().delete(featureId);
            msg = featureId + " has been DELETED";
            
        } else if (OP_RENAME_FEATURE.equalsIgnoreCase(operation)) {
            String newName = req.getParameter(NEW_NAME);
            if (getFeatureNames().contains(newName)) {
                msgType = "warning";
                msg = "Cannot rename " + featureId + " to " + newName + " : it already exists";
            } else {
                Feature newFeature = new Feature(newName, getFeatureRepository().read(featureId));
                getFeatureRepository().delete(featureId);
                getFeatureRepository().save(newFeature);
                msg = "Feature " + featureId + " has been renamed to " + newName;
            }
            
        } else if (OP_COPY_FEATURE.equalsIgnoreCase(operation)) {
            String newName = req.getParameter(NEW_NAME);
            if (getFeatureNames().contains(newName)) {
                msgType = "warning";
                msg = "Cannot copy " + featureId + " with name " + newName + " : it already exists";
            } else {
                getFeatureRepository().save(
                        new Feature(newName, getFeatureRepository().read(featureId)));
                msg = "Feature " + featureId + " has been copied to " + newName;
            }
            
        } else if (OP_TOGGLE_GROUP.equalsIgnoreCase(operation)) {
            String groupName = req.getParameter(GROUPNAME);
            if (groupName != null && !groupName.isEmpty()) {
                String operationGroup = req.getParameter(SUBOPERATION);
                if (OP_ENABLE.equalsIgnoreCase(operationGroup)) {
                    getFeatureRepository().toggleOnGroup(groupName);
                    msg = groupName + " has been ENABLED";
                    LOGGER.info("Group '" + groupName + "' has been ENABLED.");
                } else if (OP_DISABLE.equalsIgnoreCase(operationGroup)) {
                    getFeatureRepository().toggleOffGroup(groupName);
                    msg = groupName + " has been DISABLED";
                    LOGGER.info("Group '" + groupName + "' has been DISABLED.");
                }
            }
        }
        ctx.setVariable("msgType", msgType);
        ctx.setVariable("msgInfo", msg);
        renderPage(ctx);
    }
    
    /**
     * Allow to update feature.
     *
     * @param featureId
     */
    private void updateFeature(HttpServletRequest req, String featureId) {
        final String groupName   = req.getParameter(GROUPNAME);
        Feature tobeUpdated = getFeatureRepository().read(featureId);
        // Description
        final String featureDesc = req.getParameter(DESCRIPTION);
        if (Util.hasLength(featureDesc)) {
            tobeUpdated.setDescription(featureDesc);
        }
        // GroupName
        if (Util.hasLength(groupName)) {
            tobeUpdated.setGroup(groupName);
        }
        // Creation
        getFeatureRepository().save(tobeUpdated);
    }
  
    /**
     * Both get and post operation will render the page.
     *
     * @param ctx
     *            current web context
     */
    private void renderPage(WebContext ctx) {
        ctx.setVariable(KEY_TITLE, "Features");
        ctx.setVariable("listOfFeatures", getFeatureRepository().findAll()
                                     .sorted(Comparator.comparing(FF4jEntity::getUid))
                                     .collect(Collectors.toList()));
        ctx.setVariable("groupList",  getFeatureRepository().listGroupNames()
                                     .sorted(Comparator.naturalOrder())
                                     .collect(Collectors.toList()));
    }
    
    private void actionAddPermissionToUser(HttpServletRequest req, Feature feature, String featureId) {
        FF4jPermission myPermission = FF4jPermission.valueOf(req.getParameter(WebConstants.PERMISSION));
        String granteeName = req.getParameter(WebConstants.PERMISSION_GRANTEE_NAME);
        String granteeType = req.getParameter(WebConstants.PERMISSION_GRANTEE_TYPE);
       
        Map <FF4jPermission, FF4jGrantees > permissions = 
                feature.getAccessControlList().getPermissions();
        FF4jGrantees myGrantees = new FF4jGrantees();
        if (permissions.containsKey(myPermission)) {
            myGrantees = permissions.get(myPermission);
        }
        if (WebConstants.PERMISSION_GRANTEE_USER.equalsIgnoreCase(granteeType)) {
            myGrantees.getUsers().add(granteeName);
        } else if (WebConstants.PERMISSION_GRANTEE_ROLE.equalsIgnoreCase(granteeType)) {
            myGrantees.getRoles().add(granteeName);
        }
        feature.getAccessControlList()
               .getPermissions()
               .put(myPermission, myGrantees);
        LOGGER.info("Adding permission " + myPermission.name() + 
                                 " and " + granteeName + 
                                 " to "  + featureId);
    }
    
    private void actionRemovePermissionFromUser(HttpServletRequest req, Feature feature, String featureId) {
        FF4jPermission myPermission = FF4jPermission.valueOf(req.getParameter(WebConstants.PERMISSION));
        String granteeName = req.getParameter(WebConstants.PERMISSION_GRANTEE_NAME);
        String granteeType = req.getParameter(WebConstants.PERMISSION_GRANTEE_TYPE);
        feature.getAccessControlList().getPermissions().remove(myPermission);
        LOGGER.info("Remove permission " + myPermission.name() 
                    + " on " + granteeType + " " + granteeName 
                    + " to "  + featureId);
    }

}
