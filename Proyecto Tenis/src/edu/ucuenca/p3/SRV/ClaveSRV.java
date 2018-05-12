/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import edu.ucuenca.p3.SRV.exceptions.IngresoEncriptadoException;
import edu.ucuenca.p3.DAO.ClaveDAO;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

/**
 *
 * @author andre
 */
public class ClaveSRV {

    public void agregarUsuario(String tipo) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        validarTipo(tipo);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

//        ClaveDAO.getInstancia().agregarClavePublica(publicKey, tipo);
        ClaveDAO.getInstancia().guardarClavePrivada(privateKey);
//        
//        Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        rsa.init(Cipher.ENCRYPT_MODE, privateKey);
//        byte [] encriptado = rsa.doFinal(clave.getBytes());

//        ClaveDAO.getInstancia().cargarClaves();
//        ClaveDAO.getInstancia().guardarClave(encriptado, tipo);
        ClaveDAO.getInstancia().agregarClavePublica(publicKey, tipo);
    }

    public void cargarClaves() {
//        ClaveDAO.getInstancia().cargarClavesPublicas();
//        ClaveDAO.getInstancia().cargarClavePublicaAdmin();
//        ClaveDAO.getInstancia().cargarClavesArbitro();
    }

    private void validarCampos(String tipo, String clave) {
        if (tipo.equalsIgnoreCase("")) {
            throw new IllegalArgumentException("Campo de tipo no seleccionado !");
        }

        if (clave.equalsIgnoreCase("")) {
            throw new IllegalArgumentException("El campo de clave no puede estar vacío !");
        }
    }

    private void validarTipo(String tipo) {
        if (tipo.equalsIgnoreCase("")) {
            throw new IllegalArgumentException("Campo de tipo no seleccionado !");
        }
    }

    public boolean ingresoUsuario(String clave, String tipo) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, Exception {
//        cargarClaves();
//        ArrayList<PublicKey> listaClavesPublicas = ClaveDAO.getInstancia().listaClavesPublicasAdmin();
//        System.out.println("Longitud de array: "+listaClavesPublicas.size());
        try {
            PrivateKey keyPrivate = ClaveDAO.getInstancia().cargarClavePrivada();
            PublicKey clavePublica = null;
            Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            boolean acceso = false;

            if (tipo.equalsIgnoreCase("Administrador")) {
                clavePublica = ClaveDAO.getInstancia().cargarClavePublica("src//edu//ucuenca//p3//Claves//clavesPublicasAdmin.dat");
            } else {
                clavePublica = ClaveDAO.getInstancia().cargarClavePublica("src//edu//ucuenca//p3//Claves//clavesPublicasArbitro.dat");
            }

//        for (PublicKey clavePublic : listaClavesPublicas) {
            rsa.init(Cipher.ENCRYPT_MODE, keyPrivate);
            byte[] claveEncriptada = rsa.doFinal(clave.getBytes());

            rsa.init(Cipher.DECRYPT_MODE, clavePublica);
            byte[] bytesDesencriptados = rsa.doFinal(claveEncriptada);
            String textoDesencripado = new String(bytesDesencriptados);

            if (clave.equals(textoDesencripado)) {
                acceso = true;

            }
//        }
            return acceso;

        } catch (Exception ex) {
            throw new IngresoEncriptadoException("Ingreso erróneo de clave !");
        }
    }
}
