package org.example.mock;

import io.vertx.core.json.JsonObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @POST
    @Path("/post")
    public Response apiPost() {
        int status = config.getInteger(HTTP_STATUS_CODE, 200);
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

    @POST
    @Path("/config")
    public Response config(JsonObject config) {
        if (config.getValue(HTTP_STATUS_CODE) instanceof Integer) {
            this.config.put(HTTP_STATUS_CODE, config.getInteger(HTTP_STATUS_CODE));
        }

        if (config.getValue(BODY) instanceof String || config.getValue(BODY) instanceof JsonObject) {
            this.config.put(BODY, config.getValue(BODY));
        }

        return Response.ok().build();
    }
}