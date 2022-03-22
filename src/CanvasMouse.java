
import geometryObject.*;
import model.Solid;
import model.Vertex;
import texture.Texture;
import rasterOperation.Visibility;
import renderOperation.Renderer;
import transforms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * trida pro kresleni na platno: zobrazeni pixelu, ovladani mysi
 *
 * @author PGRF FIM UHK
 * @version 2017
 */
public class CanvasMouse {

    private JPanel panel;
    private BufferedImage img;

    private int oldX;
    private int newX;
    private int oldY;
    private int newY;
    private Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private JRadioButton btnPersp;
    private JRadioButton btnColorShading;
    private JRadioButton btnFilled;

    private List<Solid> scene = new ArrayList<>();

    private Visibility visibility;
    private Renderer renderer;
    Function<Vertex,Col> standardShader = vertex -> vertex.getColor().mul(1./vertex.getOne());

    private Camera cam = setDefaultCamera();
    private Mat4 persp = new Mat4PerspRH(Math.PI / 4, 1, 0.1, 200);
    private Mat4 orth = new Mat4OrthoRH(10, 10, 0.1, 200);
    private double step = 0.1;

    private Bicubic bicubic = new Bicubic(Cubic.BEZIER,
            new Point3D(0, 0, 2), new Point3D(1, 0, 0), new Point3D(2, 0, 0), new Point3D(3, 0, -2),
            new Point3D(0, 1, 0), new Point3D(1, 1, 0), new Point3D(2, 1, 0), new Point3D(3, 1, 0),
            new Point3D(0, 2, 0), new Point3D(1, 2, 0), new Point3D(2, 2, 0), new Point3D(3, 2, 0),
            new Point3D(0, 3, 2), new Point3D(1, 3, 0), new Point3D(2, 3, 0), new Point3D(3, 3, -2));


