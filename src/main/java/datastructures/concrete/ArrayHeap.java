package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int numItems;


    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.numItems = 0;
        this.heap = makeArrayOfT(20);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
        if (numItems == 0) {
            throw new EmptyContainerException();
        }
        T root = heap[0];
        T lastItem = heap[numItems-1];
        T smallChild = heap[1];

        int smallIndex = 1;
        int placeIndex = 0;

        int childIndex = (placeIndex * 4) + 1;

        while (placeIndex < numItems && childIndex < numItems) {
            for (int i = 0; i < 4; i++) {
                //System.out.println(heap[childIndex+i]);
                if ((childIndex + i) < numItems && heap[childIndex + i] != null) {
                    if (heap[childIndex + i].compareTo(smallChild) < 0) {
                        smallChild = heap[childIndex + i];
                        smallIndex = childIndex + i;
                    }
                }
            }
            if (smallChild.compareTo(lastItem) < 0) {
                heap[placeIndex] = smallChild;
                heap[smallIndex] = lastItem;
                placeIndex = smallIndex;
                childIndex = placeIndex * 4 + 1;
                if (childIndex < numItems) {
                    smallChild = heap[childIndex];
                    smallIndex = childIndex;
                }
            } else {
                heap[placeIndex] = lastItem;
                break;
            }
        }
        heap[numItems-1] = null;
        numItems--;

        return root;
    }

    @Override
    public T peekMin() {
        //return first index since that is the min value
        if (heap[0] == null) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        //resize if necessary
        if (numItems == heap.length) {
            T[] newHeap = makeArrayOfT(heap.length * 2);
            for (int i = 0; i < heap.length; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
        //if empty list, add to end
        if (numItems == 0) {
            heap[numItems] = item;
        } else {
            int parentIndex =  (numItems-1)/4;
            int prevIndex = numItems;
            if (heap[parentIndex].compareTo(item) > 0) {
                while (heap[parentIndex].compareTo(item) > 0) {
                    heap[prevIndex] = heap[parentIndex];
                    heap[parentIndex] = item;
                    prevIndex = parentIndex;
                    parentIndex = (parentIndex - 1) / 4;
                }
            } else {
                heap[numItems] = item;
            }
        }
        numItems++;
    }

    @Override
    public int size() {
        return numItems;
    }
}
