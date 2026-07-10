package com.utp.inventory.resource;

import com.utp.inventory.domain.Producto;
import com.utp.inventory.repository.ProductoDAO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {

    private final ProductoDAO dao = new ProductoDAO();

    @GET
    public Response listar() {
        try {
            List<Producto> productos = dao.listAll();
            return Response.ok(productos).build();
        } catch (Exception e) {
            return error(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") int id) {
        try {
            Producto p = dao.findById(id);
            if (p == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(p).build();
        } catch (Exception e) {
            return error(e);
        }
    }

    @POST
    public Response crear(Producto p) {
        try {
            return Response.status(Response.Status.CREATED).entity(dao.insert(p)).build();
        } catch (Exception e) {
            return error(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Producto p) {
        try {
            p.setId(id);
            if (dao.update(p)) return Response.ok(p).build();
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return error(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            if (dao.delete(id)) return Response.noContent().build();
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return error(e);
        }
    }

    private Response error(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
    }
}
