import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 {
    static List<Integer> rowsWithoutGalaxies = new ArrayList<>();
    static List<Integer> columnsWithoutGalaxies = new ArrayList<>();

    static int expansion_rate = 1000000 - 1;
    public static void main(String[] args) throws Exception {
        List<Point> galaxies = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day11.txt"));

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

        for(int i = 0; i < lines.size(); i++){
            List<Character> c = new ArrayList<>();
            int countGalaxies = 0;
            for(int j = 0; j < lines.get(0).length(); j++){
                if(lines.get(i).charAt(j) == '#'){
                    Point p = new Point(j, i);
                    galaxies.add(p);
                    countGalaxies++;
                }
                c.add(lines.get(i).charAt(j));
            }
            if(countGalaxies == 0){
                rowsWithoutGalaxies.add(i);
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

    private static long distance(Point p1 , Point p2) {
        int xFrom = Math.min(p1.x, p2.x);
        int xTo = Math.max(p1.x, p2.x);
        int yFrom = Math.min(p1.y, p2.y);
        int yTo = Math.max(p1.y, p2.y);

        long lonelyVoidOfEmptyGalaxies = 0;
        for(int i = xFrom; i < xTo; i++){
            if(columnsWithoutGalaxies.contains(i)){
                lonelyVoidOfEmptyGalaxies += expansion_rate;
            }
        }

        for(int i = yFrom; i < yTo; i++){
            if(rowsWithoutGalaxies.contains(i)){
                lonelyVoidOfEmptyGalaxies += expansion_rate;
            }
        }
        return Math.abs(p2.x-p1.x) + Math.abs(p2.y-p1.y) + lonelyVoidOfEmptyGalaxies;
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
