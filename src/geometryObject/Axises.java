package geometryObject;

import model.Part;
import model.Solid;
import model.TypeTopology;
import model.Vertex;
import transforms.Col;
import transforms.Mat4Scale;
import transforms.Point3D;

public class Axises extends Solid {

    public Axises() {
        // aby byly videt za vsemi temi Solidy
        setTransforms(getTransforms().mul(new Mat4Scale(2)));
        setTransformable(false);

        getPartBuffer().add(new Part(TypeTopology.LINES,3,0));

        getVertexBuffer().add(new Vertex(new Point3D(0,0,0),new Col(255,0,0)));   //0
        getVertexBuffer().add(new Vertex(new Point3D(1,0,0),new Col(255,0,0)));   //1

        getVertexBuffer().add(new Vertex(new Point3D(0,0,0),new Col(0,255,0)));   //2
        getVertexBuffer().add(new Vertex(new Point3D(0,1,0),new Col(0,255,0)));   //3

        getVertexBuffer().add(new Vertex(new Point3D(0,0,0),new Col(0,0,255)));   //4
        getVertexBuffer().add(new Vertex(new Point3D(0,0,1),new Col(0,0,255)));   //5

        getIndexBuffer().add(0);
        getIndexBuffer().add(1);

        getIndexBuffer().add(2);
        getIndexBuffer().add(3);

        getIndexBuffer().add(4);
        getIndexBuffer().add(5);

        getPartBuffer().add(new Part(TypeTopology.TRIANGLES,2,6));
        getVertexBuffer().add(new Vertex(new Point3D(0.95,-0.05,0),new Col(255,0,0)));    //6
        getVertexBuffer().add(new Vertex(new Point3D(0.95,0.05,0),new Col(255,0,0)));     //7
        getVertexBuffer().add(new Vertex(new Point3D(0.95,0,0.05),new Col(255,0,0)));     //8
        getVertexBuffer().add(new Vertex(new Point3D(0.95,0,-0.05),new Col(255,0,0)));    //9

        getIndexBuffer().add(1);
        getIndexBuffer().add(6);
        getIndexBuffer().add(7);

        getIndexBuffer().add(1);
        getIndexBuffer().add(8);
        getIndexBuffer().add(9);

        getPartBuffer().add(new Part(TypeTopology.TRIANGLES,2,12));
        getVertexBuffer().add(new Vertex(new Point3D(0.05, 0.95, 0),new Col(0,255,0)));   //10
        getVertexBuffer().add(new Vertex(new Point3D(-0.05, 0.95, 0),new Col(0,255,0)));  //11
        getVertexBuffer().add(new Vertex(new Point3D(0, 0.95, 0.05),new Col(0,255,0)));   //12
        getVertexBuffer().add(new Vertex(new Point3D(0, 0.95, -0.05),new Col(0,255,0)));  //13

        getIndexBuffer().add(3);
        getIndexBuffer().add(10);
        getIndexBuffer().add(11);

        getIndexBuffer().add(3);
        getIndexBuffer().add(12);
        getIndexBuffer().add(13);

        getPartBuffer().add(new Part(TypeTopology.TRIANGLES,2,18));
        getVertexBuffer().add(new Vertex(new Point3D(0.05, 0, 0.95),new Col(0,0,255)));   //14
        getVertexBuffer().add(new Vertex(new Point3D(-0.05, 0, 0.95),new Col(0,0,255)));  //15
        getVertexBuffer().add(new Vertex(new Point3D(0, 0.05, 0.95),new Col(0,0,255)));   //16
        getVertexBuffer().add(new Vertex(new Point3D(0, -0.05, 0.95),new Col(0,0,255)));  //17

        getIndexBuffer().add(5);
        getIndexBuffer().add(14);
        getIndexBuffer().add(15);

        getIndexBuffer().add(5);
        getIndexBuffer().add(16);
        getIndexBuffer().add(17);
    }
}
