/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.DAO;

import edu.ucuenca.p3.DAO.exceptions.ClubNoExisteExcepcion;
import edu.ucuenca.p3.DAO.exceptions.ClubExistenteExcepcion;
import edu.ucuenca.p3.DAO.exceptions.ClubNoExistenteExcepcion;
import edu.ucuenca.p3.Modulos.Club;
import edu.ucuenca.p3.Modulos.MezclaDirecta;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rmi.CORBA.Util;

/**
 *
 * @author andre
 */
public class ClubDAO {

    private Map<String, Club> clubes;
    private static ClubDAO instancia;
    //implementado brian
    private static long tamanio;
    private static RandomAccessFile archivoClubes;
    private static String ruta = "src/edu/ucuenca/p3/DAO_Archivos/datosClubes.dat";
    private static ArrayList<Club> listaClubes;

    private ClubDAO() {
//        try {
        clubes = new HashMap<String, Club>();
//            archivoClubes = new RandomAccessFile(ruta, "rw");
//            listaClubes = new ArrayList<>();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ClubDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public static ClubDAO getInstancia() {
        if (instancia == null) {
            return instancia = new ClubDAO();
        }
        return instancia;
    }

    public void ingresarClub(Club club) throws ClubExistenteExcepcion {

        if (club == null) {
            throw new IllegalArgumentException("Los valores del club no pueden ser nulos !");
        }

        if (clubes.get(club.getCodigo()) != null) {
            throw new ClubExistenteExcepcion("El club con el nombre: " + club.getNombre() + " ya existe !");
        }

        clubes.put(club.getCodigo(), club);
    }

    public void modificarClub(Club club) throws ClubExistenteExcepcion, ClubNoExisteExcepcion {

        if (!clubes.containsKey(club.getCodigo())) {
            throw new ClubNoExisteExcepcion("El club no existe");
        }

        clubes.put(club.getCodigo(), club);
    }

    public void eliminarClub(Club club) throws ClubExistenteExcepcion, ClubNoExisteExcepcion {

        if (!clubes.containsKey(club.getCodigo())) {
            throw new ClubNoExisteExcepcion("El club no existe");
        }

        clubes.remove(club.getCodigo());
    }

    public void alterClub(Club club, boolean update) throws ClubNoExistenteExcepcion {
        Club club_alterno = clubes.get(club.getCodigo());

        if (club_alterno == null) {
            throw new ClubNoExistenteExcepcion("El club deseado no existe en el registro!");
        }
        if (update) {
            clubes.put(club.getCodigo(), club);
        } else {
            clubes.remove(club.getCodigo());
        }
    }

    public void actualizarClub(Club club) throws ClubNoExistenteExcepcion {
        alterClub(club, true);
    }

    public void removerClub(Club club) throws ClubNoExistenteExcepcion {
        alterClub(club, false);
    }

    public Club obtenerClub(String codigo) {
        if (clubes.get(codigo) != null) {
            return clubes.get(codigo);
        }
        return null;
    }

    public Club obtenerClubRegistro(String codigo) {
        Club club = null;
        for (int i = 0; i < listaClubes.size(); i++) {
            Club club2 = listaClubes.get(i);
            if (club2.getCodigo().equalsIgnoreCase(codigo)) {
                club = club2;
                break;
            }
        }
        System.out.println("))" + club.getCodigo());
        return club;
    }

    public Club obtenerClub_Nombre(String nombre) {
        Club club = null;
        for (Map.Entry<String, Club> entrySet : clubes.entrySet()) {
            Club value = entrySet.getValue();
            System.out.println(value.getNombre());
            if (value.getNombre().equalsIgnoreCase(nombre)) {
                club = clubes.get(value.getCodigo());
            }
        }
        return club;
    }

    public List<Club> listarClubes() {
        List<Club> lista_clubes = new ArrayList<>();
        for (Map.Entry<String, Club> entrySet : clubes.entrySet()) {
            Club value = entrySet.getValue();
            lista_clubes.add(value);
        }
        return lista_clubes;
    }

