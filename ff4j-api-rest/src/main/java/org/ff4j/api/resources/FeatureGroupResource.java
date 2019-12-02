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

import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ff4j.api.FF4jApiSpecification;
import org.ff4j.json.domain.FeatureJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * WebResource representing a group of features.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@Produces(MediaType.APPLICATION_JSON)
@Api(FeatureGroupResource.PATH)
@Path(FeatureGroupResource.PATH)
public class FeatureGroupResource extends AbstractResource {
 
    /** Path for the resource. */
    public static final String PATH = FF4jResource.PATH + "/groups/{groupName}";
    
    /**
     * Read information about a group
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ FF4jApiSpecification.ROLE_READ } )
    @ApiOperation(value= "Read information about a group", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Group is retrieved"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (groupName)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 404, message= "Group has not been found"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response read(@PathParam("groupName") String groupName) {
        return Response.ok(ff4j.getRepositoryFeatures()
                               .readGroup(groupName)
                               .map(FeatureJson::new)
                               .collect(Collectors.toList())).build();
    }

    /**
     * ToggleOn features from a dedicated group.
     */
    @POST
    @Path("/" + OPERATION_TOGGLE_ON)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "ToggleOn a group", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Group is toggled ON"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (groupName)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 404, message= "Group has not been found"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response toggleOn(@PathParam("groupName") String groupName) {
        ff4j.getRepositoryFeatures().toggleOnGroup(groupName);
        return Response.ok().build();
    }
    
    /**
     * ToggleOn features from a dedicated group.
     */
    @POST
    @Path("/" + OPERATION_TOGGLE_OFF)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "ToggleOn a group", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Group is toggled OFF"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (groupName)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 404, message= "Group has not been found"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response toggleOff(@PathParam("groupName") String groupName) {
        ff4j.getRepositoryFeatures().toggleOffGroup(groupName);
        return Response.ok().build();
    }
    

}
