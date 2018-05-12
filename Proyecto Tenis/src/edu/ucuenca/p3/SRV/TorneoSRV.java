/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import edu.ucuenca.p3.DAO.ArbolBMas;
import edu.ucuenca.p3.SRV.exceptions.InscripicionExistenteException;
import edu.ucuenca.p3.SRV.exceptions.InscripcionExcepcionEdad;
import edu.ucuenca.p3.DAO.CategoriaDAO;
import edu.ucuenca.p3.DAO.ParticipanteDAO;
import edu.ucuenca.p3.DAO.TorneoDAO;
import edu.ucuenca.p3.DAO.exceptions.TorneoExistenteExcepcion;
import edu.ucuenca.p3.Modulos.Categoria;
import edu.ucuenca.p3.Modulos.Club;
import edu.ucuenca.p3.Modulos.Encuentro;
import edu.ucuenca.p3.Modulos.Equipo;
import edu.ucuenca.p3.Modulos.Etapa;
import edu.ucuenca.p3.Modulos.Inscripcion;
import edu.ucuenca.p3.Modulos.Jugador;
import edu.ucuenca.p3.Modulos.Participante;
import edu.ucuenca.p3.Modulos.Torneo;
import edu.ucuenca.p3.Modulos.Torneo_Categoria;
import edu.ucuenca.p3.Modulos.Torneo_Etapa;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.Random;

/**
 *
 * @author andre
 */
public class TorneoSRV {

    Random random = new Random();

    public void crearTorneo(String codigo, String tipo, String tipo_juego, String nombre_categoria) throws TorneoExistenteExcepcion {
        validarCampos(codigo, tipo, tipo_juego);
        Torneo torneo = new Torneo(codigo, tipo, tipo_juego);
        torneo.setEstado(true);

        Categoria categoria = CategoriaDAO.getInstancia().obtenerCategoriaNombre(nombre_categoria);
        Torneo_Categoria torneo_Categoria = new Torneo_Categoria(categoria);
        torneo.setTorneo_categorías(torneo_Categoria);
        System.out.println("Agregando torneo al árbol");
        Object[] regTorneo = TorneoDAO.getInstancia().ingresarTorneoArchivos(torneo);

        ArbolBMas arbolRegTorneo = new ArbolBMas("src/edu/ucuenca/p3/DAO_archivos/arbolTorneo");
        arbolRegTorneo.agregar((String) regTorneo[2], regTorneo);

//-------------------------------------------------------------------------
        System.out.println("Agregando torneo al árbol");
//        Object[] regTorneo = TorneoDAO.getInstancia().ingresarTorneoArchivos(torneo);
//
//        ArbolBMas arbolRegTorneo = new ArbolBMas("src/edu/ucuenca/p3/DAO_archivos/arbolTorneo");
//        arbolRegTorneo.agregar((String) regTorneo[2], regTorneo);
    }

    public void crearCategoria(String codigo, List<Inscripcion> lista_inscripcion) {
//        Torneo torneo = TorneoDAO.getInstancia().getTorneo(codigo);
//        Torneo_Categoria torneo_Categoria = torneo.getTorneo_categorías();
//
//        //Se realiza un shuffle a la lista de inscripciones
//        Collections.shuffle(lista_inscripcion, random);
//        torneo_Categoria.setInscripciones(lista_inscripcion);
//        torneo.setTorneo_categorías(torneo_Categoria);    //Ingreso la lista de categorías al torneo

        ArbolBMas arbolRegTorneo = new ArbolBMas("src/edu/ucuenca/p3/DAO_archivos/arbolTorneo");
        Object[] objetoTorneo = (Object[]) arbolRegTorneo.get(codigo);
        Torneo torneo = TorneoDAO.getInstancia().getTorneoArchivos(objetoTorneo);
        
        Torneo_Categoria torneo_Categoria = torneo.getTorneo_categorías();

        //Se realiza un shuffle a la lista de inscripciones
        Collections.shuffle(lista_inscripcion, random);
        torneo_Categoria.setInscripciones(lista_inscripcion);
        torneo.setTorneo_categorías(torneo_Categoria);    //Ingreso la lista de categorías al torneo

        TorneoDAO.getInstancia().modificarTorneo(objetoTorneo, torneo);
    }

