import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 {
    static List<Integer> rowsWithoutGalaxies = new ArrayList<>();
    static List<Integer> columnsWithoutGalaxies = new ArrayList<>();

    static int expansion_rate = 1000000 - 1;
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day11.txt"));
        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            if(line.indexOf('#') == -1){
                rowsWithoutGalaxies.add(i);
            }
        }

        for(int j = 0; j < lines.get(0).length(); j++){
            int count = 0;
            for(String l : lines){
                if(l.charAt(j) == '#'){
                    count++;
                    break;
                }
            }

            if(count == 0){
                columnsWithoutGalaxies.add(j);
            }
        }

        List<List<Character>> map = new ArrayList<>();

        for(int i = 0; i < lines.size(); i++){
            List<Character> c = new ArrayList<>();
            for(int j = 0; j < lines.get(0).length(); j++){
                c.add(lines.get(i).charAt(j));
            }
            map.add(c);
        }


        List<Point> galaxies = new ArrayList<>();
        for(int i = 0; i < map.size(); i++){
            for(int j = 0; j < map.get(0).size(); j++){
                char c = map.get(i).get(j);
                if(c == '#'){
                    Point p = new Point(j, i);
                    galaxies.add(p);

                }
            }
        }

        long distances = 0;
        for(int i = 0; i < galaxies.size(); i++){
            Point galaxy = galaxies.get(i);

            for(int j = i + 1; j < galaxies.size(); j++){
                Point other = galaxies.get(j);
                distances += distance(galaxy, other);
            }
        }

        System.out.println(distances);
    }

    static class Pair {
        Point p1;
        Point p2;

        public Pair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;

            return Objects.equals(p1, pair.p1) ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(p1, p2);
        }
    }
    private static long distance(Point p1 , Point p2) {
        int xFrom = Math.min(p1.x, p2.x);
        int xTo = Math.max(p1.x, p2.x);
        int yFrom = Math.min(p1.y, p2.y);
        int yTo = Math.max(p1.y, p2.y);

        long lonely_void_of_empty_galaxies = 0;
        for(int i = xFrom; i < xTo; i++){
            if(columnsWithoutGalaxies.contains(i)){
                lonely_void_of_empty_galaxies += expansion_rate;
            }
        }

        for(int i = yFrom; i < yTo; i++){
            if(rowsWithoutGalaxies.contains(i)){
                lonely_void_of_empty_galaxies += expansion_rate;
            }
        }
        return Math.abs(p2.x-p1.x) + Math.abs(p2.y-p1.y) + lonely_void_of_empty_galaxies;
    }

    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
