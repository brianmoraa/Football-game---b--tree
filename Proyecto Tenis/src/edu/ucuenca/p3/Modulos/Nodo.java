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
public class Nodo implements Serializable{
    public int numero_llaves = 0;//numero de llaves
    
    //cada página tiene una cantidad de 2*M elementos, excepto la raiz
    //M=3
    public String[] llaves = new String[4 * 2 - 1];
    public Object[] objetos = new Object[4 * 2 - 1];
    public long[] ubicacionHijo = new long[4 * 2];
    
    public Integer [] estados = new Integer [4 * 2 - 1];
    
    public long ubicacion_siguiente_nodo;
    public boolean valor_hoja;
    
    public long getUbicacion_siguiente_nodo(){
        return ubicacion_siguiente_nodo;
    }
    
    public void setUbicacion_siguiente_nodo(long ubicacion_siguiente_nodo){
        this.ubicacion_siguiente_nodo = ubicacion_siguiente_nodo;
    }
    
    public boolean isValor_hoja(){
        return valor_hoja;
    }
    
    public void serValor_hoja(boolean valor_hoja){
        this.valor_hoja = valor_hoja;
    }
}