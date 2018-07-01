package sm.ajpp.graficos;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Representa una linea
 * @author ajpelaez
 */
public class JLine extends JShape {
    private Line2D line;

    public JLine(Point2D p) {
        super(p);
        line = new Line2D.Double();
    }

    public void update(Point2D p){
        line.setLine(startPoint, p);
        isPainted = true;
    }
            
    public boolean contains(Point2D p){
        return line.ptLineDist(p)<=2.0;
    }
    
    public void setRelativeDistance(Point2D p){
        relativeDistanceY = p.getY() - line.getY1();
        relativeDistanceX = line.getX1() - p.getX();
    }
    
    public void setLocation(Point2D p){
        
        Point2D p1 = new Point2D.Double( p.getX()+relativeDistanceX,
                p.getY()-relativeDistanceY );

        double dx = p1.getX() - line.getX1();
        double dy = p1.getY() - line.getY1();        
        
        Point2D p2 = new Point2D.Double(line.getX2() + dx, line.getY2() + dy);
        line.setLine(p1,p2);
    }

    void drawShape(Graphics2D g2d){
        g2d.draw(line);
        if (isSelected) selectShape(line, g2d);
    }
}
