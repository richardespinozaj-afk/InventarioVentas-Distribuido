package com.utp.reporting.resource;

import com.utp.reporting.client.InventoryClient;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {
    @GET
    public Response health() {
        String node = System.getenv().getOrDefault("NODE_ID", "reporting");
        boolean upstream;
        try { new InventoryClient().get("/health"); upstream = true; }
        catch (Exception e) { upstream = false; }
        String body = "{\"status\":\"UP\",\"node\":\"" + node + "\",\"inventoryReachable\":" + upstream + "}";
        return Response.ok(body).build();
    }
}
