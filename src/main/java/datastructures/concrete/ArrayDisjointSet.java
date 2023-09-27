package datastructures.concrete;

//import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.ISet;

//import misc.exceptions.NotYetImplementedException;

/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private IDictionary<T, Integer> tree;
    private int index;
    private ISet<T> forest;

    public ArrayDisjointSet() {
        pointers = new int[40];
        forest = new ChainedHashSet<T>() {};
        tree = new ChainedHashDictionary<T, Integer>();
        index = 0;
    }

    @Override
    public void makeSet(T item) {
        if (forest.contains(item)) {
            throw new IllegalArgumentException();
        }
        // Resize if necessary, copy current pointer array over to bigger array
        if (index == pointers.length - 1) {
            int[] temp = new int[pointers.length * 2];
            for (int i = 0; i < pointers.length; i++) {
                temp[i] = pointers[i];
            }
            pointers =  temp;
        }
        forest.add(item); // forest keeps track of all current item
        tree.put(item, index); // tree keeps track of individual tree locations in pointer array
        pointers[index] = -1; // make set always create new root
        index++;
    }

    @Override
    public int findSet(T item) {
        if (!forest.contains(item)) {
            throw new IllegalArgumentException();
        }
        int currIndex = tree.get(item); // get index of the item
        int rootIndex = currIndex;
        // follow the indices of the pointer array until finds root
        while (pointers[rootIndex] > -1) {
            rootIndex = pointers[rootIndex];
        }
        // path compression optimization, set every index in path to root
        while (currIndex != rootIndex) {
            pointers[currIndex] = rootIndex;
            currIndex = pointers[currIndex];
        }
        return currIndex;
    }

    @Override
    public void union(T item1, T item2) {
        if (!forest.contains(item1) || !forest.contains(item2)) {
            throw new IllegalArgumentException();
        }
        int rep1 = findSet(item1); // get root of item 1
        int rep2 = findSet(item2); // get root of item 2
        if (rep1 == rep2) {
            return; // return if they are already in same set
        }
        //System.out.println(rep1);
        //System.out.println(rep2);

        // root indices hold rank information optimization
        int rank1 = (pointers[rep1] * -1) - 1;
        int rank2 = (pointers[rep2] * -1) - 1;
        //System.out.println(rank1);
        //System.out.println(rank2);

        // union by rank optimization
        if (rank1 > rank2) {
            pointers[rep2] = rep1;
        } else if (rank2 > rank1) {
            pointers[rep1] = rep2;
        } else { // arbitrary when equal
            pointers[rep1] = rep2;
            pointers[rep2]--;
        }
    }
}
