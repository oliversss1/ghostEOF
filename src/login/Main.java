
package login;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public Main(){
        
    }
    
    public static void processFile(File file){
        //this function process out file
        
        // We get HexCode of our file
        String fileHex = getCodeFileHex(file);
        // Now we begin to show found file 
        showAnalyzedFiles(fileHex);
    }
    
    //
    public static String getCodeFileHex(File file){
        String data="";
        
        try {
           
           FileInputStream f2 = new FileInputStream(file);
           BufferedInputStream myBuffer = new BufferedInputStream(f2);
                              
           while(myBuffer.available()>0){
               byte dato = (byte) myBuffer.read();
               data += String.format("%02X",dato);               
           }
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    //
    public static Vector getHeaders(String fileHex,String text){
        
        Vector data = new Vector();
        
        int sizeFileHex = fileHex.length();
        int sizeText = text.length();        
                
        for(int i = 0;i < sizeFileHex-(sizeText-1) ;i++){
            String subText = getSubString(i,sizeText,fileHex);
           
            if(subText.equals(text)){                
                data.add(i);
            }
        }
        
        return data;
    } 
    //
    public static String getSubString(int index,int sizeText,String fileHex){
        String data = "";
        
        for(int i = 0;i < sizeText;i++){
            data += fileHex.charAt(index+i);
        }
        
        return data;
    }
    //
    public static Vector getSizeTypeFiles(String fileHex){
        
        Vector dataHeader1 = new Vector();
        dataHeader1 = getHeaders(fileHex,"FFD8FF");        
        Vector data = new Vector(dataHeader1.size());
        
        System.out.println(dataHeader1.toString());        
        
        Vector data2 = new Vector(dataHeader1.size());
        int indexData2 = 0;
        
        for(int i = 0;i < dataHeader1.size() ;i++){
            System.out.println("s");
            if(i == dataHeader1.size()-1){                
                data2.add(dataHeader1.elementAt(i));
                data2.add(fileHex.length());
            }else{
                data2.add(dataHeader1.elementAt(i));
                data2.add((dataHeader1.elementAt(i+1)));                
            }
        }
       
        return data2;
    }
    //
    public static void showAnalyzedFiles(String fileHex){
        System.out.println("entra showAnalyzedFiles");
        Vector sizeFiles= getSizeTypeFiles(fileHex);
        System.out.println(sizeFiles.toString());
        
        for(int i=0;i<sizeFiles.size();i=i+2){
            showFiles(i,i+1,sizeFiles,fileHex);
        }
        
    }
    //
    public static void showFiles(int index,int close,Vector sizeFiles,String fileHex){
        System.out.println("entra showFiles");
        int indexFile = (int) sizeFiles.elementAt(index);
        int closeFile = (int) sizeFiles.elementAt(close);
        int closeFileHex = fileHex.length();
        
        String imageCode  = "";
        for(int i = indexFile;i <= closeFileHex;i++){
            if(i==closeFileHex){
                break;
            }
            imageCode += fileHex.charAt(i);
        }
        
        int accumulator = (close+1)/2;        
        buildNewFiles(imageCode,accumulator);
    }
    //
    public static void buildNewFiles(String imageCode,int accumulator){
        
        String[] v = imageCode.split("");
        
        byte[] arr = new byte[imageCode.length()];
        int x = 0;
        
        for(int i=0;i<(imageCode.length());i=i+2) {            
            String val1 = imageCode.charAt(i)+"";
            String val2 = imageCode.charAt(i+1)+"";
            
            String val = (val1+val2);            
            byte z = Integer.decode("0x" + val).byteValue();
            
            arr[x++] =  z;
        }
        
        try (FileOutputStream fos = new FileOutputStream("src/images/new_imagen"+accumulator+".jpg")) {
            fos.write(arr);
            fos.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    public static void main(String[] args){
           
        File file=new File("src/images/res_1.jpg");
        String imageCode = getCodeFileHex(file);
        processFile(file);
        
    }
    
}
