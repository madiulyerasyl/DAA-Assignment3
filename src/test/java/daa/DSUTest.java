package daa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class DSUTest {

    @Test
    public void testUnionAndFind() {

        DSU dsu = new DSU(5);

        assertEquals(5, dsu.components());

        dsu.union(0, 1);
        dsu.union(1, 2);

        assertEquals(dsu.find(0), dsu.find(2));
        assertEquals(3, dsu.components());

        dsu.union(3, 4);

        assertEquals(2, dsu.components());
        assertNotEquals(dsu.find(0), dsu.find(3));
    }

    @Test
    public void testDuplicateUnion() {

        DSU dsu = new DSU(3);

        assertTrue(dsu.union(0, 1));
        assertFalse(dsu.union(0, 1));

        assertEquals(2, dsu.components());
        assertEquals(1, dsu.successfulUnions);
    }

    @Test
    public void testRandomUnions() {

        int n = 100;
        DSU dsu = new DSU(n);

        int[] group = new int[n];

        for (int i = 0; i < n; i++) {
            group[i] = i;
        }

        Random random = new Random(42);

        for (int i = 0; i < 500; i++) {

            int a = random.nextInt(n);
            int b = random.nextInt(n);

            boolean sameGroup = group[a] == group[b];
            boolean merged = dsu.union(a, b);

            assertEquals(!sameGroup, merged);

            if (!sameGroup) {
                int oldGroup = group[b];
                int newGroup = group[a];

                for (int j = 0; j < n; j++) {
                    if (group[j] == oldGroup) {
                        group[j] = newGroup;
                    }
                }
            }

            int expectedComponents = 0;

            for (int j = 0; j < n; j++) {
                if (group[j] == j) {
                    expectedComponents++;
                }
            }

            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    assertEquals(group[x] == group[y],
                            dsu.find(x) == dsu.find(y));
                }
            }
        }
    }
}