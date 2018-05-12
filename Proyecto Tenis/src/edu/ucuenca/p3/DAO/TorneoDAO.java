/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.DAO;

import edu.ucuenca.p3.DAO.exceptions.TorneoExistenteExcepcion;
import edu.ucuenca.p3.DAO.exceptions.TorneoNoExistenteExcepcion;
import edu.ucuenca.p3.Modulos.Torneo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class TorneoDAO {

    private Map<String, Torneo> torneos;
    private static TorneoDAO instancia;

    private static String ruta = "src/edu/ucuenca/p3/DAO_Archivos/datosTorneos.dat";
    private static RandomAccessFile archivoTorneos;
    private static long tamanio;
    private static ArrayList<Torneo> listaTorneos;

    private TorneoDAO() {
        torneos = new LinkedHashMap<String, Torneo>();
        try {
            archivoTorneos = new RandomAccessFile(ruta, "rw");
            listaTorneos = new ArrayList<>();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TorneoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static TorneoDAO getInstancia() {
        if (instancia == null) {
            return instancia = new TorneoDAO();
        }
        return instancia;
    }

    public void ingresarTorneo(Torneo torneo) throws TorneoExistenteExcepcion {

        if (torneo == null) {
            throw new IllegalArgumentException("Datos del torneo no pueden ser nulo !");
        }

        if (torneos.get(torneo.getCodigo()) != null) {
            throw new TorneoExistenteExcepcion("El torneo ingresado ya existe en el registro !");
        }

        torneos.put(torneo.getCodigo(), torneo);
    }

    public void eliminarTorneo(Torneo torneo) throws TorneoNoExistenteExcepcion {
        Torneo torneo_alterno = torneos.get(torneo.getCodigo());

        if (torneo_alterno == null) {
            throw new TorneoNoExistenteExcepcion("Torneo no existente en el registro !");
        }

        torneos.remove(torneo);
    }

    public Torneo getTorneo(String codigo) {
        if (torneos.get(codigo) != null) {
            return torneos.get(codigo);
        }
        return null;
    }

    public Torneo getTorneoArchivos(String codigo){
        Torneo torneo = null;
        for (int i = 0; i < listaTorneos.size(); i++) {
            torneo = listaTorneos.get(i);
            if(torneo.getCodigo().equalsIgnoreCase(codigo)){
                return torneo;
            }
        }
        return null;
    }
    public void modificarTorneo(Object[] regTorneo, Torneo torneo) {
        try {
            byte[] arrayObjeto = Serializar.serialize(torneo);
            int longitudObjeto = arrayObjeto.length;
            //System.out.println("Longitud: "+longitudObjeto);
            byte[] buffer = new byte[9000];    //Máxima cantidad de bytes en el objeto Participante   

            byte[] byteEntero = Serializar.convertirInt_Byte(longitudObjeto);
            buffer[0] = byteEntero[0];
            buffer[1] = byteEntero[1];
            buffer[2] = byteEntero[2];
            buffer[3] = byteEntero[3];

            System.arraycopy(arrayObjeto, 0, buffer, 4, longitudObjeto);
            //Se salta a la posición indicada en el archivo y luego se escribe el array buffer
            long posicion = (long) regTorneo[0];
            archivoTorneos.seek(posicion);
            archivoTorneos.write(buffer);
            
            System.out.println("Torneo modificado exitosamente !");
        } catch (IOException ex) {
            Logger.getLogger(TorneoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Torneo getTorneoArchivos(Object[] regTorneo) {
        try {
//            byte[] bif = new byte[(int) regUsuario[1]];
            byte[] bif = new byte[9000];
            archivoTorneos.seek((long) regTorneo[0]);
            archivoTorneos.readFully(bif);
//            int longitud = (int) bif[0];
            byte[] byteNumero = new byte[4];
            byteNumero[0] = bif[0];
            byteNumero[1] = bif[1];
            byteNumero[2] = bif[2];
            byteNumero[3] = bif[3];
            int longitud = Serializar.convertirByte_Int(byteNumero);
            byte[] arrayObjetos = new byte[longitud];
            System.arraycopy(bif, 4, arrayObjetos, 0, longitud);
            return (Torneo) Serializar.deserialize(arrayObjetos);
        } catch (IOException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Torneo> listarTorneos() {
        List<Torneo> lista_torneo = new ArrayList<>();
        for (Map.Entry<String, Torneo> entrySet : torneos.entrySet()) {
            Torneo value = entrySet.getValue();
            lista_torneo.add(value);
        }
        return lista_torneo;
    }

    public Object[] ingresarTorneoArchivos(Torneo torneo) {
        try {
            //Array de bytes para poder guardarla en el archivo de objetos
            byte[] arrayObjeto = Serializar.serialize(torneo);
            int longitudObjeto = arrayObjeto.length;
            //System.out.println("Longitud: "+longitudObjeto);
            byte[] buffer = new byte[9000];    //Máxima cantidad de bytes en el objeto Participante   

            byte[] byteEntero = Serializar.convertirInt_Byte(longitudObjeto);
            buffer[0] = byteEntero[0];
            buffer[1] = byteEntero[1];
            buffer[2] = byteEntero[2];
            buffer[3] = byteEntero[3];

            System.arraycopy(arrayObjeto, 0, buffer, 4, longitudObjeto);
            //Se salta a la posición indicada en el archivo y luego se escribe el array buffer
            archivoTorneos.seek(tamanio);
            archivoTorneos.write(buffer);

            Object[] registro_Torneo = new Object[3];
            registro_Torneo[0] = tamanio;
            registro_Torneo[1] = buffer.length;
            registro_Torneo[2] = torneo.getCodigo();

            tamanio += buffer.length;
            listaTorneos.add(torneo);
            System.out.println("Participante ingresado correctamente !");
            return registro_Torneo;
        } catch (IOException ex) {
            Logger.getLogger(TorneoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void cargarDatos() {
        try {
            listaTorneos = new ArrayList<>();
            archivoTorneos = new RandomAccessFile(ruta, "rw");
            tamanio = archivoTorneos.length();

            try {
                while (true) {
                    byte[] buffer = new byte[9000];
                    archivoTorneos.readFully(buffer);
                    byte[] byteLongitud = new byte[4];
                    byteLongitud[0] = buffer[0];
                    byteLongitud[1] = buffer[1];
                    byteLongitud[2] = buffer[2];
                    byteLongitud[3] = buffer[3];

                    int longitud = Serializar.convertirByte_Int(byteLongitud);
                    System.out.println("Longitud de objeto: " + longitud);
                    byte[] arrayObjeto = new byte[longitud];
                    System.arraycopy(buffer, 4, arrayObjeto, 0, longitud);
                    Torneo torneo = (Torneo) Serializar.deserialize(arrayObjeto);
                    listaTorneos.add(torneo);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("DAO torneos no encontrados");

            } catch (IOException e) {
                System.out.println("DAO torneos Fin de Archivo");
            }

        } catch (IOException ex) {
            System.out.println("Fin del archivo Torneos");
        }
    }

    public ArrayList<Torneo> getListaTorneosArchivos() {
        cargarDatos();
        return listaTorneos;
    }

    public boolean existeTorneo(String codigo) {
        for(int i=0; i<listaTorneos.size(); i++){
            String cod = listaTorneos.get(i).getCodigo();
            //System.out.println("---CODIGO " + ced);
            if(cod.equals(codigo)){
                return true;
            }
        }
        return false;
    }
}
