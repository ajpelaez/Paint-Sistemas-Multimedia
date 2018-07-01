package sm.ajpp.iu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sm.ajpp.graficos.JCurve;
import sm.ajpp.graficos.JEllipse;
import sm.ajpp.graficos.JLine;
import sm.ajpp.graficos.JPolyLine;
import sm.ajpp.graficos.JRectangle;
import sm.ajpp.graficos.JRing;
import sm.ajpp.graficos.JShape;

/**
 *
 * @author ajpelaez
 * @version 1.0
 * Extiende de JPanel, clase utilizada como panel para dibujar
 */
public class Lienzo2D extends javax.swing.JPanel {
    /**
     * Tipo de figura seleccionada para dibujar
     */
    Figure figureToPaint = Figure.LINE;

    /**
     * Booleano que indica si se rellenara la figura o no
     */
    boolean filling = false;
    
    /**
    * Booleano que indica si se rellenara la figura con degradado
    */
    boolean gradientFilling = false;
    
    /**
    * Color con el que se pintara la figura
    */
    Color color = Color.BLACK;
    
    /**
    * Segundo color que sera usado para pintar el degradado
    */
    Color secondColor = Color.BLACK;
    
    /**
    * Booleano que indica si estamos editando figuras o no
    */
    boolean editAction = false;
    
    /**
    * Trazo con el que se pintara la figura
    */
    Stroke stroke = stroke = new BasicStroke(1.0f); 
    
    /**
    * Tamaño del trazo con el que se pintara la figura 
    */
    float strokeSize = 1.0f;
    
    /**
    * Booleano que indica si la figura tendrá o no transparencia 
    */
    boolean transparency = false;
    
    /**
    * Booleano que indica si la figura tendrá alisamiento de bordes o no
    */
    boolean antialiasing = false;
    
    /**
    * Grado de transparencia con el que se pintará la figura 
    */
    float transparencyGrade = 0.5f;
    
    /**
    * Indica el tipo de trazo con el que se pintara la figura "Continuo/Discontinuo"
    */
    String strokeType = "Continuo";
    
    /**
    * Booleano que indica si estamos dibujando una figura en el momento actual
    */
    boolean paintingFigure = false;
    
    /**
    * Vector que contiene la lista de figuras a dibujar
    */
    List<JShape> shapes = new ArrayList();
    
    /**
    * Figura que se va a dibujar
    */
    JShape shapeToPaint;
    
    /**
    * Figura que se va a editar
    */
    JShape shapeToEdit;
    
    /**
    * Indica el area de recorte del lienzo
    */
    Shape clip;
    
    /**
    * Crea un nuevo lienzo
    */
    public Lienzo2D() {
        initComponents();
    }
    
    public void setGradientFilling(boolean gradientFilling){
        this.gradientFilling = gradientFilling;
        updateShapeAttributes();
    }
    
    public boolean getGradientFilling(){
        return gradientFilling;
    }
    
    public void setSecondColor(Color color){
        secondColor = color;
        updateShapeAttributes();
    }
    
    public Color getSecondColor(){
        return secondColor;
    }
    
    public void setFigureToPaint(Figure figure){
        this.figureToPaint = figure;
    }
    
    public Figure getFigureToPaint(){
        return figureToPaint;
    }
    
    public void setColor(Color color){
        this.color = color;
        updateShapeAttributes();
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setFilling(boolean relleno){
        this.filling = relleno;
        updateShapeAttributes();
    }
    
    public boolean getFilling(){
        return filling;
    }
    
    public void setEditAction(boolean editAction){
        this.editAction = editAction;
        if (!editAction) unSelectShapes();
    }
    
    public boolean getEditAction(){
        return editAction;
    }

    public void setStroke(Float strokeSize){
        this.strokeSize = strokeSize;
        updateShapeAttributes();
    }

    public int getStroke(){
        return (int)strokeSize;
    }
    
    public void setStrokeType(String strokeType){
        this.strokeType = strokeType;
        updateShapeAttributes();
    }
    
    public String getStrokeType(){
        return strokeType;
    }
    
    public void setTransparency(boolean transparencia){
        this.transparency = transparencia;
        updateShapeAttributes();
    }
    
    public boolean getTransparency(){
        return transparency;
    }
    
    public void setTransparencyGrade(int transparencyGrade){
        this.transparencyGrade = transparencyGrade/100.0f;
        updateShapeAttributes();
    }
    
    public int getTransparencyGrade(){
        return (int)(transparencyGrade*100);
    }
    
    public void setAntialiasing(boolean antialiasing){
        this.antialiasing = antialiasing;
        updateShapeAttributes();
    }
    
    public boolean getAntialiasing(){
        return antialiasing;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(400, 400));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if (editAction) shapeToEdit = getSelectedShape(evt.getPoint());
        else if (paintingFigure) updateShape(evt.getPoint());
        else createShape(evt.getPoint());
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if ((editAction)&&(shapeToEdit != null)) moveShape(evt.getPoint());
        else if (!editAction) updateShape(evt.getPoint());
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if ((editAction)&&(shapeToEdit != null)) moveShape(evt.getPoint());
        else if (!editAction){ 
            updateShape(evt.getPoint());
            if (shapeToPaint.isPainted()){
                shapeToPaint = null;
                paintingFigure = false;
            }
            else{
                shapeToPaint.setPaintingStep(shapeToPaint.getPaintingStep()+1);
                paintingFigure = true;
            }
        }
    }//GEN-LAST:event_formMouseReleased

