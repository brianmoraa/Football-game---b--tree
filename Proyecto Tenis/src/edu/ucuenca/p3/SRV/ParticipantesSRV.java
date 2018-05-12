/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import edu.ucuenca.p3.DAO.ArbolBMas;
import edu.ucuenca.p3.SRV.exceptions.ClubNotSelectedException;
import edu.ucuenca.p3.DAO.ClubDAO;
import edu.ucuenca.p3.SRV.exceptions.ParticipantesDatosError;
import edu.ucuenca.p3.SRV.exceptions.EdadErroneaException;
import edu.ucuenca.p3.DAO.ParticipanteDAO;
import edu.ucuenca.p3.DAO.exceptions.ParticipanteExistenteException;
import edu.ucuenca.p3.DAO.exceptions.ParticipanteNoExistenteException;
import edu.ucuenca.p3.Modulos.Club;
import edu.ucuenca.p3.Modulos.Equipo;
import edu.ucuenca.p3.Modulos.Jugador;
import edu.ucuenca.p3.Modulos.Participante;
import edu.ucuenca.p3.Modulos.Ranking;
import edu.ucuenca.p3.SRV.exceptions.CedulaValidacionException;
import edu.ucuenca.p3.SRV.exceptions.ParticipantesDatosVaciosException;
import edu.ucuenca.p3.UI.ParticipanteGUI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Home
 */
public class ParticipantesSRV {

    //Registrar jugadores en el sistema
    public void registrarJugador(String cedula, String nombre, String apellido, int edad, Club club,
            String tipo, float puntos) throws ParticipantesDatosVaciosException, CedulaValidacionException, EdadErroneaException, ParticipantesDatosError, ClubNotSelectedException {

        validar(cedula, nombre, apellido, edad, club);
        if (verificarCedulaPersona(cedula) == true) {
        Jugador participante = new Jugador(nombre, apellido, edad, cedula, club, "JUGADOR", true);
        Ranking ranking = new Ranking(puntos);
        participante.setRanking(ranking);
            //ParticipanteDAO.getInstancia().registrarParticipante(participante);
        //        ParticipanteDAO.getInstancia().agregarParticipanteArchivos(participante);

        System.out.println("Agregando participante al árbol");
        Object[] regParticipante = ParticipanteDAO.getInstancia().agregarParticipanteArchivos(participante);

        ArbolBMas arbolRegParticipante = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolParticipante");
        arbolRegParticipante.agregar((String) regParticipante[2], regParticipante);
        } else {
            throw new CedulaValidacionException("La cedula es incorrecta.");
        }
    }

    public Participante getparticipanteArchivos(String cedula) {
        ArbolBMas arbolRegParticipante = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolParticipante");
        Object[] regParticipante = (Object[]) arbolRegParticipante.get(cedula);
        System.out.println("Clave del ese de tenis: " + regParticipante[2]);
        Participante participante = ParticipanteDAO.getInstancia().getParticipanteArchivos(regParticipante);
        return participante;
    }

    public void cargarDatosArchivo() {
        ParticipanteDAO.getInstancia().cargarDatos();
    }

    public void modificarParticipante(String cedula, String nombre, String apellido, int edad, Club club, String tipo, float puntos) throws ParticipantesDatosVaciosException, CedulaValidacionException, ParticipanteNoExistenteException, EdadErroneaException, ParticipantesDatosError, ClubNotSelectedException {
        validar(cedula, nombre, apellido, edad, club);
        Jugador jugador = new Jugador(nombre, apellido, edad, cedula, club, tipo, true);
        Ranking ranking = new Ranking(puntos);
        jugador.setRanking(ranking);
//        ParticipanteDAO.getInstancia().modificarParticipante(cedula, nombre, apellido, edad, club);
        ParticipanteDAO.getInstancia().modificarParticipante(jugador);
//        ParticipanteDAO.getInstancia().modificarParticipanteArchivos(jugador, regUsuario, edad);
    }

    public void modificarEquipo(String cedula, String nombre, String apellido, int edad, Club club, String cedula2, String nombre2, String apellido2, int edad2, Club club2) throws ParticipantesDatosVaciosException, CedulaValidacionException, ParticipanteNoExistenteException, EdadErroneaException, ParticipantesDatosError, ClubNotSelectedException {
        validar(cedula, nombre, apellido, edad, club);
        validar(cedula2, nombre2, apellido2, edad2, club2);

        Jugador jugador1 = new Jugador(nombre, apellido, edad, cedula, club, "JUGADOR", true);
        Jugador jugador2 = new Jugador(nombre2, apellido2, edad2, cedula2, club2, "JUGADOR", true);
        Participante participante = new Equipo(jugador1, jugador2, cedula, "EQUIPO", club, true);

        ParticipanteDAO.getInstancia().modificarParticipante(participante);
//        ParticipanteDAO.getInstancia().modificarParticipante(cedula, nombre, apellido, edad, club);
//        ParticipanteDAO.getInstancia().modificarParticipante(cedula2, nombre2, apellido2, edad2, club2);
    }

