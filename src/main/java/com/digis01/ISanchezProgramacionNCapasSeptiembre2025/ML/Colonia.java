package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Colonia {

    private int IdColonia;
    private String NombreColonia;
    private String CodigoPostal;
    
    @JsonProperty("MunicipioJPA")
    public Municipio Municipio;

    public Colonia() {

    }

    public Colonia(int IdColonia, String NombreColonia, String CodigoPostal) {
        this.IdColonia = IdColonia;
        this.NombreColonia = NombreColonia;
        this.CodigoPostal = CodigoPostal;
    }

    public void setIdColonia(int IdColonia) {
        this.IdColonia = IdColonia;
    }

    public int getIdColonia() {
        return IdColonia;
    }

    public void setNombreColonia(String NombreColonia) {
        this.NombreColonia = NombreColonia;
    }

    public String getNombreColonia() {
        return NombreColonia;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setMunicipio(Municipio municipio) {
        this.Municipio = municipio;
    }

    public Municipio getMunicipio() {
        return Municipio;
    }
}
