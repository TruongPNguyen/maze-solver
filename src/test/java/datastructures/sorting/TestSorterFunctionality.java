package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Sorter;
import org.junit.Test;

//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSorterFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testNegativeKThrowsException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        try {
            Sorter.topKSort(-1, list);
            fail();
        } catch (IllegalArgumentException ex) {
            // Do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testNullInputThrowsException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        try {
            Sorter.topKSort(1, null);
            fail();
        } catch (IllegalArgumentException ex) {
            // Do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testKIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        IList<Integer> results = Sorter.topKSort(0, list);
        assertEquals(0, results.size());
    }

    @Test(timeout=SECOND)
    public void testInputIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> results = Sorter.topKSort(1, list);
        assertEquals(0, results.size());
    }

    @Test(timeout=SECOND)
    public void testKSmallerThanInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        IList<Integer> results = Sorter.topKSort(5, list);
        assertEquals(5, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertEquals(i + 5, results.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testKLargerThanInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        IList<Integer> results = Sorter.topKSort(20, list);
        assertEquals(10, results.size());
        for (int i = 0; i < results.size(); i++) {
            assertEquals(i, results.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testStringInput() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        IList<String> results = Sorter.topKSort(2, list);
        assertEquals(2, results.size());
        assertEquals("c", results.get(0));
        assertEquals("d", results.get(1));
    }
}
