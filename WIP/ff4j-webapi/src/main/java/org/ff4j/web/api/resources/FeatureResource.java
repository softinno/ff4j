package org.ff4j.web.api.resources;

import static org.ff4j.web.FF4jWebConstants.LOCATION;
import static org.ff4j.web.FF4jWebConstants.OPERATION_ADDGROUP;
import static org.ff4j.web.FF4jWebConstants.OPERATION_DISABLE;
import static org.ff4j.web.FF4jWebConstants.OPERATION_ENABLE;
import static org.ff4j.web.FF4jWebConstants.OPERATION_REMOVEGROUP;
import static org.ff4j.web.FF4jWebConstants.ROLE_READ;
import static org.ff4j.web.FF4jWebConstants.ROLE_WRITE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ff4j.feature.Feature;
import org.ff4j.feature.exception.FeatureNotFoundException;
import org.ff4j.web.FF4jWebConstants;
import org.ff4j.web.api.resources.domain.FeatureApiBean;
import org.ff4j.web.api.resources.domain.PropertyApiBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Represent a feature as WebResource.
 * 
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@Path("/ff4j/store/features/{uid}")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({FF4jWebConstants.ROLE_WRITE})
@Api(value = "/ff4j/store/features/{uid}")
public class FeatureResource extends AbstractResource {
  
