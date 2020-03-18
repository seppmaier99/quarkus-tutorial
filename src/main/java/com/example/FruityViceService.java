package com.example;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api")
@RegisterRestClient
public interface FruityViceService {

    @GET
    @Path("/fruit/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FruityVice> getAllFruits();

    @GET
    @Path("/fruit/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public FruityVice getFruitByName(@PathParam("name") String name);

}