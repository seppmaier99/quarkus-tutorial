package com.example;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/fruit")
public class FruitResource {

    private final Logger logger = Logger.getLogger(FruitResource.class);
    private final FruitService fruitService;
    @RestClient
    private FruityViceService fruityViceService;
    @Inject @Claim(standard = Claims.preferred_username)
    String username;

    @Inject
    public FruitResource(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Fruit> fruits() {
        return Fruit.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{season}")
    public List<Fruit> fruitsBySeason(@PathParam("season") String season) {
        return Fruit.getAllFruitsForSeason(season);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response newFruit(Fruit fruit) {
        fruit.id = null;
        fruit.persist();
        return Response.status(CREATED).entity(fruit).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello test";
    }

    @GET
    @Path("/jwt/claim")
    @RolesAllowed("Subscriber")
    public String getClaim() {
        return username;
    }

    @GET
    @Path("/hello2")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello2() {
        logger.info("Hello2 method is called.");
        return fruitService.get("hello test2s");
    }

    @GET
    @Path("/info/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio=0.75, delay = 5000)
    @Retry(maxRetries = 3, delay = 2000)
    @Fallback(value = FruityViceRecovery.class)
    public FruityVice getFruitInfoByName(@PathParam("name") String name) {
        return fruityViceService.getFruitByName(name);
    }
}