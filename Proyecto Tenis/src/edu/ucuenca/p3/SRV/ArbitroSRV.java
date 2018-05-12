/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import edu.ucuenca.p3.DAO.ArbitroDAO;
import edu.ucuenca.p3.DAO.ClubDAO;
import edu.ucuenca.p3.DAO.exceptions.ArbitroExistenteExcepcion;
import edu.ucuenca.p3.DAO.exceptions.ArbitroNoExistenteExcepcion;
import edu.ucuenca.p3.SRV.exceptions.ArbitroDatosErrorException;
import edu.ucuenca.p3.SRV.exceptions.ArbitroDatosVaciosException;
import edu.ucuenca.p3.Modulos.Arbitro;
import edu.ucuenca.p3.SRV.exceptions.CedulaValidacionException;
import java.util.List;

/**
 *
 * @author brian
 */
public class ArbitroSRV {
    
    public void insertarArbitro(String cedula, String nombre, String apellido) throws CedulaValidacionException, ArbitroDatosVaciosException, ArbitroDatosErrorException, ArbitroExistenteExcepcion{
        validar(cedula, nombre, apellido);
        
        Arbitro arbitro = new Arbitro(cedula, nombre, apellido);
        ArbitroDAO.getInstancia().insertarArbitro(arbitro);
    }
    
    public void actualizarArbitro(String cedula, String nombre, String apellido) throws ArbitroNoExistenteExcepcion, ArbitroDatosVaciosException, ArbitroDatosErrorException{
        validar(cedula, nombre, apellido);
        
        Arbitro arbitro = new Arbitro(cedula, nombre, apellido);
        ArbitroDAO.getInstancia().actualizarArbitro(arbitro);
    }
    
    public void eliminarArbitro(String cedula, String nombre, String apellido) throws ArbitroDatosVaciosException, ArbitroDatosErrorException, ArbitroNoExistenteExcepcion{
        validar(cedula, nombre, apellido);
        
        Arbitro arbitro = new Arbitro(cedula, nombre, apellido);
        ArbitroDAO.getInstancia().eliminarArbitro(arbitro);
    }
    
    public Arbitro obtenerArbitro(String codigo){
        return ArbitroDAO.getInstancia().getArbitro(codigo);
    }
    
    public List<Arbitro> listaArbitros(){
        return ArbitroDAO.getInstancia().listarArbitros();
    }
    
    //VALIDACIONES
    public void validar(String cedula, String nombre, String apellido)throws ArbitroDatosVaciosException, ArbitroDatosErrorException{
        if (cedula.equalsIgnoreCase("") || nombre.equalsIgnoreCase("") || apellido.equalsIgnoreCase("")) {
            throw new ArbitroDatosVaciosException("Existen campos vacios.");
        }
        if (!nombre.matches("([a-z]|[A-Z]|\\s)+")) {
            throw new ArbitroDatosErrorException("Nombre debe tener solo texto.");
        }
        if (!apellido.matches("([a-z]|[A-Z]|\\s)+")) {
            throw new ArbitroDatosErrorException("Apellido debe tener solo texto.");
        }
    }
    
    //VALIDACION DE CEDULA
    
    private void validarCedula(String cedula) throws CedulaValidacionException {
        if (cedula == null || cedula.trim().length() == 0) {
            throw new CedulaValidacionException("El campo Cédula no puede estar vacío !");
        }
    }

    public boolean verificarCedulaPersona(String cedula) {
        String cadena = "212121212";
        try {
            Long.getLong(cadena);
            int suma = 0;
            int aux;
            for (int i = 0; i < 9; i++) {
                if ((i + 1) % 2 != 0) {
                    aux = Integer.parseInt(String.valueOf(cadena.charAt(i))) * Integer.parseInt(String.valueOf(cedula.charAt(i)));

                    if (aux > 9) {
                        aux -= 9;
                    }
                    suma += aux;
                } else {
                    suma = suma + Integer.parseInt(String.valueOf(cedula.charAt(i)));
                }
            }
            return (10 - (suma % 10)) == (Integer.parseInt(String.valueOf(cedula.charAt(9)))) || (suma % 10 == 0 && (Integer.parseInt(String.valueOf(cedula.charAt(9)))) == 0);
        } catch (Exception ex) {
            return false;
        }
    }
}