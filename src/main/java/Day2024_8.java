import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day2024_8 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_8.txt"));

        char[][] map = new char[lines.size()][lines.get(0).length()];
        Map<Character, List<Point>> antennas = new HashMap<>();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j);
                if(map[i][j] != '.') {
                    List<Point> points = antennas.getOrDefault(map[i][j], new ArrayList<>());
                    points.add(new Point(j, i));
                    antennas.put(map[i][j], points);
                }
            }
        }

        Map<Character, Set<Point>> antiNodes = new HashMap<>();

        //Now for each pair of the same antennas calculate antinode locations.
        for(Map.Entry<Character, List<Point>> e : antennas.entrySet()){
            char a = e.getKey();
            List<Point> locations = e.getValue();

            for(int i = 0; i < locations.size(); i++){
                for(int j = i + 1; j < locations.size(); j++){
                    Point p1 = locations.get(i);
                    Point p2 = locations.get(j);

                    //Calculate antinode locations
                    int xDist = Math.abs(p1.x - p2.x);
                    int yDist = Math.abs(p1.y - p2.y);

                    int antiNode1X = p1.x < p2.x ? p1.x - xDist : p1.x + xDist;
                    int antiNode1Y = p1.y < p2.y ? p1.y - yDist : p1.y + yDist;

                    int antiNode2X = p1.x < p2.x ? p2.x + xDist : p2.x - xDist;
                    int antiNode2Y = p1.y < p2.y ? p2.y + yDist : p2.y - yDist;


                    Set<Point> existing = antiNodes.getOrDefault(a, new HashSet<>());
                    Point antinode1 = new Point(antiNode1X, antiNode1Y);
                    Point antinode2 = new Point(antiNode2X, antiNode2Y);
                    existing.add(antinode1);
                    existing.add(antinode2);

                    while(antinode1.x >= 0 && antinode1.y >= 0 && antinode1.x < map[0].length && antinode1.y < map.length){
                        map[antinode1.y][antinode1.x] = '#';
                        int nextX = p1.x < p2.x ? antinode1.x - xDist : antinode1.x + xDist;
                        int nextY = p1.y < p2.y ? antinode1.y - yDist : antinode1.y + yDist;
                        antinode1 = new Point(nextX, nextY);
                        existing.add(antinode1);
                    }

                    while(antinode2.x >= 0 && antinode2.y >= 0 && antinode2.x < map[0].length && antinode2.y < map.length){
                        map[antinode2.y][antinode2.x] = '#';
                        int nextX = p1.x < p2.x ? antinode2.x + xDist : antinode2.x - xDist;
                        int nextY = p1.y < p2.y ? antinode2.y + yDist : antinode2.y - yDist;
                        antinode2 = new Point(nextX, nextY);
                        existing.add(antinode2);
                    }

                    antiNodes.put(a, existing);
                }
            }
        }

        print(map);
    }

    private static void print(char[][] map){
        int count = 0;
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                System.out.print(map[i][j]);
                if(map[i][j] != '.'){
                    count++;
                }
            }
            System.out.println();
        }

        System.out.println(count);
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
