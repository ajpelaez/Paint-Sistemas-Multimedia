/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.ajpp.imagen;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import static java.lang.Math.min;
import sm.image.BufferedImageOpAdapter;

/**
 *
 * @author ajpelaez
 */
public class SepiaOp extends BufferedImageOpAdapter{
    public SepiaOp () {}
    
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
                pixelComp[0] = (int) min(255 , 0.393*componentR + 0.769*componentG + 0.189*componentB);
                pixelComp[1] = (int) min(255, 0.349*componentR + 0.686*componentG + 0.168*componentB);
                pixelComp[2] = (int) min(255, 0.272*componentR + 0.534*componentG + 0.131*componentB);
                destRaster.setPixel(x, y, pixelComp);
            }
        }
    return dest;
    }
}
