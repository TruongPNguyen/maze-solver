package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int arraySize;
    private int max;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.arraySize = 0;
        this.max = 30;
        this.chains = makeArrayOfChains(max);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        int keyHash;
        if (key != null) {
            keyHash = Math.abs(key.hashCode());
        } else {
            keyHash = 0;
        }
        keyHash = keyHash % max;
        IDictionary<K, V> dict = chains[keyHash];
        if (dict != null && dict.containsKey(key)) {
            //gets if key is in dict
            return dict.get(key);
        } else {    //un-found key exception
            throw new NoSuchKeyException();
        }
    }

    @Override
    public void put(K key, V value) {
        int keyHash;
        if (key != null) {
            keyHash = Math.abs(key.hashCode());
        } else {
            keyHash = 0;
        }
        keyHash = keyHash % max;
        if (chains[keyHash] == null) {  //adds new dictionary to chain point
            IDictionary<K, V> dict = new ArrayDictionary<>();
            dict.put(key, value);
            chains[keyHash] = dict;
            this.arraySize++;
        } else {  //puts new value into existing dictionary
            IDictionary<K, V> dict = chains[keyHash];
            if (!dict.containsKey(key)) {
                this.arraySize++;
            }
            dict.put(key, value);
        }

        double lambda = (double) this.arraySize / (double) this.max;
        if (lambda > 3.0) { // threshold value should be 1-3
            this.max *= 2; // resizing method is doubling
            IDictionary<K, V>[] newChains = makeArrayOfChains(max);
            int row = 0;
            while (row < max/2) { // rehash all the key-pairs
                if (chains[row] != null) {
                    Iterator<KVPair<K, V>> iter = chains[row].iterator();
                    while (iter.hasNext()) {
                        KVPair<K, V> pair = iter.next();
                        K tempKey = pair.getKey();
                        if (key != null) {
                            keyHash = Math.abs(tempKey.hashCode()) % max;
                        } else {
                            keyHash = 0;
                        }
                        if (newChains[keyHash] == null) { // if new dictionary is needed
                            newChains[keyHash] = new ArrayDictionary<K, V>();
                        }
                        newChains[keyHash].put(tempKey, pair.getValue());
                    }
                }
                row++;
            }
            this.chains = newChains;
        }
    }

    @Override
    public V remove(K key) {
        int keyHash = 0;
        if (key != null) {
            keyHash = Math.abs(key.hashCode());
        }
        keyHash = keyHash % this.max;
        if (chains[keyHash] == null) {
            throw new NoSuchKeyException();
        } else {
            IDictionary<K, V> dict = chains[keyHash];
            if (dict != null && dict.containsKey(key)) {
                this.arraySize--;
                return chains[keyHash].remove(key);
            } else {
                throw new NoSuchKeyException();
            }
        }
    }

    @Override
    public boolean containsKey(K key) {
        int keyHash;
        if (key != null) {
            keyHash = Math.abs(key.hashCode());
        } else {
            keyHash = 0;
        }
        keyHash = keyHash % max;
        if (chains[keyHash] == null) {
            return false;
        } else {
            IDictionary<K, V> dict = chains[keyHash];
            try { dict.get(key); }
            catch(NoSuchKeyException e) {
                return false;
            }
            return true;
        }
        // throw new NotYetImplementedException();
    }

    @Override
    public int size() {
        int sum = 0;
        for (int i = 0; i < chains.length; i++) {
            if (chains[i] != null) {
                sum += chains[i].size();
            }
        }
        return sum;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int size;
        private int count;
        private Iterator<KVPair<K, V>> iter;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.size = chains.length;
            this.count = 0;
            int index = 0;
            while (chains[index] == null && index < size-1) {
                index++;
                count++;
            }
            if (chains[index] == null) {
                iter = null;
            } else {
                this.iter = chains[index].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (iter != null) {
                if (iter.hasNext()) { // true if there's an element in the current row
                    return true;
                } else { // if not, continuously check the next row
                    int index = count;
                    while (index < size - 1) {
                        index++;
                        if (chains[index] != null) {
                            //count = index;
                            return true;
                        }
                    }
                    return false; // checked all rows, found no element
                }
            } else {
                return false;
            }
        }

        @Override
        public KVPair<K, V> next() {
            if (hasNext()) { //if there is an element somewhere
                if (iter.hasNext()) { // if it's in the current row
                    return iter.next();
                } else { // if not go to next row non empty row
                    count++;
                     while (count < (size - 1) && chains[count] == null) {
                         this.count++;
                     }
                    this.iter = chains[count].iterator();
                    return iter.next();
                }
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
