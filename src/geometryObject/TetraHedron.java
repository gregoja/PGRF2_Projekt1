package geometryObject;

import model.Part;
import model.Solid;
import model.TypeTopology;
import model.Vertex;
import transforms.*;

public class TetraHedron extends Solid {

    public TetraHedron() {
        getVertexBuffer().add(new Vertex(new Point3D(0,0,0),new Col(255,255,255)));
        getVertexBuffer().add(new Vertex(new Point3D(0,0,1),new Col(0,0,255)));
        getVertexBuffer().add(new Vertex(new Point3D(0,1,0),new Col(0,255,0)));
        getVertexBuffer().add(new Vertex(new Point3D(1,0,0),new Col(255,0,0)));

        getPartBuffer().add(new Part(TypeTopology.TRIANGLES,4,0));

        getIndexBuffer().add(0);
        getIndexBuffer().add(1);
        getIndexBuffer().add(2);

        getIndexBuffer().add(0);
        getIndexBuffer().add(1);
        getIndexBuffer().add(3);

        getIndexBuffer().add(0);
        getIndexBuffer().add(2);
        getIndexBuffer().add(3);

        getIndexBuffer().add(1);
        getIndexBuffer().add(2);  
        getIndexBuffer().add(3);
    }
}