package rasterOperation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class ImageBuffer implements GridBuffer<Integer> {
    private BufferedImage ib;

    public ImageBuffer(BufferedImage ib) {
        this.ib = ib;
    }

    public ImageBuffer(int width, int height) {
        ib = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public int getWidth() {
        return ib.getWidth();
    }

    @Override
    public int getHeight() {
        return ib.getHeight();
    }

    @Override
    public Optional<Integer> getElement(int x, int y) {
        if(checkIndex(x,y)){
            return Optional.of(ib.getRGB(x,y));
        }
        return Optional.empty();
    }

    @Override
    public void setElement(Integer value, int x, int y) {
        if(checkIndex(x,y)){
            ib.setRGB(x,y,value);
        }
    }

    public void clear(Color bgColor){
        Graphics gr = ib.getGraphics();
        gr.setColor(bgColor);
        gr.fillRect(0, 0, ib.getWidth(), ib.getHeight());
    }
}