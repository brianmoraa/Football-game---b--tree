/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import edu.ucuenca.p3.DAO.ArbolBMas;
import edu.ucuenca.p3.SRV.exceptions.EncuentrosNoExistentesExcepcion;
import edu.ucuenca.p3.SRV.exceptions.EtapaFinalizadaException;
import edu.ucuenca.p3.SRV.exceptions.EncuentroExcepcion;
import edu.ucuenca.p3.SRV.exceptions.TorneoFinalizadoException;
import edu.ucuenca.p3.DAO.EncuentroDAO;
import edu.ucuenca.p3.DAO.ParticipanteDAO;
import edu.ucuenca.p3.DAO.TorneoDAO;
import edu.ucuenca.p3.Modulos.Encuentro;
import edu.ucuenca.p3.Modulos.Equipo;
import edu.ucuenca.p3.Modulos.Etapa;
import edu.ucuenca.p3.Modulos.Inscripcion;
import edu.ucuenca.p3.Modulos.Jugador;
import edu.ucuenca.p3.Modulos.Participante;
import edu.ucuenca.p3.Modulos.Ranking;
import edu.ucuenca.p3.Modulos.Tiempo_Set;
import edu.ucuenca.p3.Modulos.Torneo;
import edu.ucuenca.p3.Modulos.Torneo_Categoria;
import edu.ucuenca.p3.Modulos.Torneo_Etapa;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author andre
 */
public class EncuentroSRV {

    public boolean validarEtapa(String codigo_torneo) {
        TorneoSRV torneoSrv = new TorneoSRV();
//        Torneo torneo = torneoSrv.obtenerTorneo(codigo_torneo);
        Torneo torneo = torneoSrv.obtenerTorneoArchivos(codigo_torneo);
        List<Torneo_Etapa> listaEtapas = torneo.getTorneo_categorías().getTorneo_etapas();
        return !(listaEtapas == null || listaEtapas.isEmpty());
    }

    public List<Encuentro> encuentrosEtapa(String codigo_torneo) {
        TorneoSRV torneoSrv = new TorneoSRV();
//        Torneo torneo = torneoSrv.obtenerTorneo(codigo_torneo);
        Torneo torneo = torneoSrv.obtenerTorneoArchivos(codigo_torneo);

        List<Torneo_Etapa> listaEtapas = torneo.getTorneo_categorías().getTorneo_etapas();
        List<Encuentro> listaEncuentros = null;
        int cantidadEtapas = listaEtapas.size();
        if (!listaEtapas.isEmpty()) {
            Torneo_Etapa etapa = listaEtapas.get(cantidadEtapas - 1);
            listaEncuentros = etapa.getEncuentros();
        }
        return listaEncuentros;
    }
//    Crear lista de encuentros

