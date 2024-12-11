import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day2024_10 {
    public static void main(String [] args) throws Exception {
        //score is the number of 9-height positions reachable from that trailhead via a hiking trail
        //A trailhead is any position that starts one or more hiking trails - here, these positions will always have height 0.
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_10.txt"));

        int[][] map = new int[lines.size()][lines.get(0).length()];

        Set<Point>  trailheads = new HashSet<>();

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j) - '0';

                if(map[i][j] == 0){
                    trailheads.add( new Point(j, i));
                }
            }
        }

        long result = trailheads.stream().mapToLong(t -> getScore2(t, map)).sum();
        System.out.println(result);

    }

    private static long getScore2(Point p, int[][] map){
        Queue<Point> q = new LinkedList<>();
        q.add(p);

        int score = 0;
        while (!q.isEmpty()){
            Point cur = q.poll();

            List<Point> next = getNextSteps(cur, map, new HashSet<>());

            for(Point n : next){
                if(map[n.y][n.x] == 9){
                    score++;
                } else {
                    q.add(n);
                }
            }
        }

        return score;

    }

    private static long getScore(Point p, int[][] map){
        Set<Point> visited = new HashSet<>();
        Queue<Point> q = new LinkedList<>();
        q.add(p);

        while (!q.isEmpty()){
            Point cur = q.poll();
            visited.add(cur);
            List<Point> next = getNextSteps(cur, map, visited);
            q.addAll(next);
        }

        return visited.stream().filter(v -> map[v.y][v.x] == 9).count();
    }

    private static List<Point> getNextSteps(Point p, int[][] map, Set<Point> visited){
        return List.of(
                getNext(p.x + 1, p.y, map, p),
                getNext(p.x - 1, p.y, map, p),
                getNext(p.x, p.y + 1, map, p),
                getNext(p.x, p.y - 1, map, p)
        ).stream().filter(o -> o.isPresent()).map(o -> o.get()).filter(n -> !visited.contains(n)).collect(Collectors.toList());
    }

    private static Optional<Point> getNext(int x, int y, int[][] map, Point from) {
        try {
            int score = map[from.y][from.x];
            int nextScore = map[y][x];

            if(nextScore - score == 1 ){
                return Optional.of(new Point(x, y));
            } else {
                return Optional.empty();
            }

        } catch (ArrayIndexOutOfBoundsException e){
            return Optional.empty();
        }

    }

}

