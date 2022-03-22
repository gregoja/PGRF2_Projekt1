package rasterOperation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Visibility {
    private DepthBuffer depthBuffer;
    private ImageBuffer imageBuffer;

    private Color bgColor = new Color(0x2f2f2f);

    public Visibility(int width, int height) {
        this.depthBuffer = new DepthBuffer(width, height);
        this.imageBuffer = new ImageBuffer(width, height);
    }

    public Visibility(BufferedImage ib) {
        this.depthBuffer = new DepthBuffer(ib.getWidth(), ib.getHeight());
        this.imageBuffer = new ImageBuffer(ib);
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = new Color(bgColor);
    }

    public void clear() {
        imageBuffer.clear(bgColor);
        depthBuffer.clear();
    }

    public void drawPixel(int x, int y, float z, int color) {
        if (depthBuffer.getElement(x, y).isEmpty()) return;
        if (z < depthBuffer.getElement(x, y).get()) {
            imageBuffer.setElement(color, x, y);
            depthBuffer.setElement(z, x, y);
        }
    }

    public int getWidth() {
        return imageBuffer.getWidth();
    }

    public int getHeight() {
        return imageBuffer.getHeight();
    }
}