    public CanvasMouse(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF2 : " + this.getClass().getName());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        visibility = new Visibility(img);
        renderer = new Renderer(cam.getViewMatrix(), persp, visibility,standardShader);

        JToolBar tbTop = new JToolBar();
        tbTop.setFloatable(false);
        JButton btnReset = new JButton("Reset");
        btnPersp = new JRadioButton("Perspective",true);
        JRadioButton btnOrth = new JRadioButton("Orthogonal");
        ButtonGroup bgProjection = new ButtonGroup();
        bgProjection.add(btnPersp);
        bgProjection.add(btnOrth);
        tbTop.add(btnReset);
        tbTop.add(btnPersp);
        tbTop.add(btnOrth);
        frame.add(tbTop,BorderLayout.NORTH);

        tbTop.addSeparator();
        JRadioButton btnConstantShading = new JRadioButton("Constant");
        btnColorShading = new JRadioButton("Color interpolation",true);
        JRadioButton btnTextureShading = new JRadioButton("Texture");
        ButtonGroup bgShading = new ButtonGroup();
        bgShading.add(btnConstantShading);
        bgShading.add(btnColorShading);
        bgShading.add(btnTextureShading);
        tbTop.add(btnConstantShading);
        tbTop.add(btnColorShading);
        tbTop.add(btnTextureShading);

        tbTop.addSeparator();
        btnFilled = new JRadioButton("Filled",true);
        JRadioButton btnWireframe = new JRadioButton("Wireframe");
        ButtonGroup bgWireframe = new ButtonGroup();
        bgWireframe.add(btnFilled);
        bgWireframe.add(btnWireframe);
        tbTop.add(btnFilled);
        tbTop.add(btnWireframe);

        btnOrth.addActionListener(e -> {renderer.setProjection(orth);draw();});
        btnPersp.addActionListener(e -> {renderer.setProjection(persp);draw();});
        btnReset.addActionListener(e -> reset());

        btnConstantShading.addActionListener(e -> {
            Color color = JColorChooser.showDialog(panel,"Chose a color",Color.WHITE);
            if(color == null) color = Color.WHITE;
            Color finalColor = color;
            renderer.setShader((vertex) ->  new Col(finalColor.getRGB()));
            draw();});
        btnColorShading.addActionListener(e -> {renderer.setShader(standardShader);draw();});
        btnTextureShading.addActionListener(e -> {renderer.setShader(vertex -> Texture.getTexel(
                vertex.getU()*1./vertex.getOne(),vertex.getV()*1./vertex.getOne()));draw();});

        btnFilled.addActionListener(e -> {renderer.setWireframe(false);draw();});
        btnWireframe.addActionListener(e -> {renderer.setWireframe(true);draw();});

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel);
        frame.setVisible(true);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocus();
                oldX = e.getX();
                oldY = e.getY();
                if(!SwingUtilities.isMiddleMouseButton(e)) frame.setCursor(moveCursor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                frame.setCursor(defaultCursor);
            }
        });

        panel.addMouseWheelListener(e -> {
            double scale = e.getWheelRotation() > 0 ? 0.8 : 1.2;
            for (Solid solid : scene) {
                solid.setTransforms(solid.getTransforms().mul(new Mat4Scale(scale)));
            }
            draw();
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                newX = e.getX();
                newY = e.getY();

                int dx = oldX - newX;
                int dy = oldY - newY;

                if(e.isShiftDown()){
                    for(Solid solid : scene){
                        solid.setTransforms(solid.getTransforms().mul(new Mat4Transl(0,dx/10.0,dy/10.0)));
                    }
                }else
                if(SwingUtilities.isLeftMouseButton(e)){
                    cam = cam.addAzimuth((Math.PI * (dx)) / visibility.getWidth());
                    cam = cam.addZenith((Math.PI * (dy)) / visibility.getHeight());
                }else
                if(SwingUtilities.isRightMouseButton(e)){
                    for(Solid solid: scene){
                        // doesn't have anything to do with variables dx, dy
                        double ddx = solid.getTransforms().get(3,0);
                        double ddy = solid.getTransforms().get(3,1);
                        double ddz = solid.getTransforms().get(3,2);
                        if(ddx == 0 && ddy == 0 && ddz == 0){
                            solid.setTransforms(solid.getTransforms().mul(new Mat4RotXYZ(0,
                                    Math.PI * (dy) / visibility.getHeight()
                                    ,-(Math.PI * (dx) / visibility.getWidth()))));
                        }else{
                            solid.setTransforms(solid.getTransforms().mul(new Mat4Transl(-ddx,-ddy,-ddz)));
                            solid.setTransforms(solid.getTransforms().mul(new Mat4RotXYZ(0,
                                    Math.PI * (dy) / visibility.getHeight()
                                    ,-(Math.PI * (dx) / visibility.getWidth()))));
                            solid.setTransforms(solid.getTransforms().mul(new Mat4Transl(ddx,ddy,ddz)));
                        }
                    }
                }
                oldX = newX;
                oldY = newY;

                draw();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_W : cam = cam.forward(step); break;
                    case KeyEvent.VK_A : cam = cam.left(step); break;
                    case KeyEvent.VK_S : cam = cam.backward(step); break;
                    case KeyEvent.VK_D : cam = cam.right(step); break;
                    case KeyEvent.VK_R : reset(); break;
                }
                draw();
            }
        });
        frame.pack();
    }

    private void reset() {
        cam = setDefaultCamera();
        scene = new ArrayList<>();
        renderer.setProjection(persp);
        btnPersp.setSelected(true);
        renderer.setShader(standardShader);
        btnColorShading.setSelected(true);
        //btnFilled.doClick();
        btnFilled.setSelected(true);
        renderer.setWireframe(false);
        start();
    }

    private Camera setDefaultCamera() {
        return new Camera().withZenith(0).addAzimuth(0).withPosition(new Vec3D(-20, 0, 0));
    }

    public void clear() {
        visibility.clear();
    }

    public void present(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public void draw() {
        clear();
        renderer.setView(cam.getViewMatrix());
        renderer.render(scene);
        panel.repaint();
        img.getGraphics().drawString("r = reset, shift+mouse = translation, mouse wheel = scaling, rmb = rotation, lmb = looking",
                5, img.getHeight() - 5);
    }

    public void start() {
        scene.add(new Axises());
        Solid cube = new Cube();
        cube.setTransforms(cube.getTransforms().mul(new Mat4Scale(0.7)));
        scene.add(cube);
        Solid plate = new BicubicPlate(bicubic,10,10);
        Solid grid = new BicubicGrid(bicubic,10,10);
        grid.setTransforms(grid.getTransforms().mul(new Mat4Transl(0,-5,0)));
        plate.setTransforms(plate.getTransforms().mul(new Mat4Transl(0,+4,0)));
        scene.add(grid);
        scene.add(plate);
        scene.add(new TetraHedron2());
        draw();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new CanvasMouse(800, 600).start());
    }
}