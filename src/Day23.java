import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day23 {
    static char map[][];
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day23.txt"));

        map = new char[lines.size()][lines.get(0).length()];

        for(int i  = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j);
                char c = map[i][j];
                if(c == '^' || c == 'v' || c == '<' || c == '>') {
                   map[i][j] = '.';
                }
            }
        }

        Point start = new Point(1, 0);
        map[0][1] = '.';
        Point destination = new Point(lines.get(0).length() - 2, lines.size() - 1);

        Path path = new Path(start);

        Stack<Path> stack = new Stack<>();
        stack.add(path);

        long longestP = 0;
        outer:
        while (!stack.isEmpty()){
            //System.out.println(queue.size());
            Path currentPath = stack.pop();
            Point currentPoint = currentPath.currentPoint;

            if(currentPoint.equals(destination)){

                if(longestP < currentPath.visited.size() - 1){
                    longestP = currentPath.visited.size() - 1;
                    System.out.println("Longest: " + longestP + " Stack size: " + stack.size());
                }
                continue;
            }

            while(currentPath.getNextSteps(currentPoint).size() == 1){
                currentPath.move(currentPath.getNextSteps(currentPoint).get(0));

                if(currentPath.currentPoint.equals(destination)){

                    if(longestP < currentPath.visited.size() - 1){
                        longestP = currentPath.visited.size() - 1;
                        System.out.println("Longest: " + longestP + " Stack size: " + stack.size());
                    }
                    continue outer;
                }
                currentPoint = currentPath.currentPoint;
            }

            List<Point> nextSteps = currentPath.getNextSteps(currentPoint);

            if(!nextSteps.isEmpty()){
                Point firstNext = nextSteps.get(0);

                for(int i = 1; i < nextSteps.size(); i++){
                    //TODO: create new paths for other options.
                    Point next = nextSteps.get(i);
                    Path newPath = new Path(currentPath);
                    newPath.move(next);
                    stack.add(newPath);
                }

                currentPath.move(firstNext);
                stack.add(currentPath);
            }

            //queue.add(currentPath);
        }

        System.out.println("Longest path: " + longestP);


    }

    static void print(Path path){
        for(int i  = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                Point p = new Point(j,i);

                System.out.print(path.visited.contains(p) ? '0' : map[i][j]);
            }

            System.out.println();
        }
    }


    static class Path {
        Set<Point> visited = new HashSet<>();
        Point currentPoint;


        List<Point> getNextSteps(Point pnt){
            int x = pnt.x;
            int y = pnt.y;

            char c = map[pnt.y][pnt.x];

            if(c == '.' || c == '^' || c == 'v' || c == '<' || c == '>') {
                return List.of(
                                new Point(x + 1, y),
                                new Point(x - 1, y),
                                new Point(x, y + 1),
                                new Point(x, y - 1)

                        ).stream()
                        .filter(p -> p.x >= 0 && p.x < map[0].length && p.y >= 0 && p.y < map.length)
                        .filter(p -> map[p.y][p.x] != '#')
                        .filter(p -> !visited.contains(p))
                        .toList();
            }

            throw new IllegalArgumentException("Wrong input!");


        }

        public Path(Path path){
            this.visited = new HashSet<>(path.visited);
            this.currentPoint = path.currentPoint;
        }

        public Path(Point currentPoint) {
            this.currentPoint = currentPoint;
            visited.add(currentPoint);
        }

        public void move(Point next) {
            visited.add(next);
            currentPoint = next;
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
