package ApplicationLogic.Algorithms.AStar;

import ApplicationLogic.SearchSpace.AStar.AStarMaze;
import ApplicationLogic.SearchSpace.AStar.AStarNode;

import java.util.List;

public class AStarAlgorithmData {

    private final List<AStarNode> nodesVisited;
    private final List<AStarNode> optimalPath;
    private long finishedAtMSec;
    private AStarMaze maze;

    public AStarAlgorithmData(List<AStarNode> nodesVisited, List<AStarNode> optimalPath) {
        this.nodesVisited = nodesVisited;
        this.optimalPath = optimalPath;
        finishedAtMSec = 0;
    }

    public List<AStarNode> getNodesVisited() {
        return nodesVisited;
    }

    public List<AStarNode> getOptimalPath() {
        return optimalPath;
    }

    public long getFinishedAtMSec() {
        return finishedAtMSec;
    }

    public void setFinishedAtMSec(long finishedAtMSec) {
        this.finishedAtMSec = finishedAtMSec;
    }

    public AStarMaze getMaze() {
        return maze;
    }

    public void setMaze(AStarMaze mazeObj) {
        maze = mazeObj;
    }
}