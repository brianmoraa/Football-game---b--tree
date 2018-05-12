/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CompresionArchivos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author brian
 */
public class ComprimirZip {
    
    private static final int BUFFER_SIZE = 1024;
    
    public void comprimir(String archivoacomprimir, String archivocomprimido) throws FileNotFoundException, IOException {
        
        String inputFile=archivoacomprimir;
        FileInputStream in=new FileInputStream(inputFile);
        FileOutputStream out=new FileOutputStream(archivocomprimido);
        byte b[] =new byte[2048];
        ZipOutputStream zipOut=new ZipOutputStream(out);
        ZipEntry entry=new ZipEntry(inputFile);
        zipOut.putNextEntry(entry);
        int len=0;
        while((len=in.read(b))!=-1){
            zipOut.write(b, 0, len);  
        }
        zipOut.closeEntry();
        zipOut.close();
        in.close();
        out.close();
    }
    
     public void descomprimir(String pZipFile, String pFile) throws Exception {
		BufferedOutputStream bos = null;
		FileInputStream fis = null;
		ZipInputStream zipis = null;
		FileOutputStream fos = null;
 
		try {
			fis = new FileInputStream(pZipFile);
			zipis = new ZipInputStream(new BufferedInputStream(fis));
			if (zipis.getNextEntry() != null) {
				int len = 0;
				byte[] buffer = new byte[BUFFER_SIZE];
				fos = new FileOutputStream(pFile);
				bos = new BufferedOutputStream(fos, BUFFER_SIZE);
 
				while  ((len = zipis.read(buffer, 0, BUFFER_SIZE)) != -1)
					bos.write(buffer, 0, len);
				bos.flush();
			} else {
				throw new Exception("El zip no contenia fichero alguno");
			} // end if
		} catch (Exception e) {
			throw e;
		} finally {
			bos.close();
			zipis.close();
			fos.close();
			fis.close();
		} // end try
	} // end UnZip 
}
