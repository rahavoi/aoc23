import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day2024_20 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_20.txt"));

        char[][] map = new char[lines.size()][lines.get(0).length()];
        Point start = null;
        Point dest = null;
        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(0).length(); j++){
                map[i][j] = lines.get(i).charAt(j);

                if(map[i][j] == 'S'){
                    start = new Point(j,i);
                    map[i][j] = '.';
                }

                if(map[i][j] == 'E'){
                    dest = new Point(j,i);
                    map[i][j] = '.';
                }
            }
        }

        //Calculate costs from all points on track to dest
        Map<Point, Integer> costs = new HashMap<>();
        costs.put(dest, Integer.MAX_VALUE);
        Queue<CostToDest> q = new LinkedList<>();
        Set<Point> track = new HashSet<>();
        q.add(new CostToDest(dest, 0));

        while(!q.isEmpty()){
            CostToDest cur = q.poll();

            if(!track.add(cur.p)){
                continue;
            }
            costs.put(cur.p, cur.cost);
            getNeighbors(cur.p, map).forEach(n -> q.add(new CostToDest(n, cur.cost + 1)));
        }

        int countP1 = 0;
        int countP2 = 0;
        for(Point p : track){
            countP1 += getCheatingOpportunities(p, map, costs, 2);
            countP2 += getCheatingOpportunities(p, map, costs, 20);
        }

        System.out.println(countP1);
        System.out.println(countP2);

    }

    private static List<Point> getNeighbors(Point p, char[][] map){
        return List.of(
                new Point(p.x + 1, p.y),
                new Point(p.x - 1, p.y),
                new Point(p.x, p.y + 1),
                new Point(p.x, p.y - 1)
        ).stream().filter(n -> map[n.y][n.x] != '#').toList();
    }

    private static List<Point> getNeighborsIncludingWalls(Point p, char[][] map){
        return List.of(
                new Point(p.x + 1, p.y),
                new Point(p.x - 1, p.y),
                new Point(p.x, p.y + 1),
                new Point(p.x, p.y - 1)
        ).stream().filter(n -> n.x >= 0 && n.x < map.length && n.y >= 0 && n.y < map[0].length).toList();
    }

    private static int getCheatingOpportunities(Point p, char[][] map, Map<Point, Integer> costs, int maxMoves){
        Set<Point> cheatsSaving100 = new HashSet<>();

        PriorityQueue<PointMove> q = new PriorityQueue<>((pm1, pm2) -> pm2.movesLeft - pm1.movesLeft);
        q.add(new PointMove(p, maxMoves));

        Set<Point> visited = new HashSet<>();

        while(!q.isEmpty()) {
            PointMove cur = q.poll();

            if(!visited.add(cur.p)){
                continue;
            }

            if(cur.movesLeft == 0){
                continue;
            }

            List<Point> neighbors = getNeighborsIncludingWalls(cur.p, map);
            List<Point> track = neighbors.stream().filter(n -> map[n.y][n.x] == '.').toList();

            //Tool me a while to realize you can go through multiple walls in one cheat :(
            track.forEach(t -> {
                int savings = costs.get(p) - costs.get(t) - maxMoves + cur.movesLeft - 1;
                if(savings >= 100){
                    cheatsSaving100.add(t);
                }
            });
            neighbors.forEach(w -> q.add(new PointMove(w, cur.movesLeft - 1)));
        }

        return cheatsSaving100.size();
    }

    record Point(int x, int y){}
    record CostToDest(Point p, int cost){}
    record PointMove(Point p, int movesLeft){}
}
