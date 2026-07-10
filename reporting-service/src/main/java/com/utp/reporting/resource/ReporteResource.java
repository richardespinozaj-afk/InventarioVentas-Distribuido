package com.utp.reporting.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.utp.reporting.client.InventoryClient;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Path("/reportes")
@Produces(MediaType.APPLICATION_JSON)
public class ReporteResource {

    private final InventoryClient client = new InventoryClient();

    @GET
    @Path("/stock-bajo")
    public Response stockBajo(@QueryParam("umbral") @DefaultValue("10") int umbral) {
        try {
            JsonNode productos = client.get("/productos");
            List<Map<String, Object>> criticos = new ArrayList<>();
            for (JsonNode p : productos) {
                if (p.get("stock").asInt() < umbral) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", p.get("id").asInt());
                    m.put("codigo", p.get("codigo").asText());
                    m.put("nombre", p.get("nombre").asText());
                    m.put("stock", p.get("stock").asInt());
                    criticos.add(m);
                }
            }
            Map<String, Object> out = new HashMap<>();
            out.put("umbral", umbral);
            out.put("totalCriticos", criticos.size());
            out.put("productos", criticos);
            return Response.ok(out).build();
        } catch (Exception e) {
            return Response.status(503).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/resumen-ventas")
    public Response resumenVentas() {
        try {
            JsonNode ventas = client.get("/ventas");
            BigDecimal total = BigDecimal.ZERO;
            int n = 0;
            for (JsonNode v : ventas) {
                total = total.add(new BigDecimal(v.get("total").asText()));
                n++;
            }
            Map<String, Object> out = new HashMap<>();
            out.put("cantidadVentas", n);
            out.put("montoTotal", total);
            return Response.ok(out).build();
        } catch (Exception e) {
            return Response.status(503).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