    public List<Encuentro> lista_Encuentros(String codigo, String nombre_categoria, String etapa,
            String modalidad) throws TorneoFinalizadoException, EncuentroExcepcion {
        List<Participante> lista_participante = null;
        List<Encuentro> lista_encuentro = new LinkedList<>();
        List<Inscripcion> lista_inscripcion = new ArrayList<>();

        Encuentro encuentro = null;
        TorneoSRV torneoSrv = new TorneoSRV();

//        Torneo torneo = torneoSrv.obtenerTorneo(codigo);
        ArbolBMas arbolRegTorneo = new ArbolBMas("src/edu/ucuenca/p3/DAO_archivos/arbolTorneo");
        Object[] objetoTorneo = (Object[]) arbolRegTorneo.get(codigo);
        Torneo torneo = torneoSrv.obtenerTorneoArchivos(codigo);

        if (etapa.equalsIgnoreCase("1")) {
            lista_inscripcion = torneoSrv.obtenerLista_Inscritos_Torneo(codigo);
            lista_participante = torneoSrv.crearListaParticipantes(modalidad, lista_inscripcion);
//            lista_participante = torneoSrv.crearListaParticipantes(codigo, modalidad, nombre_categoria, lista_inscripcion);
        } else {
            lista_participante = clasificados(codigo);
            
        }

        if (lista_participante.size() > 1) {
            for (int i = 0; i < lista_participante.size() - 1; i = i + 2) {
                if (modalidad.equalsIgnoreCase("Simple")) {

                    Participante participante1 = lista_participante.get(i);
                    Participante participante2 = lista_participante.get(i + 1);

                    encuentro = new Encuentro(codigo, etapa, participante1, participante2);
                    lista_encuentro.add(encuentro);
                    EncuentroDAO.getInstancia().insertarEncuentro(encuentro);

                } else {
                    Equipo participante1 = (Equipo) lista_participante.get(i);
                    Equipo participante2 = (Equipo) lista_participante.get(i + 1);

                    encuentro = new Encuentro(codigo, etapa, participante1, participante2);
                    lista_encuentro.add(encuentro);
                    EncuentroDAO.getInstancia().insertarEncuentro(encuentro);
                }
            }
            System.out.println("Tamanio de la lista de encuentros: " + lista_encuentro.size());
            //Obtener el torneo categoría 
            Torneo_Categoria torneo_categoria = torneo.getTorneo_categorías();
            System.out.println("Categoria: " + torneo_categoria.getCategoria().getNombre());
            //Crear la etapa del torneo con la lista de encuentros y el nombre de la etapa
            Torneo_Etapa torneo_etapa = torneoSrv.crearEtapas_Torneo(etapa, lista_encuentro);
            torneo_etapa.setEncuentros(lista_encuentro);
            //Agrego a la lista de etapas del torneo la etapa recién creada
            System.out.println("Encuentros de etapa: " + torneo_etapa.getEncuentros().size());
            List<Torneo_Etapa> lista_torneo_etapa = torneo_categoria.getTorneo_etapas();
            //Ingreso las etapas en la lista
            torneoSrv.ingresarEtapas_Lista(lista_torneo_etapa, torneo_etapa);
            //Seteo los datos
            torneo_categoria.setTorneo_etapas(lista_torneo_etapa);
            torneo.setTorneo_categorías(torneo_categoria);
            System.out.println("Número de etapa: " + torneo.getTorneo_categorías().getTorneo_etapas().size());
            TorneoDAO.getInstancia().modificarTorneo(objetoTorneo, torneo);
//            lista_encuentro = new LinkedList<>();
        } else {
            if (modalidad.equalsIgnoreCase("simple")) {
                Jugador jugador_ganador = (Jugador) lista_participante.get(0);
                throw new TorneoFinalizadoException("El torneo ha finalizado !\n El campeón es: " + jugador_ganador.getNombre() + " " + jugador_ganador.getApellido());
            } else {
                Equipo equipo_ganador = (Equipo) lista_participante.get(0);
                throw new TorneoFinalizadoException("El torneo ha finalizado !\nEl campeón es: " + equipo_ganador.getCodigo());
            }
        }
        return lista_encuentro;
    }

