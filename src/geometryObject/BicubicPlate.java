package geometryObject;

import model.Part;
import model.Solid;
import model.TypeTopology;
import model.Vertex;
import transforms.Bicubic;
import transforms.Col;
import transforms.Vec2D;

public class BicubicPlate extends Solid {

    public BicubicPlate(Bicubic bicubic, int n, int m) {
        if (m < 2 || n < 2) {
            throw new IllegalArgumentException("Grid size is too small");
        }
        int startIndex = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double u = i * (1. / (n - 1));
                double v = j * (1. / (m - 1));
                // souradnice u, v s vyhodou vyuzity aby tam byl pekny barevny prechod...
                getVertexBuffer().add(new Vertex(bicubic.compute(u, v), new Col(u, 1., v),new Vec2D(u*100,v*100)));


                if (j != m - 1 && i != n - 1) {
                    getPartBuffer().add(new Part(TypeTopology.TRIANGLE_STRIP, 2, startIndex));
                    startIndex += 4;
                    int jim = j + i * m;

                    getIndexBuffer().add(jim);
                    getIndexBuffer().add(jim + 1);
                    getIndexBuffer().add(jim + m);
                    getIndexBuffer().add(jim + m + 1);
                }
            }
        }
    }

}