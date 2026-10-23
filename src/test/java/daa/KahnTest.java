package daa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KahnTest {

    @Test
    public void testDAG() {

        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        Kahn.Result result = Kahn.topologicalSort(graph);

        assertFalse(result.hasCycle);
        assertEquals(4, result.order.size());
        assertEquals(4, result.edgeScans);

        // Check topological order
        int[] position = new int[4];

        for (int i = 0; i < result.order.size(); i++) {
            position[result.order.get(i)] = i;
        }

        for (Graph.WeightedEdge edge : graph.getEdges()) {
            assertTrue(position[edge.from] < position[edge.to]);
        }
    }

    @Test
    public void testCycleDetection() {

        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        Kahn.Result result = Kahn.topologicalSort(graph);

        assertTrue(result.hasCycle);
        assertTrue(result.order.size() < graph.size());
    }

    @Test
    public void testDisconnectedDAG() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        Kahn.Result result = Kahn.topologicalSort(graph);

        assertFalse(result.hasCycle);
        assertEquals(5, result.order.size());
        assertEquals(2, result.edgeScans);
    }

    @Test
    public void testSingleVertex() {

        Graph graph = new Graph(1, true);

        Kahn.Result result = Kahn.topologicalSort(graph);

        assertFalse(result.hasCycle);
        assertEquals(1, result.order.size());
        assertEquals(0, result.edgeScans);
    }

    @Test
    public void testUndirectedGraph() {

        Graph graph = new Graph(3, false);

        assertThrows(IllegalArgumentException.class,
                () -> Kahn.topologicalSort(graph));
    }

    @Test
    public void testSelfLoop() {

        Graph graph = new Graph(2, true);

        graph.addEdge(0, 0, 1);
        graph.addEdge(0, 1, 1);

        Kahn.Result result = Kahn.topologicalSort(graph);

        assertTrue(result.hasCycle);
    }
}