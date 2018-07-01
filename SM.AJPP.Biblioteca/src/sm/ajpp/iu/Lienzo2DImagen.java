package sm.ajpp.iu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.WritableRaster;
import sm.ajpp.imagen.AumentaColorOp;
import sm.ajpp.imagen.ViñetaOp;
import sm.ajpp.imagen.SepiaOp;
import sm.image.LookupTableProducer;
import sm.image.KernelProducer;
import sm.image.color.GreyColorSpace;

/**
 *
 * @author ajpelaez
 * @version 1.0
 * Extiende la clase Lienzo2D y le agrega funcionalidad para manejar imágenes
 */
public class Lienzo2DImagen extends Lienzo2D{
    /**
    * Imagen que contendrá el lienzo
    */
    private BufferedImage img;
    
    public void setImage(BufferedImage img){
        this.img = img;
        if(img!=null) {
            setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
            this.repaint();
        }
        
        this.clip = new Rectangle(0,0,img.getWidth(),img.getHeight());
    }
    
    public BufferedImage getImage(){
        return img;
    }
    
    /**
    * Devuelve una copia de la imagen o la imagen
    * @param drawVector En caso de ser true devolveremos una copia de la imagen
    * @return imagen del lienzo o copia de esta
    */
    public BufferedImage getImage(boolean drawVector){
        if (drawVector) {
            BufferedImage imgConDibujado = deepCopy(img);
            Graphics2D g2d = (Graphics2D)imgConDibujado.getGraphics();
            paint(g2d);
            return imgConDibujado;
        }
        else{
            return getImage();
        }
    }
    
    /**
    * Escala el tamaño de la imagen
    * @param scaleSize Indica por cuanto se multiplica el tamaño de la imagen
    */
    public void scaleImage(double scaleSize){
        AffineTransform at = AffineTransform.getScaleInstance(scaleSize, scaleSize);
        AffineTransformOp atop;
        atop = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
        img = atop.filter(img, null);
        this.clip = new Rectangle(0,0,img.getWidth(),img.getHeight());
        this.repaint();        
    }
    
    /**
    * Rota la imagen
    * @param rotationGrades Indica cuantos grados se rotará la imagen
    */
    public void rotateImage(int rotationGrades){
        double r = Math.toRadians(rotationGrades);
        Point p = new Point(img.getWidth()/2, img.getHeight()/2);
        AffineTransform at = AffineTransform.getRotateInstance(r,p.x,p.y);
        AffineTransformOp atop;
        atop = new AffineTransformOp(at,AffineTransformOp.TYPE_BILINEAR);
        img = atop.filter(img, null);
        this.repaint();
    }
    
