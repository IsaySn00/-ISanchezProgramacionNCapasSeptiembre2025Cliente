package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML;


public class Pais {
    
    private int IdPais;
    private String NombrePais;
    
    public Pais(){
        
    }
    
    public Pais(int IdPais, String NombrePais){
        this.IdPais = IdPais;
        this.NombrePais =  NombrePais;
    }
    
    public void setIdPais(int IdPais){
        this.IdPais = IdPais;
    }
    
    public int getIdPais(){
        return  IdPais;
    }
    
    public void setNombrePais(String NombrePais){
        this.NombrePais = NombrePais;
    }
    
    public String getNombrePais(){
        return NombrePais;
    }
}
