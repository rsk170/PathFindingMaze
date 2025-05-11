package ApplicationLogic.Algorithms.Genetic;

public class Chromosome {

    private final Object[] allGenes;
    private final int lengthGene;
    private final GenerateAndEvaluatePath fitnessFunction;
    private final int[][] maze;

    private double fitnessValue;

    public Chromosome(int lengthGene, int[][] maze) {
        this.allGenes = new Object[lengthGene];
        this.lengthGene = lengthGene;
        this.fitnessFunction = new GenerateAndEvaluatePath();
        this.maze = maze;
    }

    //initialize allGenes[]
    public void createChromosome(double probability) throws Exception {
        //go through all of the genes and if a random number is less than
        //the probability value, assign 0, otherwise assign 1
        for (int i = 0; i < this.lengthGene; i++) {
            if (Math.random() < probability) {
                this.allGenes[i] = 0;
            } else {
                this.allGenes[i] = 1;
            }
        }
        //calculate the fitness value for each of the chromosomes
        this.setFitnessValue(fitnessFunction.buildAndAssessPath(this.allGenes, maze));
    }

    public Object getSpecificGene(int index) {
        return this.allGenes[index];
    }

    //used in mutation
    public void setSpecificGene(int index, Object object) {
        this.allGenes[index] = object;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public int getLengthGene() {
        return lengthGene;
    }

    public Object[] getAllGenes() {
        return this.allGenes;
    }
}