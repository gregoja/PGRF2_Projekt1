package model;

public class Part {
    private final TypeTopology type;
    private final int count;
    private final int startIndex;

    public Part(TypeTopology type, int count, int startIndex) {
        this.type = type;
        this.count = count;
        this.startIndex = startIndex;
    }

    public TypeTopology getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public int getStartIndex() {
        return startIndex;
    }

    @Override
    public String toString() {
        return "Part{" +
                "type=" + type +
                ", count=" + count +
                ", startIndex=" + startIndex +
                '}';
    }
}