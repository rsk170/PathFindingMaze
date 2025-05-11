package ApplicationLogic.Algorithms.Exceptions;

public class TargetNodeNotFound extends Exception {

    public TargetNodeNotFound() {
        super("Target Node not found in the given maze.");
    }
}

