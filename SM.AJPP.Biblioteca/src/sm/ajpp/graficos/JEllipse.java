package sm.ajpp.graficos;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Representa un ovalo
 * @author ajpelaez
 */
public class JEllipse extends JShape{
    
    private Ellipse2D ellipse;
    
    public JEllipse(Point2D p){
        super(p);
        ellipse = new Ellipse2D.Double();
    }
    
    public void update(Point2D p){
        ellipse.setFrameFromDiagonal(startPoint, p);
        isPainted = true;
    }
    
    public void setRelativeDistance(Point2D p){
        relativeDistanceY = p.getY() - ellipse.getMinY();
        relativeDistanceX = ellipse.getMinX() - p.getX();
    }
    
    public void setLocation(Point2D p){
        Point2D relativePoint = new Point2D.Double( p.getX()+relativeDistanceX,
                p.getY()-relativeDistanceY );

        int height = (int)ellipse.getHeight();
        int width = (int)ellipse.getWidth();
        Dimension dimension = new Dimension(width, height);
        ellipse.setFrame(relativePoint, dimension);
    }
    
    void drawShape(Graphics2D g2d){
        if (filling) fillShape(ellipse, g2d);
        g2d.draw(ellipse);
        if (isSelected) selectShape(ellipse, g2d);

    } 
    
    @Override
    public boolean contains(Point2D p) {
        return ellipse.contains(p);
    }
}
