package ApplicationLogic.Algorithms.Genetic;

public class Population {

    //multiple chromosomes
    private final Chromosome[] chromosomes;
    private final GenerateAndEvaluatePath fitnessFunction;
    private final int[][] maze;

    public Population(int lengthChromosomes, int lengthGenes, int[][] maze) {
        //create the chromosomes
        chromosomes = new Chromosome[lengthChromosomes];
        this.maze = maze;
        //instantiate each of the chromosomes in the array
        for (int i = 0; i < lengthChromosomes; i++) {
            chromosomes[i] = new Chromosome(lengthGenes, maze);
        }

        fitnessFunction = new GenerateAndEvaluatePath();
    }

    public void createPopulation(double p) throws Exception {
        for (Chromosome chromosome : this.chromosomes) {
            chromosome.createChromosome(p);
        }
    }

    //find the fittest chromosome in the population
    public Chromosome getFittest() {
        double minFitnessValue = Double.MAX_VALUE;
        Chromosome bestChromosome = null;

        //if the current chromosome is less than the minimum, update min
        for (Chromosome chromosome : this.chromosomes) {
            if (chromosome.getFitnessValue() < minFitnessValue) {
                minFitnessValue = chromosome.getFitnessValue();
                bestChromosome = chromosome;
            }
        }

        return bestChromosome;
    }

    public Chromosome[] getChromosomes() {
        return this.chromosomes;
    }

    public Chromosome getSpecificChromosome(int index) {
        return this.chromosomes[index];
    }

    public void insertChromosome(int index, Chromosome chromosome) throws Exception {
        this.chromosomes[index] = chromosome;
        chromosome.setFitnessValue(fitnessFunction.buildAndAssessPath(chromosome.getAllGenes(), maze));
    }
}