    //Crear etapas
    public Torneo_Etapa crearEtapas_Torneo(String nombre_etapa, List<Encuentro> lista_encuentro) {

        //Se instancia la etapa* 
        Etapa etapa = new Etapa(nombre_etapa);
        Torneo_Etapa torneo_etapa = new Torneo_Etapa(etapa, lista_encuentro);

        return torneo_etapa;
    }

    //Ingresar los torneos_etapa en una lista 
    public void ingresarEtapas_Lista(List<Torneo_Etapa> lista_torneos_etapas, Torneo_Etapa torneo_etapa) {

        lista_torneos_etapas.add(torneo_etapa);

    }

    //Mostrar campeón del torneo
    public Participante mostrarCampeon(List<Participante> lista_participante) {
        Participante ganador = lista_participante.get(0);
        return ganador;
    }
//

    public Inscripcion inscribirJugadores(String nombreCategoria, String codigo_participante, float costo) throws InscripcionExcepcionEdad {
//        Participante participante = ParticipanteDAO.getInstancia().getParticipante(codigo_participante);
        ArbolBMas arbolRegParticipante = new ArbolBMas("src/edu/ucuenca/p3/DAO_archivos/arbolParticipante");

        Object[] regjugador = (Object[]) arbolRegParticipante.get(codigo_participante);

        Participante participante = ParticipanteDAO.getInstancia().getParticipanteArchivos(regjugador);
//        System.out.println("+++ " + participante.getCodigo());
        System.out.println("Clave : " + regjugador[2]);
        System.out.println("Lugar en el archivo: " + regjugador[0]);
        Categoria categoria = CategoriaDAO.getInstancia().obtenerCategoriaNombre(nombreCategoria);
        Inscripcion inscripcion = null;
        Jugador jugador = (Jugador) participante;

        if (jugador.getEdad() >= categoria.getEdad_minima() && jugador.getEdad() <= categoria.getEdad_maxima()) {
            inscripcion = new Inscripcion(participante.getCodigo(), jugador, costo);
        } else {
            throw new InscripcionExcepcionEdad("Edad no permitida en éste torneo !");
        }
        return inscripcion;
    }

    //Se ingresa la inscripción en una lista de inscripciones
    public void ingresarInscripcion_Lista(Inscripcion inscripcion, List<Inscripcion> lista_inscripcion) throws InscripicionExistenteException {

        lista_inscripcion.add(inscripcion);
    }

    public void validarInscripcion(Inscripcion inscripcion, List<Inscripcion> lista_inscripcion) throws InscripicionExistenteException {
        for (int i = 0; i < lista_inscripcion.size(); i++) {
            Inscripcion get_inscripcion = lista_inscripcion.get(i);
            if (inscripcion.getCodigo().equalsIgnoreCase(get_inscripcion.getCodigo())) {
                throw new InscripicionExistenteException("El jugador ya esta inscrito.");
            }
        }
    }

    public boolean cantidad_inscritos(List<Inscripcion> lista_inscritos, String modalidad, int cantidad_inscritos) {
        if (modalidad.equalsIgnoreCase("Simple")) {
            if (lista_inscritos != null && lista_inscritos.size() == cantidad_inscritos) {
                return false;
            } else {
                return true;
            }
        } else {
            if (lista_inscritos != null && lista_inscritos.size() == cantidad_inscritos * 2) {
                return false;
            } else {
                return true;
            }
        }
    }

    public void eliminarInscripcion_List(List<Inscripcion> lista_Inscripcion, String codigo_jugador) {
        Inscripcion inscripcion = null;
        for (Inscripcion inscripcion_jugador : lista_Inscripcion) {
            if (inscripcion_jugador.getCodigo().equalsIgnoreCase(codigo_jugador)) {
                inscripcion = inscripcion_jugador;
            }
        }
        lista_Inscripcion.remove(inscripcion);
    }

    public Torneo obtenerTorneo(String codigo_torneo) {
        if (codigo_torneo != null || codigo_torneo.trim().length() > 0) {
            return TorneoDAO.getInstancia().getTorneo(codigo_torneo);

        } else {
            throw new IllegalArgumentException("Campo de código del campo inválido!");
        }
    }

    public Torneo obtenerTorneoArchivos(String codigo_torneo) {
        if (codigo_torneo != null || codigo_torneo.trim().length() > 0) {
            ArbolBMas arbolRegParticipante = new ArbolBMas("src/edu/ucuenca/p3/DAO_archivos/arbolTorneo");

            Object[] regjugador = (Object[]) arbolRegParticipante.get(codigo_torneo);

            Torneo torneo = TorneoDAO.getInstancia().getTorneoArchivos(regjugador);

            return torneo;
        } else {
            throw new IllegalArgumentException("Campo de código del campo inválido!");
        }
    }

