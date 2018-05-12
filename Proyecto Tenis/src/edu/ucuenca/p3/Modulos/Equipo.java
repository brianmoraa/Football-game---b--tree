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
public class Equipo extends Participante implements Serializable{

    private Jugador jugador1;
    private Jugador jugador2;

    public Equipo(Jugador jugador1, Jugador jugador2, String codigo, String tipo, Club club, boolean estado) {
        super(codigo, tipo, club, estado);
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
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
    
    public boolean getEstado(){
        return estado;
    }
    
    public void setEstado(boolean estado){
        this.estado = estado;
    }
    
}