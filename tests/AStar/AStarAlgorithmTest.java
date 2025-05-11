package AStar;

import ApplicationLogic.Algorithms.Exceptions.EmptyMazeArray;
import ApplicationLogic.Algorithms.Exceptions.StartNodeNotFound;
import ApplicationLogic.Algorithms.Exceptions.TargetNodeNotFound;
import ApplicationLogic.SearchSpace.AStar.AStarMaze;
import ApplicationLogic.Algorithms.AStar.AStarAlgorithm;
import ApplicationLogic.Algorithms.Exceptions.PathNotFound;
import AccessMazes.GetMazeArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AStarAlgorithmTest {

    int[][] testMaze_1 = {
            { -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };

    int[][] testMaze_2 = {
            { -1, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };


    int[][] testMaze_3 = {
            { -1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 9 }
    };

    /**
     * Test optimal path for default maze array.
     */
    @Test
    void test_optimal_path() throws Exception {
        AStarAlgorithm algo = new AStarAlgorithm(GetMazeArray.Maze(20, 1), 400);
        try {
            algo.performSearch();
        } catch (PathNotFound pathNotFound) {
            pathNotFound.printStackTrace();
        }
        assertNotNull(algo.getData().getOptimalPath());
        assertTrue(algo.getData().getOptimalPath().size() > 1);
        System.out.println("Optimal path size: " + algo.getData().getOptimalPath().size());
    }

    /**
     * Test whether optimal path found or not when
     * there is no target (9) specified
     * Try to get the graph and it's nodes to check whether they exist.
     */
    @Test
    void test_optimal_path_with_no_target()  {
        try {
            AStarAlgorithm algo = new AStarAlgorithm(testMaze_1, 400);
            AStarMaze maze = algo.getData().getMaze();
            assertEquals(1, maze.getRowNumber());
            assertEquals(testMaze_1[0].length, maze.getColumnNumber());
            assertNotNull(maze.getGraph());
            assertEquals(0, maze.getGraph().getNodes().size());
            assertEquals(-1, maze.getTargetIndex());
            algo.performSearch();
            assertNotNull(algo.getData().getOptimalPath());
            assertEquals(0, algo.getData().getOptimalPath().size());
        } catch (TargetNodeNotFound | StartNodeNotFound | PathNotFound | EmptyMazeArray ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Test optimal path when the target is very close to the
     * start point. Also how many nodes needs to be visited.
     */
    @Test
    void test_optimal_path_with_minimum_target() throws Exception {
        AStarAlgorithm algo = new AStarAlgorithm(testMaze_2, 400);
        AStarMaze maze = algo.getData().getMaze();
        assertEquals(1, maze.getRowNumber());
        assertEquals(testMaze_1[0].length, maze.getColumnNumber());
        assertNotNull(maze.getGraph());
        assertTrue(maze.getGraph().getNodes().size() > 0);
        assertEquals(2, maze.getTargetIndex());

        try {
            algo.performSearch();
        } catch (PathNotFound pathNotFound) {
            pathNotFound.printStackTrace();
        }
        assertNotNull(algo.getData().getOptimalPath());
        assertTrue(algo.getData().getOptimalPath().size() > 0);
        System.out.println("Optimal path size: " + algo.getData().getOptimalPath().size());
    }

    /**
     * Test optimal path when a maze array is blocked with walls.
     * Also the optimal path array is empty.
     */
    @Test
    void test_optimal_path_with_block_target() throws Exception {
        AStarAlgorithm algo = new AStarAlgorithm(testMaze_3, 400);
        AStarMaze maze = algo.getData().getMaze();
        assertEquals(1, maze.getRowNumber());
        assertEquals(testMaze_1[0].length, maze.getColumnNumber());
        assertNotNull(maze.getGraph());
        assertTrue(maze.getGraph().getNodes().size() > 0);
        assertEquals(13, maze.getTargetIndex());

        try {
            algo.performSearch();
        } catch (PathNotFound pathNotFound) {
            pathNotFound.printStackTrace();
        }
        assertNotNull(algo.getData().getOptimalPath());
        assertTrue(algo.getData().getOptimalPath().size() <= 0);
        System.out.println("Optimal path size: " + algo.getData().getOptimalPath().size());
    }

    /**
     * Test default maze array for visited node array is not null.
     */
    @Test
    void test_nodes_visited() throws Exception {
        AStarAlgorithm algo = new AStarAlgorithm(GetMazeArray.Maze(20, 1), 400);
        try {
            algo.performSearch();
        } catch (PathNotFound pathNotFound) {
            pathNotFound.printStackTrace();
        }
        assertNotNull(algo.getData().getNodesVisited());
        System.out.println("Nodes visited: " + algo.getData().getNodesVisited().size());
    }

    /**
     * Test how many nodes are visited when target and start point are close
     * to each other.
     */
    @Test
    void test_nodes_visited_minimum_target() throws Exception {
        AStarAlgorithm algo = new AStarAlgorithm(testMaze_2, 400);
        try {
            algo.performSearch();
        } catch (PathNotFound pathNotFound) {
            pathNotFound.printStackTrace();
        }
        assertNotNull(algo.getData().getNodesVisited());
        System.out.println("Nodes visited: " + algo.getData().getNodesVisited().size());
    }

    /**
     * Test visited nodes array is not null when target is blocked.
     */
    @Test
    void test_nodes_visited_block_target() throws Exception {
        AStarAlgorithm algo = new AStarAlgorithm(testMaze_3, 400);
        try {
            algo.performSearch();
        } catch (PathNotFound pathNotFound) {
            pathNotFound.printStackTrace();
        }
        assertNotNull(algo.getData().getNodesVisited());
        System.out.println("Nodes visited: " + algo.getData().getNodesVisited().size());
    }
}
