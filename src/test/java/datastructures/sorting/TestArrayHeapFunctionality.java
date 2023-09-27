package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testBasicInsert() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 50; i++) {
            heap.insert(i);
            assertEquals(heap.size(), i + 1);
        }
        for (int i = 0; i < 50; i++) {
            assertEquals(i, heap.removeMin());
        }
        IPriorityQueue<Integer> heap2 = makeInstance();
        int size = 0;
        for (int i = 10; i > 0; i--) {
            heap2.insert(i);
            size++;
            assertEquals(heap2.size(), size);
        }
    }

    @Test(timeout=SECOND)
    public void testBasicRemove() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 50; i > 0; i--) {
            heap.insert(i);
        }
        assertEquals(50, heap.size());
        for (int i = 0; i < 50; i++) {
            assertEquals(heap.removeMin(), i+1);
        }
        assertEquals(0, heap.size());
    }

    @Test(timeout=SECOND)
    public void testEmptyRemove() {
        IPriorityQueue<Integer> heap = makeInstance();
        try {
            Integer val = heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException e) {
            assertEquals(0, heap.size());
        }
    }

    @Test(timeout=SECOND)
    public void testNullInsert() {
        IPriorityQueue<Integer> heap = makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(0, heap.size());
        }
    }

    @Test(timeout=SECOND)
    public void testEmptyPeek() {
        IPriorityQueue<Integer> heap = makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerExpression");
        } catch (EmptyContainerException e) {
            assertEquals(0, heap.size());
        }
    }

    @Test(timeout=SECOND)
    public void testPeek() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 10; i++) {
            heap.insert(i);
            assertEquals(heap.size(), i + 1);
        }
        assertEquals(0, heap.peekMin());
    }

    @Test(timeout=SECOND)
    public void testResizing() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 70; i++) {
            heap.removeMin();
        }
        assertEquals(100-70, heap.size());
        assertTrue(!heap.isEmpty());
        heap.insert(1);
        heap.insert(2);
        assertEquals(32, heap.size());
        heap.removeMin();
        assertEquals(31, heap.size());
    }

    @Test(timeout=SECOND)
    public void testNotFourChildren() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.removeMin();
        assertEquals(2, heap.size());
    }

    @Test(timeout=SECOND)
    public void testDuplicates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        heap.insert(2);
        heap.insert(1);
        heap.insert(1);
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testStringInput() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("a");
        heap.insert("b");
        heap.insert("c");
        heap.insert("d");
        assertEquals(4, heap.size());
        assertEquals("a", heap.removeMin());
        assertEquals("b", heap.removeMin());
    }

    @Test(timeout = SECOND)
    public void testRandomInput() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int x = 0; x < 1000; x++) {
            int newSize = (int) Math.random();
            for (int i = 0; i < newSize; i++) {
                heap.insert((int) Math.random());
            }
            for (int i = 0; i < newSize; i++) {
                assertEquals(heap.peekMin(), heap.removeMin());
            }
        }

    }
}
