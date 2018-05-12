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
public class Tiempo_Set implements Serializable{

    private Participante ganador;
    private int Puntaje_1;
    private int Puntaje_2;

    public Tiempo_Set() {

    }

    public Tiempo_Set(Participante ganador, int Puntaje_1, int Puntaje_2) {
        this.ganador = ganador;
        this.Puntaje_1 = Puntaje_1;
        this.Puntaje_2 = Puntaje_2;
    }

    public Participante getGanador() {
        return ganador;
    }

    public void setGanador(Participante ganador) {
        this.ganador = ganador;
    }

    public int getPuntaje_1() {
        return Puntaje_1;
    }

    public void setPuntaje_1(int Puntaje_1) {
        this.Puntaje_1 = Puntaje_1;
    }

    public int getPuntaje_2() {
        return Puntaje_2;
    }

    public void setPuntaje_2(int Puntaje_2) {
        this.Puntaje_2 = Puntaje_2;
    }

    @Override
    public String toString() {
        return "Set: " + "ganador=" + getGanador() + ", Puntaje_1=" + getPuntaje_1() + ", Puntaje_2=" + getPuntaje_2();
    }

}
