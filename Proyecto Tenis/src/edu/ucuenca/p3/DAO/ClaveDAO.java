/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.DAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.RandomAccess;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author andre
 */
public class ClaveDAO {

    private static ClaveDAO instancia;
    private static String ruta1 = "src/edu/ucuenca/p3/Claves/clavesPublicasAdmin.dat";
    private static String ruta2 = "src/edu/ucuenca/p3/Claves/clavesPublicasArbitro.dat";

    private static File archivoClavesPublica;
    private static File archivoClavesPublicasArbitros;

    private static long tamanio;

    private static ArrayList<PublicKey> listaClavesPublicas;

    private ClaveDAO() {

    }

    public static ClaveDAO getInstancia() {
        if (instancia == null) {
            instancia = new ClaveDAO();
        }
        return instancia;
    }

    public void agregarClavePublica(Key key, String tipo) {

        try {
            archivoClavesPublica = new File(ruta1, "rw");
            archivoClavesPublicasArbitros = new File(ruta2, "rw");

            byte[] publicKeyBytes = key.getEncoded();
//            int longitudClave = publicKeyBytes.length;
//            byte[] buffer = new byte[200];
//
//            byte[] byteEntero = Serializar.convertirInt_Byte(longitudClave);
//            buffer[0] = byteEntero[0];
//            buffer[1] = byteEntero[1];
//            buffer[2] = byteEntero[2];
//            buffer[3] = byteEntero[3];
//
//            System.arraycopy(publicKeyBytes, 0, buffer, 4, longitudClave);
            if (tipo.equalsIgnoreCase("Administrador")) {
                System.out.println("clave publica administrador ");
                FileOutputStream fos = new FileOutputStream(ruta1);
                fos.write(publicKeyBytes);
                fos.close();

            } else if (tipo.equalsIgnoreCase("Árbitro")) {
                System.out.println("clave publica arbitro");
                FileOutputStream fos = new FileOutputStream(ruta2);
                fos.write(publicKeyBytes);
                fos.close();
            }

//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            KeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
//
//            PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
//
//            listaClavesPublicas.add(keyFromBytes);
        } catch (IOException ex) {
            System.out.println("Fin de archivo");
        }

    }

