package org.ff4j.api.resources;

import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ff4j.api.FF4jApiSpecification;
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
@Api(PropertiesResource.PATH)
@Path(PropertiesResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({FF4jApiSpecification.ROLE_WRITE})
public class PropertiesResource  extends AbstractResource {
  
    /** Path for the resource. */
    public static final String PATH = FF4jResource.PATH + "/properties";
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value= "List all existing <b>Properties</b>", response = Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Retrieve list"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response findAll() {
        return Response.ok(ff4j.getRepositoryProperties()
                .findAll()
                .map(PropertyJson::new)
                .collect(Collectors.toList())).build();
    }
   
    @POST
    @Path("/" + STORE_CLEAR)
    @ApiOperation(value= "Delete all <b>Properties</b> in store")
    @ApiResponses({
        @ApiResponse(code = 200, message= "Properties have been deleted"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    @Produces(MediaType.APPLICATION_JSON)
    public Response clear() {
        ff4j.getRepositoryProperties().deleteAll();
        return Response.ok().build();
    }
    
    @POST
    @Path("/" + STORE_CREATESCHEMA)
    @ApiOperation(value= "Create underlying DB schema for store")
    @ApiResponses({
        @ApiResponse(code = 200, message= "Related schema for properties"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSchema() {
        ff4j.getRepositoryProperties().createSchema();
        return Response.ok().build();
    }

}
