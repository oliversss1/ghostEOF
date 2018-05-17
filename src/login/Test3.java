/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author ghost
 */
public final class Test3 extends javax.swing.JFrame {
    
    private File ImageSelected,ImageSelected2;
    
    public Test3() {
        initComponents();
        
        jPanelResultado.setLayout(new BoxLayout(jPanelResultado, BoxLayout.X_AXIS));
    }
    
    public static void processFile(File file) {
        //this function process out file

        // We get HexCode from our file
        String fileHex = getCodeFileHex(file);
        // Now we started to show the found files
        showAnalyzedFiles(fileHex);
    }
    //
    public static String getCodeFileHex(File file) {
        String data = "";

        try {

            FileInputStream f2 = new FileInputStream(file);
            BufferedInputStream myBuffer = new BufferedInputStream(f2);

            while (myBuffer.available() > 0) {
                byte dato = (byte) myBuffer.read();
                data += String.format("%02X", dato);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test3.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }
    //
    public static Vector getHeaders(String fileHex, Vector headers) {

        Vector data = new Vector();
        int sizeFileHex = fileHex.length();
        int cH = 0;
        
        for(int v = 0;v < headers.size();v++){                        
            String text = (String) headers.elementAt(v);
            System.out.println("SCANEO nro:"+v+"-"+" para:"+text);
            cH = 0;
            int sizeText = text.length();
            for (int i = 0; i < sizeFileHex - (sizeText - 1); i++) {
                String subText = getSubString(i, sizeText, fileHex);
                if (subText.equals(text)) {
                    data.add(i);
                    cH++;                    
                }                
            }
            System.out.println("coincidencia encontrada :"+cH);
        }
        
        return data;
    }
    //
    public static String getSubString(int index, int sizeText, String fileHex) {
        String data = "";

        for (int i = 0; i < sizeText; i++) {
            data += fileHex.charAt(index + i);
        }

        return data;
    }

    public static Vector listHeaders(){
        
        Vector listHeader = new Vector();
        listHeader.add("FFD8FF");   //jpg
        listHeader.add("25504446"); //pdf
        listHeader.add("494433");   //mp3        
        listHeader.add("52617221"); //rar
        listHeader.add("D0CF11E0"); //doc        
        
        return listHeader;
    }
    
    public static Vector listHeaders2(){
        
        Vector listHeader = new Vector();
        listHeader.add("FFD8FF,jpg");    //jpg        
        listHeader.add("25504446,pdf");  //pdf
        listHeader.add("494433,mp3");    //mp3        
        listHeader.add("52617221,rar");  //rar
        listHeader.add("D0CF11E0,doc");  //doc        
        
        return listHeader;
    }    
    //
    public static Vector getSizeTypeFiles(String fileHex) {

        Vector dataHeader1 = new Vector();
        
        dataHeader1 = getHeaders(fileHex,listHeaders());
        
        System.out.println("UBICACION DE CABECERAS - INDICES");
        System.out.println(dataHeader1);
            
        Vector data2 = new Vector(dataHeader1.size());
        int indexData2 = 0;

        for (int i = 0; i < dataHeader1.size(); i++) {
            
            if (i == dataHeader1.size() - 1) {
                data2.add(dataHeader1.elementAt(i));
                data2.add(fileHex.length());
            } else {
                data2.add(dataHeader1.elementAt(i));
                data2.add((dataHeader1.elementAt(i + 1)));
            }
        }

        return data2;
    }

    public static Vector function_that_takes_a_b_merges(Vector Va, Vector Vb) {
        Vector merge = new Vector();
        merge.addAll(Va);
        merge.addAll(Vb);
        return merge;
    }
    //
    public static void showAnalyzedFiles(String fileHex) {
        
        Vector sizeFiles = getSizeTypeFiles(fileHex);
        System.out.println("DETERMINANDO CUERPO DE OBJETOS EXTRAÑOS");
        System.out.println(sizeFiles);

        for (int i = 0; i < sizeFiles.size(); i = i + 2) {
            System.out.println("armando el contenido del objeto :"+(i+1) );
            showFiles(i, i + 1, sizeFiles, fileHex);
        }
        
    }
    //
    public static void showFiles(int index, int close, Vector sizeFiles, String fileHex) {
        
        int indexFile = (int) sizeFiles.elementAt(index);
        int closeFile = (int) sizeFiles.elementAt(close);
        int closeFileHex = fileHex.length();

        String imageCode = "";
        for (int i = indexFile; i <= closeFileHex; i++) {
            if (i == closeFileHex) {
                break;
            }
            imageCode += fileHex.charAt(i);
        }

        int accumulator = (close + 1) / 2;
        buildNewFiles(imageCode, accumulator);
    }
    //
    public static void buildNewFiles(String imageCode, int accumulator) {

        String header = imageCode.charAt(0)+""+
                        imageCode.charAt(1)+""+
                        imageCode.charAt(2)+""+
                        imageCode.charAt(3)+""+
                        imageCode.charAt(4)+""+
                        imageCode.charAt(5);
        
        String typeFile="";
        
        if( header.equals("FFD8FF") ) typeFile=".jpg";
        if( header.equals("255044") ) typeFile=".pdf";
        if( header.equals("494433") ) typeFile=".mp3";
        
        String[] v = imageCode.split("");
        
        String userDir = System.getProperty("user.home");        
        
        byte[] arr = new byte[imageCode.length()];
        int x = 0;

        for (int i = 0; i < (imageCode.length()); i = i + 2) {
            String val1 = imageCode.charAt(i) + "";
            String val2 = imageCode.charAt(i + 1) + "";

            String val = (val1 + val2);
            byte z = Integer.decode("0x" + val).byteValue();

            arr[x++] = z;
        }

        try (
                FileOutputStream fos = new FileOutputStream(userDir +"/Desktop/imagesResultGhost/image" + accumulator + typeFile)
            ){
            fos.write(arr);
            fos.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelContentPane = new javax.swing.JPanel();
        jButtonProcesaImagen = new javax.swing.JButton();
        jButtonCargaImagen = new javax.swing.JButton();
        jProgressBarCargaImagen = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelResultado = new javax.swing.JPanel();
        jLabelA = new javax.swing.JLabel();
        jLabelA1 = new javax.swing.JLabel();
        jButtonCargaImagen1 = new javax.swing.JButton();
        jButtonProcesaImagen1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanelContentPane.setBackground(new java.awt.Color(3, 18, 41));

        jButtonProcesaImagen.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jButtonProcesaImagen.setForeground(new java.awt.Color(173, 94, 15));
        jButtonProcesaImagen.setText("PROCESAR");
        jButtonProcesaImagen.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(173, 94, 15)));
        jButtonProcesaImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonProcesaImagenMouseClicked(evt);
            }
        });
        jButtonProcesaImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProcesaImagenActionPerformed(evt);
            }
        });

        jButtonCargaImagen.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jButtonCargaImagen.setForeground(new java.awt.Color(173, 94, 15));
        jButtonCargaImagen.setText("CARGAR IMAGEN");
        jButtonCargaImagen.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(173, 94, 15)));
        jButtonCargaImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCargaImagenActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Century Gothic", 2, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(173, 94, 15));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GHOST");

        javax.swing.GroupLayout jPanelResultadoLayout = new javax.swing.GroupLayout(jPanelResultado);
        jPanelResultado.setLayout(jPanelResultadoLayout);
        jPanelResultadoLayout.setHorizontalGroup(
            jPanelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 886, Short.MAX_VALUE)
        );
        jPanelResultadoLayout.setVerticalGroup(
            jPanelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 587, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanelResultado);

        jLabelA.setBackground(new java.awt.Color(255, 153, 51));
        jLabelA.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(173, 94, 15)));
        jLabelA.setOpaque(true);

        jLabelA1.setBackground(new java.awt.Color(255, 153, 51));
        jLabelA1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(173, 94, 15)));
        jLabelA1.setOpaque(true);

        jButtonCargaImagen1.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jButtonCargaImagen1.setForeground(new java.awt.Color(173, 94, 15));
        jButtonCargaImagen1.setText("CARGAR IMAGEN-2");
        jButtonCargaImagen1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(173, 94, 15)));
        jButtonCargaImagen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCargaImagen1ActionPerformed(evt);
            }
        });

        jButtonProcesaImagen1.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jButtonProcesaImagen1.setForeground(new java.awt.Color(173, 94, 15));
        jButtonProcesaImagen1.setText("OCULTAR");
        jButtonProcesaImagen1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(173, 94, 15)));
        jButtonProcesaImagen1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonProcesaImagen1MouseClicked(evt);
            }
        });
        jButtonProcesaImagen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProcesaImagen1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelContentPaneLayout = new javax.swing.GroupLayout(jPanelContentPane);
        jPanelContentPane.setLayout(jPanelContentPaneLayout);
        jPanelContentPaneLayout.setHorizontalGroup(
            jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelContentPaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(193, 193, 193))
            .addGroup(jPanelContentPaneLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelContentPaneLayout.createSequentialGroup()
                        .addGroup(jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelContentPaneLayout.createSequentialGroup()
                                .addComponent(jLabelA, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jProgressBarCargaImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                    .addComponent(jButtonProcesaImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonProcesaImagen1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jButtonCargaImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelA1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonCargaImagen1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanelContentPaneLayout.setVerticalGroup(
            jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContentPaneLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCargaImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCargaImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelContentPaneLayout.createSequentialGroup()
                        .addGroup(jPanelContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelA1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(jPanelContentPaneLayout.createSequentialGroup()
                        .addComponent(jButtonProcesaImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonProcesaImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBarCargaImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelContentPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelContentPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCargaImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCargaImagenActionPerformed
        
         
        
        String userDir = System.getProperty("user.home");
        JFileChooser file = new JFileChooser(userDir +"/Desktop");        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG","jpg");        
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);                
        file.setFileFilter(filter);
        file.setMultiSelectionEnabled(false);
        
        int val = file.showOpenDialog(this);
        
        if( val == JFileChooser.APPROVE_OPTION){
            ImageSelected = file.getSelectedFile();        
            ImageIcon img = new ImageIcon(ImageSelected.toString());                
            Icon icon = new ImageIcon(img.getImage().getScaledInstance(jLabelA.getWidth(),jLabelA.getHeight(), Image.SCALE_DEFAULT));        
            jLabelA.setIcon(icon);      
        }else{
            System.out.println("NO SELECCIONO NINGUNA IMAGEN");
        }
    }//GEN-LAST:event_jButtonCargaImagenActionPerformed

    private void jButtonProcesaImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProcesaImagenActionPerformed
        
        jProgressBarCargaImagen.setValue(0);
        jPanelResultado.removeAll();
        jPanelResultado.repaint();
        
        String userDir = System.getProperty("user.home");
        File direc = new File(userDir +"/Desktop/imagesResultGhost");         
        direc.mkdir();         
                
        long startTime = System.currentTimeMillis();
        
        processFile(ImageSelected);
        
        long endTime = System.currentTimeMillis()-startTime;
        System.out.println("FIN:"+endTime);
        
        int secTime = (int) endTime/1000;
        
        System.out.println("fin:"+secTime);
        
        jProgressBarCargaImagen.setMinimum(0);
        jProgressBarCargaImagen.setMaximum(secTime-1);
        
                
        for(int i=0;i<secTime;i++){            
            try{
                 System.out.println(i+"%");
                 jProgressBarCargaImagen.setValue(i);
                 jProgressBarCargaImagen.setStringPainted(true);
                 Thread.sleep(150);
                 //java.lang.Thread.sleep(5000);
             }catch(Exception e){
             }
        }
                
        ListImages();
        
    }//GEN-LAST:event_jButtonProcesaImagenActionPerformed
    
    private void jButtonProcesaImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonProcesaImagenMouseClicked
        
    }//GEN-LAST:event_jButtonProcesaImagenMouseClicked

    private void jButtonCargaImagen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCargaImagen1ActionPerformed
        String userDir = System.getProperty("user.home");
        JFileChooser file = new JFileChooser(userDir +"/Desktop");        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG","jpg");        
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);                
        file.setFileFilter(filter);
        file.setMultiSelectionEnabled(false);
        
        int val = file.showOpenDialog(this);
        
        if( val == JFileChooser.APPROVE_OPTION){
            ImageSelected2 = file.getSelectedFile();        
            ImageIcon img = new ImageIcon(ImageSelected2.toString());                
            Icon icon = new ImageIcon(img.getImage().getScaledInstance(jLabelA1.getWidth(),jLabelA1.getHeight(), Image.SCALE_DEFAULT));        
            jLabelA1.setIcon(icon);      
        }else{
            System.out.println("NO SELECCIONO NINGUNA IMAGEN");
        }
    }//GEN-LAST:event_jButtonCargaImagen1ActionPerformed

    private void jButtonProcesaImagen1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonProcesaImagen1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonProcesaImagen1MouseClicked

    private void jButtonProcesaImagen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProcesaImagen1ActionPerformed

        String userDir = System.getProperty("user.home");
        File direc = new File(userDir +"/Desktop/imagesResultGhost");         
        direc.mkdir();         
        
        String f1 = getCodeFileHex(ImageSelected);
        String f2 = getCodeFileHex(ImageSelected2);
        String res = f1+f2;
        
        buildNewFiles(res,0000);        
    }//GEN-LAST:event_jButtonProcesaImagen1ActionPerformed
    
    private void ListImages(){
        String userDir = System.getProperty("user.home");
        String sDirectorio = userDir +"/Desktop/imagesResultGhost";
               
        File f = new File(sDirectorio);        
        File[] ficheros;
        
        if ( f.exists() ) {
            ficheros = f.listFiles();
            for (int x = 0; x < ficheros.length; x++) {
                
                System.out.println(ficheros[x].getName());
                                
                ImageIcon img = new ImageIcon(ficheros[x].toString());                
                Icon icon = new ImageIcon(img.getImage().getScaledInstance(jLabelA.getWidth()-20,jLabelA.getHeight()-20, Image.SCALE_DEFAULT));  
                
                JLabel contImagen = new JLabel();
                contImagen.setIcon(icon);
                
                jPanelResultado.add(contImagen);
            }        
        }
        else {
            System.out.println("el directorio no existe");
        }   
    }
    
    public class Imagen extends javax.swing.JPanel{

        private ImageIcon image;

        public Imagen(Dimension d, ImageIcon img) {
            setSize(d);
            image = img;
        }

        public void paint(Graphics grafico) {
            Dimension tamaño = getSize();
            //ImageIcon img = new ImageIcon(getClass().getResource("/images/animales_2.jpg"));
            grafico.drawImage(this.image.getImage(), 0, 0, tamaño.width, tamaño.height, null);
            setOpaque(false);
            super.paintComponent(grafico);
        }
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Test3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Test3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Test3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Test3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Test3().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCargaImagen;
    private javax.swing.JButton jButtonCargaImagen1;
    private javax.swing.JButton jButtonProcesaImagen;
    private javax.swing.JButton jButtonProcesaImagen1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelA;
    private javax.swing.JLabel jLabelA1;
    private javax.swing.JPanel jPanelContentPane;
    private javax.swing.JPanel jPanelResultado;
    private javax.swing.JProgressBar jProgressBarCargaImagen;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}


