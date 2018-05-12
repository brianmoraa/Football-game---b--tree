/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.DAO;

import edu.ucuenca.p3.Modulos.Encuentro;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */ 
public class EncuentroDAO {
    
    private LinkedList<Encuentro> encuentros;
    private static EncuentroDAO instancia;
    
    private EncuentroDAO(){
        encuentros = new LinkedList<>();
        
    }   
    public static EncuentroDAO getInstancia(){
        if(instancia == null){
            instancia  = new EncuentroDAO();
        }
        return instancia;
    }
 
    public void insertarEncuentro(Encuentro encuentro){
        
        if(encuentro == null){
            throw new IllegalArgumentException("Los valores de Encuentro no puede ser nulos !");
        }
        
        encuentros.add(encuentro);
    }
    
    public void eliminarEncuentro(Encuentro encuentro){
        encuentros.remove(encuentro);
    }
    
    public LinkedList<Encuentro> listarEncuentros(){
        return encuentros;
    }
    
    
    public Encuentro getEncuentro(String codigo){
        LinkedList<Encuentro> lista_encuentros = listarEncuentros();
        Encuentro encuentro_Alterno = null;
        for (Encuentro encuentro : lista_encuentros) {
            if(encuentro.getCodigo().equalsIgnoreCase(codigo)){
               encuentro_Alterno = encuentro;
            }
        }
        return encuentro_Alterno;
    }
}
