package com.utp.inventory.resource;

import com.utp.inventory.infrastructure.ConnectionDB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @GET
    public Response health() {
        String node = System.getenv().getOrDefault("NODE_ID", "inventory");
        try (Connection c = ConnectionDB.get()) {
            boolean ok = c.isValid(2);
            String body = "{\"status\":\"" + (ok ? "UP" : "DEGRADED") + "\",\"node\":\"" + node + "\",\"db\":" + ok + "}";
            return Response.ok(body).build();
        } catch (Exception e) {
            return Response.status(503)
                .entity("{\"status\":\"DOWN\",\"node\":\"" + node + "\",\"db\":false}").build();
        }
    }
}
