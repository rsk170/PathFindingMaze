package ApplicationLogic.Algorithms.Genetic;

import ApplicationLogic.SearchSpace.Genetic.GeneticNode;
import Presentation.Common.MainFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneticAlgorithm {

    public GenerateAndEvaluatePath fitnessFunction;
    private final MainFrame mainFrame;
    private final int[][] mazeArray;

    private static final String newline = System.lineSeparator();

    //controlling parameters
    private int sizePopulation;
    private int lengthGenes;
    private int maxLimitGenerations;     //limit for how many iterations
    private int tournamentSize;
    private double crossoverRate;   //between 0-1
    private double mutationRate;   //between 0-1
    private boolean allowElitism;
    private boolean showResults;
    private final List<Float> oldFitnessValues;
    private boolean stopNow = false;
    private long finishedAtMSec = 0;

    public GeneticAlgorithm(int[][] mazeArray, MainFrame parent) {
        mainFrame = parent;
        this.mazeArray = mazeArray;
        this.oldFitnessValues = new ArrayList<>();
        this.fitnessFunction = new GenerateAndEvaluatePath();
    }

    public void performSearch() {
        long milliStart = System.currentTimeMillis();

        Thread t = new Thread(() -> {
            while (!stopNow) {
                boolean found = false;
                try {
                    found = startSearch();
                } catch (Exception e) {
                    e.printStackTrace();
                    stopNow = true;
                }

                if (found) {
                    stopNow = true;
                    mainFrame.setTopInfo("SUCCESS!");
                    System.out.println("SUCCESS!");
                   finishedAtMSec = (System.currentTimeMillis() - milliStart);
                    double seconds = finishedAtMSec / 1000.0;

                    mainFrame.setTopInfo("Searching finished. Path found in: " + (seconds) + " seconds" + " which is equal to  " + (finishedAtMSec) + "milliseconds");
                    System.out.println("Searching finished. Path found in: " + (seconds) + " seconds");
                    System.out.println("\nClose window to exit the program");
                }
            }
        });
        t.start();
    }

    private boolean startSearch() throws Exception {
        boolean found;

        int[] sizePopulation = {100, 200, 300, 400};
        int[] lengthGenes = {200, 250, 300, 400};
        int[] maxLimitGenerations = {400, 500, 1000, 2000};
        double[] crossOverRate = {0.75,  0.8, 0.85, 0.9};
        double[] mutationRate = {0.15, 0,25, 0.35, 0.45};;

        int initialSize = 100;
        int initialLength = 200;
        int initialLimitGenerations = 400;
        double initialCrossOverRate = 0.75;
        double initialMutationRate = 0.15;

        for (int i = 0; i < 4; i++)
        {
            if(mazeArray[0].length < 21)
            {
                sizePopulation[i] = initialSize + (i * 100);     //100, 200, 300, 400
                lengthGenes[i] = initialLength + (i * 100);      //200, 300, 400, 500
                maxLimitGenerations[i] = initialLimitGenerations + ((i*2) * 100); // 400, 600, 800, 1000
                crossOverRate[i] = initialCrossOverRate + (double)(i / 10.0);  // 0.75, 0.85, 0,95
                mutationRate[i] = initialMutationRate + (double)(i /10.0);    //0.15, 0.25, 0,35, 0.45
            }
            else if(mazeArray[0].length < 30)
            {
                sizePopulation[i] = initialSize + ((i+2) * 100);     //300, 400, 500
                lengthGenes[i] = initialLength + ((i+1) * 100);      //300, 400, 500
                maxLimitGenerations[i] = initialLimitGenerations + (((i*2)+2) * 100); // 600, 800, 1000
                crossOverRate[i] = initialCrossOverRate + (double)((i+1) / 10.0);  //  0.85, 0,95
                mutationRate[i] = initialMutationRate + (double)((i+1) /10.0);    //0.25, 0,35, 0.45
            } else
            {
                sizePopulation[i] = initialSize + ((i+2) * 100);     //300, 500, 600,
                lengthGenes[i] = initialLength + ((i+3) * 100);      //500, 600, 700
                maxLimitGenerations[i] = initialLimitGenerations + (((i+4) * 100)); // 800, 900, 1000,
                crossOverRate[i] = initialCrossOverRate + (double)((i+2) / 10.0);  // 0,95
                mutationRate[i] = initialMutationRate + (double)((i+1) /10.0);    //0.25, 0,35, 0.45
            }
        }

        for (int generation : maxLimitGenerations) {
            for (double crossOver : crossOverRate) {
                for (double mutation : mutationRate) {
                    for (int genes : lengthGenes) {
                        for (int population : sizePopulation) {
                            if (!stopNow) {
                                String strinfo = "Running GA with population: " + population +
                                        ", genes: " + genes +
                                        ", generation: " + generation +
                                        ", crossOver: " + crossOver +
                                        ", mutation: " + mutation;

                                mainFrame.setTopInfo(strinfo);
                                mainFrame.setShowInfo(newline + strinfo + newline);

                                System.out.println("\nRunning GA with population:" + population +
                                        ", genes:" + genes +
                                        ", generation:" + generation +
                                        ", crossOver:" + crossOver +
                                        ", mutation:" + mutation +
                                        newline);
                                try {
                                    this.sizePopulation = population; // population
                                    this.lengthGenes = genes; //gene no, maximum length of a path will be geneNo/2
                                    this.maxLimitGenerations = generation; // generations
                                    this.tournamentSize = 30; // tournament size
                                    this.crossoverRate = crossOver; // crossover rate
                                    this.mutationRate = mutation; // mutation rate
                                    this.allowElitism = true; // allow elitism
                                    this.showResults = true; // show results
                                    found = findSearchPath();        //show results

                                    if (found)
                                        return true;
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }

        mainFrame.setShowInfo("A path to the target couldn't be found this time. \n" +
                "       The algorithm will run again with different parameters.\n");

        System.out.println("A path to the target couldn't be found this time.");
        System.out.println("       The algorithm will run again with different parameters.");
        return false;
    }

    // Selection - return the best chromosome of the newly created population
    private Chromosome selection(Population oldPopulation) throws Exception {
        //create an empty population, get the best one from the old population,
        //add it to the new population
        Population newPopulation = new Population(this.tournamentSize, this.lengthGenes, mazeArray);

        for (int i = 0; i < tournamentSize; i++) {
            //get random chromosomes from the old population and add them to new population
            int randomIndex = (int) (Math.random() * oldPopulation.getChromosomes().length);
            newPopulation.insertChromosome(i, oldPopulation.getSpecificChromosome(randomIndex));
        }

        return newPopulation.getFittest();
    }

    // Crossover - single index crossover
    private Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        Chromosome child = new Chromosome(parent1.getLengthGene(), mazeArray);

        int crossoverPoint = (int) Math.floor(Math.random() * child.getLengthGene());

        //get all the genes one by one
        //first get genes from 0 to the crossover point from the first parent
        for (int i = 0; i < crossoverPoint; i++) {
            child.setSpecificGene(i, parent1.getSpecificGene(i));
        }

        //second get genes from crossover point to the end from the second parent
        for (int i = crossoverPoint; i < child.getLengthGene(); i++) {
            child.setSpecificGene(i, parent2.getSpecificGene(i));
        }

        return child;
    }

    // Mutation
    private void mutate(Chromosome chromosome) {
        for (int i = 0; i < chromosome.getLengthGene(); i++) {
            if (Math.random() <= this.mutationRate) {
                int gene = (int) Math.round(Math.random());
                chromosome.setSpecificGene(i, gene);
            }
        }
    }

    //pass a population and return a population
    //keep only one individual
    private Population elitism(Population oldPopulation, Population newPopulation) throws Exception {
        Chromosome chromosome = oldPopulation.getFittest();
        newPopulation.insertChromosome(0, chromosome);
        return newPopulation;
    }

    private boolean findSearchPath() throws Exception {
        //create initial population
        Population oldPopulation = new Population(this.sizePopulation, this.lengthGenes, this.mazeArray);

        //initialize the chromosomes with genes
        oldPopulation.createPopulation(0.5);   //50% chance for one gene to be either 0 or 1

        Population newPopulation;

        // Main loop
        for (int i = 0; i < this.maxLimitGenerations; i++) {
            newPopulation = new Population(this.sizePopulation, this.lengthGenes, this.mazeArray);

            //keep some of the chromosomes
            int elitismAmount = 0;

            // Elitism
            if (this.allowElitism) {
                newPopulation = elitism(oldPopulation, newPopulation);
                elitismAmount = 1;
            }


            //skip the one that we are gonna keep from the elitism
            for (int j = elitismAmount; j < this.sizePopulation; j++) {
                // Select
                Chromosome parent1 = this.selection(oldPopulation);
                Chromosome parent2 = this.selection(oldPopulation);

                // Crossover
                Chromosome child = crossover(parent1, parent2);

                // Mutation
                if (Math.random() < this.mutationRate) {
                    this.mutate(child);
                }

                //decide whether to add this individual to the population
                if (Math.random() < this.crossoverRate) {
                    newPopulation.insertChromosome(j, child);
                }
                //otherwise add either parent one or parent two
                else {
                    if (Math.random() < 0.5) {
                        newPopulation.insertChromosome(j, parent1);
                    } else {
                        newPopulation.insertChromosome(j, parent2);
                    }
                }
            }

            //override the current pop with the new one
            oldPopulation = newPopulation;
            float fitness = (float) newPopulation.getFittest().getFitnessValue();

            mainFrame.setShowInfo("In generation# " + i + " Fittest has : " + fitness + " fitness value\n");
            if (showResults) {
                System.out.println("In generation# " + i + " Fittest has : " +
                        fitness + " fitness value");
            }

            fitnessFunction.displayPath = true;
            Object[] path = oldPopulation.getFittest().getAllGenes();
            fitnessFunction.buildAndAssessPath(path, mazeArray);

            GeneticAlgorithmData data = new GeneticAlgorithmData(fitnessFunction.geneticMaze, fitnessFunction.nodesVisitedd);
            mainFrame.addGeneticDraw(data);

            this.oldFitnessValues.add(fitness);

            //if we found best path, stop the program
            if (fitnessFunction.reached) {
                Object[] finalPath = oldPopulation.getFittest().getAllGenes();
                fitnessFunction.displayPath = true;
                fitnessFunction.buildAndAssessPath(finalPath, mazeArray);

                data.setGeneticMaze(fitnessFunction.geneticMaze);
                data.setNodesVisited(fitnessFunction.nodesVisitedd);
                mainFrame.addGeneticDraw(data);

                mainFrame.setShowInfo("Path Found in " + i + " generations with Fitness value of: " + fitness + newline);
                List<GeneticNode> temp = fitnessFunction.nodesVisitedd.stream() .distinct() .collect(Collectors.toList());
                int count = temp.size();
                mainFrame.setShowInfo("Path contains " + (count - 2) + " nodes" + newline);

                System.out.println("Path Found in " + i + " generations with Fitness value of: " + fitness);
                return true;
            }

            //if last 10 fitness values are same, stop the program
            if (this.oldFitnessValues.size() > 10) {
                int start = this.oldFitnessValues.size() - 10;
                int end = this.oldFitnessValues.size() - 1;

                if (this.oldFitnessValues.get(start).equals(this.oldFitnessValues.get(end))) {
                    Object[] finalPath = oldPopulation.getFittest().getAllGenes();
                    fitnessFunction.displayPath = true;
                    fitnessFunction.buildAndAssessPath(finalPath, mazeArray);

                    data.setGeneticMaze(fitnessFunction.geneticMaze);
                    data.setNodesVisited(fitnessFunction.nodesVisitedd);
                    mainFrame.addGeneticDraw(data);

                    mainFrame.setShowInfo("A path to the target couldn't be found this time. \n" +
                            "       The algorithm will run again with different parameters\n");

                    System.out.println("A path to the target couldn't be found this time.");
                    System.out.println("       The algorithm will run again with different parameters.");

                    return false;
                }
            }
        }

        Object[] finalPath = oldPopulation.getFittest().getAllGenes();
        fitnessFunction.displayPath = true;
        fitnessFunction.buildAndAssessPath(finalPath, mazeArray);

        GeneticAlgorithmData data = new GeneticAlgorithmData(fitnessFunction.geneticMaze, fitnessFunction.nodesVisitedd);
        mainFrame.addGeneticDraw(data);

        mainFrame.setShowInfo("A path to the target couldn't be found this time. \n" +
                "       The algorithm will run again with different parameters.\n");

        System.out.println("A path to the target couldn't be found this time.");
        System.out.println("       The algorithm will run again with different parameters.");

        return false;
    }

    public void stopSearch() {
        this.stopNow = true;
    }
}