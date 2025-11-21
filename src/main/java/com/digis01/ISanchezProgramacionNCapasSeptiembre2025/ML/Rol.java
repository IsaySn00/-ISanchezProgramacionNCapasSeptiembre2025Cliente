package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML;

public class Rol {

    private int IdRol;
    private String NombreRol;

    public Rol() {

    }

    public Rol(int IdRol, String NombreRol) {
        this.IdRol = IdRol;
        this.NombreRol = NombreRol;
    }

    public void setIdRol(int IdRol) {
        this.IdRol = IdRol;
    }

    public int getIdRol() {
        return IdRol;
    }

    public void setNombreRol(String NombreRol) {
        this.NombreRol = NombreRol;
    }

    public String getNombreRol() {
        return NombreRol;
    }

}
