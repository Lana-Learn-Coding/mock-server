package org.example.mock;

import io.vertx.core.json.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
public class MockResource {
    private static final String BODY = "body";
    private static final String HTTP_STATUS_CODE = "http_status_code";

    private final JsonObject config = new JsonObject();

    @GET
    public Response apiGet() {
        return response();
    }

    @POST
    public Response apiPost() {
        return response();
    }

    @GET
    @Path("/any{p:/?}{any: .*}")
    public Response apiGetAny() {
        return response();
    }

    @POST
    @Path("/any{p:/?}{any: .*}")
    public Response apiPostAny() {
        return response();
    }

    @GET
    @Path("/code/{code}{p:/?}{any: .*}")
    public Response apiGetCode(@PathParam("code") int code) {
        return response(code);
    }

    @POST
    @Path("/code/{code}{p:/?}{any: .*}")
    public Response apiPostCode(@PathParam("code") int code) {
        return response(code);
    }

    @POST
    @Path("/config")
    public Response config(JsonObject config) {
        System.out.println(config.toString());
        if (config.getValue(HTTP_STATUS_CODE) instanceof Integer) {
            this.config.put(HTTP_STATUS_CODE, config.getInteger(HTTP_STATUS_CODE));
        }

        if (config.getValue(BODY) instanceof String || config.getValue(BODY) instanceof JsonObject) {
            try {
                this.config.put(BODY, new JsonObject(config.getString(BODY)));
            } catch (Exception e) {
                this.config.put(BODY, config.getString(BODY));
            }
        }

        return Response.ok().build();
    }

    private Response response() {
        int status = config.getInteger(HTTP_STATUS_CODE, 200);
        return response(status);
    }

    private Response response(int status) {
        if (config.getValue(BODY) instanceof String) {
            return Response.status(status)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                .entity(config.getString(BODY))
                .build();
        }

        if (config.getValue(BODY) instanceof JsonObject) {
            return Response.status(status)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .entity(config.getJsonObject(BODY))
                .build();
        }

        return Response.status(status)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
            .build();
    }
}