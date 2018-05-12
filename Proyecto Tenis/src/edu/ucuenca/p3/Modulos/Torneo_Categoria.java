/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.Modulos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Home
 */
public class Torneo_Categoria implements Serializable{
    
    private Categoria categoria;
//    private Torneo_Etapa torneo_etapa;
    List<Inscripcion> inscripciones;
    List<Torneo_Etapa> torneo_etapas;
    
    public Torneo_Categoria(){
        
    }

    public Torneo_Categoria(Categoria categoria) {
        this.categoria = categoria;
        this.inscripciones = new ArrayList<>();
        this.torneo_etapas = new ArrayList<>();
    }
    
    
    
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    
    public List<Torneo_Etapa> getTorneo_etapas() {
        return torneo_etapas;
    }

    public void setTorneo_etapas(List<Torneo_Etapa> torneo_etapas) {
        this.torneo_etapas = torneo_etapas;
    }
    
    

//    public Torneo_Etapa getTorneo_etapa() {
//        return torneo_etapa;
//    }
//
//    public void setTorneo_etapa(Torneo_Etapa torneo_etapa) {
//        this.torneo_etapa = torneo_etapa;
//    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }
    
}
