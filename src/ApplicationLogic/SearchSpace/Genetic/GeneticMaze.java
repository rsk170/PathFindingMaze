package ApplicationLogic.SearchSpace.Genetic;

import ApplicationLogic.Algorithms.Exceptions.StartNodeNotFound;
import ApplicationLogic.Algorithms.Exceptions.TargetNodeNotFound;
import ApplicationLogic.SearchSpace.Common.Graph;

import java.util.ArrayList;
import java.util.List;

public class GeneticMaze {

    private final int targetValue = 9;
    private final int startValue = -1;
    private final int mazeDrawSize = 600;

    private int[][] maze = {};
    private GeneticNode[][] nodesArray;
    private Graph<GeneticNode> graph;
    private int columnNumber = 0;
    private int rowNumber = 0;

    private int targetIndex = -1;
    private int startIndex = -1;
    private int squareSize;

    // Methods
    public GeneticMaze(int[][] mazeArray) throws Exception {
        if (isMazeArrayEmpty(mazeArray)) {
            System.err.println("Cannot initialize Maze. Null array found. Provide a 2D array.");
            return;
        }
        this.maze = mazeArray;
        this.rowNumber = maze.length;
        this.columnNumber = maze[0].length;

        //determine square size based on the dimensions
        this.squareSize = determineSquareSize();
        this.graph = new Graph<>();
        this.nodesArray = new GeneticNode[this.rowNumber][this.columnNumber];

        //create nodes and add them to nodesMatrix
        initializeNodesArray();
        initializeGraph();
    }

    private void initializeNodesArray() throws TargetNodeNotFound, StartNodeNotFound {
        int index = 1;
        for (int i = 0; i < this.rowNumber; i++) {
            for (int j = 0; j < this.columnNumber; j++) {
                this.nodesArray[i][j] = new GeneticNode(index);
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

    //different square size according to the dimensions of the maze
    private int determineSquareSize() {
        this.squareSize = mazeDrawSize / this.columnNumber;
        return squareSize;
    }

    public boolean isMazeArrayEmpty(int[][] mazeArray) {
        return (mazeArray.length <= 0 || mazeArray[0].length <= 0);
    }

    //add the nodes to the graph
    //first find the neighbours of each node and then create the graph
    private void initializeGraph() throws Exception {
        List<GeneticNode> allGeneticNodes = new ArrayList<>();

        boolean targetBlocked = false;
        int startingPoints = 0;
        int finishingPoints = 0;

        //row number and column number of the final state
        int idx = getTargetIndex();

        if (idx <= 0) {
            System.err.println("No target Index found");
            return;
        }
        if (this.rowNumber <= 0) return;
        if (this.columnNumber <= 0) return;

        for (int i = 0; i < this.rowNumber; i++) {
            for (int j = 0; j < this.columnNumber; j++) {
                if (this.maze[i][j] == 1) {
                    this.nodesArray[i][j].setVisited(true);
                }
                if (this.maze[i][j] == 9) {
                    finishingPoints++;
                    if (this.maze[i - 1][j] == 1 && this.maze[i + 1][j] == 1 && this.maze[i][j - 1] == 1 && this.maze[i][j + 1] == 1) {
                        targetBlocked = true;
                    }
                }
                if (this.maze[i][j] == -1) {
                    startingPoints++;
                }
                addAllNeighbours(i, j);
                allGeneticNodes.add(nodesArray[i][j]);
            }
        }
        // Test cases
        if (startingPoints > 1) {
            System.out.println("Error: There can be single starting point!");
            throw new Exception("Error: There can be single starting point!");
        }
        if (finishingPoints > 1) {
            System.out.println("Error: There can be single finishing point!");
            throw new Exception("Error: There can be single finishing point!");
        }
        if (targetBlocked) {
            System.out.println("Error: Target is blocked!");
            throw new Exception("Error: Target is blocked!");
        }

        this.graph.setNodes(allGeneticNodes);
    }


    private void addAllNeighbours(int row, int column) {
        if (isInsideMaze(row, column + 1, this.rowNumber, this.columnNumber)) {
            this.nodesArray[row][column].addNeighbour(this.nodesArray[row][column + 1]);
        }

        if (isInsideMaze(row, column - 1, this.rowNumber, this.columnNumber)) {
            this.nodesArray[row][column].addNeighbour(this.nodesArray[row][column - 1]);
        }

        if (isInsideMaze(row + 1, column, this.rowNumber, this.columnNumber)) {
            this.nodesArray[row][column].addNeighbour(this.nodesArray[row + 1][column]);
        }

        if (isInsideMaze(row - 1, column, this.rowNumber, this.columnNumber)) {
            this.nodesArray[row][column].addNeighbour(this.nodesArray[row - 1][column]);
        }
    }

    private boolean isInsideMaze(int rowNumber, int columnNumber, int maxRow, int maxColumn) {
        if ((columnNumber < 0) || (rowNumber < 0)) {
            return false;
        }

        return (columnNumber < maxColumn) && (rowNumber < maxRow);
    }


    public int[][] getMaze() {
        return maze;
    }

    public Graph<GeneticNode> getGraph() {
        return graph;
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

    public GeneticNode[][] getNodeArray() {
        return nodesArray;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getTargetIndex() {
        return targetIndex;
    }
}
