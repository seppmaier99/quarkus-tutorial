package com.example;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class FruitResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/fruit")
          .then()
             .statusCode(200)
             .body(is("hello test"));
    }

    @Test
    @Order(1)
    public void testInsertFruit() {
        Fruit fruit = new Fruit();
        fruit.name = "Watermelon";
        fruit.season = "Summer";

        given()
                .contentType("application/json")
                .body(fruit)
                .when()
                .post("/fruit")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    @Order(2)
    public void testFindFruitsBySeason() {
        final List<Fruit> fruits = given()
                .when()
                .get("/fruit/{season}", "Summer")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Fruit.class);

        assertThat(fruits)
                .extracting("name")
                .containsExactlyInAnyOrder("Banana", "Watermelon", "Plum", "Blueberry");
    }

}