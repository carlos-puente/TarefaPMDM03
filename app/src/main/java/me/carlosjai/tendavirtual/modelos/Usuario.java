package me.carlosjai.tendavirtual.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {
    private String nome;
    //TODO avatarimage
    private String apelidos;
    private String email;
    private String id;
    private boolean admin;

    public Usuario() {

    }

    public Usuario(String id, String nome, String apelidos, String email, boolean admin) {
        this.id = id;
        this.nome = nome;
        this.apelidos = apelidos;
        this.admin = admin;
        this.email = email;

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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getNomeApelidos() {
        return nome + " " + apelidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", apelidos='" + apelidos + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                '}';
    }
}
