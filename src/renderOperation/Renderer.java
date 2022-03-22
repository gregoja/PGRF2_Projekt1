package renderOperation;

import model.Part;
import model.Solid;
import model.Vertex;
import rasterOperation.Visibility;
import transforms.Col;
import transforms.Mat4;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Renderer {
    private Mat4 view;
    private Mat4 projection;
    private boolean wireframe = false;
    private Mat4 vp;

    private final Rasterizer rasterizer;

    public Renderer(Mat4 view, Mat4 projection, Visibility v, Function<Vertex, Col> shader) {
        this.view = view;
        this.projection = projection;
        this.vp = view.mul(projection);
        this.rasterizer = new Rasterizer(v, shader);
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
        this.vp = view.mul(projection);
    }

    public void setView(Mat4 view) {
        this.view = view;
        this.vp = view.mul(projection);
    }

    public void setShader(Function<Vertex, Col> shader) {
        this.rasterizer.setShader(shader);
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

    public void render(List<Solid> scene) {
        for (Solid solid : scene) {
            render(solid);
        }
    }

    public void render(Solid solid) {
        Mat4 mvp = solid.getTransforms().mul(vp);
        List<Vertex> transformed = solid.getVertexBuffer().stream().map(vertex -> vertex.transform(mvp)).collect(Collectors.toList());
        for (Part part : solid.getPartBuffer()) {
            switch (part.getType()) {
                case TRIANGLES: {
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = transformed.get(solid.getIndexBuffer().get(3 * i + part.getStartIndex()));
                        Vertex b = transformed.get(solid.getIndexBuffer().get(3 * i + part.getStartIndex() + 1));
                        Vertex c = transformed.get(solid.getIndexBuffer().get(3 * i + part.getStartIndex() + 2));

                        if (!wireframe) renderTriangle(a, b, c);
                        else {
                            renderLine(a, b);
                            renderLine(b, c);
                            renderLine(c, a);
                        }
                    }
                    break;
                }
                case TRIANGLE_STRIP: {
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = transformed.get(solid.getIndexBuffer().get(part.getStartIndex() + i));
                        Vertex b = transformed.get(solid.getIndexBuffer().get(part.getStartIndex() + i + 1));
                        Vertex c = transformed.get(solid.getIndexBuffer().get(part.getStartIndex() + i + 2));

                        if (!wireframe) renderTriangle(a, b, c);
                        else {
                            renderLine(a, b);
                            renderLine(b, c);
                            renderLine(c, a);
                        }
                    }
                    break;
                }
                case LINES: {
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = transformed.get(solid.getIndexBuffer().get(2 * i + part.getStartIndex()));
                        Vertex b = transformed.get(solid.getIndexBuffer().get(2 * i + part.getStartIndex() + 1));

                        renderLine(a, b);
                    }
                    break;
                }
                case LINE_STRIP: {
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = transformed.get(solid.getIndexBuffer().get(part.getStartIndex() + i));
                        Vertex b = transformed.get(solid.getIndexBuffer().get(part.getStartIndex() + i + 1));

                        renderLine(a, b);
                    }
                    break;
                }
                default:
                    throw new java.lang.UnsupportedOperationException("Not supported yet.");
            }
        }
    }

    private void renderTriangle(Vertex a, Vertex b, Vertex c) {
        if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return;
        if (-a.getW() > a.getX() && -b.getW() > b.getX() && -c.getW() > c.getX()) return;
        if (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) return;
        if (-a.getW() > a.getY() && -b.getW() > b.getY() && -c.getW() > c.getY()) return;
        if (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) return;
        if (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0) return;

        Vertex temp;
        if (a.getZ() < b.getZ()) {
            temp = a;
            a = b;
            b = temp;
        }
        if (b.getZ() < c.getZ()) {
            temp = b;
            b = c;
            c = temp;
        }
        if (a.getZ() < b.getZ()) {
            temp = a;
            a = b;
            b = temp;
        }

        if (a.getZ() < 0) {
            return;
        }
        if (b.getZ() < 0) {
            double t = (0 - a.getZ()) / (b.getZ() - a.getZ());
            Vertex ab = a.mul(1 - t).add(b.mul(t));

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            rasterizer.rasterizeTriangle(a, ab, ac);
            return;
        }
        if (c.getZ() < 0) {
            double t = (0 - b.getZ()) / (c.getZ() - b.getZ());
            Vertex bc = b.mul(1 - t).add(c.mul(t));

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            rasterizer.rasterizeTriangle(a, b, bc);
            rasterizer.rasterizeTriangle(a, ac, bc);
            return;
        }
        rasterizer.rasterizeTriangle(a, b, c);
    }

    private void renderLine(Vertex a, Vertex b) {
        if (a.getX() > a.getW() && b.getX() > b.getW()) return;
        if (-a.getW() > a.getX() && -b.getW() > b.getX()) return;
        if (a.getY() > a.getW() && b.getY() > b.getW()) return;
        if (-a.getW() > a.getY() && -b.getW() > b.getY()) return;
        if (a.getZ() > a.getW() && b.getZ() > b.getW()) return;
        if (a.getZ() < 0 && b.getZ() < 0) return;

        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (a.getZ() < 0) return;
        if (b.getZ() < 0) {
            double t = (0 - a.getZ()) / (b.getZ() - a.getZ());
            Vertex ab = a.mul(1 - t).add(b.mul(t));

            rasterizer.rasterizeLine(a, ab);
            return;
        }
        rasterizer.rasterizeLine(a, b);

    }
}
