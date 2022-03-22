package texture;

import transforms.Col;

public class Texture {
    private static Col col1 = new Col(255, 0, 0);
    private static Col col2 = new Col(0, 0, 0);

    public static Col getTexel(double u, double v) {
        if (u % 15 <= 8 && v % 15 <= 8) {
            return col1;
        } else {
            return col2;
        }
    }
}
