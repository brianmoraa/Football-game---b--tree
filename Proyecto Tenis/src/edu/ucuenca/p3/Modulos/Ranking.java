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
public class Ranking implements Serializable{
    
    private float puntaje;
    
    public Ranking(){
        
    }

    public Ranking(float puntaje) {
        this.puntaje = puntaje;
    }

    public float getPuntos() {
        return puntaje;
    }

    public void setPuntos(float puntos) {
        this.puntaje = puntos;
    }
}