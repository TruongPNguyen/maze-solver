package datastructures.concrete;

//import com.sun.net.httpserver.Filter;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.ISet;
import datastructures.interfaces.IEdge;
import datastructures.interfaces.IList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.NoPathExistsException;
//import misc.exceptions.NotYetImplementedException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends IEdge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated than usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of IEdge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the IEdge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.
    private int verticesNum;
    private int edgesNum;
    private IList<V> vertices;
    private IList<E> edges;
    private IDictionary<V, Vertex> weightedVertex;
    private IDictionary<Vertex, ISet<E>> adjacency;
    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.adjacency = new ChainedHashDictionary<>();
        this.weightedVertex = new ChainedHashDictionary<>();
        for (V vertex : vertices) {
            if (vertex == null) {
                throw new IllegalArgumentException();
            } else {
                weightedVertex.put(vertex, new Vertex(vertex));
                adjacency.put(weightedVertex.get(vertex), new ChainedHashSet<>());
            }
        }

        for (E edge : edges) {
            if (edge == null || edge.getWeight() < 0) {
                throw new IllegalArgumentException();
            } else if (!vertices.contains(edge.getVertex1()) || !vertices.contains(edge.getVertex2())) {
                throw new IllegalArgumentException();
            } else {
                Vertex vertex1 = weightedVertex.get(edge.getVertex1());
                Vertex vertex2 = weightedVertex.get(edge.getVertex2());
                ISet<E> edges1 = adjacency.get(vertex1);
                ISet<E> edges2 = adjacency.get(vertex2);
                edges1.add(edge);
                edges2.add(edge);
                adjacency.put(vertex1, edges1);
                adjacency.put(vertex2, edges2);
            }
        }

        this.vertices = vertices;
        this.edges = edges;
        this.verticesNum = vertices.size();
        this.edgesNum = edges.size();
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        if (set == null) {
            throw new IllegalArgumentException();
        }
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return verticesNum;
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edgesNum;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        IDisjointSet<V> vertexes = new ArrayDisjointSet<>();
        ISet<E> mst = new ChainedHashSet<>();
        for (V vertex : vertices) {
            vertexes.makeSet(vertex);
        }
        sort(edges, edges.size());

        for (E edge : edges) {
            if (vertexes.findSet(edge.getVertex1()) != vertexes.findSet(edge.getVertex2())) {
                mst.add(edge);
                vertexes.union(edge.getVertex1(), edge.getVertex2());
            }
        }

        return mst;
    }
    //mergesort algorithm for MST search
    private void sort(IList<E> list, int length) {
        if (length >= 2) {
            int midpoint = (length) / 2;

            IList<E> sub1 = new DoubleLinkedList<>();
            IList<E> sub2 = new DoubleLinkedList<>();

            for (int i = 0; i < midpoint; i++) {
                sub1.add(list.get(i));
            }
            for (int i = midpoint; i < list.size(); i++) {
                sub2.add(list.get(i));
            }

            sort(sub1, midpoint);
            sort(sub2, length - midpoint);

            merge(list, sub1, sub2);
        }
    }

    private void merge(IList<E> list, IList<E> sub1, IList<E> sub2) {
        int index1 = 0;
        int index2 = 0;
        int index = 0;

        while (index1 < sub1.size() && index2 < sub2.size()) {
            if (sub1.get(index1).getWeight() <= sub2.get(index2).getWeight()) {
                list.set(index, sub1.get(index1));
                index1++;
            } else {
                list.set(index, sub2.get(index2));
                index2++;
            }
            index++;
        }

        while (index1 < sub1.size() && sub1.get(index1) != null) {
            list.set(index, sub1.get(index1));
            index1++;
            index++;
        }

        while (index2 < sub2.size() && sub2.get(index2) != null) {
            list.set(index, sub2.get(index2));
            index2++;
            index++;
        }
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     * @throws IllegalArgumentException if start or end is null
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException();
        }
        IList<E> path = new DoubleLinkedList<>();
        if (start == end) {
            return path;
        }

        if (!vertices.contains(start) || !vertices.contains(end)) {
            throw new NoPathExistsException();
        }

        for (V vertex : vertices) {
            weightedVertex.get(vertex).distance = Double.POSITIVE_INFINITY;
            weightedVertex.get(vertex).predecessor = null;
            weightedVertex.get(vertex).predecessorEdge = null;
        }

        Vertex<V> weightedStart = weightedVertex.get(start);
        weightedStart.setDistance(0.0);
        IPriorityQueue<Vertex<V>> priorityQueue = new ArrayHeap<>();
        priorityQueue.insert(weightedStart);
        ISet<Vertex<V>> processed = new ChainedHashSet<>();

        while (!priorityQueue.isEmpty()) {
            Vertex u = priorityQueue.removeMin();
            while (processed.contains(u) && !priorityQueue.isEmpty()) {
                u = priorityQueue.removeMin();
            }
            if (u.getVertex().equals(end)) {
                break;
            }
            //System.out.println("vertex");
            V unweight = (V) u.getVertex();
            for (E edge : adjacency.get(u)) {
                //System.out.println("outgoing edges");
                V vert = edge.getOtherVertex(unweight);
                Vertex weighted = weightedVertex.get(vert);
                double oldDistance = weightedVertex.get(vert).getDistance();
                double newDist = u.getDistance() + edge.getWeight();
                if (newDist < oldDistance) {
                    weightedVertex.get(vert).distance = newDist;
                    weightedVertex.get(vert).predecessor = u;
                    weightedVertex.get(vert).predecessorEdge = edge;
                    priorityQueue.insert(weightedVertex.get(vert));
                }
                processed.add(u);
            }
        }

        Vertex currVertex = weightedVertex.get(end);
        if (currVertex.predecessorEdge == null) {
            //System.out.println("noooooo!");
            throw new NoPathExistsException();
        } else {
            while (currVertex.predecessor != null) {
                //System.out.println("yeet");
                path.add((E) (currVertex.predecessorEdge));
                currVertex = currVertex.predecessor;
            }
        }
        IList<E> reversePath = new DoubleLinkedList<>();
        System.out.println(path.size());
        int pathSize = path.size();
        for (int i = 0; i < pathSize; i++) {
             E temp = path.remove();
             //System.out.print(temp);
             reversePath.add(temp);
        }
        return reversePath;
    }

    private final class Vertex<V> implements Comparable<Vertex<V>> {
        private double distance;
        private Vertex predecessor;
        private E predecessorEdge;
        private V unweighted;

        private Vertex(V vertex) {
            this.predecessor = null;
            this.predecessorEdge = null;
            this.distance = 0;
            this.unweighted = vertex;
        }

        private void setDistance(double x) {
            this.distance = x;
        }

        private Vertex getPredecessor() {
            return this.predecessor;
        }

        private void setPredecessor(Vertex vertex) {
            this.predecessor = vertex;
        }

        private V getVertex() {
            return this.unweighted;
        }

        private double getDistance() {
            return this.distance;
        }

        public int compareTo(Vertex<V> otherVertex) {
            if (otherVertex.distance < this.distance) {
                return 1;
            } else if (otherVertex.distance > this.distance) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
