import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class Line {
    private final List<Point> points = new ArrayList<>();
    private Color color;
    private int stroke;

    public Line() {
    }

    public void setPoint(Point p) {
        this.points.add(p);
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public Color getColor() {
        return this.color;
    }

    public int getStroke() {
        return this.stroke;
    }
}

public class Paint extends JPanel {
    private final static List<Line> lines = new ArrayList<>();
    private Line currentLine;
    private int lineStroke = 3;
    private static final String[] colorNames = { "Rojo", "Negro", "Azul", "Verde", "Amarillo", "Gris", "BORRADOR" };
    private static final Color[] colors = { Color.RED, Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.GRAY,
            Color.WHITE };
    private Color currentColor = Color.BLACK;
    private static final String[] lineTypes = { "Libre", "Línea recta", "Rectángulo", "Círculo" };
    private String currentShapeType = "Libre";
    private Point startPoint, endPoint;

    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onMouseReleased(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseDragged(e);
            }
        });
    }

    /**
     * cuando se presiona el ratón
     * 
     * @param e evento de mouse
     */
    private void onMousePressed(MouseEvent e) {
        startPoint = e.getPoint();
        if (currentShapeType.equals("Libre")) {
            currentLine = new Line();
            currentLine.setPoint(startPoint);
            currentLine.setStroke(lineStroke);
            currentLine.setColor(currentColor);
            lines.add(currentLine);
        }
    }

    /**
     * cuando se libera el ratón
     * 
     * @param e evento de mouse
     */
    private void onMouseReleased(MouseEvent e) {
        endPoint = e.getPoint();
        if (!currentShapeType.equals("Libre")) {
            currentLine = new Line();
            currentLine.setStroke(lineStroke);
            currentLine.setColor(currentColor);
            drawShape();
            lines.add(currentLine);
            repaint();
        }
    }

    /**
     * cuando se mueve el ratón
     * 
     * @param e evento de mouse
     */
    private void onMouseDragged(MouseEvent e) {
        if (currentShapeType.equals("Libre")) {
            currentLine.setPoint(e.getPoint());
            repaint();
        }
    }

    /**
     * dibuja la forma seleccionada
     */
    private void drawShape() {
        if (currentShapeType.equals("Línea recta")) {
            currentLine.setPoint(startPoint);
            currentLine.setPoint(endPoint);
        } else if (currentShapeType.equals("Rectángulo")) {
            drawRectangle();
        } else {
            drawCircle();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Line line : lines) {
            List<Point> points = line.getPoints();
            for (int j = 1; j < points.size(); j++) {
                Point p1 = points.get(j - 1);
                Point p2 = points.get(j);

                g2d.setStroke(new BasicStroke(line.getStroke()));
                g2d.setColor(line.getColor());
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    /**
     * setea el grosor de la línea
     * 
     * @param stroke grosor de la línea
     */
    public void setLineStroke(int stroke) {
        this.lineStroke = stroke;
        repaint();
    }

    /**
     * setea el color de la línea
     * 
     * @param color color de la línea
     */
    public void setNewColor(Color color) {
        this.currentColor = color;
        repaint();
    }

    /**
     * setea el tipo de línea
     * 
     * @param shapeType tipo de línea
     */
    public void setShapeType(String shapeType) {
        this.currentShapeType = shapeType;
    }

    /**
     * crear el panel de herramientas
     * 
     * @param p              panel donde se mostrarán los componentes
     * @param sliderStroke   slider con el grosor de la línea
     * @param colorsCombo    combo box con los colores
     * @param lineTypesCombo combo box con los tipos de línea
     * @return panel de herramientas
     */
    private static JPanel createToolsPanel(Paint p, JSlider sliderStroke, JComboBox<String> colorsCombo,
            JComboBox<String> lineTypesCombo) {
        JPanel toolsPanel = new JPanel();
        toolsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        toolsPanel.add(new JLabel("Grosor:"));
        toolsPanel.add(sliderStroke);
        toolsPanel.add(new JLabel("Colores:"));
        toolsPanel.add(colorsCombo);
        toolsPanel.add(new JLabel("Tipo de línea:"));
        toolsPanel.add(lineTypesCombo);

        return toolsPanel;
    }

    /**
     * crear el slider para el grosor de la línea
     * 
     * @param p panel donde se mostrará el slider
     * @return slider con el grosor de la línea
     */
    private static JSlider createSlider(Paint p) {
        JSlider sliderStroke = new JSlider(1, 70, p.lineStroke);
        sliderStroke.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                p.setLineStroke(sliderStroke.getValue());
            }
        });
        return sliderStroke;
    }

    /**
     * crear el combo box para selección de color
     * 
     * @param p panel donde se mostrará el combo box
     * @return combo box con los colores
     */
    private static JComboBox<String> createColorComboBox(Paint p) {
        JComboBox<String> colorsCombo = new JComboBox<>(colorNames);
        colorsCombo.setSelectedIndex(1);
        colorsCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.setNewColor(colors[colorsCombo.getSelectedIndex()]);
            }
        });
        return colorsCombo;
    }

    /**
     * crear el combo box para selección de tipo de línea
     * 
     * @param p panel donde se mostrará el combo box
     * @return combo box con los tipos de línea
     */
    private static JComboBox<String> createLineTypeComboBox(Paint p) {
        JComboBox<String> lineTypesCombo = new JComboBox<>(lineTypes);
        lineTypesCombo.setSelectedIndex(0);
        lineTypesCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.setShapeType(lineTypes[lineTypesCombo.getSelectedIndex()]);
            }
        });
        return lineTypesCombo;
    }

    /**
     * crear el menú de archivo
     * 
     * @param f Frame donde se mostrará el menú
     * @return JMenu con las opciones de archivo
     */
    private static JMenu createFileMenu(JFrame f) {
        JMenu m = new JMenu("Archivo");
        JMenuItem mNew = new JMenuItem("Nuevo");
        JMenuItem mClose = new JMenuItem("Cerrar");

        mNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lines.clear();
                f.repaint();
            }
        });

        mClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        m.add(mNew);
        m.add(mClose);
        return m;
    }

    /**
     * dibuja un rectángulo o cuadrado
     */
    private void drawRectangle() {
        int x = Math.min(startPoint.x, endPoint.x);
        int y = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(startPoint.x - endPoint.x);
        int height = Math.abs(startPoint.y - endPoint.y);

        currentLine.setPoint(new Point(x, y));
        currentLine.setPoint(new Point(x + width, y));
        currentLine.setPoint(new Point(x + width, y + height));
        currentLine.setPoint(new Point(x, y + height));
        currentLine.setPoint(new Point(x, y));
    }

    /**
     * dibuja un círculo
     */
    private void drawCircle() {
        int radius = (int) startPoint.distance(endPoint);
        int x = startPoint.x - radius;
        int y = startPoint.y - radius;

        for (int i = 0; i <= 360; i++) {
            int cx = (int) (x + radius + radius * Math.cos(Math.toRadians(i)));
            int cy = (int) (y + radius + radius * Math.sin(Math.toRadians(i)));
            currentLine.setPoint(new Point(cx, cy));
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Paint básico en Java");
        f.setLayout(new BorderLayout());

        Paint p = new Paint();
        p.setBackground(Color.WHITE);

        // crear barra de menú
        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = createFileMenu(f);
        mb.add(fileMenu);
        f.setJMenuBar(mb);

        // crear componentes del panel de herramientas
        JSlider sliderStroke = createSlider(p);
        JComboBox<String> colorsCombo = createColorComboBox(p);
        JComboBox<String> lineTypesCombo = createLineTypeComboBox(p);

        // añadir el panel de herramientas
        JPanel toolsPanel = createToolsPanel(p, sliderStroke, colorsCombo, lineTypesCombo);

        // añadir paneles y componentes al frame
        f.add(toolsPanel, BorderLayout.NORTH);
        f.add(p, BorderLayout.CENTER);

        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
