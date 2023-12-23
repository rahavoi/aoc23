import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day21 {
    static char[][] map;
    static Set<Point> visited;
    static Set<Point> finalDestinations = new HashSet<>();
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day21.txt"));

        map = new char[lines.size()][lines.get(0).length()];

        Point start = null;
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j);

                if(map[i][j] == 'S'){
                    start = new Point(j,i);

                }
            }
        }




        AtomicInteger maxScore = new AtomicInteger(1);
        visited = new HashSet<>();
        visited.add(start);

        Set<Possibility> exploredPossibilities = new HashSet<>();
        Queue<Possibility> q = new LinkedList<>();

        //65 = 3752

        q.add(new Possibility(start, 196, 1));

        while(!q.isEmpty()){
            Possibility possibility = q.poll();

            visited.add(possibility.p);

            if(exploredPossibilities.add(possibility)) {
                List<Point> nextSteps = getNextPossibleSteps(possibility.p);

                //System.out.println("Steps left: " + possibility.stepsLeft);
                //System.out.println("Visited: " + finalDestinations.size());
                //print();
                nextSteps
                        .stream()
                        .map(next -> {
                            int s = visited.add(next) ? possibility.score + 1 : possibility.score;
                            return new Possibility(next, possibility.stepsLeft - 1, s);
                        })
                        .forEach(np -> {
                            maxScore.set(Math.max(maxScore.get(), np.score));

                            if(np.stepsLeft > 0){
                                q.add(np);
                            } else {
                                finalDestinations.add(np.p);
                            }
                        });
            }

        }

        print();
        System.out.println(finalDestinations.size());
    }

    static void print(){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                char c = finalDestinations.contains(new Point(j, i)) ? '0' : map[i][j];
                System.out.print(c);
            }
            System.out.println();
        }

        System.out.println();
    }

    static List<Point> getNextPossibleSteps(Point point){
        int x = point.x;
        int y = point.y;
        return List.of(
                new Point(x + 1, y),
                new Point(x - 1, y),
                new Point(x, y + 1),
                new Point(x, y - 1)
        ).stream()
                .filter(p -> p.x >= 0 && p.x < map[0].length && p.y >= 0 && p.y < map.length)
                .filter(p -> map[p.y][p.x] != '#')
                //.filter(p -> !visited.contains(p))
                .toList();
    }

    static class Possibility {
        Point p;
        int stepsLeft;
        int score;

        public Possibility(Point p, int stepsLeft, int score) {
            this.p = p;
            this.stepsLeft = stepsLeft;
            this.score = score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Possibility that = (Possibility) o;
            return stepsLeft == that.stepsLeft && score == that.score && Objects.equals(p, that.p);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p, stepsLeft, score);
        }
    }

    static class Point {
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
 }
