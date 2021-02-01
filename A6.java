
package a6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import graph.Edge;
import graph.Node;

/** This class contains the solution to A6, shortest-path algorithm, <br>
 * and other methods for an undirected graph. */
public class A6 {

    /** Return the shortest path from node v to node last <br>
     * ---or the empty list if a path does not exist. <br>
     * Note: The empty list is a list with 0 elements ---it is not "null". */
    public static List<Node> shortestPath(Node v, Node last) {
        // Contains an entry for each node in the frontier set. The priority of a node
        // is the length of the shortest known path from v to the node using only settled
        // node except for the last node, which is in F
        HashMap<Node, NodeData> SandF= new HashMap<>();
        Heap<Node> F= new Heap<>(true);
        F.add(v, 0);
        NodeData vData= new NodeData(null, 0);
        SandF.put(v, vData);
        while (F.size != 0) {
            Node f= F.poll();
            int distance= SandF.get(f).dist;
            if (f == last) { return path(SandF, last); }
            List<Edge> fEdgeList= f.getExits();
            for (Edge e : fEdgeList) {
                Node w= e.getOther(f);
                int wgt= e.length;
                int d_w= distance + wgt;
                NodeData wData= new NodeData(f, d_w);
                if (SandF.containsKey(w) == false) {
                    SandF.put(w, wData);
                    F.add(w, d_w);
                } else if (distance + wgt < SandF.get(w).dist) {
                    SandF.put(w, wData);
                    F.changePriority(w, d_w);
                }
            }
        }
        return new LinkedList<>();
    }

    /** An instance contains information about a node: <br>
     * the Distance of this node from the start node and <br>
     * its Backpointer: the previous node on a shortest path <br>
     * from the first node to this node (null for the start node). */
    private static class NodeData {
        /** shortest known distance from the start node to this one. */
        private int dist;
        /** backpointer on path (with shortest known distance) from start node to this one */
        private Node bkptr;

        /** Constructor: an instance with dist d from the start node and<br>
         * backpointer p. */
        private NodeData(Node p, int d) {
            dist= d;     // Distance from start node to this one.
            bkptr= p;    // Backpointer on the path (null if start node)
        }

        /** return a representation of this instance. */
        @Override
        public String toString() {
            return "dist " + dist + ", bckptr " + bkptr;
        }
    }

    /** = the path from the start node to node last.<br>
     * Precondition: mapSF contains all the necessary information about<br>
     * ............. the path. */
    public static List<Node> path(HashMap<Node, NodeData> mapSF, Node last) {
        List<Node> path= new LinkedList<>();
        Node p= last;
        // invariant: All the nodes from p's successor to node last are in
        // path, in reverse order.
        while (p != null) {
            path.add(0, p);
            p= mapSF.get(p).bkptr;
        }
        return path;
    }

    /** Return the sum of the weights of the edges on path p. <br>
     * Precondition: pa contains at least 1 node. <br>
     * If 1 node, it's a path of length 0, i.e. with no edges. */
    public static int pathSum(List<Node> p) {
        synchronized (p) {
            Node w= null;
            int sum= 0;
            for (Node n : p) {
                if (w != null) sum= sum + w.getEdge(n).length;
                w= n;
            }
            return sum;
        }
    }

}
