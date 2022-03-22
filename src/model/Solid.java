package model;

import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.ArrayList;
import java.util.List;

public class Solid {
    private List<Vertex> vertexBuffer;
    private List<Integer> indexBuffer;
    private List<Part> partBuffer;

    private Mat4 transforms;
    private boolean transformable;

    public Solid() {
        vertexBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        partBuffer = new ArrayList<>();

        transforms = new Mat4Identity();
        transformable = true;
    }

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Part> getPartBuffer() {
        return partBuffer;
    }

    public void setTransforms(Mat4 transforms) {
        if(transformable){
            this.transforms = transforms;
        }
    }

    public Mat4 getTransforms() {
        return transforms;
    }

    public void setTransformable(boolean transformable) {
        this.transformable = transformable;
    }
}