    public Object[] ingresarClubArchivos(Club club) {
        try {
            //Array de bytes para poder guardarla en el archivo de objetos
            byte[] arrayObjeto = Serializar.serialize(club);
            int longitudObjeto = arrayObjeto.length;
            //System.out.println("Longitud original: " + longitudObjeto);
            byte[] buffer = new byte[500];    //Máxima cantidad de bytes en el objeto Participante            

            byte[] byteEntero = Serializar.convertirInt_Byte(longitudObjeto);
            buffer[0] = byteEntero[0];
            buffer[1] = byteEntero[1];
            buffer[2] = byteEntero[2];
            buffer[3] = byteEntero[3];

            System.arraycopy(arrayObjeto, 0, buffer, 4, longitudObjeto);
            //Se salta a la posición indicada en el archivo y luego se escribe el array buffer
            archivoClubes.seek(tamanio);
            archivoClubes.write(buffer);
            //int longitud = (buffer[0] & 255);
            //int longitud = (int) buffer[0] & 0xff;
            //System.out.println("Longitud en decimal: " + longitud);

            Object[] registro_Club = new Object[3];
            registro_Club[0] = tamanio;
            registro_Club[1] = buffer.length;
            registro_Club[2] = club.getCodigo();

            tamanio += buffer.length;
            listaClubes.add(club);
            System.out.println("Club ingresado correctamente !");
            return registro_Club;
        } catch (IOException ex) {
            Logger.getLogger(ClubDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Club> getListaClubArchivos() {
        cargarDatos();
        return listaClubes;
    }

    public void cargarDatos() {
        try {

            archivoClubes = new RandomAccessFile(ruta, "rw");
            listaClubes = new ArrayList<>();
            tamanio = archivoClubes.length();
            try {

                while (true) {
                    byte[] buffer = new byte[500];
                    archivoClubes.readFully(buffer);
                    byte[] byteLongitud = new byte[4];
                    byteLongitud[0] = buffer[0];
                    byteLongitud[1] = buffer[1];
                    byteLongitud[2] = buffer[2];
                    byteLongitud[3] = buffer[3];

                    int longitud = Serializar.convertirByte_Int(byteLongitud);
                    byte[] arrayObjeto = new byte[longitud];
                    System.arraycopy(buffer, 4, arrayObjeto, 0, longitud);
                    Club club = (Club) Serializar.deserialize(arrayObjeto);
                    listaClubes.add(club);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("DAO Clubes no encontrados");

            } catch (IOException e) {
                System.out.println("DAO clubes Fin de Archivo");
            }
        } catch (IOException ex) {
            System.out.println("Fin del archivo Club");
        }
    }

    public void modificarClubArchivos(Club club, Object[] regClub) {

        try {
            byte[] arrayObjeto = Serializar.serialize(club);
            byte[] buffer = new byte[500];
            int longitud = arrayObjeto.length;

            byte[] byteEntero = Serializar.convertirInt_Byte(longitud);
            buffer[0] = byteEntero[0];
            buffer[1] = byteEntero[1];
            buffer[2] = byteEntero[2];
            buffer[3] = byteEntero[3];

            System.arraycopy(arrayObjeto, 0, buffer, 4, longitud);

            archivoClubes.seek((long) regClub[0]);
            archivoClubes.write(buffer);

            for (int i = 0; i < listaClubes.size(); i++) {
                Club get = listaClubes.get(i);
                if (get.getCodigo().equalsIgnoreCase(club.getCodigo())) {
                    listaClubes.remove(get);
                    listaClubes.add(i, club);
                    break;
                }
            }
        } catch (IOException ex) {

        }

    }

    public void eliminarClubArchivos(Club club, Object[] regClub, int posicion) {
        try {
            club.setEstado(false);
            byte[] arrayObjeto = Serializar.serialize(club);
            byte[] buffer = new byte[500];
            int longitud = arrayObjeto.length;

            byte[] byteEntero = Serializar.convertirInt_Byte(longitud);
            buffer[0] = byteEntero[0];
            buffer[1] = byteEntero[1];
            buffer[2] = byteEntero[2];
            buffer[3] = byteEntero[3];

            System.arraycopy(arrayObjeto, 0, buffer, 4, longitud);

            archivoClubes.seek((long) regClub[0]);
            archivoClubes.write(buffer);

            listaClubes.remove(club);

        } catch (IOException ex) {
            Logger.getLogger(ClubDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Club obtenerClubNombre_Archivos(String nombre) {
        Club club = null;
        for (Club get : listaClubes) {
            if (get.getNombre().equalsIgnoreCase(nombre)) {
                club = get;
                break;
            }
        }
        return club;
    }

    public Club getClubArchivos(Object[] regClub) {

        try {
            byte[] bif = new byte[500];
            archivoClubes.seek((long) regClub[0]);
            archivoClubes.readFully(bif);

            byte[] byteNumero = new byte[4];
            byteNumero[0] = bif[0];
            byteNumero[1] = bif[1];
            byteNumero[2] = bif[2];
            byteNumero[3] = bif[3];

            int longitud = Serializar.convertirByte_Int(byteNumero);
            byte[] arrayObjetos = new byte[longitud];
            System.arraycopy(bif, 4, arrayObjetos, 0, longitud);
            return (Club) Serializar.deserialize(arrayObjetos);

        } catch (IOException ex) {
            Logger.getLogger(ClubDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClubDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Club> getListaClubesOrdenadaArchivos(String ordenarPor, String enOrden) {
        cargarDatos();
        ArrayList<Club> listaParticipant = listaClubes;

        ArrayList<Club> listaParticipantes2 = new ArrayList<>();
        for (int i = 0; i < listaParticipant.size(); i++) {
            Club participante = listaParticipant.get(i);
            if (participante.isEstado()) {
                listaParticipantes2.add(participante);
            }
        }

        String ruta = ("src/edu/ucuenca/p3/DAO_archivos");
        File archivo = new File(ruta, "participantesXclave.dat");
        try {
            DataOutputStream archivoEscribir = new DataOutputStream(new FileOutputStream(archivo));
            for (int i = 0; i < listaParticipantes2.size(); i++) {
                Club club = (Club) listaParticipantes2.get(i);
                int cod;
                if (ordenarPor.equalsIgnoreCase("Código")) {
                    cod = Integer.parseInt(club.getCodigo());
                    archivoEscribir.writeInt(cod);
                } else if (ordenarPor.equalsIgnoreCase("Nombre")) {
                    String apellido = club.getNombre();
                    cod = apellido.charAt(0);
                    archivoEscribir.writeInt(cod);
                } else if (ordenarPor.equalsIgnoreCase("Propietario")) {
                    String apellido = club.getPropietario();
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
        ArrayList<Club> listaParticipanteOrdenado = new ArrayList<>();

        try {
            int clave, cod;

            DataInputStream archivoXclave = new DataInputStream(new FileInputStream(new File(ruta, "participantesXclave.dat")));
            System.out.println("\n");
            while (archivoXclave.available() != 0) {
                clave = archivoXclave.readInt();

                for (int i = 0; i < listaParticipantes2.size(); i++) {
                    Club club = (Club) listaParticipantes2.get(i);

                    if (ordenarPor.equalsIgnoreCase("Código")) {
                        cod = Integer.parseInt(club.getCodigo());
                        if (cod == clave) {
                            listaParticipanteOrdenado.add(club);
                        }
                    } else if (ordenarPor.equalsIgnoreCase("Nombre")) {
                        String apellido = club.getNombre();
                        cod = apellido.charAt(0);
                        if (cod == clave) {
                            listaParticipanteOrdenado.add(club);
                        }
                    } else if (ordenarPor.equalsIgnoreCase("Propietario")) {
                        String apellido = club.getPropietario();
                        cod = apellido.charAt(0);
                        if (cod == clave) {
                            listaParticipanteOrdenado.add(club);
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParticipanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < listaParticipanteOrdenado.size(); i++) {
            Club j = (Club) listaParticipanteOrdenado.get(i);
            System.out.println("->" + j.getCodigo() + "  " + j.getNombre() + "  " + j.getPropietario());
        }

        ArrayList<Club> listaSinDuplicarOrdenada = new ArrayList<>();

        for (int i = 0; i < listaParticipanteOrdenado.size(); i++) {
            Club cl1 = (Club) listaParticipanteOrdenado.get(i);

            if (comparaLista(cl1.getCodigo(), listaSinDuplicarOrdenada) == false) {
                listaSinDuplicarOrdenada.add(listaParticipanteOrdenado.get(i));
            }
        }

        System.out.println("-------------------------------------------------");
        for (int i = 0; i < listaParticipanteOrdenado.size(); i++) {
            Club j = (Club) listaParticipanteOrdenado.get(i);
            System.out.println("->" + j.getCodigo() + "  " + j.getNombre() + "  " + j.getPropietario());
        }
        return listaSinDuplicarOrdenada;
    }

    private boolean comparaLista(String codigo, ArrayList<Club> listaSinDuplicarOrdenada) {
        int h = 0;
        for (int i = 0; i < listaSinDuplicarOrdenada.size(); i++) {
            Club j = (Club) listaSinDuplicarOrdenada.get(i);
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

    public ArrayList<Club> listaClubes() {
        return listaClubes;
    }

    public boolean existeClub(String codigo) {
        for (int i = 0; i < listaClubes.size(); i++) {
            String ced = listaClubes.get(i).getCodigo();
            //System.out.println("---CODIGO " + ced);
            if (ced.equals(codigo)) {
                return true;
            }
        }
        return false;
    }
}
