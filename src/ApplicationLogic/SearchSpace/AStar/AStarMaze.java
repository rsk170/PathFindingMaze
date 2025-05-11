package ApplicationLogic.SearchSpace.AStar;

import ApplicationLogic.Algorithms.Exceptions.EmptyMazeArray;
import ApplicationLogic.Algorithms.Exceptions.StartNodeNotFound;
import ApplicationLogic.Algorithms.Exceptions.TargetNodeNotFound;
import ApplicationLogic.SearchSpace.Common.Graph;

import java.util.ArrayList;
import java.util.List;

public class AStarMaze {

    private final int targetValue = 9;
    private final int startValue = -1;
    private final int mazeDrawSize;

    private int[][] maze = {};
    private AStarNode[][] nodesArray;
    private Graph<AStarNode> graph;
    private int columnNumber = 0;
    private int rowNumber = 0;

    private int targetIndex = -1;
    private int startIndex = -1;
    private int squareSize;


    // User can input a maze
    public AStarMaze(int[][] mazeArray, int mazeDrawSize) throws TargetNodeNotFound, StartNodeNotFound, EmptyMazeArray {
        this.mazeDrawSize = mazeDrawSize;
        if (isMazeArrayEmpty(mazeArray)) {
            throw new EmptyMazeArray();
        }

        this.maze = mazeArray;
        this.rowNumber = maze.length;
        this.columnNumber = maze[0].length;
        this.squareSize = determineSquareSize();
        this.graph = new Graph<>();
        this.nodesArray = new AStarNode[this.rowNumber][this.columnNumber];

        initializeNodesArray();
        initializeGraph();
    }

    //different square size according to the dimensions of the maze
    private int determineSquareSize() {
        this.squareSize = mazeDrawSize / this.columnNumber;
        return squareSize;
    }

    public boolean isMazeArrayEmpty(int[][] mazeArray) {
        return (mazeArray.length <= 0 || mazeArray[0].length <= 0);
    }

    private void initializeNodesArray() throws TargetNodeNotFound, StartNodeNotFound {
        int index = 1;
        for (int i = 0; i < this.rowNumber; i++) {
            for (int j = 0; j < this.columnNumber; j++) {
                this.nodesArray[i][j] = new AStarNode(index);
                index++;

                if (targetIndex <= 0 && maze[i][j] == targetValue)
                    targetIndex = index - 1;

                if (startIndex <= 0 && maze[i][j] == startValue)
                    startIndex = index - 1;
            }
        }

        if (targetIndex < 0)
            throw new TargetNodeNotFound();

        if (startIndex < 0)
            throw new StartNodeNotFound();
    }

    private void initializeGraph() {
        //create the graph out of a list of nodes
        List<AStarNode> allAStarNodes = new ArrayList<>();

        //row number and column number of the final state
        int idx = getTargetIndex();

        if (idx <= 0) {
            System.err.println("No target Index found");
            return;
        }
        if (this.rowNumber <= 0) return;
        if (this.columnNumber <= 0) return;

        int iTarget = idx / (this.columnNumber);    //row number of the target
        int jTarget = (idx % this.columnNumber) - 1;   //column number

        //start with one node, find the neighbours, add the nodes one by one and build the graph
        for (int i = 0; i < this.rowNumber; i++) {
            for (int j = 0; j < this.columnNumber; j++) {
                //calculate heuristics - calculate the Euclidean distance
                double euclideanDistance = getEuclideanDistance(iTarget, jTarget, i, j);
                this.nodesArray[i][j].setHeuristicValue(euclideanDistance); //assign the h value

                //if it is a wall, make it visited
                if (this.maze[i][j] == 1) {
                    nodesArray[i][j].setVisited(true);
                }
                addNeighbours(i, j);
                allAStarNodes.add(nodesArray[i][j]);
            }
        }

        this.graph.setNodes(allAStarNodes);
    }

    private double getEuclideanDistance(int iTarget, int jTarget, int row, int col) {
        double dx = row - iTarget;
        double dy = col - jTarget;
        return Math.sqrt(dx * dx + dy * dy); // Euclidean Distance
    }

    private void addNeighbours(int rowNum, int colNum) {
        //move up
        if (isInsideMaze(colNum + 1, rowNum, this.rowNumber, this.columnNumber)) {
            this.nodesArray[rowNum][colNum].addNeighbour(this.nodesArray[rowNum][colNum + 1]);
        }

        //move down
        if (isInsideMaze(colNum, rowNum + 1, this.rowNumber, this.columnNumber)) {
            this.nodesArray[rowNum][colNum].addNeighbour(this.nodesArray[rowNum + 1][colNum]);
        }

        //move left
        if (isInsideMaze(colNum - 1, rowNum, this.rowNumber, this.columnNumber)) {
            this.nodesArray[rowNum][colNum].addNeighbour(this.nodesArray[rowNum][colNum - 1]);
        }

        //move right
        if (isInsideMaze(colNum, rowNum - 1, this.rowNumber, this.columnNumber)) {
            this.nodesArray[rowNum][colNum].addNeighbour(this.nodesArray[rowNum - 1][colNum]);
        }
    }

    //don't overflow out of the graph,
    // don't access anything greater than the number of rows, columns of the matrix
    private boolean isInsideMaze(int colNum, int rowNum, int maxRow, int maxCol) {
        if ((colNum < 0) || (rowNum < 0)) {
            return false;
        }

        return (colNum < maxCol) && (rowNum < maxRow);
    }

    public Graph<AStarNode> getGraph() {
        return this.graph;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public int[][] getMaze() {
        return maze;
    }

    public AStarNode[][] getNodeArray() {
        return this.nodesArray;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getTargetIndex() {
        return targetIndex;
    }
}