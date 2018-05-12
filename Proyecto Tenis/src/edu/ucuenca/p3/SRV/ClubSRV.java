/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import edu.ucuenca.p3.DAO.ArbolBMas;
import edu.ucuenca.p3.SRV.exceptions.ClubDatosError;
import edu.ucuenca.p3.SRV.exceptions.ClubDatosVaciosException;
import edu.ucuenca.p3.DAO.ClubDAO;
import edu.ucuenca.p3.DAO.exceptions.ClubNoExisteExcepcion;
import edu.ucuenca.p3.DAO.exceptions.ClubExistenteExcepcion;
import edu.ucuenca.p3.Modulos.Club;
import edu.ucuenca.p3.SRV.exceptions.CedulaValidacionException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andre
 */
public class ClubSRV {

    public void ingresarClub(String codigo, String nombre, String propietario) throws ClubExistenteExcepcion, ClubDatosVaciosException, ClubDatosError {
        validar(codigo, nombre, propietario);
        Club club = new Club(codigo, nombre, propietario);
        club.setEstado(true);
//        ClubDAO.getInstancia().ingresarClub(club);
        //ClubDAO.getInstancia().ingresarClubArchivos(club);

        System.out.println("Agregando club al árbol");
        Object[] regClub = ClubDAO.getInstancia().ingresarClubArchivos(club);

        ArbolBMas arbolRegClub = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolClub");
        arbolRegClub.agregar((String) regClub[2], regClub);
    }

    public void modificarClub(String codigo, String nombre, String propietario) throws ClubDatosVaciosException, ClubDatosError, ClubExistenteExcepcion, ClubNoExisteExcepcion {
        validar(codigo, nombre, propietario);
        Club club = new Club(codigo, nombre, propietario);
        ClubDAO.getInstancia().modificarClub(club);
    }

    public void modificarClubArchivos(String codigo, String nombre, String propietario) throws ClubDatosVaciosException, ClubDatosError {
        validar(codigo, nombre, propietario);
        Club club = new Club(codigo, nombre, propietario);
        club.setEstado(true);
        
        ArbolBMas arbolRegClubes = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolClub");
        Object[] objetoClub = (Object[]) arbolRegClubes.get(codigo);
//        System.out.println("posicion de archivo: "+objetoClub[0]);
        ClubDAO.getInstancia().modificarClubArchivos(club, objetoClub);
    }

    public void eliminarClub(String codigo, int posicion) throws ClubDatosVaciosException, ClubDatosError, ClubExistenteExcepcion, ClubNoExisteExcepcion {
        
        ArbolBMas arbolRegClub = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolClub");
        Object[] regClub = (Object[]) arbolRegClub.get(codigo);

        Club club = ClubDAO.getInstancia().getClubArchivos(regClub);
        ClubDAO.getInstancia().eliminarClubArchivos(club, regClub, posicion);
        
        arbolRegClub.eliminar(codigo);
    }

    public Club obtenerClub_Nombre(String nombre) {
        return ClubDAO.getInstancia().obtenerClub_Nombre(nombre);
    }

    public Club obtenerClub_Nombre_Archivos(String nombre) {

//        ArbolBMas arbolRegClub = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolClub");
//        Object[] regClub = (Object[]) arbolRegClub.get(nombre);
//        System.out.println("Clave del pelaverga ese de tenis: "+regClub[2]);
//        Club club = ClubDAO.getInstancia().getClubArchivos(regClub);
        Club club = ClubDAO.getInstancia().obtenerClubNombre_Archivos(nombre);
//        System.out.println("PP " + club.getNombre());
        return club;
    }

    public Club obtenerClub(String nombre) {
        return ClubDAO.getInstancia().obtenerClub(nombre);
    }

    public Club obtenerClubRegistro(String codigo) {
        return ClubDAO.getInstancia().obtenerClubRegistro(codigo);
    }

    private void validarCedula(String cedula) throws CedulaValidacionException {
        if (cedula == null || cedula.trim().length() == 0) {
            throw new CedulaValidacionException("El campo Cédula no puede estar vacío !");
        }
    }

    public void validar(String codigo, String nombre, String propietario) throws ClubDatosVaciosException, ClubDatosError {

        if (codigo.equals("") || nombre.equals("") || propietario.equals("")) {
            throw new ClubDatosVaciosException("Existen campos vacios.");
        }
        if (!nombre.matches("([a-z]|[A-Z]|\\s)+")) {
            throw new ClubDatosError("Nombre debe tener solo texto.");
        }
        if (!propietario.matches("([a-z]|[A-Z]|\\s)+")) {
            throw new ClubDatosError("Propietario debe tener solo texto.");
        }
    }

    public List<Club> listaClubes() {
        return ClubDAO.getInstancia().listarClubes();
    }

    public ArrayList<Club> listarClubArchivos() {
        ArrayList<Club> listaClub = ClubDAO.getInstancia().getListaClubArchivos();
        ArrayList<Club> listaFinal = new ArrayList<>();
        for (int i = 0; i < listaClub.size(); i++) {
            Club club = listaClub.get(i);
            if (club.isEstado()) {
                listaFinal.add(club);
            }
        }
        return listaFinal;
    }

    public void cargarDatosArchivo() {
        ClubDAO.getInstancia().cargarDatos();
//        listarClubArchivos();
    }

    public ArrayList<Club> getListaClubesOrdenadaArchivos(String ordenarPor, String enOrden) {
        return ClubDAO.getInstancia().getListaClubesOrdenadaArchivos(ordenarPor, enOrden);
    }

    public boolean existeClub(String codigo) {
        return ClubDAO.getInstancia().existeClub(codigo);
    }
}
