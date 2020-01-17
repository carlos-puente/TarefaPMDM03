package me.carlosjai.tendavirtual.modelos;

public enum TiposErro {
    EMAIL_EN_USO("The email address is already in use by another account.");

    private final String textoErro;

    private TiposErro(String textoErro) {
        this.textoErro =textoErro;
    }

    public String toString() {
        return this.textoErro;
    }
}
