package com.utp.inventory.domain;

import java.math.BigDecimal;

public class Producto {
    private Integer id;
    private String codigo;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;

    public Producto() {}

    public Producto(Integer id, String codigo, String nombre, BigDecimal precio, Integer stock) {
        this.id = id; this.codigo = codigo; this.nombre = nombre;
        this.precio = precio; this.stock = stock;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
