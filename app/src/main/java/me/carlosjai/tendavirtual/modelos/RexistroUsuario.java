package me.carlosjai.tendavirtual.modelos;

public class RexistroUsuario {
    private String nome;
    private String apelidos;
    private String email;
    private String contrasinal;
    private boolean admin;

    public RexistroUsuario() {
    }

    public RexistroUsuario(String nome, String apelidos, String email, String contrasinal, boolean admin) {
        this.nome = nome;
        this.apelidos = apelidos;
        this.email = email;
        this.contrasinal = contrasinal;
        this.admin = admin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelidos() {
        return apelidos;
    }

    public void setApelidos(String apelidos) {
        this.apelidos = apelidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasinal() {
        return contrasinal;
    }

    public void setContrasinal(String contrasinal) {
        this.contrasinal = contrasinal;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
