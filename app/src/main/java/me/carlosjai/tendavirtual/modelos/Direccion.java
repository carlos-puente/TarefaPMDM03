package me.carlosjai.tendavirtual.modelos;


import java.util.HashMap;
import java.util.Map;

public class Direccion {
    private String rua;
    private String cidade;
    private String codigoPostal;

    public Direccion(String rua, String cidade, String codigoPostal) {
        this.rua = rua;
        this.cidade = cidade;
        this.codigoPostal = codigoPostal;
    }


    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("direccion", rua);
        data.put("cidade", cidade);
        data.put("codigo_postal", codigoPostal);

        return data;
    }
}
