package me.carlosjai.tendavirtual.modelos;


import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Pedido implements Serializable {
    String id;
    String categoria;
    String producto;
    Calendar fecha;
    int cantidad;
    Direccion direccion;
    boolean enTramite;
    boolean rexeitado;

    public Pedido() {
    }

    public Pedido(String id, String categoria, String producto, int cantidad, Calendar fecha) {
        this.id = id;
        this.categoria = categoria;
        this.producto = producto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.enTramite = true;
        this.rexeitado = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRexeitado() {
        return rexeitado;
    }

    public void setRexeitado(boolean rexeitado) {
        this.rexeitado = rexeitado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }
    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public boolean isEnTramite() {
        return enTramite;
    }

    public void setEnTramite(boolean enTramite) {
        this.enTramite = enTramite;
    }

    public Map<String, Object> toMap(String idUsuario) {
        Map<String, Object> data = new HashMap<>();
        data.put("categoria", categoria);
        data.put("producto", producto);
        data.put("cantidad", cantidad);
        data.put("fecha", new Timestamp(fecha.getTime()));
        data.put("en_tramite", enTramite);
        data.put("direccion", direccion.toMap());
        data.put("id_usuario", idUsuario);
        data.put("rexeitado", rexeitado);

        return data;
    }
}

