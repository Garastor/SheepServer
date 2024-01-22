package com.paazl.rest;

import com.paazl.service.ShepherdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/shepherdmanager")
public class ShepherdResource {
    private final ShepherdService service;

    @Autowired
    public ShepherdResource(ShepherdService service) {
        this.service = service;
    }

    @GET
    @Path("/status")
    public HttpStatus getServerStatus(){
        return HttpStatus.OK;
    }

    @GET
    @Path("/orderNewSheep/{nofSheepDesired}")
    public String orderNewSheep(@PathParam("nofSheepDesired") int nofSheepDesired) {
        return service.orderNewSheep(nofSheepDesired);
    }
}