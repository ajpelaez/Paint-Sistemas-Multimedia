/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.ajpp.imagen;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;

/**
 * De los 3 componentes RGB busca el componente con más valor y lo aumenta 
 * consiguiendo asi un aumento del color
 * @author ajpelaez
 */
public class AumentaColorOp extends BufferedImageOpAdapter{
    public AumentaColorOp () {}
    
    public BufferedImage filter(BufferedImage src, BufferedImage dest){
        if (src == null) {
            throw new NullPointerException("src image is null");
        }
        if (dest == null) {
            dest = createCompatibleDestImage(src, null);
        }   
        WritableRaster srcRaster = src.getRaster();
        WritableRaster destRaster = dest.getRaster();
        
        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                int[] pixelComp=null;
                pixelComp = srcRaster.getPixel(x, y, pixelComp);
                int componentR = pixelComp[0];
                int componentG = pixelComp[1];
                int componentB = pixelComp[2];
                
                if((componentR>componentG)&&(componentR>componentB)){
                    pixelComp[0] = (int) Math.min(255,componentR*1.25);
                }
                else if((componentG>componentR)&&(componentG>componentB)){
                    pixelComp[1] = (int) Math.min(255,componentG*1.25);
                }
                else{
                    pixelComp[2] = (int) Math.min(255,componentB*1.25);                    
                }

                destRaster.setPixel(x, y, pixelComp);
            }
        }
    return dest;
    }
}
