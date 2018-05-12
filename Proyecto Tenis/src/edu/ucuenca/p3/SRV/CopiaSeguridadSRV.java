/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import CompresionArchivos.ComprimirZip;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Thread.sleep;

/**
 *
 * @author brian
 */
public class CopiaSeguridadSRV {

    public void copiaSeguridad() throws IOException {
        ComprimirZip comprimirZip = new ComprimirZip();

        comprimirZip.comprimir("src\\edu\\ucuenca\\p3\\DAO_Archivos\\datosJugador.dat", "src\\ArchivosCopias\\datosJugador.dat");
        comprimirZip.comprimir("src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolParticipante.dat", "src\\ArchivosCopias\\arbolParticipante.dat");

        comprimirZip.comprimir("src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolClub.dat", "src\\ArchivosCopias\\arbolClub.dat");
        comprimirZip.comprimir("src\\edu\\ucuenca\\p3\\DAO_archivos\\datosClubes.dat", "src\\ArchivosCopias\\datosClubes.dat");

        comprimirZip.comprimir("src\\edu\\ucuenca\\p3\\DAO_archivos\\datosTorneos.dat", "src\\ArchivosCopias\\datosTorneos.dat");
        comprimirZip.comprimir("src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolTorneo.dat", "src\\ArchivosCopias\\arbolTorneo.dat");
    }

    public void recuperarCopiaSeguridad() throws Exception {
        File fichero1 = new File("src\\edu\\ucuenca\\p3\\DAO_Archivos\\datosJugador.dat");
        File fichero2 = new File("src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolParticipante.dat");
        File fichero3 = new File("src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolClub.dat");
        File fichero4 = new File("src\\edu\\ucuenca\\p3\\DAO_archivos\\datosClubes.dat");
        File fichero5 = new File("src\\edu\\ucuenca\\p3\\DAO_archivos\\datosTorneos.dat");
        File fichero6 = new File("src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolTorneo.dat");

        ComprimirZip descomprimirZip = new ComprimirZip();
        if (fichero1.delete() && fichero2.delete() && fichero3.delete() && fichero4.delete() && fichero5.delete() && fichero6.delete()) {
            System.out.println("El fichero ha sido borrado satisfactoriamente");
        }

        descomprimirZip.descomprimir("src\\ArchivosCopias\\datosJugador.dat", "src\\edu\\ucuenca\\p3\\DAO_Archivos\\datosJugador.dat");
        descomprimirZip.descomprimir("src\\ArchivosCopias\\arbolParticipante.dat", "src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolParticipante.dat");
        descomprimirZip.descomprimir("src\\ArchivosCopias\\arbolClub.dat", "src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolClub.dat");
        descomprimirZip.descomprimir("src\\ArchivosCopias\\datosClubes.dat", "src\\edu\\ucuenca\\p3\\DAO_archivos\\datosClubes.dat");
        descomprimirZip.descomprimir("src\\ArchivosCopias\\datosTorneos.dat", "src\\edu\\ucuenca\\p3\\DAO_archivos\\datosTorneos.dat");
        descomprimirZip.descomprimir("src\\ArchivosCopias\\arbolTorneo.dat", "src\\edu\\ucuenca\\p3\\DAO_archivos\\arbolTorneo.dat");
    }
}