    /**
    * Crea una nueva figura en funcion de la figura que haya elegida para pintar
    * crea dicha figura a partir de un punto
    * @param startPoint Punto desde el que se empezará a dibujar la figura
    */
    private void createShape(Point2D startPoint){
        if (figureToPaint == Figure.LINE) shapeToPaint = new JLine(startPoint);
        else if (figureToPaint == Figure.RECTANGLE) shapeToPaint = new JRectangle(startPoint);
        else if (figureToPaint == Figure.OVAL) shapeToPaint = new JEllipse(startPoint);
        else if (figureToPaint == Figure.CURVE) shapeToPaint = new JCurve(startPoint);
        else if (figureToPaint == Figure.POLYLINE) shapeToPaint = new JPolyLine(startPoint);
        else if (figureToPaint == Figure.RING) shapeToPaint = new JRing(startPoint);

        
        shapeToPaint.setAttributes(color, strokeSize, transparency, antialiasing,
                filling, transparencyGrade, strokeType, secondColor, gradientFilling);
        shapes.add(shapeToPaint);
    }
    
    /**
    * Sigue dibujando la figura actualizandola con un nuevo punto
    * @param p Nuevo punto que se agregará a la figura
    */
    private void updateShape(Point2D p){
        shapeToPaint.update(p);
        this.repaint();
    }
    
    /**
    * Obtiene la figura seleccionada en el lienzo deseleccionando todas las demas
    * @param p Punto donde se espera encontrar la figura
    * @return Figura encontrada en dicho punto
    */
    private JShape getSelectedShape(Point2D p){
        unSelectShapes();
        for(JShape shape:shapes)
            if(shape.contains(p)){
                shape.setRelativeDistance(p);
                shape.setSelected(true);
                return shape;
            }
        return null;
    }
    
    /**
    * Deselecciona todas las figuras del lienzo
    */
    private void unSelectShapes(){
        for(JShape shape:shapes)
            shape.setSelected(false);
        this.repaint();
    }
    
    /**
    * Actualiza los atributos de la figura que esta siendo editada
    */
    private void updateShapeAttributes(){
        if (shapeToEdit!=null){
            shapeToEdit.setAttributes(color, strokeSize, transparency, 
                    antialiasing, filling, transparencyGrade, strokeType, secondColor, gradientFilling);
            this.repaint();
        }
    }
    
    /**
    * Mueve la figura que esta siendo editada a una nueva posicion
    * @param p Nuevo punto donde se moverá la figura
    */
    private void moveShape(Point2D p){
        shapeToEdit.setLocation(p);
        this.repaint();        
    }
    
    /**
    * Cambia la posicion de la figura en el vector de figuras
    * @param position Nueva posición que tendrá la figura en el vector (1, -1) una posición mas o menos
    */
    public void moveShapePosition(int position){
        if (shapeToEdit!=null){
            try{
                int currentPosition = shapes.indexOf(shapeToEdit);
                Collections.swap(shapes, currentPosition, currentPosition+position);
                this.repaint();
            } catch (java.lang.ArrayIndexOutOfBoundsException ex){
                //Do nothing
            }
        }            
    }
    
    /**
    * Mueve la figura que esta siendo editada a la ultima posicion 
    * del vector de figuras
    */
    public void moveShapeToLastPosition(){
        if (shapeToEdit!=null){
            shapes.remove(shapeToEdit);
            shapes.add(0, shapeToEdit);
            this.repaint();
        }
    }
    
    /**
    * Mueve la figura que esta siendo editada a la primera posicion 
    * del vector de figuras
    */
    public void moveShapeToFirstPosition(){
        if (shapeToEdit!=null){
            shapes.remove(shapeToEdit);
            shapes.add(shapeToEdit);
            this.repaint();
        }        
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        
        if (clip!=null){
            g2d.setClip(clip);
        }
        
        for(JShape shape:shapes){
            shape.paint(g2d);
        }  
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
