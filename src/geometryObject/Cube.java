package geometryObject;

import model.Part;
import model.Solid;
import model.TypeTopology;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

import java.util.Arrays;
import java.util.Random;

public class Cube extends Solid {
    private Random r = new Random();
    public Cube() {

	    //https://stackoverflow.com/questions/5032334/how-many-vertices-needed-in-to-draw-a-cube-in-opengl-es
        getPartBuffer().add(new Part(TypeTopology.TRIANGLE_STRIP,12,0));
        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, 1), getRandomCol()));
        getVertexBuffer().add(new Vertex(new Point3D(1, -1, 1), getRandomCol()));
        getVertexBuffer().add(new Vertex(new Point3D(-1, 1, 1), getRandomCol()));
        getVertexBuffer().add(new Vertex(new Point3D(1, 1, 1), getRandomCol()));

        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, -1), getRandomCol()));
        getVertexBuffer().add(new Vertex(new Point3D(1, -1, -1), getRandomCol()));
        getVertexBuffer().add(new Vertex(new Point3D(-1, 1, -1), getRandomCol()));
        getVertexBuffer().add(new Vertex(new Point3D(1, 1, -1), getRandomCol()));

        getIndexBuffer().addAll(Arrays.asList(0, 1, 2, 3, 7, 1, 5, 4, 7, 6, 2, 4, 0, 1));
    }

    private Col getRandomCol() {
        return new Col(r.nextInt(255),r.nextInt(255),r.nextInt(255));
    }
}
