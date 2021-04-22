import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Main m = new Main();
        try {
            m.solve1();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int t, n, m, M;
    List<Integer> A,B,C;
    int MaxFlow = 1 << 30;

    public void solve1() throws IOException {
        Scanner cin = new Scanner(System.in);
        while(cin.hasNext()) {
            t = cin.nextInt();
            for (int i = 0; i < t; i++) {
                n = cin.nextInt();
                m = cin.nextInt();
                M = cin.nextInt();
                A = new ArrayList<>();
                B = new ArrayList<>();
                C = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    int a = cin.nextInt();
                    int b = cin.nextInt();
                    int c = cin.nextInt();
                    A.add(a);
                    B.add(b);
                    C.add(c);
                }
            }
        }
    }

    // 使用链式前向星进行存图
    public class NcfxyGraph2  {
        public class Edge {
            Integer to, value;
            Edge next;
            Edge reverseEdge;   // 反向边
            public Edge(int to, int value) {
                this.to = to;
                this.value = value;
                this.next = null;
            }
        }

        private List<Edge> edgeList;
        private List<Edge> head;
        int INF = 1 << 30;

        // 网络流算法里的专有变量
        private int startPoint, endPoint;
        private List<Integer> bfsLevel;
        private List<NcfxyGraph2.Edge> currentEdgeOptimization; // cur用于当前弧优化标记增广起点

        public NcfxyGraph2() {
            this.edgeList = new ArrayList<>();
            this.head = new ArrayList<>();
        }

        public void addPoint(int totalPointNumber) {
            this.head = new ArrayList<>(totalPointNumber);
            for (int i = 0; i < totalPointNumber; i++) {
                this.head.add(null);
            }
        }

        public Edge addEdge(int from, int to, int value) {
            Edge edge = new Edge(to, value);
            edgeList.add(edge);
            edge.next = head.get(from);
            head.set(from, edge);
            return edge;
        }

        public void makeReverse(Edge edge1, Edge edge2) {
            edge1.reverseEdge = edge2;
            edge2.reverseEdge = edge1;
        }

        public boolean dinicBfs() {
            bfsLevel = new ArrayList<>();
            currentEdgeOptimization = new ArrayList<>();
            for (int i = 0; i < this.head.size(); i++) {
                bfsLevel.add(-1);
                currentEdgeOptimization.add(this.head.get(i));
            }
            Queue<Integer> queue = new LinkedList<>();
            queue.add(startPoint);
            this.bfsLevel.set(startPoint, 0);
            while (!queue.isEmpty()) {
                int currentPoint = queue.poll();
                for (Edge edge = this.head.get(currentPoint); edge != null; edge = edge.next) {
                    int to = edge.to;
                    int weight = edge.value;
                    if (weight > 0 && this.bfsLevel.get(to) == -1) {
                        this.bfsLevel.set(to, this.bfsLevel.get(currentPoint) + 1);
                        queue.add(to);
                    }
                }
            }
            return this.bfsLevel.get(endPoint) != -1;
        }

        public int dinicDfs(int currentPoint, int flowLeft) {
            if (currentPoint == endPoint) {
                return flowLeft;
            }
            int remain = flowLeft;
            for (Edge edge = this.currentEdgeOptimization.get(currentPoint); edge != null && remain > 0; edge = edge.next) {
                this.currentEdgeOptimization.set(currentPoint, edge);       // 弧优化，记录当前遍历到的弧
                int to = edge.to;
                int weight = edge.value;
                if (weight > 0 && this.bfsLevel.get(to) == this.bfsLevel.get(currentPoint) + 1) {   // 往层数高的方向增广
                    int findDfs = dinicDfs(to, Math.min(weight, remain));    // 进一步深度搜索找最大的可增广流量
                    remain -= findDfs;
                    edge.value -= findDfs;
                    if (edge.reverseEdge != null) {
                        edge.reverseEdge.value += findDfs;
                    }
                }
            }
            return flowLeft - remain;
        }

        public int dinic() {
            int ans = 0;
            this.startPoint = 0;
            this.endPoint = this.head.size() -1;
            while(dinicBfs()) {
                ans += dinicDfs(startPoint, INF);
            }
            return ans;
        }

    }
}