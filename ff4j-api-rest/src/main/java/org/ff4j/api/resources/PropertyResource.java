package org.ff4j.api.resources;

import static javax.ws.rs.core.HttpHeaders.LOCATION;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ff4j.api.FF4jApiSpecification;
import org.ff4j.json.domain.FeatureJson;
import org.ff4j.json.domain.PropertyJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/*
 * #%L
 * ff4j-webapi
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

/**
 * WebResource representing the store.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@Api(PropertyResource.PATH)
@Path(PropertyResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({FF4jApiSpecification.ROLE_WRITE})
public class PropertyResource extends AbstractResource {
 
    /** Path for the resource. */
    public static final String PATH = PropertiesResource.PATH + "/{uid}";
    
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
    @ApiOperation(value= "Find a property by its identifier", response=FeatureJson.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Property is retrieved"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 404, message= "Feature has not been found"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response find(@PathParam("uid") String id) {
       return Response.ok(new PropertyJson(
                           ff4j.getRepositoryProperties()
                               .read(id))).build();
    }

    /**
     * Create the property if not exist or update it
     * 
     * @param headers
     *            current request header
     * @param data
     *            feature serialized as JSON
     * @return 204 or 201
     */
    @PUT
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Property upsert (based on uid)", response=Response.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Property has been updated"),
        @ApiResponse(code = 201, message= "Property has been created"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response upsert(@Context HttpHeaders headers, @PathParam("uid") String id, PropertyJson property) {
        boolean exist = ff4j.getRepositoryProperties().exists(id);
        ff4j.getRepositoryProperties().save(property.asProperty());
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
     * Delete property by its id.
     */
    @DELETE
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Delete a property", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 404, message= "Property has not been found"), 
        @ApiResponse(code = 204, message= "No content, feature is deleted"),
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response delete(@PathParam("uid") String id) {
        ff4j.getRepositoryProperties().delete(id);
        return Response.noContent().build();
    }
    
}