    //Método para jugar los encuentros
    public void jugarEncuentro(String codigo, int numero_encuentro, String nombre_jugador1, String nombre_jugador2,
            int puntaje1, int puntaje2) throws EncuentroExcepcion, TorneoFinalizadoException {

        TorneoSRV torneoSrv = new TorneoSRV();
        
        /*Se obtiene el torneo del árbol y del archivo de registros*/
        ArbolBMas arbolRegTorneo = new ArbolBMas("src/edu/ucuenca/p3/DAO_archivos/arbolTorneo");
        Object[] objetoTorneo = (Object[]) arbolRegTorneo.get(codigo);
        Torneo torneo = torneoSrv.obtenerTorneoArchivos(codigo);
        
        /*Variable para indicar el número de etapa*/
        int numero_etapa = torneo.getTorneo_categorías().getTorneo_etapas().size();
        
        Torneo_Categoria torneo_categoria = torneo.getTorneo_categorías();
        List<Torneo_Etapa> lista_torneo_etapa = torneo.getTorneo_categorías().getTorneo_etapas();
        
        /*La posición de la etapa en la lista*/
        Torneo_Etapa torneoEtapa = lista_torneo_etapa.get(numero_etapa - 1);
        
        /*Se extrae la lista de encuentro de la etapa*/
        List<Encuentro> lista_encuentros = torneoEtapa.getEncuentros();
        System.out.println("Cantidad de encuentros: " + lista_encuentros.size());

        /*Se verifica si existen partidos pendientes en la lista de encuentros*/
        boolean pendiente = false;
        for (Encuentro value : lista_encuentros) {
            if (value.getSet() == null) {
                pendiente = true;
            }
        }

        Tiempo_Set tiempo_set = null;
        
        if (pendiente) {
            Encuentro encuentro = lista_encuentros.get(numero_encuentro);
            Tiempo_Set set = encuentro.getSet();
            if (set == null) {
                /*Se instancia el tiempo_set y se setea su valor en el encuentro*/
                tiempo_set = jugarSet(puntaje1, puntaje2, nombre_jugador1, nombre_jugador2, numero_etapa);
                encuentro.setSet(tiempo_set);
                Participante participante_ganador = encuentro.getSet().getGanador();
                
                /*Se obtiene la etapa correspondiente */
                List<Participante> lista_ganadores = torneoEtapa.getGanadores();
                /*Se setea los valores en la lista de encuentros*/
                lista_ganadores.add(participante_ganador);
                lista_encuentros.remove(numero_encuentro);
                lista_encuentros.add(numero_encuentro, encuentro);

            } else {
                throw new EncuentroExcepcion("Resultado del Set ya ingresado !");
            }
        }
        torneoEtapa.setEncuentros(lista_encuentros);
        lista_torneo_etapa.remove(numero_etapa - 1);
        lista_torneo_etapa.add(numero_etapa - 1, torneoEtapa);
        torneo_categoria.setTorneo_etapas(lista_torneo_etapa);
        torneo.setTorneo_categorías(torneo_categoria);

        TorneoDAO.getInstancia().modificarTorneo(objetoTorneo, torneo);
    }

    public boolean cantidad_Encuentros(List<Participante> lista_participantes, int cant_encuentros) {
        return cant_encuentros == lista_participantes.size();
    }
    
    public List<Participante> clasificados(String codigo){
        TorneoSRV torneoSrv = new TorneoSRV();
        /*Se obtiene el torneo del archivo */
        Torneo torneo = torneoSrv.obtenerTorneoArchivos(codigo);
        int cantidad_etapas = torneo.getTorneo_categorías().getTorneo_etapas().size();
        /*Se obtiene la etapa actual del torneo*/
        Torneo_Etapa torneo_Etapa = torneo.getTorneo_categorías().getTorneo_etapas().get(cantidad_etapas - 1);
        /*Se obtiene la lista de clasificados de la etapa*/
        List<Participante> lista_clasificados = torneo_Etapa.getGanadores();
        return lista_clasificados;
    }
    
    public boolean encuentrosJugados(String codigo_torneo){
        TorneoSRV torneoSrv = new TorneoSRV();
        Torneo torneo = torneoSrv.obtenerTorneoArchivos(codigo_torneo);
        boolean estado = true;
        List<Torneo_Etapa> lista_etapa = torneo.getTorneo_categorías().getTorneo_etapas();
        int cantidad_etapas = lista_etapa.size();
        /*Obtengo la lista de encuentros de la etapa actual*/
        List<Encuentro> lista_encuentros = torneo.getTorneo_categorías().getTorneo_etapas().get(cantidad_etapas - 1).getEncuentros();
        for (Encuentro value : lista_encuentros) {
            if(value.getSet() == null){
                estado = false;
            }
        }
        return estado;
    }
    
