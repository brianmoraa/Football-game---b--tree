/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.DAO;

import edu.ucuenca.p3.DAO.exceptions.ParticipanteExistenteException;
import edu.ucuenca.p3.DAO.exceptions.ParticipanteNoExistenteException;
import edu.ucuenca.p3.Modulos.Club;
import edu.ucuenca.p3.Modulos.Encuentro;
import edu.ucuenca.p3.Modulos.Equipo;
import edu.ucuenca.p3.Modulos.Jugador;
import edu.ucuenca.p3.Modulos.MezclaDirecta;
import edu.ucuenca.p3.Modulos.Participante;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class ParticipanteDAO {

    private Map<String, Participante> participantes;
    private static ParticipanteDAO instancia;
//
//    private static String ruta2 = "src/edu/ucuenca/p3/DAO_Archivos/";

    private static String ruta = "src/edu/ucuenca/p3/DAO_Archivos/datosJugador.dat";
    private static RandomAccessFile archivoParticipantes;
    private static ArrayList<Participante> listaParticipantes;
    private static long tamanio;

    public static ParticipanteDAO getInstancia() {
        if (instancia == null) {
            instancia = new ParticipanteDAO();
        }
        return instancia;
    }

    private ParticipanteDAO() {
//        try {
        participantes = new TreeMap<>();
//            archivoParticipantes = new RandomAccessFile(ruta, "rw");
        //La siguiente línea hay que eliminarla luego
        //Recuérdalo motherfucker !!! 
//            listaParticipantes = new ArrayList<>();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public Participante getParticipante(String cedula) {
        if (participantes.get(cedula) != null) {
            return participantes.get(cedula);
        }
        return null;
    }

    public void modificarParticipanteArchivos(Participante participante, Object[] regUsuario) {

        try {
            byte[] arrayObjeto = Serializar.serialize(participante);
            byte[] buffer = new byte[1000];
            int longitud = arrayObjeto.length;

            byte[] byteEntero = Serializar.convertirInt_Byte(longitud);
            buffer[0] = byteEntero[0];
            buffer[1] = byteEntero[1];
            buffer[2] = byteEntero[2];
            buffer[3] = byteEntero[3];

            System.arraycopy(arrayObjeto, 0, buffer, 4, longitud);
            System.out.println("ubicación: "+regUsuario[0]);
            archivoParticipantes.seek((long) regUsuario[0]);
            archivoParticipantes.write(buffer);

            for (int i = 0; i < listaParticipantes.size(); i++) {
                Participante get = listaParticipantes.get(i);
                if (get.getCodigo().equalsIgnoreCase(participante.getCodigo())) {
                    listaParticipantes.remove(get);
                    listaParticipantes.add(i, participante);
                    break;

                }
            }

        } catch (IOException ex) {
            
        }

    }

    public void eliminarParticipanteArchivos(Participante participante, Object[] regUsuario, int posicion) {
        try {
            participante.setEstado(false);
            byte[] arrayObjeto = Serializar.serialize(participante);
            byte[] buffer = new byte[1000];
            int longitud = arrayObjeto.length;

            byte[] byteEntero = Serializar.convertirInt_Byte(longitud);
            buffer[0] = byteEntero[0];
            buffer[1] = byteEntero[1];
            buffer[2] = byteEntero[2];
            buffer[3] = byteEntero[3];

            System.arraycopy(arrayObjeto, 0, buffer, 4, longitud);

            archivoParticipantes.seek((long) regUsuario[0]);
            archivoParticipantes.write(buffer);

            //Eliminamos de la lista el registro indicado
            listaParticipantes.remove(participante);

        } catch (IOException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Participante getParticipanteArchivos(Object[] regUsuario) {
        try {
//            byte[] bif = new byte[(int) regUsuario[1]];
            byte[] bif = new byte[1000];
            archivoParticipantes.seek((long) regUsuario[0]);
            archivoParticipantes.readFully(bif);
//            int longitud = (int) bif[0];
            byte[] byteNumero = new byte[4];
            byteNumero[0] = bif[0];
            byteNumero[1] = bif[1];
            byteNumero[2] = bif[2];
            byteNumero[3] = bif[3];
            int longitud = Serializar.convertirByte_Int(byteNumero);
            System.out.println("Longitud : " + longitud);
            byte[] arrayObjetos = new byte[longitud];
            System.arraycopy(bif, 4, arrayObjetos, 0, longitud);
            return (Participante) Serializar.deserialize(arrayObjetos);
        } catch (IOException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Participante obtenerParticipante(String cedula) {
        Participante participante = null;
        for (int i = 0; i < listaParticipantes.size(); i++) {
            Participante get = listaParticipantes.get(i);
            if (get.getCodigo().equalsIgnoreCase(cedula)) {
                participante = get;
                break;
            }
        }
        return participante;
    }

    public void cargarDatos() {
        try {
            listaParticipantes = new ArrayList<>();
            archivoParticipantes = new RandomAccessFile(ruta, "rw");
            tamanio = archivoParticipantes.length();

            try {
                while (true) {
                    byte[] buffer = new byte[1000];
                    archivoParticipantes.readFully(buffer);
                    byte[] byteLongitud = new byte[4];
                    byteLongitud[0] = buffer[0];
                    byteLongitud[1] = buffer[1];
                    byteLongitud[2] = buffer[2];
                    byteLongitud[3] = buffer[3];
//                    System.out.println("Longitud de objeto Jugador");
//                    for (int i = 0; i < byteLongitud.length; i++) {
//                        System.out.print(byteLongitud[i]);
//
//                    }
                    int longitud = Serializar.convertirByte_Int(byteLongitud);
//                    System.out.println("Longitud de objeto: " + longitud);
                    byte[] arrayObjeto = new byte[longitud];
                    System.arraycopy(buffer, 4, arrayObjeto, 0, longitud);
                    Participante participante = (Participante) Serializar.deserialize(arrayObjeto);
                    Jugador jugador = (Jugador) participante;
                    listaParticipantes.add(jugador);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("DAO Participantes no encontrados");

            } catch (IOException e) {
                System.out.println("DAO Participantes Fin de Archivo");
            }

        } catch (IOException ex) {
            System.out.println("Fin del archivo Participantes");
        }
    }

    public Object[] agregarParticipanteArchivos(Participante participante) {
        try {
            //Array de bytes para poder guardarla en el archivo de objetos
            byte[] arrayObjeto = Serializar.serialize(participante);
            int longitudObjeto = arrayObjeto.length;
            byte[] buffer = new byte[1000];    //Máxima cantidad de bytes en el objeto Participante   

            byte[] byteEntero = Serializar.convertirInt_Byte(longitudObjeto);
            buffer[0] = byteEntero[0];
            buffer[1] = byteEntero[1];
            buffer[2] = byteEntero[2];
            buffer[3] = byteEntero[3];

            System.arraycopy(arrayObjeto, 0, buffer, 4, longitudObjeto);
            //Se salta a la posición indicada en el archivo y luego se escribe el array buffer
            archivoParticipantes.seek(tamanio);
            archivoParticipantes.write(buffer);

            Object[] registro_Participante = new Object[3];
            registro_Participante[0] = tamanio;
            registro_Participante[1] = buffer.length;
            registro_Participante[2] = participante.getCodigo();

            tamanio += buffer.length;
            listaParticipantes.add(participante);
            System.out.println("Participante ingresado correctamente !");
            return registro_Participante;
        } catch (IOException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Participante> getListaUsuariosArchivos() {
        cargarDatos();
        return listaParticipantes;
    }

    public void registrarParticipante(Participante p) throws ParticipanteExistenteException {
        if (participantes.containsKey(p.getCodigo())) {
            throw new ParticipanteExistenteException("El participante ya esta registrado.");
        }
        participantes.put(p.getCodigo(), p);
    }

    public void modificarParticipante(Participante participante) throws ParticipanteNoExistenteException {
        if (!participantes.containsKey(participante.getCodigo())) {
            throw new ParticipanteNoExistenteException("El participante no existe.");
        }

        participantes.put(participante.getCodigo(), participante);
    }

    public void eliminarParticipante(Participante p) throws ParticipanteNoExistenteException {
        if (!participantes.containsKey(p.getCodigo())) {
            throw new ParticipanteNoExistenteException("El participante no existe.");
        }
        participantes.remove(p.getCodigo());
    }

    public List<Participante> listar() {
        List<Participante> listaParticipantes = new ArrayList();
        for (Map.Entry<String, Participante> entrySet : participantes.entrySet()) {
            Participante value = entrySet.getValue();
            listaParticipantes.add(value);

        }
        return listaParticipantes;
    }

    public Participante obtenerParticipante_nombre(String nombre) {
        Participante participante = null;
        Jugador jugador = null;
        for (Map.Entry<String, Participante> entrySet : participantes.entrySet()) {
            String key = entrySet.getKey();
            Participante value = entrySet.getValue();
            if (value.getTipo().trim().equalsIgnoreCase("EQUIPO")) {
                if (nombre.equalsIgnoreCase(value.getCodigo())) { //En caso de que sea equipo
                    participante = value;                       //se iguala el valor a la variable participante
                }
            } else {
                jugador = (Jugador) value;
                String nombre_completo = jugador.getNombre() + " " + jugador.getApellido();
                if (nombre.equalsIgnoreCase(nombre_completo)) {
                    participante = value;
                }
            }
        }
        return participante;
    }
    
    public Participante obtenerParticipante_nombre_Archivos(String nombre){
        /*Se carga los datos del registro*/
        cargarDatos();
        Participante participante = null;
        Jugador jugador = null;
        for (int i = 0; i < listaParticipantes.size(); i++) {
            Participante value = listaParticipantes.get(i);
            jugador = (Jugador) value;
            String nombre_completo = jugador.getNombre()+" "+jugador.getApellido();
            if(nombre.equalsIgnoreCase(nombre_completo)){
                participante = value;
            }
        }
        return participante;
    }
    
    public Participante obtenerParticipante_Codigo(String codigo) throws ParticipanteNoExistenteException {
        if (participantes.get(codigo) == null) {
            throw new ParticipanteNoExistenteException("El participante no existe.");
        }

        return participantes.get(codigo);
    }

    public ArrayList<Participante> getListaParticipantesOrdenadaArchivos(String ordenarPor, String enOrden) {
        cargarDatos();
        ArrayList<Participante> listaParticipant = listaParticipantes;

        ArrayList<Participante> listaParticipantes2 = new ArrayList<>();
        for (int i = 0; i < listaParticipant.size(); i++) {
            Participante participante = listaParticipant.get(i);
            if (participante.isEstado()) {
                listaParticipantes2.add(participante);
            }
        }

        String ruta = ("src/edu/ucuenca/p3/DAO_archivos");
        File archivo = new File(ruta, "participantesXclave.dat");
        try {
            DataOutputStream archivoEscribir = new DataOutputStream(new FileOutputStream(archivo));
            for (int i = 0; i < listaParticipantes2.size(); i++) {
                Jugador jugador = (Jugador) listaParticipantes2.get(i);
                int cod;
                if (ordenarPor.equalsIgnoreCase("Código")) {
                    cod = Integer.parseInt(jugador.getCodigo());
                    archivoEscribir.writeInt(cod);
                } else if (ordenarPor.equalsIgnoreCase("Ranking")) {
                    cod = (int) jugador.getRanking().getPuntos();
                    archivoEscribir.writeInt(cod);
                } else if (ordenarPor.equalsIgnoreCase("Apellido")) {
                    String apellido = jugador.getApellido();
                    cod = apellido.charAt(0);
                    archivoEscribir.writeInt(cod);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        int lenSubListas = 1, numero_iteracion = 1;

        while (lenSubListas < listaParticipantes2.size()) {
            MezclaDirecta.repartir(lenSubListas);
//            MezclaDirecta.mostrarValores(lenSubListas);

            System.out.println("\n\nMezcla " + numero_iteracion);
            MezclaDirecta.ordenar(lenSubListas);
            MezclaDirecta.mostar(lenSubListas);

            lenSubListas *= 2;
            numero_iteracion++;

        }
        ArrayList<Participante> listaParticipanteOrdenado = new ArrayList<>();

        try {
            int clave, cod;

            DataInputStream archivoXclave = new DataInputStream(new FileInputStream(new File(ruta, "participantesXclave.dat")));
            System.out.println("\n");
            while (archivoXclave.available() != 0) {
                clave = archivoXclave.readInt();

                for (int i = 0; i < listaParticipantes2.size(); i++) {
                    Jugador jugador = (Jugador) listaParticipantes2.get(i);

                    if (ordenarPor.equalsIgnoreCase("Código")) {
                        cod = Integer.parseInt(jugador.getCodigo());
                        if (cod == clave) {
                            listaParticipanteOrdenado.add(jugador);
                        }
                    } else if (ordenarPor.equalsIgnoreCase("Ranking")) {
                        cod = (int) jugador.getRanking().getPuntos();
                        if (cod == clave) {
                            listaParticipanteOrdenado.add(jugador);
                        }
                    } else if (ordenarPor.equalsIgnoreCase("Apellido")) {
                        String apellido = jugador.getApellido();
                        cod = apellido.charAt(0);
                        if (cod == clave) {
                            listaParticipanteOrdenado.add(jugador);
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
//        for(int i=0; i<listaParticipanteOrdenado.size(); i++){
//            Jugador j = (Jugador) listaParticipanteOrdenado.get(i);
//            System.out.println("->" + j.getCodigo() + "  " + j.getApellido() + "  " + j.getRanking().getPuntos() );
//        }

        ArrayList<Participante> listaSinDuplicarOrdenada = new ArrayList<>();

        for (int i = 0; i < listaParticipanteOrdenado.size(); i++) {
            Jugador j1 = (Jugador) listaParticipanteOrdenado.get(i);

            if (comparaLista(j1.getCodigo(), listaSinDuplicarOrdenada) == false) {
                listaSinDuplicarOrdenada.add(listaParticipanteOrdenado.get(i));
            }
        }

//        System.out.println("-------------------------------------------------");
//        for(int i=0; i<listaSinDuplicarOrdenada.size(); i++){
//            Jugador j = (Jugador) listaSinDuplicarOrdenada.get(i);
//            System.out.println("->" + j.getCodigo() + "  " + j.getApellido() + "  " + j.getRanking().getPuntos() );
//        }
        return listaSinDuplicarOrdenada;
    }

    private boolean comparaLista(String codigo, ArrayList<Participante> listaSinDuplicarOrdenada) {
        int h = 0;
        for (int i = 0; i < listaSinDuplicarOrdenada.size(); i++) {
            Jugador j = (Jugador) listaSinDuplicarOrdenada.get(i);
            if (codigo.equalsIgnoreCase(j.getCodigo())) {
                h = 1;
            }
        }
        if (h == 0) {
            return false;
        } else {
            return true;
        }
    }
//    public int cargarDatosParticipante() {//
//        listaParticipantes = new ArrayList<>();
//        try {
//
//            archivoParticipantes = new RandomAccessFile(participantesRegArrayBytes, "rw");
//            tamanio = participantesRegArrayBytes.length();
//
//            listaRegistroParticipantes = new ArrayList<Object[]>();
//
//            FileInputStream flujoEntrada = new FileInputStream(archivo);
//            ObjectInputStream dataIs = new ObjectInputStream(flujoEntrada);
//            System.out.println("DAO Leyendo Archivos Usuarios");
//
//            try {
//                while (true) {
//                    Participante p = null;
//                    Jugador usuarioLeido = (Jugador) dataIs.readObject();
//                    usuarioLeido = (Jugador) p;
//                    listaParticipantes.add(usuarioLeido);
//                }
//            } catch (ClassNotFoundException e) {
//                System.out.println("DAO Usuarios no encontrados");
//            } catch (IOException e) {
//                System.out.println("DAO Usuarios Fin de Archivo");
//            }
//
//            dataIs.close();
//            return listaParticipantes.size();
//        } catch (FileNotFoundException e) {
//            crearArchivosParticipante();
//        } catch (IOException e) {
//            System.out.println("Fin del Achivo Persona");
//        }
//        return 0;
//    }
//
//    public void crearArchivosParticipante() {//
//        try {
//
//            FileOutputStream flujoSalida = new FileOutputStream(archivo);
//            ObjectOutputStream escribirUsuarios = new ObjectOutputStream(flujoSalida);
//            escribirUsuarios.close();
//
//            System.out.println("Archivo Participante creado");
//        } catch (FileNotFoundException ex) {
////            Logger.getLogger(DAOPredios.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
////            Logger.getLogger(DAOPredios.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        cargarDatosParticipante();
//    }

    public boolean existeJugador(String cedula) {
        for (int i = 0; i < listaParticipantes.size(); i++) {
            Participante participante = listaParticipantes.get(i);
            Jugador jugador = (Jugador) participante;
            String codigo = participante.getCodigo();
            //System.out.println("---CODIGO " + ced);
            if (codigo.equalsIgnoreCase(cedula) && jugador.isEstado()) {
                return true;
            }
        }
        return false;
    }
}
