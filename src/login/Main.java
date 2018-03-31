
package login;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;

public class Main {

    public Main(){
        
    }
    
    //
    public static void processFile(File file){
        String fileHex = getCodeFileHex(file);
        showAnalyzedFiles(fileHex);
    }
    
    //
    public static String getCodeFileHex(File file){
        String data="";
        
        try (FileInputStream f2=new FileInputStream(file);DataInputStream dti=new DataInputStream(f2)){
            while (true) {                
                byte dato=dti.readByte(); 
                data+=String.format("%02X ", dato);
                //System.out.println(String.format("%02X ", dato));
            }
            
                        
        }catch (EOFException e) {
            System.out.println("This file finish...");
        }catch (IOException e){
            System.out.println("Fatal Error into this file " + e.toString());
        }
        
        return data;
    }
    //
    public static int[] getHeaders(String fileHex,String text){
        int []data = null;
        
        int indexArrayData = 0;
        int sizeFileHex = fileHex.length();
        int sizeText = text.length();        
        
        for(int i = 0;i < sizeFileHex-(sizeText-1) ;i++){
            String subText = getSubString(i,sizeText,fileHex);
            if(subText.equals(text)){
                data[indexArrayData] = i;
                indexArrayData++;
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
    public static int[] getSizeTypeFiles(String fileHex){
        
        int data[] = null;
        
        int dataHeader1[] = getHeaders(fileHex,"ffd8ff");
        int dataHeader2[] = getHeaders(fileHex,"255044");
        int dataHeader3[] = getHeaders(fileHex,"526172");
        
        System.arraycopy( dataHeader1, 0, data, 0, dataHeader1.length );
        System.arraycopy( dataHeader2, 0, data, dataHeader1.length, dataHeader2.length );
        System.arraycopy( dataHeader3, 0, data, dataHeader2.length, dataHeader3.length );
        
        Arrays.sort(data);
        
        int data2[] = null;
        int indexData2 = 0;
        
        for(int i = 0;i < data.length ;i++){
            if(i == data.length-1){
                data2[indexData2] = data[i];
                indexData2++;
                data2[indexData2] = fileHex.length();
                indexData2++;
            }else{
                data2[indexData2] = data[i];
                indexData2++;
                data2[indexData2] = (data[i-1]-1);
                indexData2++;
            }
        }
       
        return data;
    }
    //
    public static void showAnalyzedFiles(String fileHex){
        int sizeFiles[] = getSizeTypeFiles(fileHex);
        
        for(int i=0;i<sizeFiles.length;i++){
            showFiles(i,i+1,sizeFiles,fileHex);
        }
    }
    //
    public static void showFiles(int index,int close,int sizeFiles[],String fileHex){
        int indexFile = sizeFiles[index];
        int closeFile = sizeFiles[close];
        int closeFileHex = fileHex.length();
        
        String imageCode  = "";
        for(int i = 0;i <= closeFileHex;i++){
            if(i==closeFileHex){
                break;
            }
            imageCode += fileHex.charAt(i);
        }
        
        int accumulator = (closeFile+1)/2;
        buildNewFiles(imageCode,accumulator);
    }
    //
    public static void buildNewFiles(String imageCode,int accumulator){
        
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
           
        File file=new File("src/images/res_1.jpg");
        String archivoHex = getCodeFileHex(file);
        int sizeArchivoHex = archivoHex.length();
        //System.out.println(archivoHex);
              
//        String subFile = "";
//        for(int i=0;i<archivoHex.length()*0.5;i++){
//            subFile += archivoHex.charAt(i);
//        }
        
        String[] v = archivoHex.split(" ");
        int sizeV = v.length;
        
        //System.out.println(archivoHex);
        System.out.println(archivoHex.charAt(sizeArchivoHex-2));
        //System.out.println(v[sizeV-4]);
//        byte[] arr = new byte[v.length];
//        int x = 0;
//        
//        for(String val: v) {
//            arr[x++] =  Integer.decode("0x" + val).byteValue();
//
//        }
//        
//        FileOutputStream fos=new FileOutputStream("src/images/2_builded.jpg");
//        fos.write(arr);
//        fos.flush();
//        fos.close();
        
    }
    
}
