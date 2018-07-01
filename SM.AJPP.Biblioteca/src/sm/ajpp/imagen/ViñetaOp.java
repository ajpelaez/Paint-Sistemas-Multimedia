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
 * Calcula la distancia entre el componente y el centro de la imagen y en función
 * a esta distancia se multiplica el componente por un valor consiguiendo así
 * que los componentes a menor distancia se vean correctamente mientras que los 
 * que estan a mas distancia se vayan viendo más oscuros o totalmente negros
 * @author ajpelaez
 */
public class ViñetaOp extends BufferedImageOpAdapter{
    public ViñetaOp () {}
    
    public BufferedImage filter(BufferedImage src, BufferedImage dest){
        if (src == null) {
            throw new NullPointerException("src image is null");
        }
        if (dest == null) {
            dest = createCompatibleDestImage(src, null);
        }
        WritableRaster srcRaster = src.getRaster();
        WritableRaster destRaster = dest.getRaster();
        int centroImagenX = src.getWidth()/2;
        int centroImagenY = src.getHeight()/2;
        for (int x = 1; x < src.getWidth(); x++)
            for (int y = 1; y < src.getHeight(); y++)
                for (int band = 0; band < srcRaster.getNumBands(); band++) {
                    int sample = srcRaster.getSample(x, y, band);
                    float distanciaX = Math.abs(centroImagenX-x);
                    float distanciaY = Math.abs(centroImagenY-y);
                    float factor = (1.0f-(distanciaX/centroImagenX)) * (1.0f-(distanciaY/centroImagenY));
                    sample = (int)(sample*factor);
                    destRaster.setSample(x, y, band, sample);
                }
            
        return dest;
    }
}
