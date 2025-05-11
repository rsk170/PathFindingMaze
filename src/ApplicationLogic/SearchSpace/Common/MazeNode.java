package ApplicationLogic.SearchSpace.Common;

public abstract class MazeNode {

    private final int cellIndex;
    private boolean isVisited;

    public MazeNode(int cellIndex) {
        this.cellIndex = cellIndex;
        this.isVisited = false;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
