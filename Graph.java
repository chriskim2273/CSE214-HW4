import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    private final double[][] adjacencyMatrix;
    private Graph(double[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public static Graph fromMatrixString(String s) {
        Scanner scanner = new Scanner(s);
        ArrayList<ArrayList<Double>> edges = new ArrayList<>();

        //corner case
        if(s.equals(""))
            return new Graph(new double[0][0]);

        int count = 0;
        while (scanner.hasNextLine()) {
            Scanner scanner2 = new Scanner(scanner.nextLine());
            edges.add(new ArrayList<Double>());
            while (scanner2.hasNext()) {
                edges.get(count).add(Double.parseDouble(scanner2.next()));
            }
            count++;
        }

        double[][] graph = new double[edges.size()][edges.get(0).size()];
        for(int i = 0; i< edges.size(); i++){
            for(int j = 0; j < edges.get(i).size(); j++){
                graph[i][j] = edges.get(i).get(j);
            }
        }

        return new Graph(graph);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                builder.append(String.format("%1.2f ", adjacencyMatrix[i][j]));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private boolean isCyclic(double[][] tree, boolean[] checked, int i, int vertex) {
        checked[i] = true;
        for (int j = 0; j < adjacencyMatrix.length; j++) {
            if (tree[i][j] == 0)
                continue;
            if (!checked[j]) {
                return isCyclic(tree, checked, j, i);
            } else if (j != vertex) {
                return true;
            }
        }
        return false;
    }

    private boolean isCyclic(double[][] matrix) {
        return isCyclic(matrix, new boolean[adjacencyMatrix.length], 0, 0);
    }

    private int getMinimum(boolean[] checked, double[] matrix) {
        int vertex = adjacencyMatrix.length;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if(matrix[i] == 0)
                continue;
            if (!checked[i]) {
                if (min > matrix[i]) {
                    min = matrix[i];
                    vertex = i;
                }
            }
        }
        return vertex;
    }

    private boolean allInTree(boolean[] vertices) {
        for (Boolean value : vertices) {
            if (!value) {
                return value;
            }
        }
        return true;
    }

    public Graph getTree() {
        double[][] tree = new double[adjacencyMatrix.length][adjacencyMatrix[0].length];
        boolean[] vertices = new boolean[adjacencyMatrix.length];

        boolean mst_found = false;

        //Start at 0 (first vertex)
        vertices[0] = true;

        while (!mst_found) {
            boolean[] checked = vertices.clone();
            int vertex_one = 0;
            int vertex_two = -1;
            double minimum = Double.MAX_VALUE;

            boolean isCyclic = true;

            while(isCyclic){
                minimum = Double.MAX_VALUE;
                for (int i = 0; i < adjacencyMatrix.length; i++) {
                    if (vertices[i]) {
                        int vertex = getMinimum(checked, adjacencyMatrix[i]);
                        if(vertex == adjacencyMatrix.length)
                            continue;
                        if(adjacencyMatrix[i][vertex] < minimum) {
                            vertex_one = i;
                            vertex_two = vertex;
                            minimum = adjacencyMatrix[i][vertex];
                        }
                    }
                }
                checked[vertex_two] = true;

                isCyclic = isCyclic(tree);
            }
            tree[vertex_two][vertex_one] = minimum;
            tree[vertex_one][vertex_two] = minimum;
            vertices[vertex_two] = true;

            mst_found = allInTree(vertices);
        }

        return new Graph(tree);
    }

    private static final String s =
            "0.0 0.0 0.5 0.7 0.0 0.0\n" +
            "0.0 0.0 0.7 0.0 1.0 0.0\n" +
            "0.5 0.7 0.0 1.0 0.0 0.9\n" +
            "0.7 0.0 1.0 0.0 0.0 0.0\n" +
            "0.0 1.0 0.0 0.0 0.0 0.7\n" +
            "0.0 0.0 0.9 0.0 0.7 0.0";

    public static void main(String... args) {
        // you can assume that incorrect strings will not be provided as input,
        // i.e., any input string will be a valid adjacency matrix
        // representation of an undirected weighted graph
        Graph g = Graph.fromMatrixString(s);
        System.out.println(g.getTree());
        //System.out.println(g);
    }
}