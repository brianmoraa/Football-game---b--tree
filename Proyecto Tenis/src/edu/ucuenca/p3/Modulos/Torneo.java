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
public class Torneo implements Serializable{

    private boolean estado;
    private String codigo;    
    private String tipo;
    private String tipo_juego;

    private Torneo_Categoria torneo_categorías;

    public Torneo() {

    }

    
    public Torneo(String codigo, String tipo, String tipo_juego) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.tipo_juego = tipo_juego;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Torneo_Categoria getTorneo_categorías() {
        return torneo_categorías;
    }

    public void setTorneo_categorías(Torneo_Categoria torneo_categorías) {
        this.torneo_categorías = torneo_categorías;
    }


    public String getTipo_juego() {
        return tipo_juego;
    }

    public void setTipo_juego(String tipo_juego) {
        this.tipo_juego = tipo_juego;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
}
