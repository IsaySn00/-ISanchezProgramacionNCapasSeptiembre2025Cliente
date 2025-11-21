package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Estado {

    private int IdEstado;
    private String NombreEstado;
    
    @JsonProperty("PaisJPA")
    public Pais Pais;

    public Estado() {

    }

    public Estado(int IdEstado, String NombreEstado) {
        this.IdEstado = IdEstado;
        this.NombreEstado = NombreEstado;
    }

    public void setIdEstado(int IdEstado) {
        this.IdEstado = IdEstado;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public void setNombreEstado(String NombreEstado) {
        this.NombreEstado = NombreEstado;
    }

    public String getNombreEstado() {
        return NombreEstado;
    }

    public void setPais(Pais pais) {
        this.Pais = pais;
    }

    public Pais getPais() {
        return Pais;
    }

}
