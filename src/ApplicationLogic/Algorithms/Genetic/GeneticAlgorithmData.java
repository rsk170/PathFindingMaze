package ApplicationLogic.Algorithms.Genetic;

import ApplicationLogic.SearchSpace.Genetic.GeneticMaze;
import ApplicationLogic.SearchSpace.Genetic.GeneticNode;

import java.util.List;

public class GeneticAlgorithmData {

    private GeneticMaze geneticMaze;
    private List<GeneticNode> nodesVisited;

    public GeneticAlgorithmData(GeneticMaze geneticMaze, List<GeneticNode> nodesVisited) {
        this.geneticMaze = geneticMaze;
        this.nodesVisited = nodesVisited;
    }

    public GeneticMaze getGeneticMaze() {
        return geneticMaze;
    }

    public void setGeneticMaze(GeneticMaze geneticMaze) {
        this.geneticMaze = geneticMaze;
    }

    public List<GeneticNode> getNodesVisited() {
        return nodesVisited;
    }

    public void setNodesVisited(List<GeneticNode> nodesVisited) {
        this.nodesVisited = nodesVisited;
    }
}