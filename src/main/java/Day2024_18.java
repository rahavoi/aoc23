import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day2024_18 {
    public static void main(String[] args) throws Exception {
        List<Point> points = Files.readAllLines(Paths.get("resources/Day2024_18.txt")).stream().map(l ->
            new Point(Integer.parseInt(l.split(",")[1]), Integer.parseInt(l.split(",")[0]))
        ).toList();

        int min = 1024;
        int max = points.size();

        while (min < max) {
            int mid = (max + min) / 2;
            if(isPathExists(points, mid)){
                min = mid + 1;
            } else {
                max = mid - 1;
            }
        }

        System.out.println("First blocking byte: " + points.get(min - 1).y + "," + points.get(min - 1).x);
    }

    private static boolean isPathExists(List<Point> points, int dropNum){
        int[][] map = new int[71][71];

        for(int i = 0; i < dropNum; i++){
            map[points.get(i).x][points.get(i).y] = 1;
        }

        Point dest = new Point(map.length - 1, map[0].length - 1);
        Point start = new Point(0, 0);

        PriorityQueue<State> q = new PriorityQueue<>(Comparator.comparingLong(s -> s.cost));
        q.add(new State(start, 0L));

        Set<Point> visited = new HashSet<>();

        while(!q.isEmpty()){
            State s = q.poll();
            if(!visited.add(s.p)){
                continue;
            }

            if(s.p.equals(dest)){
                return true;
            }

            List<Point> neighbors = List.of(
                    new Point(s.p.x + 1, s.p.y),
                    new Point(s.p.x - 1, s.p.y),
                    new Point(s.p.x, s.p.y + 1),
                    new Point(s.p.x, s.p.y - 1)
            ).stream().filter(p -> !visited.contains(p) &&
                    p.x >= 0 &&
                    p.y >= 0 &&
                    p.x < map.length &&
                    p.y < map[0].length &&
                    map[p.y][p.x] != 1).toList();

            neighbors.forEach(n -> q.add(new State(n, s.cost + 1)));
        }
        return false;
    }

    public record State(Point p, long cost){}
    public record Point(int x, int y){}
}
