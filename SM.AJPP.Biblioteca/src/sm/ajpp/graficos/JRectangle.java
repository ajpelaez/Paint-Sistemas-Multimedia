package sm.ajpp.graficos;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Representa un rectángulo
 * @author ajpelaez
 */
public class JRectangle extends JShape{
    
    private Rectangle rectangle;
    
    public JRectangle(Point2D p){
        super(p);
        rectangle = new Rectangle();
    }
        
    public void update(Point2D p){
        rectangle.setFrameFromDiagonal(startPoint, p);
        isPainted = true;
    }
    
    public void setRelativeDistance(Point2D p){
        relativeDistanceY = p.getY() - rectangle.getMinY();
        relativeDistanceX = rectangle.getMinX() - p.getX();
    }
    
    public void setLocation(Point2D p){
        Point2D relativePoint = new Point2D.Double( p.getX()+relativeDistanceX,
                p.getY()-relativeDistanceY );

        rectangle.setLocation((int)relativePoint.getX(), (int)relativePoint.getY());
    }
    
    void drawShape(Graphics2D g2d){
        if (filling) fillShape(rectangle, g2d);
        g2d.draw(rectangle);
        if (isSelected) selectShape(rectangle, g2d);
    }
    
    @Override
    public boolean contains(Point2D p) {
        return rectangle.contains(p);
    }
}
