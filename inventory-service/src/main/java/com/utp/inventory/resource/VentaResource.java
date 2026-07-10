package com.utp.inventory.resource;

import com.utp.inventory.domain.Venta;
import com.utp.inventory.repository.VentaDAO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ventas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VentaResource {

    private final VentaDAO dao = new VentaDAO();

    @GET
    public Response listar() {
        try {
            return Response.ok(dao.listAll()).build();
        } catch (Exception e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    public Response registrar(Venta venta) {
        try {
            if (venta.getItems() == null || venta.getItems().isEmpty())
                return Response.status(400).entity("{\"error\":\"La venta no tiene items\"}").build();
            return Response.status(Response.Status.CREATED).entity(dao.registrar(venta)).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Response.status(409).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
