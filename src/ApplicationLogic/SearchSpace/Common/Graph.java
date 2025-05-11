package ApplicationLogic.SearchSpace.Common;

import java.util.ArrayList;
import java.util.List;

public class Graph<T> {

    private List<T> nodes;

    public Graph() {
        nodes = new ArrayList<>();
    }

    public List<T> getNodes() {
        return nodes;
    }

    public void setNodes(List<T> nodes) {
        this.nodes = nodes;
    }
}

