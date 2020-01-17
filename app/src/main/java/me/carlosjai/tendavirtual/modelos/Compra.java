package me.carlosjai.tendavirtual.modelos;

public class Compra {
    String idPedido;
    String textoPedido;

    public Compra(String idPedido, String textoPedido) {
        this.idPedido = idPedido;
        this.textoPedido = textoPedido;
    }

    public Compra() {
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getTextoPedido() {
        return textoPedido;
    }

    public void setTextoPedido(String textoPedido) {
        this.textoPedido = textoPedido;
    }
}