    public void eliminarParticipante(String cedula, int posicion) throws ParticipantesDatosVaciosException, CedulaValidacionException, ParticipanteNoExistenteException, EdadErroneaException, ParticipantesDatosError, ClubNotSelectedException {
        validarCedula(cedula);

        ArbolBMas arbolRegParticipante = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolParticipante");
        Object[] regParticipante = (Object[]) arbolRegParticipante.get(cedula);

        Participante participante = ParticipanteDAO.getInstancia().getParticipanteArchivos(regParticipante);
        ParticipanteDAO.getInstancia().eliminarParticipanteArchivos(participante, regParticipante, posicion);

//        arbolRegParticipante.eliminar(cedula);
    }

    public List<Participante> listaParticipantes() {
        return ParticipanteDAO.getInstancia().listar();
    }

    //Método para devolver en una lista los participantes existentes
    //Ésto se valida por el estado de éste
    public ArrayList<Participante> listarParticpantesArchivos() {
        ArrayList<Participante> listaParticipantes = ParticipanteDAO.getInstancia().getListaUsuariosArchivos();
        ArrayList<Participante> listaFinal = new ArrayList<>();
        for (int i = 0; i < listaParticipantes.size(); i++) {
            Participante participante = listaParticipantes.get(i);
            if (participante.isEstado()) {
                listaFinal.add(participante);
            }
        }
        return listaFinal;
    }

    private void validarCedula(String cedula) throws CedulaValidacionException {
        if (cedula == null || cedula.trim().length() == 0) {
            throw new CedulaValidacionException("El campo Cédula no puede estar vacío !");
        }
    }

    public Participante obtenerParticipante(String cedula) {
        return ParticipanteDAO.getInstancia().obtenerParticipante(cedula);
    }

    public Participante obtener(String id) throws ParticipanteNoExistenteException {
        return ParticipanteDAO.getInstancia().obtenerParticipante_Codigo(id);
    }

    public List<String> listarClubs() {
        List<String> nombre = new ArrayList<>();
        List<Club> listaClubs = ClubDAO.getInstancia().listarClubes();
        for (int i = 0; i < listaClubs.size(); i++) {
            nombre.add(listaClubs.get(i).getNombre());
        }
        return nombre;
    }

    public void validar(String cedula, String nombre, String apellido, int edad, Club club) throws ParticipantesDatosVaciosException, CedulaValidacionException, EdadErroneaException, ParticipantesDatosError, ClubNotSelectedException {
        validarCedula(cedula);
//        if (verificarCedulaPersona(cedula) == false) {
//            throw new CedulaValidacionException("Ingrese una cédula correcta.");
//        }
        if (nombre.equals("") || apellido.equals("")) {
            throw new ParticipantesDatosVaciosException("Existen campos vacios.");
        }
        if (!nombre.matches("([a-z]|[A-Z]|\\s)+")) {
            throw new ParticipantesDatosError("Nombre debe tener solo texto.");
        }
        if (!apellido.matches("([a-z]|[A-Z]|\\s)+")) {
            throw new ParticipantesDatosError("Apellido debe tener solo texto.");
        }
        if (edad < 10) {
            throw new EdadErroneaException("La edad no puede ser menor a 10");
        }
        if (club == null) {
            throw new ClubNotSelectedException("Seleccione un club");
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

    public ArrayList<Participante> getListaParticipantesOrdenadaArchivos(String ordenarPor, String enOrden) {
        return ParticipanteDAO.getInstancia().getListaParticipantesOrdenadaArchivos(ordenarPor, enOrden);
    }

    public boolean existeJugador(String cedula) {
        return ParticipanteDAO.getInstancia().existeJugador(cedula);
    }

    public void modificarParticipanteArchivos(String cedula, String nombre, String apellido, int edad, Club club, String tipo, float puntos) throws CedulaValidacionException, ParticipantesDatosVaciosException, EdadErroneaException, ParticipantesDatosError, ClubNotSelectedException {
        validar(cedula, nombre, apellido, edad, club);
//        if (verificarCedulaPersona(cedula) == true) {
        ArbolBMas arbolRegParticipante = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolParticipante");
        Object[] objetoParticipante = (Object[]) arbolRegParticipante.get(cedula);
        Jugador participante = new Jugador(nombre, apellido, edad, cedula, club, tipo, true);
        Ranking ranking = new Ranking(puntos);
        participante.setRanking(ranking);
        ParticipanteDAO.getInstancia().modificarParticipanteArchivos(participante, objetoParticipante);
//            Jugador participante = (Jugador) ParticipanteDAO.getInstancia().getParticipanteArchivos(objetoParticipante);

//        } else {
//            throw new CedulaValidacionException("La cedula es incorrecta.");
//        }
    }
}
