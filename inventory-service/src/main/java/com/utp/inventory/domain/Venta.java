package com.utp.inventory.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private Integer id;
    private String cliente;
    private String fecha;
    private BigDecimal total;
    private List<ItemVenta> items = new ArrayList<>();

    public Venta() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<ItemVenta> getItems() { return items; }
    public void setItems(List<ItemVenta> items) { this.items = items; }
}
