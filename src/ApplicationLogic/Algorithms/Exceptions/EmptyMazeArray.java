package ApplicationLogic.Algorithms.Exceptions;

public class EmptyMazeArray extends Exception {

    public EmptyMazeArray() {
        super("Cannot initialize Maze. Null array found. Provide a 2D array.");
    }
}
