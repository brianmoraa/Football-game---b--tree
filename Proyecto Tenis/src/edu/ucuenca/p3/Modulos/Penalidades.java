/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.Modulos;

import java.io.Serializable;

/**
 *
 * @author brian
 */
public class Penalidades implements Serializable{
    private float puntos;

    public Penalidades() {
    }

    public Penalidades(float puntos) {
        this.puntos = puntos;
    }
    
    public float getPuntos() {
        return puntos;
    }

    public void setPuntos(float puntos) {
        this.puntos = puntos;
    }
}