    //Crear etapas 
    public Participante obtenerParticipante_nombre(String nombre) {
        Participante participante = ParticipanteDAO.getInstancia().obtenerParticipante_nombre(nombre);

        return participante;
    }

    //Método para jugar un set y devuelve una lista de sets
    public Tiempo_Set jugarSet(int puntaje1, int puntaje2, String nombre_jugador1, String nombre_jugador2, int etapa) {
        float rank;

        Participante participante_ganador = null;
        if (puntaje1 > puntaje2) {
//            participante_ganador = ParticipanteDAO.getInstancia().obtenerParticipante_nombre(nombre_jugador1);
            participante_ganador = ParticipanteDAO.getInstancia().obtenerParticipante_nombre_Archivos(nombre_jugador1);
        } else {
            participante_ganador = ParticipanteDAO.getInstancia().obtenerParticipante_nombre_Archivos(nombre_jugador2);
//            participante_ganador = ParticipanteDAO.getInstancia().obtenerParticipante_nombre(nombre_jugador2);
        }
        Tiempo_Set tiempo_set = new Tiempo_Set(participante_ganador, puntaje1, puntaje2);

        String cedula = participante_ganador.getCodigo();
        ArbolBMas arbolRegParticipante = new ArbolBMas("src/edu/ucuenca/p3/DAO_Archivos/arbolParticipante");
        Object[] regParticipante = (Object[]) arbolRegParticipante.get(cedula);

        //para dar puntaje
        Jugador jugador = (Jugador) participante_ganador;
        Ranking ranking = jugador.getRanking();

        rank = ranking.getPuntos();

        if (etapa == 1) {
            rank = rank + 8;
            Ranking ranking2 = new Ranking(rank);
            jugador.setRanking(ranking2);
        } else if (etapa == 2) {
            rank = rank + 15;
            Ranking ranking2 = new Ranking(rank);
            jugador.setRanking(ranking2);
        } else if (etapa == 3) {
            rank = rank + 30;
            Ranking ranking2 = new Ranking(rank);
            jugador.setRanking(ranking2);
        } else if (etapa == 4) {
            rank = rank + 20;
            Ranking ranking2 = new Ranking(rank);
            jugador.setRanking(ranking2);
        } else if (etapa == 5) {
            rank = rank + 45;
            Ranking ranking2 = new Ranking(rank);
            jugador.setRanking(ranking2);
        } else if (etapa == 6) {
            rank = rank + 60;
            Ranking ranking2 = new Ranking(rank);
            jugador.setRanking(ranking2);
        }
        /*Reescritura de datos de ranking de jugador*/
        ParticipanteDAO.getInstancia().modificarParticipanteArchivos(participante_ganador, regParticipante);
        return tiempo_set;
    }

    public void CobrarPenalidad(String nombre, String tipo) {
        float rank;

        Jugador jugador = (Jugador) null;
        Participante participante = null;
        Ranking ranking = new Ranking();

        participante = ParticipanteDAO.getInstancia().obtenerParticipante_nombre(nombre);

        jugador = (Jugador) participante;
        ranking = jugador.getRanking();

        rank = ranking.getPuntos();

        if (tipo.equalsIgnoreCase("abandono")) {
            rank = rank - 10;
            Ranking ranking2 = new Ranking(rank);
            jugador.setRanking(ranking2);
        } else if (tipo.equalsIgnoreCase("noPresenta")) {
            rank = rank - 6;
            Ranking ranking2 = new Ranking(rank);
            jugador.setRanking(ranking2);
        }
    }

    public boolean validarListaInscritos(List<Inscripcion> lista_Inscritos) {
        return lista_Inscritos == null || lista_Inscritos.isEmpty();
    }
//    public Participante obtenerCampeonTorneo(List<Participante> lista_participante) {
//        Participante participante = null;
//        if (lista_participante.size() == 1) {
//            participante = lista_participante.get(0);
//        }
//        return participante;
//    }
}
