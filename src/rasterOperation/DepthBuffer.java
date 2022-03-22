package rasterOperation;

import java.util.Arrays;
import java.util.Optional;

public class DepthBuffer implements GridBuffer<Float> {
    private float[][] buffer;

    private int height;
    private int width;

    public DepthBuffer(int width,int height) {
        this.width = width;
        this.height = height;

        buffer = new float[width][height];
        clear();
    }

    public void clear() {
        Arrays.stream(buffer).forEach(a -> Arrays.fill(a, 1f));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Optional<Float> getElement(int x, int y) {
        if(checkIndex(x,y)){
            return Optional.of(buffer[x][y]);
        }
        return Optional.empty();
    }

    @Override
    public void setElement(Float value, int x, int y) {
        if(checkIndex(x,y)){
            buffer[x][y] = value;
        }
    }
}