package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Direccion {

    private int IdDireccion;
    private String Calle;
    private String NumeroInterior;
    private String NumeroExterior;
    
    @JsonProperty("ColoniaJPA")
    public Colonia Colonia;
    
    public Usuario Usuario;

    public Direccion() {

    }

    public Direccion(int IdDireccion, String Calle, String NumeroInterior, String NumeroExterior) {
        this.IdDireccion = IdDireccion;
        this.Calle = Calle;
        this.NumeroInterior = NumeroInterior;
        this.NumeroExterior = NumeroExterior;
    }

    public int getIdDireccion() {
        return IdDireccion;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public void setColonia(Colonia colonia) {
        this.Colonia = colonia;
    }

    public Colonia getColonia() {
        return Colonia;
    }

    public void setUsuario(Usuario usuario) {
        this.Usuario = usuario;
    }

    public Usuario getUsuario() {
        return Usuario;
    }
}
