package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Municipio {

    private int IdMunicipio;
    private String NombreMunicipio;
    
    @JsonProperty("EstadoJPA")
    public Estado Estado;

    public Municipio() {

    }

    public Municipio(int IdMunicipio, String NombreMunicipio) {
        this.IdMunicipio = IdMunicipio;
        this.NombreMunicipio = NombreMunicipio;
    }

    public void setIdMunicipio(int IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public void setNombreMunicipio(String NombreMunicipio) {
        this.NombreMunicipio = NombreMunicipio;
    }

    public String getNombreMunicipio() {
        return NombreMunicipio;
    }

    public void setEstado(Estado estado) {
        this.Estado = estado;
    }

    public Estado getEstado() {
        return Estado;
    }
}
