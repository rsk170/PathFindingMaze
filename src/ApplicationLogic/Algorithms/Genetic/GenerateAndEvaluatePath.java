package ApplicationLogic.Algorithms.Genetic;

import ApplicationLogic.SearchSpace.Genetic.GeneticMaze;
import ApplicationLogic.SearchSpace.Genetic.GeneticNode;
import ApplicationLogic.SearchSpace.Common.Graph;

import java.util.ArrayList;
import java.util.List;

public class GenerateAndEvaluatePath {

    public boolean displayPath = false;
    public boolean reached = false;
    public List<GeneticNode> nodesVisitedd = new ArrayList<>();
    public GeneticMaze geneticMaze;

    public List<GeneticNode> getNodesVisitedd() {
        return nodesVisitedd;
    }

    //find distance of each node from target node
    private static float findDistance(int currentNodeIndex, int finishNodeIndex, int mazeColumns) {
        int x1 = currentNodeIndex / mazeColumns;
        int y1 = (currentNodeIndex % mazeColumns) - 1;

        int x2 = finishNodeIndex / mazeColumns;
        int y2 = (finishNodeIndex % mazeColumns) - 1;

        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    //evaluate how good a path is and calculate for each chromosome
    //use the genes to show the path, i.e the movement directions
    public double buildAndAssessPath(Object[] allGenes, int[][] maze_) throws Exception {
        //every pair of genes indicates the movement direction - {0,0} - up {0,1} - right  {1,0} - left {1,1} - down
        //if there are 10 genes -> 5 nodes (direction of movement in 5 consecutive nodes)
        GeneticMaze maze = new GeneticMaze(maze_);
        Graph<GeneticNode> graph = maze.getGraph();

        int lengthArray = allGenes.length / 2;

        //movement directions, stored in a decimal array
        int[] decimalArray = new int[lengthArray];

        int index = 0;
        //go through all genes, extract each pair and convert them to decimal numbers(convert binary to decimal)
        for (int i = 0; i < allGenes.length; i += 2) {
            if ((int) allGenes[i] == 0 && (int) allGenes[i + 1] == 0) {
                decimalArray[index] = 0;
            }

            if ((int) allGenes[i] == 0 && (int) allGenes[i + 1] == 1) {
                decimalArray[index] = 1;
            }

            if ((int) allGenes[i] == 1 && (int) allGenes[i + 1] == 0) {
                decimalArray[index] = 2;
            }

            if ((int) allGenes[i] == 1 && (int) allGenes[i + 1] == 1) {
                decimalArray[index] = 3;
            }
            index++;
        }

        //use the decimals to make a path and the evaluate it
        List<GeneticNode> allGeneticNodes = graph.getNodes();
        List<GeneticNode> allNeighbours;
        List<GeneticNode> nodesVisited = new ArrayList<>();
        int startNodeIndex = maze.getStartIndex() - 1;

        //start making the path using the genes
        GeneticNode startGeneticNode = allGeneticNodes.get(startNodeIndex);

        int finishNodeIndex = maze.getTargetIndex();

        int mazeColumns = maze.getColumnNumber();

        for (int j : decimalArray) {
            nodesVisited.add(startGeneticNode);
            startGeneticNode.setVisited(true);

            allNeighbours = startGeneticNode.getNodeNeighbours();

            //get to a dead end and there's no way to go(no neighbours)
            if (allNeighbours.isEmpty()) {
                throw new IllegalAccessError("There is no way to go!");
            }

            //make a decision based on the decimal array
            if (j == 0) {
                //if node is visited, then skip it
                //stay until you find an unvisited node
                if (!allNeighbours.get(0).isVisited()) {
                    startGeneticNode = allNeighbours.get(0);
                }
            } else if (allNeighbours.size() >= 2 && j == 1) {
                if (!allNeighbours.get(1).isVisited()) {
                    startGeneticNode = allNeighbours.get(1);
                }
            } else if (allNeighbours.size() >= 3 && j == 2) {
                if (!allNeighbours.get(2).isVisited()) {
                    startGeneticNode = allNeighbours.get(2);
                }
            } else if (allNeighbours.size() == 3 && j == 3) {
                if (!allNeighbours.get(2).isVisited()) {
                    startGeneticNode = allNeighbours.get(2);
                }
            } else if (allNeighbours.size() >= 3 && j == 3) {
                if (!allNeighbours.get(3).isVisited()) {
                    startGeneticNode = allNeighbours.get(3);
                }
            }
        }

        // fitness function to calculate the average distance of each node on the path to the target node
        float sumDistance = 0;
        for (GeneticNode geneticNode : nodesVisited) {
            int nodeIndex = geneticNode.getCellIndex();
            float dist = findDistance(nodeIndex, finishNodeIndex, mazeColumns);
            sumDistance += dist;
        }
        float fitnessValue = sumDistance / (nodesVisited.size() * 2);
        fitnessValue += findDistance(nodesVisited.get(nodesVisited.size() - 1).getCellIndex(), finishNodeIndex, mazeColumns);

        if (isFound(nodesVisited, finishNodeIndex, mazeColumns)) {
            reached = true;

        }
        if (displayPath) {
            this.geneticMaze = maze;
            this.nodesVisitedd = nodesVisited;
            displayPath = false;
        }
        return fitnessValue;
    }

    private boolean isFound(List<GeneticNode> nodesVisited, int finishNodeIndex, int mazeColumns) {
        int min_ind = 0;
        float min_dis = 999999;

        //indicates the closest block to the target which was in out path
        //get the minimum index which was closest to the target
        for (GeneticNode geneticNode : nodesVisited) {
            float dis = findDistance(geneticNode.getCellIndex(), finishNodeIndex, mazeColumns);
            if (dis < min_dis) {
                min_ind = geneticNode.getCellIndex();
                min_dis = dis;
            }
        }

        //find where the target is
        //conditions that if it's the same (the final path and the target is the same or in the surroundings
        int x1 = min_ind / mazeColumns;
        int y1 = (min_ind % mazeColumns) - 1;

        int x2 = finishNodeIndex / mazeColumns;
        int y2 = (finishNodeIndex % mazeColumns) - 1;

        if (x1 == x2 && y1 == y2)
            return true;
        else if (x1 == x2 && y1 == y2 - 1)
            return true;
        else if (x1 == x2 && y1 == y2 + 1)
            return true;
        else if (x1 == x2 - 1 && y1 == y2)
            return true;
        else if (x1 == x2 + 1 && y1 == y2)
            return true;
        else if (x1 == x2 + 1 && y1 == y2 + 1)
            return true;
        else if (x1 == x2 - 1 && y1 == y2 - 1)
            return true;
        else if (x1 == x2 + 1 && y1 == y2 - 1)
            return true;
        else return x1 == x2 - 1 && y1 == y2 + 1;
    }
}