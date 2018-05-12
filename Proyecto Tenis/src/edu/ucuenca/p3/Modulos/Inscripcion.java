/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.Modulos;

import java.io.Serializable;

/**
 *
 * @author Home
 */
public class Inscripcion implements Serializable{
    
//    private Categoria categoria;
    private String codigo;      //El campo código será la clave
    private float costo;
    private Participante participante;

//    public Inscripcion(Categoria categoria, String codigo, Participante participante, float costo) {
//        this.categoria = categoria;
//        this.codigo = codigo;
//        this.participante = participante;
//        this.costo = costo;
//    }

    public Inscripcion(String codigo, Participante participante, float costo) {
        this.codigo = codigo;
        this.participante = participante;
        this.costo = costo;
    }

    
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    
}
