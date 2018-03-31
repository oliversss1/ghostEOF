
package login;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public Main(){
        
    }
    
    public static String getCodeFileHex(File file){
        String data="";
        
        try (FileInputStream f2=new FileInputStream(file);DataInputStream dti=new DataInputStream(f2)){
            while (true) {                
                byte dato=dti.readByte(); 
                data+=String.format("%02X ", dato);
            }
        }catch (EOFException e) {
            System.out.println("This fille finish...");
        }catch (IOException e){
            System.out.println("Fatal Error into this file " + e.toString());
        }
        
        return data;
    }
    
    
    
    
    public static void main(String[] args) {
   
        //analycing files
        File file=new File("src/images/2.jpg");
        String archivoHex = getCodeFileHex(file);
        System.out.println(archivoHex);

        
    }
    
}
