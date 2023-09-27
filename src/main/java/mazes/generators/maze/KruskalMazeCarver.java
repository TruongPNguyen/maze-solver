package mazes.generators.maze;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.Graph;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Wall;
import mazes.entities.Room;
//import misc.exceptions.NotYetImplementedException;
import java.util.Random;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        Random rand = new Random();
        ISet<Wall> walls = new ChainedHashSet<>();
        walls = maze.getWalls();
        for (Wall wall : walls) {
            double randomWeight = rand.nextDouble();
            wall.setDistance(randomWeight);
        }
        ISet<Room> rooms = new ChainedHashSet<>();
        rooms = maze.getRooms();
        Graph<Room, Wall> graph = new Graph<Room, Wall>(rooms, walls);
        ISet<Wall> wallsToRemove = new ChainedHashSet<>();
        wallsToRemove = graph.findMinimumSpanningTree();
        for (Wall wall : walls) {
            wall.resetDistanceToOriginal();
        }
        return wallsToRemove;
    }
}
