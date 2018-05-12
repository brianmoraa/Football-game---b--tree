/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.Modulos;

import java.io.Serializable;

/**
 *
 * @author andre
 */
public class Penalidad implements Serializable{
    
    private String codigo;
    private String tipo_penalidad;

    public Penalidad(String codigo, String tipo_penalidad) {
        this.codigo = codigo;
        this.tipo_penalidad = tipo_penalidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo_penalidad() {
        return tipo_penalidad;
    }

    public void setTipo_penalidad(String tipo_penalidad) {
        this.tipo_penalidad = tipo_penalidad;
    }
    
    
    
}
