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
public class Encuentro implements Serializable{
    
    private String codigo;      //El código de cada encuentro será la fecha del sistema
    private String etapa;
    private Participante participante_1;
    private Participante participante_2;
    private Participante participante_ganador;
    
    private Arbitro arbitro;
    
    private Tiempo_Set set;
    
//    private List<Tiempo_Set> sets;
    private List<Penalidad> penalidades;
    
    private Fecha fecha;

    public Encuentro(){
        
    }
    
    
    public Encuentro(String codigo, String etapa, Participante participante_1, 
            Participante participante_2) {
        this.codigo = codigo;
        this.etapa = etapa;
        this.participante_1 = participante_1;
        this.participante_2 = participante_2;       
        this.penalidades = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getSede() {
        return etapa;
    }

    public void setSede(String sede) {
        this.etapa = sede;
    }

    public Participante getParticipante_1() {
        return participante_1;
    }

    public void setParticipante_1(Participante participante_1) {
        this.participante_1 = participante_1;
    }

    public Participante getParticipante_2() {
        return participante_2;
    }

    public void setParticipante_2(Participante participante_2) {
        this.participante_2 = participante_2;
    }

    public Participante getParticipante_ganador() {
        return participante_ganador;
    }

    public void setParticipante_ganador(Participante participante_ganador) {
        this.participante_ganador = participante_ganador;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    public List<Penalidad> getPenalidades() {
        return penalidades;
    }

    public void setPenalidades(List<Penalidad> penalidades) {
        this.penalidades = penalidades;
    }

    public Tiempo_Set getSet() {
        return set;
    }

    public void setSet(Tiempo_Set set) {
        this.set = set;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }
    
    
}
