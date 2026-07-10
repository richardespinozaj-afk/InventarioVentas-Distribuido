package com.utp.inventory.repository;

import com.utp.inventory.domain.Producto;
import com.utp.inventory.infrastructure.ConnectionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listAll() throws Exception {
        List<Producto> out = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre, precio, stock FROM producto ORDER BY id";
        try (Connection c = ConnectionDB.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    public Producto findById(int id) throws Exception {
        String sql = "SELECT id, codigo, nombre, precio, stock FROM producto WHERE id=?";
        try (Connection c = ConnectionDB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public Producto insert(Producto p) throws Exception {
        String sql = "INSERT INTO producto(codigo, nombre, precio, stock) VALUES(?,?,?,?)";
        try (Connection c = ConnectionDB.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getStock() == null ? 0 : p.getStock());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
        return p;
    }

    public boolean update(Producto p) throws Exception {
        String sql = "UPDATE producto SET codigo=?, nombre=?, precio=?, stock=? WHERE id=?";
        try (Connection c = ConnectionDB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM producto WHERE id=?";
        try (Connection c = ConnectionDB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Producto map(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"),
            rs.getBigDecimal("precio"), rs.getInt("stock"));
    }
}
