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

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 * Expos API for audit information
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Api(AuditResource.PATH)
@Path(AuditResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class AuditResource extends AbstractResource {
    
    /** Path for the resource. */
    public static final String PATH = FF4jResource.PATH + "/auditlog";
    
    @POST
    @Path("/" + STORE_CREATESCHEMA)
    @ApiOperation(value= "Create underlying DB schema for store")
    @ApiResponses({
        @ApiResponse(code = 200, message= "Related schema for audit"), 
        @ApiResponse(code = 401, message= "You may be authenticated"),
        @ApiResponse(code = 403, message= "Forbidden, operation not allowed, check you roles"),
        @ApiResponse(code = 500, message= "Internal technical error"),
        @ApiResponse(code = 503, message= "Cannot contact underlying DB")})
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSchema() {
        if (ff4j.getAuditTrail().isPresent()) {
            ff4j.getAuditTrail().get().createSchema();
            return Response.ok().build();
        }
        return Response.status(Status.NOT_FOUND)
                       .entity("Audit trail is not enabled").build();
    }
    
    

}