    /**
     * Allows to retrieve feature by its id.
     * 
     * @param featId
     *            target feature identifier
     * @return feature is exist
     */
    @GET
    @RolesAllowed({ROLE_READ})
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value= "Read information about a feature", response=FeatureApiBean.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Information about features"), 
        @ApiResponse(code = 404, message= "Feature not found") })
    public Response read(@PathParam("uid") String id) {
       if (!ff4j.getRepositoryFeatures().exists(id)) {
            String errMsg = new FeatureNotFoundException(id).getMessage();
            return Response.status(Response.Status.NOT_FOUND).entity(errMsg).build();
       }
       return  Response.ok(new FeatureApiBean(ff4j.getRepositoryFeatures().read(id))).build();
    }

    /**
     * Create the feature if not exist or update it
     * 
     * @param headers
     *            current request header
     * @param data
     *            feature serialized as JSON
     * @return 204 or 201
     */
    @PUT
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Create of update a feature", response=Response.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses({
        @ApiResponse(code = 201, message= "Feature has been created"), 
        @ApiResponse(code = 204, message= "No content, feature is updated") })
    public Response upsertFeature(@Context HttpHeaders headers, @PathParam("uid") String id, FeatureApiBean fApiBean) {
        // Parameter validations
        if ("".equals(id) || !id.equals(fApiBean.getUid())) {
            String errMsg = "Invalid identifier expected " + id;
            return Response.status(Response.Status.BAD_REQUEST).entity(errMsg).build();
        }
        
        Feature feat = new Feature(id);
        feat.setDescription(fApiBean.getDescription());
        feat.setEnable(fApiBean.isEnable());
        feat.setGroup(fApiBean.getGroup());
        // Properties
        Map<String, PropertyApiBean> mapProperties = fApiBean.getCustomProperties();
        if (mapProperties != null) {
            for(PropertyApiBean propertyBean : mapProperties.values()) {
                feat.addProperty(propertyBean.asProperty());
            }       
        }
        
        // Update or create ? 
        if (!getFeatureRepository().exists(feat.getUid())) {
            getFeatureRepository().save(feat);
            String location = String.format("%s", uriInfo.getAbsolutePath().toString());
            try {
                return Response.created(new URI(location)).build();
            } catch (URISyntaxException e) {
                return Response.status(Response.Status.CREATED).header(LOCATION, location).entity(id).build();
            }
        }
        
        // Create
        getFeatureRepository().save(feat);
        return Response.noContent().build();
    }

    /**
     * Delete feature by its id.
     * 
     * @return delete by its id.
     */
    @DELETE
    @RolesAllowed({ROLE_WRITE})
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value= "Delete a feature", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 404, message= "Feature has not been found"), 
        @ApiResponse(code = 204, message= "No content, feature is deleted"),
        @ApiResponse(code = 400, message= "Bad identifier"),
        })
    public Response deleteFeature(@PathParam("uid") String id) {
        if (id == null || "".equals(id)) {
            String errMsg = "Invalid URL : Must be '/features/{uid}' with {uid} not null nor empty";
            return Response.status(Response.Status.BAD_REQUEST).entity(errMsg).build();
        }
        if (!ff4j.getRepositoryFeatures().exists(id)) {
            String errMsg = new FeatureNotFoundException(id).getMessage();
            return Response.status(Response.Status.NOT_FOUND).entity(errMsg).build();
        }
        getFeatureRepository().delete(id);
        return Response.noContent().build();
    }

    /**
     * Convenient method to update partially the feature: Here enabling
     * 
     * @return http response.
     */
    @POST
    @Path("/" + OPERATION_ENABLE)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Enable a feature", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 204, message= "Features has been enabled"), 
        @ApiResponse(code = 404, message= "Feature not found") })
    public Response operationEnable(@PathParam("uid") String id) {
        if (!ff4j.getRepositoryFeatures().exists(id)) {
            String errMsg = new FeatureNotFoundException(id).getMessage();
            return Response.status(Response.Status.NOT_FOUND).entity(errMsg).build();
        }
        getFeatureRepository().toggleOn(id);
        return Response.noContent().build();
    }

    /**
     * Convenient method to update partially the feature: Here disabling
     * 
     * @return http response.
     */
    @POST
    @Path("/" + OPERATION_DISABLE)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Disable a feature", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 204, message= "Features has been disabled"), 
        @ApiResponse(code = 404, message= "Feature not found") })
    public Response operationDisable(@PathParam("uid") String id) {
        if (!ff4j.getRepositoryFeatures().exists(id)) {
            String errMsg = new FeatureNotFoundException(id).getMessage();
            return Response.status(Response.Status.NOT_FOUND).entity(errMsg).build();
        }
        ff4j.getRepositoryFeatures().toggleOff(id);
        return Response.noContent().build();
    }
    
    /**
     * Convenient method to update partially the feature: Adding to a group
     * 
     * @return http response.
     */
    @POST
    @RolesAllowed({ROLE_WRITE})
    @Path("/" + OPERATION_ADDGROUP  + "/{groupName}" )
    @ApiOperation(value= "Define the group of the feature", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 204, message= "Group has been defined"), 
        @ApiResponse(code = 404, message= "Feature not found"),
        @ApiResponse(code = 400, message= "Invalid GroupName") })
    public Response operationAddGroup(@PathParam("uid") String id, @PathParam("groupName") String groupName) {
        if (!ff4j.getRepositoryFeatures().exists(id)) {
            String errMsg = new FeatureNotFoundException(id).getMessage();
            return Response.status(Response.Status.NOT_FOUND).entity(errMsg).build();
        }
        if ("".equals(groupName)) {
            String errMsg = "Invalid groupName should not be null nor empty";
            return Response.status(Response.Status.BAD_REQUEST).entity(errMsg).build();
        }
        getFeatureRepository().addToGroup(id, groupName);
        return Response.noContent().build();
    }
    
    /**
     * Convenient method to update partially the feature: Removing from a group
     * 
     * @return http response.
     */
    @POST
    @RolesAllowed({ROLE_WRITE})
    @Path("/" + OPERATION_REMOVEGROUP  + "/{groupName}")
    @ApiOperation(value= "Remove the group of the feature", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 204, message= "Group has been removed"), 
        @ApiResponse(code = 404, message= "Feature not found"),
        @ApiResponse(code = 400, message= "Invalid GroupName") })
    public Response operationRemoveGroup(@PathParam("uid") String id,  @PathParam("groupName") String groupName) {
        if (!ff4j.getRepositoryFeatures().exists(id)) {
            String errMsg = new FeatureNotFoundException(id).getMessage();
            return Response.status(Response.Status.NOT_FOUND).entity(errMsg).build();
        }
        // Expected behaviour is no error even if invalid groupname
        // .. but invalid if group does not exist... 
        if (!ff4j.getRepositoryFeatures().existGroup(groupName)) {
            String errMsg = "Invalid groupName should be " + groupName;
            return Response.status(Response.Status.BAD_REQUEST).entity(errMsg).build();
        }
        getFeatureRepository().removeFromGroup(id, groupName);
        return Response.noContent().build();
    }

}