    public Torneo_Categoria obtenerCategoria(String codigo_torneo, String nombre_categoria) {
        Torneo_Categoria torneoCategoria = null;
        Torneo torneo = TorneoDAO.getInstancia().getTorneo(codigo_torneo);
        torneoCategoria = torneo.getTorneo_categorías();
        return torneoCategoria;
    }

    //Se usa la primera vez para extraer los participantes de la lista de inscritos
    public List<Participante> crearListaParticipantes(String modalidad, List<Inscripcion> lista_inscritos) {
        List<Participante> lista_participantes = new ArrayList<>();

        Inscripcion inscripcion_1 = null;
        Inscripcion inscripcion_2 = null;

        if (modalidad.equalsIgnoreCase("Simple")) {
            for (int i = 0; i < lista_inscritos.size(); i++) {
                inscripcion_1 = lista_inscritos.get(i);
                Participante participante = inscripcion_1.getParticipante();
                Jugador jugador = (Jugador) participante;
                lista_participantes.add(jugador);
            }
        } else {
            for (int i = 0; i < lista_inscritos.size() - 1; i = i + 2) {
                inscripcion_1 = lista_inscritos.get(i);
                inscripcion_2 = lista_inscritos.get(i + 1);
                Participante participante1 = inscripcion_1.getParticipante();
                Participante participante2 = inscripcion_2.getParticipante();
                Jugador jugador1 = (Jugador) participante1;
                Jugador jugador2 = (Jugador) participante2;
                String clave = jugador1.getApellido() + " -  " + jugador2.getApellido();
                Club club = jugador1.getClub();
                Equipo equipo = new Equipo(jugador1, jugador2, clave, "Equipo", club, true);
                lista_participantes.add(equipo);
            }
        }
//        Collections.shuffle(lista_inscritos, random);
//
//        crearCategoria(codigo_torneo, nombre_categoria, lista_inscritos);
        return lista_participantes;
    }

    public List<Inscripcion> obtenerLista_Inscritos_Torneo(String codigo) {
//        Torneo torneo = obtenerTorneo(codigo);
        Torneo torneo = obtenerTorneoArchivos(codigo);
        List<Inscripcion> lista_inscripcion = torneo.getTorneo_categorías().getInscripciones();
        return lista_inscripcion;
    }

    public List<Torneo> getListaTorneo() {
        return TorneoDAO.getInstancia().listarTorneos();
    }

    public void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().length() == 0) {
            throw new IllegalArgumentException("El campo código no puede contener valores nulos !");
        }
    }

    public void validarCampos(String codigo, String tipo, String tipo_juego) {

        if (codigo == null || codigo.trim().length() == 0) {
            throw new IllegalArgumentException("El campo código no puede contener valores nulos !");
        }
        if (tipo == null || tipo.trim().length() == 0) {
            throw new IllegalArgumentException("El campo de tipo no puede contener valores nulo !");
        }
        if (tipo_juego == null || tipo_juego.trim().length() == 0) {
            throw new IllegalArgumentException("El campo de tipo de juego no puede contener valores nulos !");
        }
    }

    public boolean validarCantidadInscritos(List<Inscripcion> lista_inscritos, int cantidad_inscritos, String modalidad) {
        if (modalidad.equalsIgnoreCase("simple")) {
            System.out.println("Aqui si llega ");
            return lista_inscritos.size() == cantidad_inscritos;
        } else {
            return lista_inscritos.size() == cantidad_inscritos * 2;
        }

    }

    public void cargarDatosArchivo() {
        TorneoDAO.getInstancia().cargarDatos();
    }

    public ArrayList<Torneo> listarTorneosArchivos() {
        ArrayList<Torneo> listaTorneos = TorneoDAO.getInstancia().getListaTorneosArchivos();
        ArrayList<Torneo> listaFinal = new ArrayList<>();
        for (int i = 0; i < listaTorneos.size(); i++) {
            Torneo torneo = listaTorneos.get(i);
            if (torneo.isEstado()) {
                listaFinal.add(torneo);
            }
        }
        return listaFinal;
    }

    public boolean existeTorneo(String codigo) {
        return TorneoDAO.getInstancia().existeTorneo(codigo);
    }
}
