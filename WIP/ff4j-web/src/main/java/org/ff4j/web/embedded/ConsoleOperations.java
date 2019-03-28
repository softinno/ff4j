package org.ff4j.web.embedded;

import static org.ff4j.web.embedded.ConsoleConstants.DESCRIPTION;
import static org.ff4j.web.embedded.ConsoleConstants.FEATID;
import static org.ff4j.web.embedded.ConsoleConstants.GROUPNAME;

/*
 * #%L
 * ff4j-web
 * %%
 * Copyright (C) 2013 - 2015 FF4J
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

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ff4j.FF4j;
import org.ff4j.feature.Feature;
import org.ff4j.feature.repository.FeatureRepository;
import org.ff4j.parser.FF4jConfigFile;
import org.ff4j.parser.xml.XmlParserV2;
import org.ff4j.property.Property;
import org.ff4j.property.PropertyFactory;
import org.ff4j.property.repository.PropertyRepository;
import org.ff4j.utils.Util;
import org.ff4j.web.bean.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConsoleOperations {
    
    /** Logger for this class. */
    private static Logger LOGGER = LoggerFactory.getLogger(ConsoleOperations.class);
    
    private ConsoleOperations() {}
    
    /**
     * User action to create a new Feature.
     * 
     * @param req
     *            http request containing operation parameters
     */
    public static void createFeature(FF4j ff4j, HttpServletRequest req) {
        // uid
        final String featureId = req.getParameter(FEATID);
        if (featureId != null && !featureId.isEmpty()) {
            Feature fp = new Feature(featureId, false);

            // Description
            final String featureDesc = req.getParameter(DESCRIPTION);
            if (null != featureDesc && !featureDesc.isEmpty()) {
                fp.setDescription(featureDesc);
            }

            // GroupName
            final String groupName = req.getParameter(GROUPNAME);
            if (null != groupName && !groupName.isEmpty()) {
                fp.setGroup(groupName);
            }

            // Creation
            ff4j.getRepositoryFeatures().save(fp);
            LOGGER.info(featureId + " has been created");
        }
    }
    
    /**
     * Sample Element should be updated like name, description, value
     * @param ff4j
     * @param req
     */
    public static void updateProperty(FF4j ff4j, HttpServletRequest req) {
        String name         = req.getParameter("name");
        String type         = req.getParameter("pType");
        String description  = req.getParameter("desc");
        String value        = req.getParameter("pValue");
        String uid          = req.getParameter("uid");
        String featureId    = req.getParameter(WebConstants.FEATURE_UID);
        
        Property<?> ap;
        // To update the core the uid is the name (rename, edit)
        if (uid == null) {
            uid = name;
        }
        
        // Update Feature property
        if (Util.hasLength(featureId)) {
            Feature current = ff4j.getRepositoryFeatures().read(featureId);
            current.addProperty(PropertyFactory.createProperty(uid, value));
            ff4j.getRepositoryFeatures().save(current);
             
        } else if (ff4j.getRepositoryProperties().exists(uid)) {
            // Do not change name, just and update
            if (uid.equalsIgnoreCase(name)) {
                ap = ff4j.getRepositoryProperties().read(uid);
                // just an update for the value
                if (ap.getClassName().equalsIgnoreCase(type)) {
                    ap.setDescription(description);
                    ap.setValueFromString(value);
                    ff4j.getRepositoryProperties().save(ap);
                } else {
                    ap = PropertyFactory.createProperty(name, type, value);
                    ap.setDescription(description);
                    // Note : Fixed Values are LOST if type changed => cannot cast ? to T
                    LOGGER.warn("By changing property type you loose the fixedValues, cannot evaluate ? at runtime");
                    ff4j.getRepositoryProperties().delete(name);
                    ff4j.getRepositoryProperties().save(ap);
                }
            } else {
                // Name change delete and create a new
                ap = PropertyFactory.createProperty(name, type, value);
                ap.setDescription(description);
                // Note : Fixed Values are LOST if name changed => cannot cast ? to T
                LOGGER.warn("By changing property name you loose the fixedValues, cannot evaluate generics at runtime (type inference)");
                ff4j.getRepositoryProperties().delete(uid);
                ff4j.getRepositoryProperties().save(ap);
            }
        }
    }
    
    /**
     * Create new property in store.
     *
     * @param ff4j
     *      current ff4j instance.
     * @param req
     *      current http request
     */
    public static void createProperty(FF4j ff4j, HttpServletRequest req) {
        String name         = req.getParameter("name");
        String type         = req.getParameter("pType");
        String description  = req.getParameter("desc");
        String value        = req.getParameter("pValue");
        String featureId    = req.getParameter(WebConstants.FEATURE_UID);
        Property<?> ap = PropertyFactory.createProperty(name, type, value);
        ap.setDescription(description);
        
        if (Util.hasLength(featureId)) {
            Feature current = ff4j.getRepositoryFeatures().read(featureId);
            current.addProperty(ap);
            ff4j.getRepositoryFeatures().save(current);
        } else {
            ff4j.getRepositoryProperties().save(ap);
        }
    }
    
    /**
     * User action to update a target feature's description.
     * 
     * @param req
     *            http request containing operation parameters
     */
    public static void updateFeatureDescription(FF4j ff4j, HttpServletRequest req) {
        // uid
        final String featureId = req.getParameter(FEATID);
        if (featureId != null && !featureId.isEmpty()) {
            // https://github.com/clun/ff4j/issues/66
            Feature old = ff4j.getRepositoryFeatures().read(featureId);
            Feature fp = new Feature(featureId, old.isEnabled());
            // <--
            
            // Description
            final String featureDesc = req.getParameter(DESCRIPTION);
            if (null != featureDesc && !featureDesc.isEmpty()) {
                fp.setDescription(featureDesc);
            }

            // GroupName
            final String groupName = req.getParameter(GROUPNAME);
            if (null != groupName && !groupName.isEmpty()) {
                fp.setGroup(groupName);
            }
            
            // Creation
            ff4j.getRepositoryFeatures().save(fp);
            LOGGER.info(featureId + " has been updated");
        }
    }

    /**
     * User action to import Features from a properties files.
     * 
     * @param in
     *            inpustream from configuration file
     * @throws IOException
     *             Error raised if the configuration cannot be read
     */
    public static void importFile(FF4j ff4j, InputStream in) 
    throws IOException {
        // Provide Explicit Parser
        FF4jConfigFile ff4jConfig = new XmlParserV2().parse(in);
        FeatureRepository store = ff4j.getRepositoryFeatures();
        ff4jConfig.getFeatures().values().forEach(store::save);
        LOGGER.info("Features have been imported.");
        PropertyRepository pRepo = ff4j.getRepositoryProperties();
        ff4jConfig.getProperties().values().forEach(pRepo::save);
        LOGGER.info("Properties have been imported.");
    }
    
    /**
     * Build Http response when invoking export features.
     * 
     * @param res
     *            http response
     * @throws IOException
     *             error when building response
     */
    public static void exportFile(FF4j ff4j, HttpServletResponse res) throws IOException {
        //ServletOutputStream sos = null;
        //try {
            /*
             * InputStream in = new XmlParserV2().exportFeatures(ff4j.getC);
            sos = res.getOutputStream();
            res.setContentType("text/xml");
            res.setHeader("Content-Disposition", "attachment; filename=\"ff4j.xml\"");
            // res.setContentLength()
            org.apache.commons.io.IOUtils.copy(in, sos);*/
           // LOGGER.info(features.size() + " features have been exported.");
        //} finally {
        //    if (in != null) {
        //        in.close();
        //    }
        //    if (sos != null) {
        //        sos.flush();
        //        sos.close();
        //    }
        //}
    }
}
