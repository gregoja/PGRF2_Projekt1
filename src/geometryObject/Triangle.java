package geometryObject;

import model.Part;
import model.Solid;
import model.TypeTopology;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

public class Triangle extends Solid {

    public Triangle() {
        getVertexBuffer().add(new Vertex(new Point3D(0,0,1),new Col(0,0,255),new Vec2D(0,100)));
        getVertexBuffer().add(new Vertex(new Point3D(0,1,0),new Col(0,255,0),new Vec2D(100,100)));
        getVertexBuffer().add(new Vertex(new Point3D(1,0,0),new Col(255,0,0),new Vec2D(100,0)));

        getPartBuffer().add(new Part(TypeTopology.TRIANGLES,1,0));

        getIndexBuffer().add(0);
        getIndexBuffer().add(1);
        getIndexBuffer().add(2);

    }
}
