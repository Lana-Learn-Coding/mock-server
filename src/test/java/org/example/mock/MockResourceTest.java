package org.example.mock;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MockResourceTest {
    @Test
    public void apiPostJson() {
        JsonObject config = new JsonObject()
            .put("http_status_code", 404)
            .put("body", new JsonObject().put("name", "lana").put("age", 18));

        given()
            .when()
            .body(config.toString())
            .contentType(ContentType.JSON)
            .post("/api/config")
            .then()
            .statusCode(200);

        given()
            .when()
            .contentType(ContentType.JSON)
            .post("/api/post")
            .then()
            .statusCode(404)
            .body("name", equalTo("lana"),
                "age", equalTo(18));
    }

    @Test
    public void apiPostPlainText() {
        JsonObject config = new JsonObject()
            .put("http_status_code", 201)
            .put("body", "Hi, my name is lana");

        given()
            .when()
            .contentType(ContentType.JSON)
            .body(config.toString())
            .post("/api/config")
            .then()
            .statusCode(200);

        given()
            .when()
            .contentType(ContentType.JSON)
            .post("/api/post")
            .then()
            .statusCode(201)
            .body(is("Hi, my name is lana"));
    }
}