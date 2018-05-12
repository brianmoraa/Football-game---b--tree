/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.DAO;

import edu.ucuenca.p3.DAO.exceptions.ArbitroNoExistenteExcepcion;
import edu.ucuenca.p3.DAO.exceptions.ArbitroExistenteExcepcion;
import edu.ucuenca.p3.Modulos.Arbitro;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Home
 */
public class ArbitroDAO {
    
    private Map<String, Arbitro> arbitros;
    private static ArbitroDAO instancia;
    
    private ArbitroDAO(){
        arbitros = new TreeMap<String, Arbitro>();
    }
    
    public static ArbitroDAO getInstancia(){
        if(instancia == null){
            instancia = new ArbitroDAO();
        }
        return instancia;
    }
    
    public void insertarArbitro(Arbitro arbitro) throws ArbitroExistenteExcepcion{
        
        if(arbitro == null){
            throw new IllegalArgumentException("No se puede ingresar un árbitro con valores nulos !");
        }
        
        if(arbitros.get(arbitro.getCedula())!= null){
            throw new ArbitroExistenteExcepcion("Árbitro ya existente en el sistema !");
        }
        
        arbitros.put(arbitro.getCedula(), arbitro);
        
    }
    
    public void alterArbitro(Arbitro arbitro, boolean update) throws ArbitroNoExistenteExcepcion{
        
        Arbitro arbitroAlterno = arbitros.get(arbitro.getCedula());
        
        if(arbitroAlterno == null){
            throw new ArbitroNoExistenteExcepcion("Árbitro no existente en el registro !");
        }
        
        if(update){
            arbitros.put(arbitro.getCedula(), arbitro);
        }else{
            arbitros.remove(arbitro.getCedula());
        }
    }
    
    public void actualizarArbitro(Arbitro arbitro) throws ArbitroNoExistenteExcepcion{
        alterArbitro(arbitro, true);
    }
    
    public void eliminarArbitro(Arbitro arbitro) throws ArbitroNoExistenteExcepcion{
        alterArbitro(arbitro, false);
    }
    
    public Arbitro getArbitro(String cedula){
        if(arbitros.get(cedula) != null){
            return arbitros.get(cedula);
        }
        
        return null;
    }
    
    public List<Arbitro> listarArbitros(){
        
        List lista_arbitros = new ArrayList<>();
        for (Map.Entry<String, Arbitro> entrySet : arbitros.entrySet()) {
            Arbitro value = entrySet.getValue();
            lista_arbitros.add(value);
        }
        return lista_arbitros;
    }
}
