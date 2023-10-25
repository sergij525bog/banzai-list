package org.oldman.resources;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(TaskResource.class)
class TaskResourceTest {

    @Test
    void getAll() {
//        when().get()
//                .then()
//                .statusCode(200);
        given().contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(200);
    }

    @Test
    void getTaskById() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getTasksByList() {
    }

    @Test
    void search() {
    }

    @Test
    void count() {
    }
}