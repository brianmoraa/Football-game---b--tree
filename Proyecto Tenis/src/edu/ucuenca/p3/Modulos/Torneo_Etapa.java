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
public class Torneo_Etapa implements Serializable{
    
    private Etapa etapa;
    List<Encuentro> encuentros;
    List<Participante> ganadores;

    public Torneo_Etapa(Etapa etapa, List<Encuentro> encuentros) {
        this.etapa = etapa;
        this.encuentros = new ArrayList<>();
        this.ganadores = new ArrayList<>();
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public List<Encuentro> getEncuentros() {
        return encuentros;
    }

    public void setEncuentros(List<Encuentro> encuentros) {
        this.encuentros = encuentros;
    }   

    public List<Participante> getGanadores() {
        return ganadores;
    }

    public void setGanadores(List<Participante> ganadores) {
        this.ganadores = ganadores;
    }
}
