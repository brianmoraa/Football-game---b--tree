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
public abstract class Participante implements Serializable{
   
    protected String codigo;
    protected String tipo;
    protected Club club;
    protected boolean estado;
    
    public Participante(String codigo, String tipo, Club club, boolean estado) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.club = club;
        this.estado = estado;
    }
    
    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
}
