package model;

public interface Vectorable<V> {
    V mul(double scalar);
    V add(V vec);
}
