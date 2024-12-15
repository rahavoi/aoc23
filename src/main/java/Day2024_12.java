import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2024_12 {
    public static void main(String[] args) throws Exception {
        char[][] map = init();

        Set<Point> visited = new HashSet<>();
        List<List<Point>> regions = new ArrayList<>();
        long total = 0;
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                Point p = new Point(j, i);
                if(visited.contains(p)){
                    continue;
                }
                List<Point> region = new ArrayList<>();

                Queue<Point> q = new LinkedList<>();
                q.add(new Point(j, i));
                long perimeter = 0;
                long area = 0;

                while (!q.isEmpty()){
                    Point cur = q.poll();
                    visited.add(cur);
                    region.add(cur);
                    area++;
                    List<Point> neighbors = getNeighbors(map, cur);
                    perimeter += getPerimeterIncrementP1(cur, neighbors);
                    q.addAll(neighbors.stream().filter(n -> !visited.contains(n)).toList());
                    visited.addAll(neighbors);
                }

                regions.add(region);

                //System.out.println("A region of " + type + " plants with price " + area + " * " + perimeter + " = " + perimeter * area);
                total += perimeter * area;
            }
        }

        //print(map);
        System.out.println("Part1: " + total);
        part2(regions, map);
    }

    private static void part2(List<List<Point>> regions, char[][] map) {
        long total = regions.stream().mapToLong(r -> {
            long area = r.size();

            long sides = r.stream().mapToLong(p -> {
                long corners = 0;
                char c = map[p.y][p.x];
                //Outer corners:
                corners += getNeighbor(map, p, p.x - 1, p.y).isEmpty() && getNeighbor(map, p, p.x, p.y - 1).isEmpty() ? 1 : 0 ;
                corners += getNeighbor(map, p, p.x + 1, p.y).isEmpty() && getNeighbor(map, p, p.x, p.y - 1).isEmpty() ? 1 : 0 ;
                corners += getNeighbor(map, p, p.x  - 1, p.y).isEmpty() && getNeighbor(map, p, p.x, p.y + 1).isEmpty() ? 1 : 0 ;
                corners += getNeighbor(map, p, p.x  + 1, p.y).isEmpty() && getNeighbor(map, p, p.x, p.y + 1).isEmpty() ? 1 : 0 ;

                //Inner Corners:
                corners += getNeighbor(map, p, p.x - 1, p.y).isPresent() && getNeighbor(map, p, p.x, p.y - 1).isPresent() && getNeighbor(map, p, p.x - 1, p.y - 1).isEmpty() ? 1 : 0 ;
                corners += getNeighbor(map, p, p.x + 1, p.y).isPresent() && getNeighbor(map, p, p.x, p.y - 1).isPresent() & getNeighbor(map, p, p.x + 1, p.y - 1).isEmpty() ? 1 : 0 ;
                corners += getNeighbor(map, p, p.x  - 1, p.y).isPresent() && getNeighbor(map, p, p.x, p.y + 1).isPresent() && getNeighbor(map, p, p.x - 1, p.y + 1).isEmpty() ? 1 : 0 ;
                corners += getNeighbor(map, p, p.x  + 1, p.y).isPresent() && getNeighbor(map, p, p.x, p.y + 1).isPresent() && getNeighbor(map, p, p.x + 1, p.y + 1).isEmpty()  ? 1 : 0 ;

                return corners;
            }).sum();

            return area * sides;

        }).sum();

        System.out.println("Part2: " + total);
    }

    private static long getPerimeterIncrementP1(Point p, List<Point> neighbors) {
        return Stream.of(
                new Point(p.x, p.y + 1),
                new Point(p.x, p.y - 1),
                new Point(p.x + 1, p.y),
                new Point(p.x - 1, p.y)
        ).filter(n -> !neighbors.contains(n)).count();
    }

    private static List<Point> getNeighbors(char[][] map, Point p) {
        return Stream.of(
                getNeighbor(map, p, p.x + 1, p.y),
                getNeighbor(map, p, p.x - 1, p.y),
                getNeighbor(map, p, p.x, p.y + 1),
                getNeighbor(map, p, p.x, p.y - 1)
        ).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

    }

    private static Optional<Point> getNeighbor(char[][] map, Point p, int x, int y){
        try {
            if(map[y][x] != map[p.y][p.x]){
                return Optional.empty();
            }
            return Optional.of(new Point(x,y));
        } catch (ArrayIndexOutOfBoundsException e){
            return Optional.empty();
        }
    }

    private static char[][] init() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_12.txt"));
        char[][] map = new char[lines.size()][lines.get(0).length()];

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j);
            }
        }

        return map;
    }
    public record Point(int x, int y){}
}
