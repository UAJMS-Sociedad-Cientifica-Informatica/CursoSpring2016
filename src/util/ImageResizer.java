package util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
 
public class ImageResizer {
 
    /*ESTE MÉTODO ES EL DE LA MAGIA RECIBE LA RUTA AL ARCHIVO ORIGINAL Y LA RUTA DONDE VAMOS A GUARDAR LA COPIA
    copyImage("C:\\Users\\IngenioDS\\Desktop\\test.png","C:\\Users\\IngenioDS\\Desktop\\Copia\\test2.png");*/
 
    public void copyImage(String filePath, String copyPath, int MAX_WIDTH, int MAX_HEIGHT) {
        BufferedImage bimage = loadImage(filePath);
        if(bimage.getHeight() > MAX_HEIGHT || bimage.getWidth() > MAX_WIDTH){
        	if(bimage.getHeight()>bimage.getWidth()){
                int heigt = (bimage.getHeight() * MAX_WIDTH) / bimage.getWidth();
                bimage = resize(bimage, MAX_WIDTH, heigt);
                int width = (bimage.getWidth() * MAX_HEIGHT) / bimage.getHeight();
                bimage = resize(bimage, width, MAX_HEIGHT);
            }else{
                int width = (bimage.getWidth() * MAX_HEIGHT) / bimage.getHeight();
                bimage = resize(bimage, width, MAX_HEIGHT);
                int heigt = (bimage.getHeight() * MAX_WIDTH) / bimage.getWidth();
                bimage = resize(bimage, MAX_WIDTH, heigt);
            }
        }
        saveImage(bimage, copyPath);
    }
     
    /*
    ESTE MÉTODO SE UTILIZA PARA CARGAR LA IMAGEN DE DISCO
    */
    private BufferedImage loadImage(String pathName) {
        BufferedImage bimage = null;
        try {
            bimage = ImageIO.read(new File(pathName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bimage;
    }
 
    /*
    ESTE MÉTODO SE UTILIZA PARA ALMACENAR LA IMAGEN EN DISCO
    */
    private void saveImage(BufferedImage bufferedImage, String pathName) {
        try {
            String format = (pathName.endsWith(".png")) ? "png" : "jpg";
            File file =new File(pathName);
            file.getParentFile().mkdirs();
            ImageIO.write(bufferedImage, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    /*
    ESTE MÉTODO SE UTILIZA PARA REDIMENSIONAR LA IMAGEN
    */
    private BufferedImage resize(BufferedImage bufferedImage, int newW, int newH) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        BufferedImage bufim = new BufferedImage(newW, newH, bufferedImage.getType());
        Graphics2D g = bufim.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(bufferedImage, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return bufim;
    }

}