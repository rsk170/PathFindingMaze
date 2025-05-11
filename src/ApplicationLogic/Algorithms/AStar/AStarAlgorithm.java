package ApplicationLogic.Algorithms.AStar;

import ApplicationLogic.Algorithms.Exceptions.EmptyMazeArray;
import ApplicationLogic.SearchSpace.AStar.AStarMaze;
import ApplicationLogic.SearchSpace.AStar.AStarNode;
import ApplicationLogic.Algorithms.Exceptions.PathNotFound;
import ApplicationLogic.Algorithms.Exceptions.StartNodeNotFound;
import ApplicationLogic.Algorithms.Exceptions.TargetNodeNotFound;
import ApplicationLogic.SearchSpace.Common.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithm {

    private final PriorityQueue<AStarNode> openSet;
    private List<AStarNode> nodesVisited;
    private List<AStarNode> optimalPath;
    private AStarNode[][] nodesArray;
    private long finishedAtMSec = 0;
    private int startNodeIndex = 1;
    private int targetNodeIndex = 1;
    private AStarMaze mazeObj;
    private Graph<AStarNode> graph;
    private boolean[] closedSet;

    public AStarAlgorithm(int[][] mazeArray, int mazeDrawSize) throws TargetNodeNotFound, StartNodeNotFound, EmptyMazeArray {
        mazeObj = new AStarMaze(mazeArray, mazeDrawSize);
        graph = mazeObj.getGraph();
        nodesArray = mazeObj.getNodeArray();
        startNodeIndex = mazeObj.getStartIndex();
        targetNodeIndex = mazeObj.getTargetIndex();

        //compare two nodes - node1 and node2
        openSet = new PriorityQueue<>((AStarNode n1, AStarNode n2) -> {
            if (n1.getFinalCost() < n2.getFinalCost()) {
                return -1;
            } else if (n1.getFinalCost() > n2.getFinalCost()) {
                return 1;
            } else {
                return 0;
            }
        });

        this.optimalPath = new ArrayList<>();
        this.nodesVisited = new ArrayList<>();
    }

    public void performSearch() throws PathNotFound {
        if (startNodeIndex <= 0 || targetNodeIndex <= 0)
            return;

        List<AStarNode> AStarNodeList = graph.getNodes();
        if (AStarNodeList.size() == 0)
            return;

        long milliStart = System.currentTimeMillis();
        System.out.println("Performing A* path search...");

        closedSet = new boolean[AStarNodeList.size()];

        AStarNode root = AStarNodeList.get(startNodeIndex - 1);

        List<AStarNode> neighbours;
        root.setgValue(0);
        root.setVisited(true);

        openSet.add(root);     //first thing in the open list

        AStarNode current;

        while (true) {
            //remove from the open list(queue) and add it to the closed list
            current = openSet.poll();

            //if the queue is empty, when visiting all of the nodes
            if (current == null) {
                break;
            }

            //every time we remove from the queue we add it to the nodeVisited
            nodesVisited.add(current);

            int currentIndex = current.getCellIndex();
            if (currentIndex == targetNodeIndex) {
                this.optimalPath = reconstructPath(current, startNodeIndex);
                finishedAtMSec = (System.currentTimeMillis() - milliStart);
                System.out.println("Searching finished. Optimal found in: " + (finishedAtMSec) + " milliseconds");
                return;
            }

            this.closedSet[current.getCellIndex() - 1] = true;

            neighbours = current.getNeighbours();

            //add neighbours to the queue
            for (AStarNode n : neighbours) {
                updateOpenSet(current, n);
            }
        }
        finishedAtMSec = (System.currentTimeMillis() - milliStart);
        throw new PathNotFound("Searching finished. No path found in: " + (finishedAtMSec) + " milliseconds");
    }

    private List<AStarNode> reconstructPath(AStarNode current, int startNodeIndex) {
        List<AStarNode> path = new ArrayList<>();

        while (current.getCellIndex() != startNodeIndex) {
            path.add(/*0,*/ current);
            current = current.getParent();
        }

        path.add(0, current);
        return path;
    }

    private void updateOpenSet(AStarNode current, AStarNode n) {
        double Ver_Hor_COST = 1;

        if (n == null || this.closedSet[n.getCellIndex() - 1] || n.isVisited())
            return;

        boolean isOpen = openSet.contains(n);

        //if it is not inside the open list
        if (!isOpen) {
            n.setParent(current);
            n.setgValue(current.getgValue() + Ver_Hor_COST);
            n.setFinalCost(n.getgValue() + n.getHeuristicValue());
            openSet.add(n);
        }
    }

    public AStarAlgorithmData getData() {
        AStarAlgorithmData data = new AStarAlgorithmData(nodesVisited, optimalPath);
        data.setMaze(mazeObj);
        data.setFinishedAtMSec(finishedAtMSec);
        return data;
    }
}