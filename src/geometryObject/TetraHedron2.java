package geometryObject;

import model.Part;
import model.Solid;
import model.TypeTopology;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class TetraHedron2 extends Solid {

    public TetraHedron2() {
        getPartBuffer().add(new Part(TypeTopology.TRIANGLES,4,0));

        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, -1),new Col(255,0,0)));    //A,0
        getVertexBuffer().add(new Vertex(new Point3D(1, -1, 1),new Col(255,0,0)));      //B,1
        getVertexBuffer().add(new Vertex(new Point3D(-1, 1, 1),new Col(255,0,0)));      //C,2

        getIndexBuffer().add(0);
        getIndexBuffer().add(1);
        getIndexBuffer().add(2);

        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, -1),new Col(0,255,0)));    //A,3
        getVertexBuffer().add(new Vertex(new Point3D(1, -1, 1),new Col(0,255,0)));      //B,4
        getVertexBuffer().add(new Vertex(new Point3D(1, 1, -1),new Col(0,255,0)));      //D,5

        getIndexBuffer().add(3);
        getIndexBuffer().add(4);
        getIndexBuffer().add(5);

        getVertexBuffer().add(new Vertex(new Point3D(1, -1, 1),new Col(0,255,255)));      //B,6
        getVertexBuffer().add(new Vertex(new Point3D(-1, 1, 1),new Col(0,255,255)));      //C,7
        getVertexBuffer().add(new Vertex(new Point3D(1, 1, -1),new Col(0,255,255)));      //D,8

        getIndexBuffer().add(6);
        getIndexBuffer().add(7);
        getIndexBuffer().add(8);

        getVertexBuffer().add(new Vertex(new Point3D(-1, 1, 1),new Col(255,255,255)));      //C,9
        getVertexBuffer().add(new Vertex(new Point3D(1, 1, -1),new Col(255,255,255)));      //D,10
        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, -1),new Col(255,255,255)));    //A,11

        getIndexBuffer().add(9);
        getIndexBuffer().add(10);
        getIndexBuffer().add(11);
    }
}