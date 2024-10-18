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
    private static final String[] lineTypes = { "Libre", "Línea recta", "Rectángulo", "Cuadrado", "Círculo" };
    private String currentShapeType = "Libre"; 
    private Point startPoint, endPoint; 

    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                if (currentShapeType.equals("Libre")) {
                    currentLine = new Line();
                    currentLine.setPoint(startPoint);
                    currentLine.setStroke(lineStroke);
                    currentLine.setColor(currentColor);
                    lines.add(currentLine);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
                if (!currentShapeType.equals("Libre")) {
                    currentLine = new Line();
                    currentLine.setStroke(lineStroke);
                    currentLine.setColor(currentColor);

                    if (currentShapeType.equals("Línea recta")) {
                        currentLine.setPoint(startPoint);
                        currentLine.setPoint(endPoint);
                    } else if (currentShapeType.equals("Rectángulo") || currentShapeType.equals("Cuadrado")) {
                        // dibujar rectangulo o cuadrado
                        // drawRectangleOrSquare();
                    } else if (currentShapeType.equals("Círculo")) {
                        // dibujar círculo
                        // drawCircle();
                    }
                    lines.add(currentLine);
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentShapeType.equals("Libre")) {
                    currentLine.setPoint(e.getPoint());
                    repaint();
                }
            }
        });
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

    public void setLineStroke(int stroke) {
        this.lineStroke = stroke;
        repaint();
    }

    public void setNewColor(Color color) {
        this.currentColor = color;
        repaint();
    }

    public void setShapeType(String shapeType) {
        this.currentShapeType = shapeType;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Paint básico en Java");
        f.setLayout(new BorderLayout());

        Paint p = new Paint();
        p.setBackground(Color.WHITE);

        // Crear barra de menú
        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = createFileMenu(f);
        mb.add(fileMenu);
        f.setJMenuBar(mb);

        // Crear panel de herramientas para la parte superior
        JPanel toolsPanel = new JPanel();
        toolsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Crear slider para el grosor de la línea
        JSlider sliderStroke = new JSlider(1, 70, p.lineStroke);
        sliderStroke.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                p.setLineStroke(sliderStroke.getValue());
            }
        });

        // Crear combo box para selección de color
        JComboBox<String> colorsCombo = new JComboBox<>(colorNames);
        colorsCombo.setSelectedIndex(1);
        colorsCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.setNewColor(colors[colorsCombo.getSelectedIndex()]);
            }
        });

        // Combo box para tipos de línea
        JComboBox<String> lineTypesCombo = new JComboBox<>(lineTypes);
        lineTypesCombo.setSelectedIndex(0);
        lineTypesCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.setShapeType(lineTypes[lineTypesCombo.getSelectedIndex()]);
            }
        });

        // Añadir los componentes al panel de herramientas
        toolsPanel.add(new JLabel("Grosor:"));
        toolsPanel.add(sliderStroke);
        toolsPanel.add(new JLabel("Colores:"));
        toolsPanel.add(colorsCombo);
        toolsPanel.add(new JLabel("Tipo de línea:"));
        toolsPanel.add(lineTypesCombo);

        // Añadir paneles y componentes al frame
        f.add(toolsPanel, BorderLayout.NORTH);
        f.add(p, BorderLayout.CENTER);

        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

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
}
