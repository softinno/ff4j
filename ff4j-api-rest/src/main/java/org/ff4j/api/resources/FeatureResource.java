package org.ff4j.api.resources;

/*-
 * #%L
 * ff4j-api-rest
 * %%
 * Copyright (C) 2013 - 2019 FF4J
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


import static javax.ws.rs.core.HttpHeaders.LOCATION;

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

import org.ff4j.api.FF4jApiSpecification;
import org.ff4j.feature.Feature;
import org.ff4j.json.domain.FeatureJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Represent a feature as WebResource.
 * 
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@Path(FeatureResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({FF4jApiSpecification.ROLE_WRITE})
@Api(value = FeatureResource.PATH)
public class FeatureResource extends AbstractResource {
  
    /** Path for the resource. */
    public static final String PATH = FeaturesResource.PATH + "/{uid}";
    
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
    @ApiOperation(value= "Find a feature by its identifier", response=FeatureJson.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Feature is retrieved"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 404, message= "Feature has not been found"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response find(@PathParam("uid") String id) {
       // AssertViolation is trapped in RuntimeExceptionMapper as BAD_REQUEST
       // FeatureNotFoundException is trapped in RuntimeExceptionMapper as NOT_FOUND
       Feature feature = ff4j.getRepositoryFeatures().read(id);
       // Mapping explicit Json to reuse the marshalling to JSON in stores
       return Response.ok(new FeatureJson(feature)).build();
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
    @ApiOperation(value= "Feature upsert (based on uid)", response=Response.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Feature has been updated"),
        @ApiResponse(code = 201, message= "Feature has been created"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response upsert(@Context HttpHeaders headers, @PathParam("uid") String id, FeatureJson feature) {
        boolean exist = ff4j.getRepositoryFeatures().exists(id);
        ff4j.getRepositoryFeatures().save(feature.asFeature());
        if (!exist) {
            String location = String.format("%s", uriInfo.getAbsolutePath().toString());
            return Response.status(Response.Status.CREATED)
                           .header(LOCATION, location)
                           .entity(id)
                           .build();
        } else {
            return Response.ok().build();
        }
    }
    
    /**
     * Delete feature by its id.
     */
    @DELETE
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Delete a feature", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 404, message= "Feature has not been found"), 
        @ApiResponse(code = 204, message= "No content, feature is deleted"),
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response delete(@PathParam("uid") String id) {
        ff4j.getRepositoryFeatures().delete(id);
        return Response.noContent().build();
    }

    /**
     * ToggleOn Feature.
     */
    @POST
    @Path("/" + OPERATION_TOGGLE_ON)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Enable a feature", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features has been enabled"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response toggleOn(@PathParam("uid") String id) {
        ff4j.getRepositoryFeatures().toggleOn(id);
        return Response.ok().build();
    }
    
    /**
     * ToggleOn Feature.
     */
    @POST
    @Path("/" + OPERATION_TOGGLE_OFF)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Enable a feature", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features has been enabled"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response toggleOff(@PathParam("uid") String id) {
        ff4j.getRepositoryFeatures().toggleOff(id);
        return Response.ok().build();
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
        @ApiResponse(code = 200, message= "Features has been added to group"),
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid or groupName)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response addToGroup(@PathParam("uid") String id, @PathParam("groupName") String groupName) {
        ff4j.getRepositoryFeatures().addToGroup(id, groupName);
        return Response.ok().build();
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
        @ApiResponse(code = 200, message= "Features has been enabled"),
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response removeFromGroup(@PathParam("uid") String id, @PathParam("groupName") String groupName) {
        ff4j.getRepositoryFeatures().removeFromGroup(id, groupName);
        return Response.noContent().build();
    }

}
