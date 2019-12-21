package com.intentfilter.transactionservice.controllers

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("health-check")
class HealthCheckController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun healthCheck(): Response {
        return Response.ok().build()
    }
}