    /**
    * Aplica contraste a la imagen
    * @param contrastType Indica el tipo de contraste que se aplicará a la imagen
    */
    public void applyContrast(int contrastType){
        if (img!=null){
            try{
                LookupTable lt = LookupTableProducer.createLookupTable(contrastType);
                LookupOp lop = new LookupOp(lt, null);
                lop.filter( img , img);
                this.repaint();
                } catch(Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
        }
    }
    
    /**
    * Aplica la funcion seno a la imagen
    * @param w Grados usados para calcular la tabla de la función seno
    */
    public void applySinus(double w){
        if (img!=null){
            try{
                LookupTable lt = sinusTable(w);
                LookupOp lop = new LookupOp(lt, null);
                lop.filter( img , img);
                this.repaint();
                } catch(Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
        }        
    }

    /**
    * Aplica la funcion negativo a la imagen
    */
    public void aplicarNegativo(){
        if (img!=null){
            try{
                LookupTable lt = negativeTable();
                LookupOp lop = new LookupOp(lt, null);
                lop.filter( img , img);
                this.repaint();
                } catch(Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
        }          
    }
    
    /**
    * Aplica una función de intensificación a la imagen
    * @param valorTono Valor de los tonos que se intensificarán (0-255)
    * @param img Imagen usada para aplicar la operación antes de guardar cambios
    */
    public void applyIntensification(int valorTono, BufferedImage img){
        if (img!=null){
            try{
                LookupTable lt = intensificationTable(valorTono);
                LookupOp lop = new LookupOp(lt, null);
                lop.filter( img , this.img);
                this.repaint();
                } catch(Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
        }          
    }
    
    /**
    * Rellena la imagne de blanco
    */
    public void imageFillWhite(){
        Graphics2D g2d = img.createGraphics();
        int alto = img.getHeight();
        int ancho = img.getWidth();
        g2d.setPaint(Color.white);
        Rectangle rectangulo = new Rectangle(0,0,ancho,alto);
        g2d.fill(rectangulo);
        g2d.draw(rectangulo);        
    }
    
    /**
    * Aplica un filtro a la imagen
    * @param filterType Tipo de filtro que se aplicara a la imagen
    */
    public void applyFilter(int filterType){
        Kernel k = KernelProducer.createKernel(filterType);
        ConvolveOp cop = new ConvolveOp(k,ConvolveOp.EDGE_NO_OP,null);
        img = cop.filter(img, null);
        this.repaint();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(img!=null) {
            g.drawImage(img,0,0,this);

        }
    }
    
    /**
    * Devuelve una copia de la imagen
    * @param bi Imagen que será copiada
    * @return copia de la imagen bi
    */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    /**
    * Crea la tabla para la función seno
    * @param w Grados con los que se calculará la función
    * @return tabla con los valores para aplicar la función seno
    */
    public LookupTable sinusTable(double w){
        double K = 255.0; // Cte de normalización
        // Código implementado f(x)=|sin(wx)|
        byte lt[] = new byte[256];
        for (int l=0; l<256; l++){
            lt[l] = (byte)(K/(Math.abs(Math.sin(Math.toRadians(w*l)))));
        }
        ByteLookupTable slt = new ByteLookupTable(0,lt);
        return slt;
        
    }
    
    /**
    * Crea la tabla para la función negativo
    * @return tabla con los valores para aplicar el negativo
    */
    public LookupTable negativeTable(){
        byte lt[] = new byte[256];
        for (int l=0; l<256; l++){
            lt[l] = (byte)(255-l);
        }
        ByteLookupTable slt = new ByteLookupTable(0,lt);
        return slt;
    }
    
    /**
    * Crea la tabla para la función de intensificación
    * @param toneValue Valor de los tonos a los que se le aplicará la intensificación
    * @return tabla con los valores de intensificación
    */
    public LookupTable intensificationTable(int toneValue){
        byte lt[] = new byte[256];
        for (int l=0; l<256; l++){
            lt[l] = (byte)l;
        }
        for (int l=toneValue-40; l<=toneValue+40; l++){
            lt[l] = (byte)Math.min(255,(l*1.25));
        }
        ByteLookupTable slt = new ByteLookupTable(0,lt);
        return slt;
    }
    
    /**
    * Extrae una banda de la imagen
    * @param band banda que se extraera
    * @return imagen con de la banda extraida
    */
    public BufferedImage extractBand(int band){
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ComponentColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        int bandList[] = {band};
        WritableRaster bandRaster = (WritableRaster)img.getRaster().createWritableChild(0,0, img.getWidth(), img.getHeight(), 0, 0, bandList);
        BufferedImage imgBanda = new BufferedImage(cm, bandRaster, false, null);
        return imgBanda;
    }
    
    /**
    * Devuelve una imagen con un nuevo espacio de color
    * @param colorSpace Espacio de color para la nueva imagen
    * @return imagen en el espacio de color previamente definido
    */
    public BufferedImage newColorSpace(String colorSpace){
        BufferedImage imgOut;
        if(colorSpace == "YCC"){
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_PYCC);
            ColorConvertOp cop = new ColorConvertOp(cs, null);
            System.out.print(colorSpace);
            imgOut = cop.filter(img, null);            
        }
        else if(colorSpace == "GREY"){
            ColorSpace cs = new GreyColorSpace();
            ColorConvertOp cop = new ColorConvertOp(cs, null);
            System.out.print(colorSpace);
            imgOut = cop.filter(img, null);            
        }
        else{
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            ColorConvertOp cop = new ColorConvertOp(cs, null);
            System.out.print(colorSpace);
            imgOut = cop.filter(img, null);                
        }
        return imgOut;
    }
    
    /**
    * Aplica una operación a la imagen
    * @param opType Tipo de operación que se le aplicará a la imagen
    * @return Imagen con la operación aplicada
    */
    public BufferedImage applyOp(String opType){
        BufferedImage imgOut = null;
        if (opType == "SEPIA"){
            SepiaOp op = new SepiaOp();
            imgOut = op.filter(img, null);
        }
        if (opType == "VIÑETA"){
            ViñetaOp op = new ViñetaOp();
            imgOut = op.filter(img, null);
        }
        if (opType == "AUMENTA_COLOR"){
            AumentaColorOp op = new AumentaColorOp();
            imgOut = op.filter(img, null);
        }        
        return imgOut;
    }
    
    
}
