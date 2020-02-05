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
 * WebResource representing the store.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@Api(FeaturesResource.PATH)
@Path(FeaturesResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({FF4jApiSpecification.ROLE_WRITE})
public class FeaturesResource extends AbstractResource {
    
    /** Path for the resource. */
    public static final String PATH = FF4jResource.PATH + "/features";
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value= "List all existing <b>Features</b>", response = Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Retrieve list"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response findAll() {
        return Response.ok(ff4j.getRepositoryFeatures()
                .findAll()
                .map(FeatureJson::new)
                .collect(Collectors.toList())).build();
    }
    
    @POST
    @Path("/" + STORE_CLEAR)
    @ApiOperation(value= "Delete all <b>Features</b> in store")
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features have been deleted"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    @Produces(MediaType.APPLICATION_JSON)
    public Response clear() {
        ff4j.getRepositoryFeatures().deleteAll();
        return Response.ok().build();
    }
    
    @POST
    @Path("/" + STORE_CREATESCHEMA)
    @ApiOperation(value= "Create underlying DB schema for store")
    @ApiResponses({
        @ApiResponse(code = 200, message= "Related schema for features"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSchema() {
        ff4j.getRepositoryFeatures().createSchema();
        return Response.ok().build();
    }
    
}
