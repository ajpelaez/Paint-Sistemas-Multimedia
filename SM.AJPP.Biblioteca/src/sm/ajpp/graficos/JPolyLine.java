package sm.ajpp.graficos;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * Representa trazo libre usando GeneralPath
 * @author ajpelaez
 */
public class JPolyLine extends JShape {
    private GeneralPath polyline;

    public JPolyLine(Point2D p) {
        super(p);
        polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        polyline.moveTo(p.getX(), p.getY());
    }

    public void update(Point2D p){
        polyline.lineTo(p.getX(), p.getY());
        isPainted = true;
    }
            
    public boolean contains(Point2D p){
        return polyline.contains(p);
    }
    
    public void setRelativeDistance(Point2D p){
        relativeDistanceY = p.getY() - polyline.getBounds2D().getY();
        relativeDistanceX = polyline.getBounds2D().getX() - p.getX();
    }
    
    public void setLocation(Point2D p){
        Point2D p1 = new Point2D.Double( p.getX()+relativeDistanceX,
                p.getY()-relativeDistanceY );
        
        double dx = p1.getX() - polyline.getBounds2D().getX();
        double dy = p1.getY() - polyline.getBounds2D().getY();
        
        polyline.transform(AffineTransform.getTranslateInstance(dx,dy));
    }

    void drawShape(Graphics2D g2d){
        g2d.draw(polyline);
        if (isSelected) selectShape(polyline, g2d);
    }
}
