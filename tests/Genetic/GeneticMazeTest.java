package Genetic;

import ApplicationLogic.SearchSpace.Genetic.GeneticMaze;
import AccessMazes.GetMazeArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Only testing whether the maze initialized
 */
class GeneticMazeTest {

    /**
     * Test maze initialization with empty maze array
     */
    @Test
    void test_Maze_Array_With_Constructor_Value() throws Exception {
        GeneticMaze maze = new GeneticMaze(new int[][]{});

        System.out.println("Row: " + maze.getRowNumber());
        System.out.println("Column: " + maze.getColumnNumber());

        assertNull(maze.getGraph());
        assertNull(maze.getNodeArray());

        assertTrue(maze.isMazeArrayEmpty(maze.getMaze()));
    }

    /**
     * Test whether the nodes array has same as row and column
     * of the maze array.
     */
    @Test
    void test_maze_nodes_array() throws Exception {
        GeneticMaze maze = new GeneticMaze(GetMazeArray.Maze(20, 1));
        assertTrue(maze.getNodeArray().length > 0);

        assertEquals(maze.getNodeArray().length, maze.getRowNumber());
        assertEquals(maze.getNodeArray()[0].length, maze.getColumnNumber());

        System.out.println("Row: " + maze.getRowNumber());
        System.out.println("Column: " + maze.getColumnNumber());
        System.out.println("Nodes row: " + maze.getNodeArray().length);
        System.out.println("Nodes Column: " + maze.getNodeArray()[0].length);
    }

    /**
     * Test if node array is null with empty maze array
     */
    @Test
    void test_maze_nodes_array_2() throws Exception {
        GeneticMaze maze = new GeneticMaze(new int[][]{});
        assertNull(maze.getNodeArray());
    }

    /**
     * Test graph nodes count is equal to maze size (row * column)
     */
    @Test
    void test_maze_has_graph() throws Exception {
        GeneticMaze maze = new GeneticMaze(GetMazeArray.Maze(20, 1));
        System.out.println("Maze array count: " + maze.getRowNumber() * maze.getColumnNumber());

        assertTrue(maze.getGraph().getNodes().size() > 0);
        System.out.println("Graph nodes count: " + maze.getGraph().getNodes().size());

        assertEquals(maze.getRowNumber() * maze.getColumnNumber(), maze.getGraph().getNodes().size());

        maze = new GeneticMaze(new int[][]{});
        assertNull(maze.getGraph());
        System.out.println("Graph nodes count_2: " + 0);
    }

    /**
     * Test start index and target index variable
     * for when maze array is null and not null.
     */
    @Test
    void test_start_target_index_from_maze() throws Exception {
        GeneticMaze maze = new GeneticMaze(GetMazeArray.Maze(20, 1));

        assertTrue(maze.getStartIndex() >= 0);
        System.out.println("Start index_1: " + maze.getStartIndex());

        assertTrue(maze.getTargetIndex() >= 0);
        System.out.println("Target index_1: " + maze.getTargetIndex());

        maze = new GeneticMaze(new int[][]{});

        assertEquals(-1, maze.getStartIndex());
        System.out.println("Start index_2: " + maze.getStartIndex());

        assertEquals(-1, maze.getTargetIndex());
        System.out.println("Target index_2: " + maze.getTargetIndex());
    }
}