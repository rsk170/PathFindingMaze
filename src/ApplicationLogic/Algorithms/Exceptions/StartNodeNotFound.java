package ApplicationLogic.Algorithms.Exceptions;

public class StartNodeNotFound extends Exception {

    public StartNodeNotFound() {
        super("Start node not found in the given maze.");
    }
}

