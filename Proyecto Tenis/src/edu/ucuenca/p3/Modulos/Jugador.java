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
public class Jugador extends Participante implements Serializable{
    
    private String nombre; 
    private String apellido;
    private int edad;
    private Ranking ranking;

    public Jugador(String nombre, String apellido, int edad, String codigo, Club club, String tipo, boolean estado) {
        super(codigo, tipo, club, estado);
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
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

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean compareTo(Jugador u,String atributo){
        String rank1 = Float.toString(this.getRanking().getPuntos());
        String rank2 = Float.toString(u.getRanking().getPuntos());
        
        switch(atributo){
            case "Código Ascendente":
                return this.getCodigo().compareTo(u.getCodigo())<=0;
            case "Código Descendente":
                return this.getCodigo().compareTo(u.getCodigo())>=0;
            case "Ranking Ascendente":
                return rank1.compareTo(rank2)<=0;
            case "Ranking Descendente":
                return rank1.compareTo(rank2)>=0;
        }
        return false;
    }
    
//    //Comparación de valores ranking
//    @Override
//    public int compareTo(Jugador o) {
//        int resultado = 0;
//        if(this.getRanking().getPuntos() > o.getRanking().getPuntos()){
//            resultado = 1;
//        }else if(this.getRanking().getPuntos() < o.getRanking().getPuntos()){
//            resultado = -1;
//        }else{
//            resultado = 0;
//        }
//        return resultado;
//    }
}