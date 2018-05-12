/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.Modulos;

import edu.ucuenca.p3.Modulos.Jugador;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brian
 */
public class MezclaDirecta {

    public static int IntercalacionPersonalizadaUsuario(String atributo) {
        String ruta = ("src/edu/ucuenca/p3/DAO_archivos");
        File archivoUno = new File(ruta, "datosInter1.dat");
        File archivoDos = new File(ruta, "datosInter2.dat");
        File archivo = new File(ruta, "datosJugador.dat");

        ArrayList ArregloUno = new ArrayList();
        ArrayList ArregloDos = new ArrayList();

        try {

            FileOutputStream flujoSalida = new FileOutputStream(archivo);
            ObjectOutputStream dataOS = new ObjectOutputStream(flujoSalida);

            FileInputStream flujoEntradaUno = new FileInputStream(archivoUno);
            ObjectInputStream oisUno = new ObjectInputStream(flujoEntradaUno);

            FileInputStream flujoEntradaDos = new FileInputStream(archivoDos);
            ObjectInputStream oisDos = new ObjectInputStream(flujoEntradaDos);

            Jugador datoUsuario = (Jugador) oisUno.readObject();
            Jugador datoUsuarioDos = null;

            Jugador elementArchivoUno = datoUsuario;
            Jugador elementArchivoDos = null;
             //El registro de archivo 2 es nulo en caso de que exista un solo registro
            //y solo se cargará un registro en un solo archivo
            boolean ultimoArchivoUno = false;
            boolean ultimoArchivoDos = false;

            try {
                elementArchivoDos = (Jugador) oisDos.readObject();
                datoUsuarioDos = elementArchivoDos;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No existe registros en el archivo 2");
                ultimoArchivoDos = true;
            }

            int numSublistas = 0;
            System.out.println("\n");
            while (ultimoArchivoUno == false || ultimoArchivoDos == false) {

                ArrayList Arreglo = new ArrayList();
                System.out.println(datoUsuario.compareTo(elementArchivoUno, atributo));
                while ((datoUsuario.compareTo(elementArchivoUno, atributo) && ultimoArchivoUno == false) || (datoUsuario == elementArchivoUno)) {
                    try {
                        Arreglo.add(elementArchivoUno);
                        ArregloUno.add(elementArchivoUno);
                        datoUsuario = elementArchivoUno;
                        elementArchivoUno = (Jugador) oisUno.readObject();
                    } catch (IOException ex) {
                        ultimoArchivoUno = true;
                        System.out.println("Fin del archivo 1");
                        break;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MezclaDirecta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (ultimoArchivoUno == false) {
                    try {
                        Arreglo.add(elementArchivoUno);
                        ArregloUno.add(elementArchivoUno);
                        datoUsuario = (Jugador) oisUno.readObject();
                        elementArchivoUno = datoUsuario;
                    } catch (IOException | ClassNotFoundException ex) {
                        System.out.println("Fin del archivo 1");
                        ultimoArchivoUno = true;
                    }
                }

                System.out.println("Sublista del archivo 1: " + ArregloUno);
                ArregloUno.clear();

                if (null != elementArchivoDos) {
                    while ((datoUsuarioDos.compareTo(elementArchivoDos, atributo) && ultimoArchivoDos == false) || (datoUsuarioDos == elementArchivoDos)) {
                        try {
                            Arreglo.add(elementArchivoDos);
                            ArregloDos.add(elementArchivoDos);
                            datoUsuarioDos = elementArchivoDos;
                            elementArchivoDos = (Jugador) oisDos.readObject();
                        } catch (IOException e) {
                            ultimoArchivoDos = true;
                            System.out.println("Fin del archivo 2");
                            elementArchivoDos = null;
                            break;
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(MezclaDirecta.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    if (ultimoArchivoDos == false) {
                        try {
                            Arreglo.add(elementArchivoDos);
                            ArregloDos.add(elementArchivoDos);
                            elementArchivoDos = (Jugador) oisDos.readObject();
                            datoUsuarioDos = elementArchivoDos;
                        } catch (IOException | ClassNotFoundException ex) {
                            System.out.println("Fin del archivo 2");
                            ultimoArchivoDos = true;
                        }
                    }

                }

                System.out.println("Sublista del Archivo 2: " + ArregloDos);
                ArregloDos.clear();

                System.out.println("Ultimo archivo 1:: " + ultimoArchivoUno + ", Ultimoarchivo 2:: " + ultimoArchivoDos);

                //Collections.sort(Arreglo, verificarComparadorUsuario(atributo));
                int numero_iteracion = 1;
                while (numSublistas < Arreglo.size()) {
                    MezclaDirecta.repartir(numSublistas);
                    //            MezclaDirecta.mostrarValores(lenSubListas);

                    System.out.println("\n\nMezcla " + numero_iteracion);

                    MezclaDirecta.ordenar(numSublistas);
                    MezclaDirecta.mostar(numSublistas);

                    numSublistas *= 2;
                    numero_iteracion++;
                }

                numSublistas++;

                for (int i = 0; i < Arreglo.size(); i++) {
                    dataOS.writeObject((Jugador) Arreglo.get(i));
                }
            }

            oisUno.close();
            oisDos.close();
            dataOS.close();

            return numSublistas;

        } catch (FileNotFoundException ex) {
            //Logger.getLogger(LecturaArchivoOrigen.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No se encuentra archivo.");
        } catch (IOException ex) {
            System.out.println("Erro de Lectura en Intercalacion Natural");
            Logger.getLogger(MezclaDirecta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MezclaDirecta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static void repartir(int lenSubListas) {
        DataOutputStream archivoEscribir1 = null;
        DataOutputStream archivoEscribir2 = null;
        int valor_parejas = 0;
        try {
            String ruta = ("src/edu/ucuenca/p3/DAO_archivos");
            DataInputStream archivoLeerPrincipal = new DataInputStream(new FileInputStream(new File(ruta, "participantesXclave.dat")));
            archivoEscribir1 = new DataOutputStream(new FileOutputStream(new File(ruta, "datosInter1.dat")));
            archivoEscribir2 = new DataOutputStream(new FileOutputStream(new File(ruta, "datosInter2.dat")));

            while (archivoLeerPrincipal.available() != 0) {
                valor_parejas = 0;
                while (valor_parejas < lenSubListas && archivoLeerPrincipal.available() != 0) {
                    archivoEscribir1.writeInt(archivoLeerPrincipal.readInt());
                    valor_parejas++;
                }
                valor_parejas = 0;
                while (valor_parejas < lenSubListas && archivoLeerPrincipal.available() != 0) {
                    archivoEscribir2.writeInt(archivoLeerPrincipal.readInt());
                    valor_parejas++;
                }
            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                archivoEscribir1.close();
                archivoEscribir2.close();

            } catch (IOException ex) {
                //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void ordenar(int lenSubListas) {
        DataOutputStream archivoPrincipal = null;
        DataInputStream archivoAux1 = null;
        DataInputStream archivoAux2 = null;
        try {
            String ruta = "src/edu/ucuenca/p3/DAO_archivos";
            archivoPrincipal = new DataOutputStream(new FileOutputStream(new File(ruta, "participantesXclave.dat")));
            archivoAux1 = new DataInputStream(new FileInputStream(new File(ruta, "datosInter1.dat")));
            archivoAux2 = new DataInputStream(new FileInputStream(new File(ruta, "datosInter2.dat")));
            int datoA;
            int datoB;
            int alternoArchivo1;
            int alternoArchivo2;

            while (archivoAux1.available() != 0 && archivoAux2.available() != 0) {
                datoA = archivoAux1.readInt();
                datoB = archivoAux2.readInt();
                alternoArchivo1 = 0;
                alternoArchivo2 = 0;
                while (alternoArchivo1 < lenSubListas && alternoArchivo2 < lenSubListas) {
                    if (datoA < datoB) {
                        //System.out.println("Se escribe el valor A: " + datoA);
                        archivoPrincipal.writeInt(datoA);
                        alternoArchivo1++;
                        if (archivoAux1.available() == 0 || alternoArchivo1 >= lenSubListas) {
                            //System.out.println("Se escribe el dato consecuente de A" + datoB);
                            archivoPrincipal.writeInt(datoB);
                            alternoArchivo2++;
                            while (alternoArchivo2 < lenSubListas && archivoAux2.available() != 0) {
                                //Se escribe la tupla restante
                                int tuplaA = archivoAux2.readInt();
                                //System.out.println("Valor de la tupla: "+tuplaA);

                                archivoPrincipal.writeInt(tuplaA);
                                alternoArchivo2++;
                            }
                            break;
                        }
                        //Se lee el siguiente dato del archivo
                        datoA = archivoAux1.readInt();
                    } else {
                        //System.out.println("Se escribe el valor B: " + datoB);
                        archivoPrincipal.writeInt(datoB);
                        alternoArchivo2++;
                        if (archivoAux2.available() == 0 || alternoArchivo2 >= lenSubListas) {
                            //System.out.println("Se escribe el dato consecuente de B: " + datoA);
                            archivoPrincipal.writeInt(datoA);
                            alternoArchivo1++;

                            while (alternoArchivo1 < lenSubListas && archivoAux1.available() != 0) {
                                //Se escribe tupla restante
                                int tuplaB = archivoAux1.readInt();
//                                archivoPrincipal.writeInt(archivoAux1.readInt());
                                archivoPrincipal.writeInt(tuplaB);
                                alternoArchivo1++;
                            }
                            break;
                        }
                        //Se lee el siguiente dato del archivo.
                        datoB = archivoAux2.readInt();
                    }
                }
            }
            while (archivoAux1.available() != 0) {
                archivoPrincipal.writeInt(archivoAux1.readInt());
            }
            while (archivoAux2.available() != 0) {
                archivoPrincipal.writeInt(archivoAux2.readInt());
            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                archivoPrincipal.close();
                archivoAux1.close();
                archivoAux2.close();
            } catch (IOException ex) {
                //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void mostar(int LenSubLista) {
        DataInputStream archivoLeerPrincipal = null;
        DataInputStream archivoLeerAux1 = null;
        DataInputStream archivoLeerAux2 = null;
        try {
            String ruta = "src/edu/ucuenca/p3/DAO_archivos";
            archivoLeerAux1 = new DataInputStream(new FileInputStream(new File(ruta, "datosInter1.dat")));
            archivoLeerAux2 = new DataInputStream(new FileInputStream(new File(ruta, "datosInter2.dat")));
            archivoLeerPrincipal = new DataInputStream(new FileInputStream(new File(ruta, "participantesXclave.dat")));
            System.out.println("\nArchivo principal");
            int separador = 0;
            while (archivoLeerPrincipal.available() != 0) {
                System.out.print(archivoLeerPrincipal.readInt() + " ");
                separador++;
                if (separador == LenSubLista) {
                    System.out.print(",");
                    separador = 0;
                }
            }
//            System.out.println("\nArchivo Auxiliar 1");
//            separador = 0;
//            while (archivoLeerAux1.available() != 0) {
//                System.out.print(archivoLeerAux1.readInt() + " ");
//                separador++;
//                if (separador == LenSubLista) {
//                    System.out.print(",");
//                    separador = 0;
//                }
//
//            }
//            System.out.println("\nArchivo Auxiliar 2");
//            separador = 0;
//            while (archivoLeerAux2.available() != 0) {
//                System.out.print(archivoLeerAux2.readInt() + " ");
//                separador++;
//                if (separador == LenSubLista) {
//                    System.out.print(",");
//                    separador = 0;
//                }
//            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                archivoLeerPrincipal.close();
                archivoLeerAux2.close();
                archivoLeerAux1.close();
            } catch (IOException ex) {
                //Logger.getLogger(ManejoArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
