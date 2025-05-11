package AStar;

import static org.junit.jupiter.api.Assertions.*;

import ApplicationLogic.SearchSpace.AStar.AStarMaze;
import ApplicationLogic.SearchSpace.AStar.AStarNode;
import AccessMazes.GetMazeArray;
import org.junit.jupiter.api.Test;

/**
 * Test whether the maze creation is successful
 */
class AStarMazeTest {

    /**
     * Test maze initialization with empty maze array
     */
    @Test
    void test_Maze_Array_With_Constructor_Value() {
        try {
            AStarMaze maze = new AStarMaze(new int[][]{}, 400);

            // Try to access row and col
            System.out.println("Row: " + maze.getRowNumber());
            System.out.println("Column: " + maze.getColumnNumber());

            assertNull(maze.getGraph());
            assertNull(maze.getNodeArray());

            assertTrue(maze.isMazeArrayEmpty(maze.getMaze()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Test whether the nodes array has the same rows and columns
     * as the maze array.
     */
    @Test
    void test_maze_nodes_array() throws Exception {
        AStarMaze maze = new AStarMaze(GetMazeArray.Maze(20, 1), 400);
        assertTrue(maze.getNodeArray().length > 0);

        assertEquals(maze.getNodeArray().length, maze.getRowNumber());
        assertEquals(maze.getNodeArray()[0].length, maze.getColumnNumber());

        System.out.println("Row: " + maze.getRowNumber());
        System.out.println("Column: " + maze.getColumnNumber());
        System.out.println("Nodes row: " + maze.getNodeArray().length);
        System.out.println("Nodes Column: " + maze.getNodeArray()[0].length);
    }

    /**
     * Test node array is null with empty maze array
     */
    @Test
    void test_maze_nodes_array_2() {
        try {
            AStarMaze maze = new AStarMaze(new int[][]{}, 400);
            assertNull(maze.getNodeArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Test graph nodes count is equal to maze size (row * column)
     */
    @Test
    void test_maze_has_graph() {
        try {
            AStarMaze maze = new AStarMaze(GetMazeArray.Maze(20, 1), 400);
            System.out.println("Maze array count: " + maze.getRowNumber() * maze.getColumnNumber());

            assertTrue(maze.getGraph().getNodes().size() > 0);
            System.out.println("Graph nodes count: " + maze.getGraph().getNodes().size());

            assertEquals(maze.getRowNumber() * maze.getColumnNumber(), maze.getGraph().getNodes().size());

            maze = new AStarMaze(new int[][]{}, 400);
            assertNull(maze.getGraph());
            System.out.println("Graph nodes count_2: " + 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Test whether heuristic value is set and
     * greater than zero.
     */
    @Test
    void test_maze_graph_has_heuristic_value() throws Exception {
        AStarMaze maze = new AStarMaze(GetMazeArray.Maze(20, 1), 400);
        for (AStarNode node : maze.getGraph().getNodes()) {
            assertTrue(node.getHeuristicValue() >= 0);
            System.out.println("Node: " + node.toString());
        }
    }

    /**
     * Test start index and target index variable
     * for when maze array is null and not null.
     */
    @Test
    void test_start_target_index_from_maze() {
        try {
            AStarMaze maze = new AStarMaze(GetMazeArray.Maze(20, 1), 400);

            assertTrue(maze.getStartIndex() >= 0);
            System.out.println("Start index_1: " + maze.getStartIndex());

            assertTrue(maze.getTargetIndex() >= 0);
            System.out.println("Target index_1: " + maze.getTargetIndex());

            maze = new AStarMaze(new int[][]{}, 400);

            assertEquals(-1, maze.getStartIndex());
            System.out.println("Start index_2: " + maze.getStartIndex());

            assertEquals(-1, maze.getTargetIndex());
            System.out.println("Target index_2: " + maze.getTargetIndex());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}