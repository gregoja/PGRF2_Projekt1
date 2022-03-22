package model;

import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;


public class Vertex implements Vectorable<Vertex> {
    private final Point3D position;
    private final Col color;
    private final Vec2D uv;
    private final double one;

    private final boolean transformable;

    public Vertex(Point3D position, Col color, Vec2D uv) {
        this.position = position;
        this.color = color;
        this.uv = uv;
        this.one = 1;
        this.transformable = true;
    }

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
        this.uv = new Vec2D(0,0);
        this.one = 1;
        this.transformable = true;
    }

    private Vertex(Point3D position, Col color, Vec2D uv, double one, boolean transformable) {
        this.position = position;
        this.color = color;
        this.uv = uv;
        this.one = one;
        this.transformable = transformable;
    }

    public Col getColor() {
        return color;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getZ() {
        return position.getZ();
    }

    public double getW() {
        return position.getW();
    }

    public double getU() {
        return uv.getX();
    }

    public double getV() {
        return uv.getY();
    }

    public double getOne() {
        return one;
    }

    public Vertex transform(Mat4 transforms) {
        if(transformable){
            return new Vertex(position.mul(transforms),color,uv,one,true);
        }
        else throw new IllegalStateException("Transformations are no longer allowed");
    }

    // tech par vyjimek je mala dan za to jak to tato metoda usnadni praci.
    public Vertex transformToWindow(int width,int height) {
        if(Math.abs(position.getW() -1) > 0.000001) throw new IllegalStateException("Attempted to call this method on non-dehomogenised Vertex");
        else if(!transformable) throw new IllegalStateException("Already in window coordinates");
        else{
            double x = (((getX() + 1) / 2) * width - 1);
            double y = (((-getY() + 1) / 2) * height - 1);
            return new Vertex(new Point3D(x, y, getZ()), color, uv,one,false);
        }
    }

    @Override
    public Vertex mul(double scalar) {
        return new Vertex(position.mul(scalar), color.mul(scalar), uv.mul(scalar),one * scalar,transformable);
    }

    @Override
    public Vertex add(Vertex vec) {
        return new Vertex(position.add(vec.position), color.add(vec.color), uv.add(vec.uv),this.one + vec.one,transformable);
    }

    public Vertex dehomog() {
        return this.mul(1 / getW());
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "position=" + position +
                ", color=" + new Color(color.getRGB()) +
                ", uv=" + uv +
                ", one=" + one +
                ", transformable=" + transformable +
                '}';
    }
}