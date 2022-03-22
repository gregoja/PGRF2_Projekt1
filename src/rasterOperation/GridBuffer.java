package rasterOperation;

import java.util.Optional;

public interface GridBuffer<V> {
    int getWidth();
    int getHeight();

    Optional<V> getElement(int x, int y);
    void setElement(V value, int x, int y);

    default boolean checkIndex(int x, int y){
        return (x >= 0 && y >= 0 && x < getWidth() && y < getHeight());
    }
}