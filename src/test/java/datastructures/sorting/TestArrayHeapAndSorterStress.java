package datastructures.sorting;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.IList;
import misc.BaseTest;
import misc.Sorter;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapAndSorterStress extends BaseTest {

    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=10*SECOND)
    public void testHeapInsertStress() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 1000000; i++) {
            heap.insert(i);
        }
        assertEquals(1000000, heap.size());
    }

    @Test(timeout=10*SECOND)
    public void testHeapRemoveStress() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 100000; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 100000; i++) {
            assertEquals(i, heap.removeMin());
        }
        assertTrue(heap.size()==0);
    }

    @Test(timeout=10*SECOND)
    public void testHeapBoth() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 2000000; i++) {
            heap.insert(i);
        }
        assertEquals(2000000, heap.size());
        for (int i = 0; i < 1000000; i++) {
            assertEquals(i, heap.removeMin());
        }
        assertTrue(heap.size() == 1000000);
    }

    @Test(timeout=10*SECOND)
    public void testTopSortStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 1999999; i > 0; i--) {
            list.add(i);
        }
        IList<Integer> results = Sorter.topKSort(30000, list);
        assertEquals(30000, results.size());
        for (int i = 0; i < 30000; i++) {
            assertEquals(i + 1970000, results.get(i));
        }
    }

    @Test(timeout=10*SECOND)
    public void testTopSortStress2() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 799999; i > 0; i--) {
            list.add(i);
        }
        IList<Integer> results = Sorter.topKSort(90000, list);
        assertEquals(90000, results.size());
        for (int i = 0; i < 90000; i++) {
            assertEquals(710000 + i, results.get(i));
        }
    }
}
