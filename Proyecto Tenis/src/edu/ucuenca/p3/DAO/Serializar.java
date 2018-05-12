/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.DAO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author brian
 */
public class Serializar {
    public static byte[] serialize(Object obj) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException{
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    
    public static byte[] convertirInt_Byte(int numero){
        byte bytes_array[] = new byte[4];
        bytes_array[3] = (byte) (numero & 0x000000ff);
        bytes_array[2] = (byte) ((numero & 0x0000ff00) >> 8);
        bytes_array[1] = (byte) ((numero & 0x00ff0000) >> 16);
        bytes_array[0] = (byte) ((numero & 0xff000000) >> 24);
        
        return bytes_array;
    }
    
    public static int convertirByte_Int(byte bytes_array[]){
        int longitud = (bytes_array[0]<<24)&0xff000000|
       (bytes_array[1]<<16)&0x00ff0000|
       (bytes_array[2]<< 8)&0x0000ff00|
       (bytes_array[3]<< 0)&0x000000ff;
        
        return longitud;
    }
}