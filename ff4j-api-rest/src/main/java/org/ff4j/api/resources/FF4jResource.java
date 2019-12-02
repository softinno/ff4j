package org.ff4j.api.resources;

import java.util.List;
import java.util.Map;

/*
 * #%L
 * ff4j-web
 * %%
 * Copyright (C) 2013 - 2014 Ff4J
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

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.ff4j.api.FF4jApiSpecification;
import org.ff4j.api.domain.FF4jDescription;
import org.ff4j.api.security.FF4JSecurityContextHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This is the parent class for FF4J the REST API.
 * 
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
@Path(FF4jResource.PATH)
@Api(FF4jResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({FF4jApiSpecification.ROLE_WRITE})
public class FF4jResource extends AbstractResource {
    
    /** Path for the resource. */
    public static final String PATH = "/ff4j";
    
    /**
     * Provide core information on ff4J and available sub resources.
     * @return
     *      status bean
     */
    @GET
    @Produces(
            MediaType.APPLICATION_JSON)
    @ApiOperation(
            value= "Display <b>ff4j</b> status overview",
            notes= "Display informations related to <b>Monitoring</b>, <b>Security</b>, <b>Cache</b> and <b>Store</b>",
            response = FF4jDescription.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Feature list has been created"), 
        @ApiResponse(code = 400, message= "Invalid parameter provided (uid)"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public FF4jDescription describe() {
       return new FF4jDescription(ff4j);
    }
   
    /**
     * Check if feature if flipped
     * 
     * @param formParams
     *      target custom params
     * @return
     *      boolean if feature if flipped
     */
    @GET
    @Path("/" + OPERATION_TEST + "/{uid}") 
    @ApiOperation(value= "<b>Simple check</b> feature toggle", response=Boolean.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "if feature is flipped"),
        @ApiResponse(code = 400, message= "Invalid parameter"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response test(@Context HttpHeaders headers, @PathParam("uid") String uid) {
        FF4JSecurityContextHolder.save(securityContext);
        for (Map.Entry<String, List<String>> entry : headers.getRequestHeaders().entrySet()) {
            ff4j.getContext().put(entry.getKey(), entry.getValue());   
        }
        return Response.ok(String.valueOf(ff4j.test(uid))).build();
    }
    
    /**
     * Check if feature if flipped
     * 
     * @param formParams
     *      target custom params
     * @return
     *      boolean if feature if flipped
     */
    @POST
    @Path("/" + OPERATION_TEST + "/{uid}") 
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value= "<b>Advanced check</b> feature toggle (parameterized)", response=Boolean.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "if feature is flipped"),
        @ApiResponse(code = 400, message= "Invalid parameter"),
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response test(
            @Context HttpHeaders headers, 
            @PathParam("uid") String uid, 
            MultivaluedMap<String, String> formParams) {
        FF4JSecurityContextHolder.save(securityContext);
        for (Map.Entry<String, List<String>> entry : headers.getRequestHeaders().entrySet()) {
            ff4j.getContext().put(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, List<String>> entry : formParams.entrySet()) {
            ff4j.getContext().put(entry.getKey(), entry.getValue());
        }
        return Response.ok(String.valueOf(ff4j.test(uid))).build();
    }
    
    @POST
    @Path("/" + OPERATION_TOGGLE_AUDIT_ON)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Disable Audit", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features has been enabled"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response toggleOnAudit() {
        ff4j.withAudit();
        return Response.ok().build();
    }

    @POST
    @Path("/" + OPERATION_TOGGLE_AUDIT_OFF)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Disable Audit", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features has been enabled"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response toggleOffAudit() {
        ff4j.withoutAudit();
        return Response.ok().build();
    }
    
    @POST
    @Path("/" + OPERATION_TOGGLE_FEATUREHIT_ON)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Disable Audit", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features has been enabled"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response toggleOnFeatureHit() {
        ff4j.withFeaturesHitTracking();
        return Response.ok().build();
    }

    @POST
    @Path("/" + OPERATION_TOGGLE_FEATUREHIT_OFF)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Disable Audit", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features has been enabled"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response toggleOffFeatureHit() {
        ff4j.withoutFeaturesHitTracking();
        return Response.ok().build();
    }
    
    @POST
    @Path("/" + CLEAR_CACHE)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Clear Cache", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features has been enabled"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response clearCache() {
        ff4j.clearCache();
        return Response.ok().build();
    }
    
    @POST
    @Path("/" + STORE_CREATESCHEMA)
    @RolesAllowed({ROLE_WRITE})
    @ApiOperation(value= "Clear Cache", response=Response.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "Features has been enabled"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    public Response createSchema() {
        ff4j.createSchema();
        return Response.ok().build();
    }
    
    
    
}
