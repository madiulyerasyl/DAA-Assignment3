package daa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphGeneratorTest {

    @Test
    public void testUndirectedGraph() {

        Graph graph = GraphGenerator.undirectedGraph(100, 400);

        assertEquals(100, graph.size());
        assertEquals(400, graph.getEdges().size());
        assertFalse(graph.isDirected());

        Kruskal.Result result = Kruskal.mst(graph);

        assertEquals(1, result.components);
        assertEquals(99, result.edgeCount);
    }

    @Test
    public void testDirectedGraph() {

        Graph graph = GraphGenerator.directedGraph(100, 400);

        assertEquals(100, graph.size());
        assertEquals(400, graph.getEdges().size());
        assertTrue(graph.isDirected());

        Kosaraju.Result result = Kosaraju.scc(graph);

        assertTrue(result.components >= 1);
        assertTrue(result.components <= 100);

        // Kosaraju scans every edge twice
        assertEquals(800, result.edgeScans);
    }

    @Test
    public void testDAG() {

        Graph graph = GraphGenerator.dagGraph(100, 400);

        assertEquals(100, graph.size());
        assertEquals(400, graph.getEdges().size());

        Kahn.Result result = Kahn.topologicalSort(graph);

        assertFalse(result.hasCycle);
        assertEquals(100, result.order.size());
        assertEquals(400, result.edgeScans);
    }

    @Test
    public void testReproducibility() {

        Graph first = GraphGenerator.undirectedGraph(100, 400);
        Graph second = GraphGenerator.undirectedGraph(100, 400);

        for (int i = 0; i < 400; i++) {

            Graph.WeightedEdge a = first.getEdges().get(i);
            Graph.WeightedEdge b = second.getEdges().get(i);

            assertEquals(a.from, b.from);
            assertEquals(a.to, b.to);
            assertEquals(a.weight, b.weight);
        }
    }

    @Test
    public void testMSTAgreement() {

        Graph graph = GraphGenerator.undirectedGraph(1000, 4000);

        Kruskal.Result kruskal = Kruskal.mst(graph);
        Prim.Result prim = Prim.mst(graph);

        assertEquals(kruskal.totalWeight, prim.totalWeight);
        assertEquals(999, kruskal.edgeCount);
        assertEquals(999, prim.edgeCount);
    }
}