package com.utp.inventory.repository;

import com.utp.inventory.domain.ItemVenta;
import com.utp.inventory.domain.Venta;
import com.utp.inventory.infrastructure.ConnectionDB;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public Venta registrar(Venta venta) throws Exception {
        try (Connection c = ConnectionDB.get()) {
            c.setAutoCommit(false);
            try {
                BigDecimal total = BigDecimal.ZERO;
                // validar stock y calcular total
                for (ItemVenta it : venta.getItems()) {
                    BigDecimal precio = precioYValidaStock(c, it.getProductoId(), it.getCantidad());
                    it.setPrecioUnitario(precio);
                    total = total.add(precio.multiply(BigDecimal.valueOf(it.getCantidad())));
                }
                // cabecera
                String sqlV = "INSERT INTO venta(cliente, fecha, total) VALUES(?, NOW(), ?)";
                int ventaId;
                try (PreparedStatement ps = c.prepareStatement(sqlV, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, venta.getCliente());
                    ps.setBigDecimal(2, total);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); ventaId = rs.getInt(1); }
                }
                // detalle + descuento de stock
                String sqlD = "INSERT INTO detalle_venta(venta_id, producto_id, cantidad, precio_unitario) VALUES(?,?,?,?)";
                String sqlS = "UPDATE producto SET stock = stock - ? WHERE id=?";
                try (PreparedStatement pd = c.prepareStatement(sqlD);
                     PreparedStatement psk = c.prepareStatement(sqlS)) {
                    for (ItemVenta it : venta.getItems()) {
                        pd.setInt(1, ventaId);
                        pd.setInt(2, it.getProductoId());
                        pd.setInt(3, it.getCantidad());
                        pd.setBigDecimal(4, it.getPrecioUnitario());
                        pd.addBatch();
                        psk.setInt(1, it.getCantidad());
                        psk.setInt(2, it.getProductoId());
                        psk.addBatch();
                    }
                    pd.executeBatch();
                    psk.executeBatch();
                }
                c.commit();
                venta.setId(ventaId);
                venta.setTotal(total);
                return venta;
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    private BigDecimal precioYValidaStock(Connection c, int productoId, int cantidad) throws Exception {
        String sql = "SELECT precio, stock FROM producto WHERE id=? FOR UPDATE";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalArgumentException("Producto " + productoId + " no existe");
                int stock = rs.getInt("stock");
                if (stock < cantidad)
                    throw new IllegalStateException("Stock insuficiente para producto " + productoId);
                return rs.getBigDecimal("precio");
            }
        }
    }

    public List<Venta> listAll() throws Exception {
        List<Venta> out = new ArrayList<>();
        String sql = "SELECT id, cliente, fecha, total FROM venta ORDER BY id DESC";
        try (Connection c = ConnectionDB.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Venta v = new Venta();
                v.setId(rs.getInt("id"));
                v.setCliente(rs.getString("cliente"));
                v.setFecha(String.valueOf(rs.getTimestamp("fecha")));
                v.setTotal(rs.getBigDecimal("total"));
                out.add(v);
            }
        }
        return out;
    }
}
