package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    private int max;
    private Pair<K, V> target;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        this.size = 0;
        this.max = 29;
        this.target = null;
        this.pairs = makeArrayOfPairs(max);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        /* for (Pair<K, V> item : pairs) {
            if (item.key == key) {
                return item.value;
            }
        }
        throw new NoSuchKeyException(); */

        for (int i = 0; i < size; i++) {
            if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        /* if (!this.containsKey(key)) {
            Pair<K, V> add = new Pair<>(key, value);
            pairs[this.size-1] = add;
            this.size++;
        } */
        if (size >= max) {
            max  *= 2;
            Pair<K, V>[] temp = makeArrayOfPairs(max);
            for (int i = 0; i < size; i++) {
                temp[i] = pairs[i];
            }
            pairs = temp;
        }
        if (containsKey(key)) {
            for (int i = 0; i < size; i++) {
                if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
                    pairs[i] = new Pair<K, V>(key, value);
                }
            }
        } else {
            pairs[size] = new Pair<K, V>(key, value);
            size++;
        }
    }

    @Override
    public V remove(K key) {
        /* if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        int index = indexOf(key);
        V value = pairs[index].value;
        while (index < size) {
            pairs[index] = pairs[index+1];
            index++;
        }
        pairs[index] = null;
        return value; */
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        for (int i = 0; i < size; i++) {
            if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
                target = pairs[i];
                pairs[i] = pairs[size - 1];
                break;
            }
        }
        size--;
        return target.value;
    }

    @Override
    public boolean containsKey(K key) {
        /* for (Pair<K, V> item : pairs) {
            if (item.key == key) {
                return true;
            }
        }
        return false; */
        for (int i = 0; i < size; i++) {
            if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<K, V>(pairs, size);
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int size;
        private int count;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
            this.count = 0;
        }

        public boolean hasNext() {
            return count <= (size-1);
        }

        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Pair<K, V> temp = pairs[count];
            count++;
            KVPair<K, V> nextPair = new KVPair<K, V>(temp.key, temp.value);
            return nextPair;
        }
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
            //throw new NotYetImplementedException();
        }
    }
}
