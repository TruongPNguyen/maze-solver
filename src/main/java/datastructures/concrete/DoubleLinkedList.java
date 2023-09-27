package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (front == null) { //if empty list create first element
            front = new Node<>(item);
            back = front;
        } else { // Normal case: update old back to be new back.prev and add new back
            Node<T> curr = back;
            curr.next = new Node<>(back, item, null);
            back = curr.next;
        }
        size++;

    }

    @Override
    public T remove() {
        if (front == null) {    //empty list case
            throw new EmptyContainerException();
        }
        T data = back.data; //save back data value for return later
        if (size == 1) { // Only one node case
            front = null;
            back = null;
        } else { // Normal case: set back to back.prev
            back = back.prev;
            back.next = null;
        }
        size--; //update size
        return data;

    }

    @Override
    public T get(int index) {
        Node<T> curr = this.front;
        if (index < 0 || index >= this.size()) {    //check for valid index input
            throw new IndexOutOfBoundsException();
        }
        while (index > 0) { //get to index
            index--;
            curr = curr.next;
        }
        return curr.data;   //returns data
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || size <= index) {   //checks for valid index input
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {   //first item set case
            if (size == 1) {//if there is only one element in the list
                front = new Node<>(null, item, null);
                back = front;
            } else {    //for first item sets of size not equal to one
                front = new Node<>(null, item, front.next);
                front.next.prev = front;
            }
        } else {    //default case
            Node<T> curr = front;
            while (index > 1) {
                curr = curr.next;
                index--;
            }
            curr.next = new Node<>(curr, item, curr.next.next);
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || size + 1 <= index) {   //check for valid index input
            throw new IndexOutOfBoundsException();
        }
        if (index == size) { // Insert at end case
            add(item);
        } else if (index == 0) { // Insert at start case
            front = new Node<>(null, item, front);
            front.next.prev = front;
            size++;
        } else { // Normal case
            if (index > size/2) {   //parse from the back node if index is past halfway for efficiency
                int fromEnd = size - index;
                Node<T> curr = back;
                while (fromEnd > 1) {
                    curr = curr.prev;
                    fromEnd--;
                }
                curr.prev = new Node<>(curr.prev, item, curr);
                curr.prev.prev.next = curr.prev;
            } else {    //parse normally
                Node<T> curr = front;
                while (index > 1) {
                    curr = curr.next;
                    index--;
                }
                curr.next = new Node<>(curr.prev, item, curr.next);
                curr.next.next.prev = curr.next;
            }
            size++; //update size
        }
    }

    @Override
    public T delete(int index) {
        if (index >= this.size() || index < 0) {    //check for valid index input
            throw new IndexOutOfBoundsException();
        }

        if (this.size == 1 || index == size-1) {   //cases: only 1 item or last item in pair
            return remove();
        } else if (index == 0) {    //first item delete
            T data = front.data;
            front = front.next;
            front.prev = null;
            size--; //update size is inside because of remove method in if statement
            return data;
        } else {    //default case
            if (index > size/2) {   //same as insert: check for past halfway for efficiency
                int fromEnd = size-index;   //begins to parse from back
                Node<T> curr = back;
                while (fromEnd > 1) {
                    curr = curr.prev;
                    fromEnd--;
                }
                T data = curr.data;
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                size--;
                return data;
            } else {
                Node<T> curr = front;   //parses from front if index is smaller than size/2
                while (index > 0) {
                    curr = curr.next;
                    index--;
                }
                T data = curr.data;
                curr.next.prev = curr.prev;
                curr.prev.next = curr.next;
                size--; //update size
                return data;
            }
        }

    }

    @Override
    public int indexOf(T item) {
        int index = 0;  //finds index of data value or returns -1 if not found
        Node<T> curr = front;
        while (curr != null) {
            if (item == null) { //null input check
                if (curr.data == item) {
                    return index;
                }
            } else {
                if (item.equals(curr.data)) {
                    return index;
                }
            }
            index++;
            curr = curr.next;

        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;   //returns size of pairs
    }

    @Override
    public boolean contains(T other) {
        Node<T> curr = this.front;
        while (curr != null) {
            if (other == null) {    //null input management
                if (other == curr.data) {
                    return true;
                }
            } else {
                if (other.equals(curr.data)) {
                    return true;
                }
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            if (this.current != null) {
                return true;
            }
            return false;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            T value = current.data;
            current = current.next;
            return value;
        }
    }
}
