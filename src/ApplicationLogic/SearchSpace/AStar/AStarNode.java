package ApplicationLogic.SearchSpace.AStar;

import ApplicationLogic.SearchSpace.Common.MazeNode;

import java.util.ArrayList;
import java.util.List;

public class AStarNode extends MazeNode {

    private final List<AStarNode> neighbours;

    //heuristic information needed for each of the nodes
    private double heuristicValue;
    private double gValue;
    private double finalCost;
    private AStarNode parent;

    public AStarNode(int cellIndex) {
        super(cellIndex);
        this.neighbours = new ArrayList<>();
        this.heuristicValue = 0;
        this.gValue = 0;
        this.finalCost = 0;
        this.parent = null;
    }

    public double getHeuristicValue() {
        return this.heuristicValue;
    }

    public void setHeuristicValue(double heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public double getgValue() {
        return gValue;
    }

    public void setgValue(double gValue) {
        this.gValue = gValue;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }

    public AStarNode getParent() {
        return this.parent;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;
    }

    public void addNeighbour(AStarNode n) {
        this.neighbours.add(n);
    }

    public List<AStarNode> getNeighbours() {
        return neighbours;
    }

    public String toString() {
        String out = "Node " + getCellIndex() + " IsVisited: " + isVisited() + " Parent: " +
                (parent == null ? "null" : parent.getCellIndex()) + "\n";
        out += "HValue: " + heuristicValue + " GValue: " + gValue + " Neighbours: " + neighbours.size() + "\n";

        return out;
    }
}