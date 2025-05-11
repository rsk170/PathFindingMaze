package ApplicationLogic.SearchSpace.Genetic;

import ApplicationLogic.SearchSpace.Common.MazeNode;

import java.util.ArrayList;
import java.util.List;

public class GeneticNode extends MazeNode {

    private final List<GeneticNode> geneticNodeNeighbours;

    public GeneticNode(int cellIndex) {
        super(cellIndex);
        this.geneticNodeNeighbours = new ArrayList<>();
    }

    public void addNeighbour(GeneticNode n) {
        this.geneticNodeNeighbours.add(n);
    }

    public List<GeneticNode> getNodeNeighbours() {
        return geneticNodeNeighbours;
    }
}