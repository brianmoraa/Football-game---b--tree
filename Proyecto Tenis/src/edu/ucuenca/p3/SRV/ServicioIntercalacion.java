/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.SRV;

import edu.ucuenca.p3.Modulos.MezclaDirecta;

/**
 *
 * @author brian
 */
public class ServicioIntercalacion {
    /**
     *
     * @param atributo
     */
    
    public static void intercalarDatosParticipante(String atributo) {
        
        System.out.println("\nOrdenando por : "+atributo);
        if (LecturaArchivoOrigen.LecturaArchivoParticipante(atributo)){
            System.out.println("Sin DATOS en el archivo");
            return;
        }
        
        while(MezclaDirecta.IntercalacionPersonalizadaUsuario(atributo) != 1){
            LecturaArchivoOrigen.LecturaArchivoParticipante(atributo);
        }
    }
}