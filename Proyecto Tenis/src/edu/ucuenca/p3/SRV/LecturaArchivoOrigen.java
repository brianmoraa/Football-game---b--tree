/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import edu.ucuenca.p3.DAO.Serializar;
import edu.ucuenca.p3.Modulos.Jugador;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brian
 */
public class LecturaArchivoOrigen {
    
    public static boolean LecturaArchivoParticipante(String atributo) {

        String ruta = ("src/edu/ucuenca/p3/DAO_Archivos");
        File archivoUno = new File(ruta, "datosInter1.dat");
        File archivoDos = new File(ruta, "datosInter2.dat");
        File archivo = new File(ruta, "datosJugador.dat");

        try {
            FileOutputStream flujoSalidaUno = new FileOutputStream(archivoUno);
            ObjectOutputStream dataOSUno = new ObjectOutputStream(flujoSalidaUno);

            FileOutputStream flujoSalidaDos = new FileOutputStream(archivoDos);
            ObjectOutputStream dataOSDos = new ObjectOutputStream(flujoSalidaDos);

            FileInputStream flujoEntrada = new FileInputStream(archivo);
            ObjectInputStream dataIS = new ObjectInputStream(flujoEntrada);

            System.out.println("Leyendo Datos del Archivo Origen...");

            Jugador elementEnArchivoUno = (Jugador) dataIS.readObject();
            dataOSUno.writeObject(elementEnArchivoUno);

            Jugador elementEnAchivoDos = null;

            boolean enArchivoUno = true;
//            System.out.print("Sublistas: \nArchivo 1: ");
//            System.out.println(elementEnArchivoUno + ", ");
            while (true) {

                Jugador element = null;
                try {
                    byte [] arrayPrevio = new byte[1000]; 
                    byte [] arrayCantidad = new byte[4];
                    dataIS.read(arrayPrevio);
                    
                    arrayCantidad[0] = arrayPrevio[0];
                    arrayCantidad[1] = arrayPrevio[1];
                    arrayCantidad[2] = arrayPrevio[2];
                    arrayCantidad[3] = arrayPrevio[3];
                    
                    int longitud = Serializar.convertirByte_Int(arrayCantidad);
                    byte[] arrayObjecto = new byte[longitud];
                    System.arraycopy(arrayPrevio, 4, arrayObjecto, 0, longitud);
                    element = (Jugador) Serializar.deserialize(arrayObjecto);
                    
                } catch (IOException ex) {
                    break;
                }

                if (elementEnArchivoUno.compareTo(element, atributo) && enArchivoUno == true) {
                    dataOSUno.writeObject(element);
                    elementEnArchivoUno = element;
//                    System.out.println(element+", ");
                } else if (!elementEnArchivoUno.compareTo(element, atributo) && enArchivoUno == true) {
                    dataOSDos.writeObject(element);
                    elementEnAchivoDos = element;
                    enArchivoUno = false;
//                    System.out.println("\nArchivo 2: "+element+", ");
                } else if (elementEnAchivoDos.compareTo(element, atributo) && enArchivoUno == false) {
                    dataOSDos.writeObject(element);
                    elementEnAchivoDos = element;
//                    System.out.print(element+", ");
                } else if (!elementEnAchivoDos.compareTo(element, atributo) && enArchivoUno == false) {
                    dataOSUno.writeObject(element);
                    elementEnArchivoUno = element;
                    enArchivoUno = true;
//                    System.out.print("\nArchivo 1: "+element+", ");
                }
            }
            dataIS.close();
            dataOSUno.close();
            dataOSDos.close();
            return true;

        } catch (FileNotFoundException | ClassNotFoundException ex) {
            Logger.getLogger(LecturaArchivoOrigen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Lectura Archivos Origen Puntero nulo");
            Logger.getLogger(LecturaArchivoOrigen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
