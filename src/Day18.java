import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day18 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part2() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day18.txt"));
        List<Point> points = new ArrayList<>();
        Point cur = new Point(0,0);
        points.add(cur);

        int x = 0;
        int y = 0;

        long perimeter = 0;
        for(String s : lines){
            String[] parts = s.split(" ");
            String input = parts[2].replace("(", "").replace(")", "");
            int steps = Integer.parseInt(input.substring(1, 6), 16);
            int direction = Integer.parseInt(input.substring(input.length() - 1));

            perimeter += steps;

            switch (direction){
                //R
                case 0:
                    cur = new Point(cur.x + steps, cur.y);
                    break;
                //D
                case 1:
                    cur = new Point(cur.x, cur.y + steps);
                    break;
                //R
                case 2:
                    cur = new Point(cur.x - steps, cur.y);
                    break;
                //U
                case 3:
                    cur = new Point(cur.x, cur.y - steps);
                    break;
            }

            points.add(cur);
        }


        long area = 0;

        for(int i = 0; i < points.size() - 1; ++i){
            area += ((long) points.get(i).x * points.get(i + 1).y) - ((long) points.get(i + 1).x * points.get(i).y);
        }


        //Googled Pick's theorem 0_0
        System.out.println(Math.abs(area / 2) + perimeter / 2 + 1);
    }

    private static void part1() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day18.txt"));
        List<Point> points = new ArrayList<>();
        points.add(new Point(0,0));

        int x = 0;
        int y = 0;

        for(String s : lines){
            String[] parts = s.split(" ");
            String direction = parts[0];
            int steps = Integer.parseInt(parts[1]);

            for(int i = 0; i < steps; i++){
                switch (direction){
                    case "R":
                        points.add(new Point(++x, y));
                        break;
                    case "L":
                        points.add(new Point(--x, y));
                        break;
                    case "U":
                        points.add(new Point(x, --y));
                        break;
                    case "D":
                        points.add(new Point(x, ++y));
                        break;
                }
            }
        }

        part1Naive(points);
    }

    private static void part1Naive(List<Point> points){
        int minX = points.stream().mapToInt(p -> p.x).min().getAsInt();
        int maxX = points.stream().mapToInt(p -> p.x).max().getAsInt();
        int minY = points.stream().mapToInt(p -> p.y).min().getAsInt();
        int maxY = points.stream().mapToInt(p -> p.y).max().getAsInt();

        int finalMinX = minX;
        int finalMinY = minY;
        points.forEach(p -> {
            p.x -= finalMinX;
            p.y -= finalMinY;
        });

        for(int i = minY; i < maxY + 1; i++){
            int row = i;
            List<Point> pointsInRow =  points.stream().filter(p -> p.y == row).toList();

            System.out.println("In x = " + i + ": " + pointsInRow.size() + "points");
        }

        minX = points.stream().mapToInt(p -> p.x).min().getAsInt();
        maxX = points.stream().mapToInt(p -> p.x).max().getAsInt();
        minY = points.stream().mapToInt(p -> p.y).min().getAsInt();
        maxY = points.stream().mapToInt(p -> p.y).max().getAsInt();

        char[][] map = new char[maxY + 1][maxX + 1];
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = '.';
            }
        }

        points.stream().forEach(p -> {
            map[p.y][p.x] = '#';
        });

        int curX = 0;

        for(int i = 0; i < map[0].length; i++ ){
            if(map[1][i] == '#'){
                //Found edge

                curX = i + 1;
                break;
            }
        }

        map[1][curX] = '#';
        Queue<Point> q = new LinkedList<>();
        q.addAll(getEmptyNeighbors(new Point(curX, 1), map));

        while (!q.isEmpty()){
            Point p = q.poll();
            //Filling
            map[p.y][p.x] = '#';

            List<Point> neighbors = getEmptyNeighbors(p, map);

            neighbors.forEach(n -> map[n.y][n.x] = '#');

            q.addAll(neighbors);
        }


        //print(map);

        System.out.println(countFilled(map));

    }

    private static List<Point> getEmptyNeighbors(Point p, char[][] map){
        List<Point> result = List.of(new Point(p.x + 1, p.y),
                new Point(p.x - 1, p.y),
                new Point(p.x, p.y + 1),
                new Point(p.x, p.y - 1)
        ).stream()
                .filter(x -> x.x > 0 && x.y > 0 && x.x < map[0].length && x.y < map.length)
                .filter(x -> map[x.y][x.x] == '.')
                .toList();

        return result;
    }

    private static void print(char[][] map){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    private static long countFilled(char[][] map){
        long count = 0;
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] == '#'){
                    count++;
                }
            }
        }

        return count;
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
