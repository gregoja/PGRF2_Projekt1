package renderOperation;

import model.Vertex;
import rasterOperation.Visibility;
import transforms.Col;

import java.util.function.Function;

public class Rasterizer {
    private Visibility visibility;
    private Function<Vertex, Col> shader;

    public Rasterizer(Visibility visibility, Function<Vertex, Col> shader) {
        this.visibility = visibility;
        this.shader = shader;
    }

    public void setShader(Function<Vertex, Col> shader) {
        this.shader = shader;
    }

    public void rasterizeTriangle(Vertex a, Vertex b, Vertex c) {
        int windowWidth = visibility.getWidth();
        int windowHeight = visibility.getHeight();
        a = a.dehomog().transformToWindow(windowWidth,windowHeight);
        b = b.dehomog().transformToWindow(windowWidth,windowHeight);
        c = c.dehomog().transformToWindow(windowWidth,windowHeight);

        Vertex temp;
        if (a.getY() > b.getY()) {
            temp = a;
            a = b;
            b = temp;
        }
        if (b.getY() > c.getY()) {
            temp = b;
            b = c;
            c = temp;
        }
        if (a.getY() > b.getY()) {
            temp = a;
            a = b;
            b = temp;
        }

        for (int y = Math.max((int) a.getY() + 1, 0); y <= Math.min(b.getY(), visibility.getHeight() - 1); y++) {
            double t1 = (y - a.getY()) / (b.getY() - a.getY());
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));

            double t2 = (y - a.getY()) / (c.getY() - a.getY());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            drawScanLine(y, ab, ac);
        }

        for (int y = Math.max((int) b.getY() + 1, 0); y <= Math.min(c.getY(), visibility.getHeight() - 1); y++) {
            double t1 = (y - b.getY()) / (c.getY() - b.getY());
            Vertex bc = b.mul(1 - t1).add(c.mul(t1));

            double t2 = (y - a.getY()) / (c.getY() - a.getY());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            drawScanLine(y, bc, ac);
        }
    }

    private void drawScanLine(int y, Vertex a, Vertex b) {
        if (a.getX() > b.getX()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        for (int x = Math.max((int) a.getX() + 1, 0); x <= Math.min(b.getX(), visibility.getWidth() - 1); x++) {
            double s = (x - a.getX()) / (b.getX() - a.getX());
            Vertex abc = a.mul(1 - s).add(b.mul(s));
            drawColorPixel(x, y, abc);
        }
    }

    private void drawColorPixel(int x, int y, Vertex abc) {
        int color = shader.apply(abc).getRGB();
        visibility.drawPixel(x, y, (float) abc.getZ(), color);
    }

    public void rasterizeLine(Vertex a, Vertex b) {
        int windowWidth = visibility.getWidth();
        int windowHeight = visibility.getHeight();
        a = a.dehomog().transformToWindow(windowWidth,windowHeight);
        b = b.dehomog().transformToWindow(windowWidth,windowHeight);

        if (Math.abs(a.getX() - b.getX()) > Math.abs(a.getY() - b.getY())) { // x axis
            if (a.getX() > b.getX()) {
                Vertex temp = a;
                a = b;
                b = temp;
            }

            for (int x = Math.max((int) a.getX() + 1, 0); x < Math.min(b.getX(), visibility.getWidth() - 1); x++) {
                double t = (x - a.getX()) / (b.getX() - a.getX());
                Vertex ab = a.mul(1 - t).add(b.mul(t));
                drawColorPixel(x, (int) ab.getY(), ab);
            }
        }else{
            if (a.getY() > b.getY()) {
                Vertex temp = a;
                a = b;
                b = temp;
            }

            for (int y = Math.max((int) a.getY() + 1, 0); y < Math.min(b.getY(), visibility.getWidth() - 1); y++) {
                double t = (y - a.getY()) / (b.getY() - a.getY());
                Vertex ab = a.mul(1 - t).add(b.mul(t));
                drawColorPixel((int)ab.getX(),y, ab);
            }
        }
    }
}