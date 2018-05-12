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
public class Categoria implements Serializable{
    
    private String codigo;
    private int edad_minima;
    private int edad_maxima;
    private String nombre;

    public Categoria(String codigo, int edad_minima, int edad_maxima, String nombre) {
        this.codigo = codigo;
        this.edad_minima = edad_minima;
        this.edad_maxima = edad_maxima;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getEdad_minima() {
        return edad_minima;
    }

    public void setEdad_minima(int edad_minima) {
        this.edad_minima = edad_minima;
    }

    public int getEdad_maxima() {
        return edad_maxima;
    }

    public void setEdad_maxima(int edad_maxima) {
        this.edad_maxima = edad_maxima;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Categoria: " + "codigo=" + getCodigo() + ", edad_minima=" + getEdad_minima() + ", edad_maxima=" + 
                getEdad_maxima() + ", nombre=" + getNombre();
    }
    
    
}