    public void guardarClavePrivada(Key key) {
        try {
            byte[] bytesClavePrivada = key.getEncoded();
            JFileChooser file = new JFileChooser();
            file.showSaveDialog(null);
            File guarda = file.getSelectedFile();

            if (guarda != null) {
//                FileWriter save = new FileWriter(guarda + ".dat");
//                save.write(bytesClavePrivada);
                FileOutputStream fos = new FileOutputStream(guarda);
                fos.write(bytesClavePrivada);
                fos.close();
                JOptionPane.showMessageDialog(null,
                        "El archivo se ha guardado exitosamente",
                        "Información", JOptionPane.INFORMATION_MESSAGE);

            }else{
                JOptionPane.showMessageDialog(null, "El archivo no ha sido guardado !");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Su archivo no se ha guardado",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

    }

//    public void guardarClave(byte claveByte[], String tipo) {
//        try {
//            archivoClaves_admin = new RandomAccessFile(ruta3, "rw");
//            archivoClaves_arbitro = new RandomAccessFile(ruta4, "rw");
////            tamanio = archivoClaves.length();
////            for (int i = 0; i < claveByte.length; i++) {
////                byte c = claveByte[i];
////                System.out.print(c);
////            }
////            int longitudClave = claveByte.length;
////            System.out.println("Longitud de la clave encriptada: " + longitudClave);
////            byte[] buffer = new byte[400];
////
////            byte[] byteEntero = Serializar.convertirInt_Byte(longitudClave);
////            buffer[0] = byteEntero[0];
////            buffer[1] = byteEntero[1];
////            buffer[2] = byteEntero[2];
////            buffer[3] = byteEntero[3];
////
////            System.arraycopy(claveByte, 0, buffer, 4, longitudClave);
//
////            System.out.println("Buffer");
////            for (int i = 0; i < buffer.length; i++) {
////                byte c = buffer[i];
////                System.out.print(c);
////            }
//            if (tipo.equalsIgnoreCase("Administrador")) {
//                long tamanio_archivo = archivoClaves_admin.length();
//                archivoClaves_admin.seek(tamanio_archivo);
//                archivoClaves_admin.write(claveByte);
//
//            } else {
//                long tamanio_archivo = archivoClaves_arbitro.length();
//                archivoClaves_arbitro.seek(tamanio_archivo);
//                archivoClaves_arbitro.write(claveByte);
//            }
//            listaClaves.add(claveByte);
//
//        } catch (IOException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//    public void cargarClavesPublicas() {
//        try {
//            listaClavesPublicas = new ArrayList<>();
//            archivoClavesPublica = new RandomAccessFile(ruta1, "rw");
//            archivoClavesPublicasArbitros = new RandomAccessFile(ruta2, "rw");
//
//            try {
//
//                if (archivoClavesPublica.length() > 0) {
//                    /*Lectura de las claves públicas de los administradores*/
//                    int tamanio_archivo = (int) archivoClavesPublica.length();
//                    byte[] buffer = new byte[tamanio_archivo];
//                    archivoClavesPublica.readFully(buffer);
//                    System.out.println("Aqui si llega administradores !");
////                    byte[] byteLongitud = new byte[4];
////                    byteLongitud[0] = buffer[0];
////                    byteLongitud[1] = buffer[1];
////                    byteLongitud[2] = buffer[2];
////                    byteLongitud[3] = buffer[3];
////                    int longitud = Serializar.convertirByte_Int(byteLongitud);
////                    System.out.println("Longitud de objeto: " + longitud);
////                    byte[] arrayObjeto = new byte[longitud];
////                    System.arraycopy(buffer, 4, arrayObjeto, 0, longitud);
//
//                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                    KeySpec keySpec = new X509EncodedKeySpec(buffer);
//
//                    PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
//
//                    listaClavesPublicas.add(keyFromBytes);
//                }
//
//            } catch (IOException ex) {
//                System.out.println("Fin del archivo de claves de administradores");
//            } catch (NoSuchAlgorithmException ex) {
//                Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (InvalidKeySpecException ex) {
//                Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            try {
//                /*Lectura de las claves públicas del archivo de árbitros*/
//                if (archivoClavesPublicasArbitros.length() > 0) {
//                    int tamanio_archivo = (int) archivoClavesPublicasArbitros.length();
//                    byte[] buffer2 = new byte[tamanio_archivo];
//                    archivoClavesPublicasArbitros.readFully(buffer2);
//                    System.out.println("Aqui si llega  arbitros!");
////                    byte[] byteLongitud2 = new byte[4];
////                    byteLongitud2[0] = buffer2[0];
////                    byteLongitud2[1] = buffer2[1];
////                    byteLongitud2[2] = buffer2[2];
////                    byteLongitud2[3] = buffer2[3];
////                    int longitud2 = Serializar.convertirByte_Int(byteLongitud2);
////                    System.out.println("Longitud de objeto: " + longitud2);
////                    byte[] arrayObjeto2 = new byte[longitud2];
////                    System.arraycopy(buffer2, 4, arrayObjeto2, 0, longitud2);
//
//                    KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
//                    KeySpec keySpec2 = new X509EncodedKeySpec(buffer2);
//
//                    PublicKey keyFromBytes2 = keyFactory2.generatePublic(keySpec2);
//
//                    listaClavesPublicas.add(keyFromBytes2);
//                }
//
//            } catch (IOException ex) {
//                System.out.println("Fin del archivo de claves de árbitros ");
//
//            } catch (NoSuchAlgorithmException ex) {
//                Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (InvalidKeySpecException ex) {
//                Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public PublicKey cargarClavePublica(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        int numBtyes = fis.available();
        byte[] bytes = new byte[numBtyes];

        fis.read(bytes);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new X509EncodedKeySpec(bytes);

        PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
        return keyFromBytes;
    }
//    public void cargarClaves(String tipo) {
//        try {
//            listaClaves = new ArrayList<>();
//            archivoClaves_admin = new RandomAccessFile(ruta3, "rw");
//            archivoClaves_arbitro = new RandomAccessFile(ruta4, "rw");
//
//            try {
//
//                if(tipo.equalsIgnoreCase("Administrador")){
//                    int longitud_archivo = (int) archivoClavesPublicasAdmin.length();
//                    byte [] buffer = new byte[longitud_archivo];
//                    archivoClavesPublicasAdmin.readFully(buffer);
//                    listaClaves.add(buffer);
//                }else{
//                    int longitud_archivo = (int) archivoClavesPublicasArbitros.length();
//                    byte [] buffer = new byte[longitud_archivo];
//                    archivoClavesPublicasArbitros.readFully(buffer);
//                    listaClaves.add(buffer);
//                }
//
//            } catch (IOException ex) {
//                System.out.println("Fin del archivo de claves");
//            }
//        } catch (IOException ex) {
//            System.out.println("Fin del archivo de claves");
//        }
//
//    }

    public PrivateKey cargarClavePrivada() {
        JFileChooser file = new JFileChooser();
        file.showOpenDialog(null);
        try {
            File archivo = file.getSelectedFile();
            if (archivo != null) {
                FileInputStream fis = new FileInputStream(archivo);
                int numBytes = fis.available();
                byte[] bytesClave = new byte[numBytes];
                fis.read(bytesClave);
                fis.close();

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                KeySpec keySpec = new PKCS8EncodedKeySpec(bytesClave);

                PrivateKey keyFromBytes = keyFactory.generatePrivate(keySpec);
                return keyFromBytes;

            }else{
                JOptionPane.showMessageDialog(null, "Error al cargar el archivo !");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex + ""
                    + "\nNo se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<PublicKey> listaClavesPublicasAdmin() {
        return listaClavesPublicas;
    }
//
//    public ArrayList<byte[]> listaClaves() {
//        return listaClaves;
//    }
}
//
//    public void cargarClavesArbitro() {
//        try {
//            archivoClavesPublicasArbitros = new RandomAccessFile(ruta2, "rw");
//            listaClavesPublicasArbitro = new ArrayList<>();
//
//            long tamanioArchivo = archivoClavesPublicasArbitros.length();
//            if (tamanioArchivo > 0) {
//                while (true) {
//                    byte[] buffer = new byte[1000];
//                    archivoClavesPublicasArbitros.readFully(buffer);
//                    System.out.println("Aqui si llega !");
//                    byte[] byteLongitud = new byte[4];
//                    byteLongitud[0] = buffer[0];
//                    byteLongitud[1] = buffer[1];
//                    byteLongitud[2] = buffer[2];
//                    byteLongitud[3] = buffer[3];
//                    int longitud = Serializar.convertirByte_Int(byteLongitud);
//                    System.out.println("Longitud de objeto: " + longitud);
//                    byte[] arrayObjeto = new byte[longitud];
//                    System.arraycopy(buffer, 4, arrayObjeto, 0, longitud);
//
//                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                    KeySpec keySpec = new X509EncodedKeySpec(arrayObjeto);
//
//                    PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
//                    listaClavesPublicasArbitro.add(keyFromBytes);
//                }
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidKeySpecException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void cargarClavePublicaAdmin() {
//        try {
//            archivoClavesPublicasAdmin = new RandomAccessFile(ruta1, "rw");
//            listaClavesPublicas = new ArrayList<>();
//
//            long tamanioArchivo = archivoClavesPublicasAdmin.length();
//            if (tamanioArchivo > 0) {
//                while (true) {
//                    byte[] buffer = new byte[1000];
//                    archivoClavesPublicasAdmin.readFully(buffer);
//                    byte[] byteLongitud = new byte[4];
//                    byteLongitud[0] = buffer[0];
//                    byteLongitud[1] = buffer[1];
//                    byteLongitud[2] = buffer[2];
//                    byteLongitud[3] = buffer[3];
//                    int longitud = Serializar.convertirByte_Int(byteLongitud);
//                    System.out.println("Longitud de objeto: " + longitud);
//                    byte[] arrayObjeto = new byte[longitud];
//                    System.arraycopy(buffer, 4, arrayObjeto, 0, longitud);
//
//                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                    KeySpec keySpec = new X509EncodedKeySpec(arrayObjeto);
//
//                    PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
//                    listaClavesPublicas.add(keyFromBytes);
//                }
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidKeySpecException ex) {
//            Logger.getLogger(ClaveDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }

