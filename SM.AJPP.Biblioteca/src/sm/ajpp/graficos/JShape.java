package sm.ajpp.graficos;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * Extiende de shape y contiene nuevos atributos y funcionalidad para las figuras
 * @author ajpelaez
 */
public abstract class JShape implements Shape{
    /**
    * Composite usado para la transparencia
    */
    Composite transparencyComp;
    /**
    * Composite usado para cuando no hay transparencia
    */
    static final Composite NORMAL_COMP =
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
    /**
    * RenderingHints usado para alisado de bordes
    */
    static final RenderingHints ANTIALIASING_RENDER = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    /**
    * Separación entre líneas
    */
    final static float dash1[] = {10.0f};
    
    /**
    * BasicStroke para trazo discontinuo
    */
    final static BasicStroke dashedStroke = new BasicStroke(1.0f, 
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    
    /**
    * Color con el que se pintará la figura
    */
    Color color;
    
    /**
    * Segundo color usado para el degradado
    */
    Color secondColor;
    
    /**
    * Trazo con el que se pintara la figura
    */
    Stroke stroke;
    
    /**
    * Tamaño de trazo
    */
    float strokeSize;
    
    /**
    * Indica si la figura tendrá transparencia o no
    */
    boolean transparency;
    
    /**
    * Indica si la figura tendrá alisado de bordes o no
    */
    boolean antialiasing;
    
    /**
    * Indica si la figura se rellenará o no
    */
    boolean filling;

    /**
    * Punto donde se empieza a dibujar la figura
    */
    Point2D startPoint;
    
    /**
    * Paso por el que vamos dibujando la figura
    */
    int paintingStep;
    
    /**
    * Distancia x relativa desde el punto donde se hace click y la esquina 
    * superior izquierda de la figura
    */
    double relativeDistanceX;
    
    /**
    * Distancia y relativa desde el punto donde se hace click y la esquina 
    * superior izquierda de la figura
    */
    double relativeDistanceY;
    
    /**
    * Indica si la figura esta seleccionada para edicción o no
    */
    boolean isSelected;
    
    /**
    * Indica si la figura esta totalmente pintada
    */
    boolean isPainted;
    
    /**
    * India si se usara degradado para el relleno
    */
    boolean gradientFilling;
    
    
    /**
    * Crea una nueva figura a partir de un punto, esta no estará pintada aún
    * y el paso de dibujo sera el 0
    * @param p punto donde empezará a dibujarse la figura
    */
    public JShape(Point2D p){
        startPoint = p;
        paintingStep = 0;
        isPainted = false;
    }
    
    /**
    * Actualiza la figura con un nuevo punto a dibujar 
    * @param p punto que se agregará a la figura
    */
    public abstract void update(Point2D p);
    
    /**
    * Cambia la localización de la figura a un nuevo punto
    * @param p nuevo punto donde se localizará la figura
    */
    public abstract void setLocation(Point2D p);
    
    /**
    * Fija la distancia relativa de la figura hasta el punto recibido
    * @param p punto desde donde se ha hecho click para seleccionar la figura
    */
    public abstract void setRelativeDistance(Point2D p);
    
    /**
    * Dibuja la figura en el Graphics2D recibido como parámetro
    * @param g2d Graphics donde se pintará la figura
    */
    abstract void drawShape(Graphics2D g2d);
    
    /**
    * Fija los atributos que tendrá la figura 
    * @param color color con el que se pintará
    * @param strokeSize tamaño de trazo
    * @param transparency indica si tendrá transparencia 
    * @param antialiasing indica si tendrá alisado de bordes
    * @param filling indica si se debe rellenar
    * @param transparencyGrade grado de transparencia
    * @param strokeType tipo de trazo (continuo, punteado)
    * @param secondColor segundo color usado para el degradado
    * @param gradientFilling indica si se usará degradado para el relleno
    */
    public void setAttributes(Color color, float strokeSize, 
            boolean transparency, boolean antialiasing, boolean filling, 
            float transparencyGrade, String strokeType, Color secondColor, boolean gradientFilling){
        
        this.color = color;
        this.strokeSize = strokeSize;
        this.transparency = transparency;
        this.antialiasing = antialiasing;
        this.filling = filling;
        this.gradientFilling = gradientFilling;
        this.secondColor = secondColor;
        this.transparencyComp = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, transparencyGrade);
        
        if (strokeType == "Continuo") this.stroke = new BasicStroke(strokeSize);
        else this.stroke = new BasicStroke(strokeSize, BasicStroke.CAP_BUTT, 
                BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    }
    
    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }
    
    public int getPaintingStep(){
        return paintingStep;
    }
    
    public void setPaintingStep(int paintingStep){
        this.paintingStep = paintingStep;
    }
    
    public boolean isPainted(){
        return isPainted;
    }
    
    /**
    * Rellena la figura con o sin degradado
    * @param shape figura que se rellenará
    * @param g2d Graphics donde se pintará la figura rellenada
    */
    void fillShape(Shape shape, Graphics2D g2d){
        if (gradientFilling){
            Rectangle bounds = shape.getBounds();
            Point p1 = new Point(bounds.x, bounds.y);
            Point p2 = new Point((int)bounds.getMaxX(), bounds.y);
            g2d.setPaint(new GradientPaint(p1, color, p2, secondColor));
        }
        g2d.fill(shape);  
    }
    
    /**
    *  Pinta la linea discontina de selección de la figura 
    * @param shape figura alrededor de la cual se pintará la lina de selección
    * @param g2d Graphics donde se pintará la linea de selección
    */
    void selectShape(Shape shape, Graphics2D g2d){
        g2d.setStroke(dashedStroke);
        Rectangle bounds = shape.getBounds();
        int strokeWidth = (int)strokeSize/2;
        bounds.setLocation(bounds.x-(5+strokeWidth), bounds.y-(5+strokeWidth));
        bounds.setSize(bounds.width+10+(int)strokeSize, bounds.height+10+(int)strokeSize);
        g2d.draw(bounds);
    }
    
    public void paint(Graphics2D g2d){
        g2d.setPaint(color);
        g2d.setStroke(stroke);
        if (transparency) g2d.setComposite(transparencyComp);
        else g2d.setComposite(NORMAL_COMP);
        if (antialiasing) g2d.setRenderingHints(ANTIALIASING_RENDER);
        drawShape(g2d);
    }    

   @Override
    public Rectangle getBounds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Rectangle2D getBounds2D() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(Rectangle2D r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
