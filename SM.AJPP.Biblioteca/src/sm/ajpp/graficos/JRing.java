package sm.ajpp.graficos;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Representa un anillo calculado usando 2 areas de un ovalo
 * @author ajpelaez
 */
public class JRing extends JShape {
    /**
    * Ovalo que se usara para generar las areas
    */
    private Ellipse2D ellipse;
    
    /**
    * Area del anillo exterior 
    */
    private Area externalRing;
    
    /**
    * Area del anillo interior 
    */ 
    private Area internalRing;

    public JRing(Point2D p) {
        super(p);
        ellipse = new Ellipse2D.Double();
        externalRing = new Area(ellipse);
        internalRing = new Area(ellipse);
    }

    public void update(Point2D p){
        ellipse.setFrameFromDiagonal(startPoint, p);
        externalRing = new Area(ellipse);
        double x, y;
    
        
        if( p.getX() > startPoint.getX() )
            x = (p.getX()-startPoint.getX())/5;
        else x = (p.getX()-startPoint.getX())/5;

        
        if( p.getY() > startPoint.getY() )
            y = (p.getY()-startPoint.getY())/5;
        else y = (p.getY()-startPoint.getY())/5;
        
        Point2D p1 = new Point2D.Double(startPoint.getX()+x, startPoint.getY()+y); 
        Point2D p2 = new Point2D.Double(p.getX()-x, p.getY()-y); 
        
        ellipse.setFrameFromDiagonal(p1, p2);
        internalRing = new Area(ellipse);
        externalRing.subtract(internalRing);
        isPainted = true;
    }
            
    public boolean contains(Point2D p){
        return externalRing.contains(p);
    }
    
    public void setRelativeDistance(Point2D p){
        relativeDistanceY = p.getY() - externalRing.getBounds2D().getY();
        relativeDistanceX = externalRing.getBounds2D().getX() - p.getX();
    }
    
    public void setLocation(Point2D p){
        Point2D p1 = new Point2D.Double( p.getX()+relativeDistanceX,
                p.getY()-relativeDistanceY );
        
        double dx = p1.getX() - externalRing.getBounds2D().getX();
        double dy = p1.getY() - externalRing.getBounds2D().getY();
        
        externalRing.transform(AffineTransform.getTranslateInstance(dx,dy));
    }

    void drawShape(Graphics2D g2d){
        if (filling) fillShape(externalRing, g2d);
        g2d.draw(externalRing);
        if (isSelected) selectShape(externalRing, g2d);
    }
}
