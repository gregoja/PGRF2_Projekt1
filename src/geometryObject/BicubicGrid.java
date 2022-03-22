package geometryObject;

import model.Part;
import model.Solid;
import model.TypeTopology;
import model.Vertex;
import transforms.Bicubic;
import transforms.Col;

public class BicubicGrid extends Solid {

    public BicubicGrid(Bicubic bicubic, int n, int m) {
        if(m < 2 || n < 2){
            throw new IllegalArgumentException("Grid size is too small");
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double u = i * (1. / (n - 1));
                double v = j * (1. / (m - 1));
                // souradnice u, v s vyhodou vyuzity aby tam byl pekny barevny prechod...
                getVertexBuffer().add(new Vertex(bicubic.compute(u,v),new Col(u,1.,v)));

                if (j != m - 1) {
                    getIndexBuffer().add(j + i * m);
                    getIndexBuffer().add(j + i * m + 1);
                }
                if (i != n - 1) {
                    getIndexBuffer().add(j + i * m);
                    getIndexBuffer().add(j + i * m + m);
                }
            }
        }
        getPartBuffer().add(new Part(TypeTopology.LINES, getIndexBuffer().size()/2, 0));
    }

}
