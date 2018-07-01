package sm.ajpp.graficos;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

/**
 * Representa una curva usando QuadCurve2D
 * También usa Line2D para el primer paso de dibujo de la curva
 * @author ajpelaez
 */
public class JCurve extends JShape {
    private QuadCurve2D curve;
    private Line2D line;
    private Point2D controlPoint;

    public JCurve(Point2D p) {
        super(p);
        curve = new QuadCurve2D.Double();
        line = new Line2D.Double();
    }

    public void update(Point2D p){
        if (paintingStep == 0){ 
            line.setLine(startPoint, p);
            controlPoint = p;
        }
        else if (paintingStep == 1){
            curve.setCurve(startPoint, controlPoint, p);
            isPainted = true;
            line = null;
        } 
    }
            
    public boolean contains(Point2D p){
        return curve.contains(p);
    }
    
    public void setRelativeDistance(Point2D p){
        relativeDistanceY = p.getY() - curve.getY1();
        relativeDistanceX = curve.getX1() - p.getX();
    }
    
    public void setLocation(Point2D p){
        Point2D p1 = new Point2D.Double( p.getX()+relativeDistanceX,
                p.getY()-relativeDistanceY );

        double dx = p1.getX() - curve.getX1();
        double dy = p1.getY() - curve.getY1();        
        
        Point2D p2 = new Point2D.Double(curve.getX2() + dx, curve.getY2() + dy);
        
        controlPoint = new Point2D.Double(controlPoint.getX() + dx, 
                controlPoint.getY() + dy);
                        
        curve.setCurve(p1,controlPoint,p2);    
    }

    void drawShape(Graphics2D g2d){
        if (line!=null)g2d.draw(line);
        else g2d.draw(curve);
        if (isSelected) selectShape(curve, g2d);
    }
}
