import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Day2024_14 {
    static int width = 101;
    static int height = 103;
    public static void main(String[] args) throws Exception {
        List<Robot> robots = Files.readAllLines(Paths.get("resources/Day2024_14.txt")).stream().map(l -> {
            String[] p = l.split(" ");
            String[] p1 = p[0].replaceAll("p=", "").split(",");
            String[] p2 = p[1].replaceAll("v=", "").split(",");
            int x = Integer.parseInt(p1[0]);
            int y = Integer.parseInt(p1[1]);
            int vx = Integer.parseInt(p2[0]);
            int vy = Integer.parseInt(p2[1]);

            return new Robot(x, y, vx, vy);
        }).collect(Collectors.toList());

        for(int i = 0; i < 100000000; i++){
            robots.forEach(Day2024_14::move);
            int connected = countConnected(robots);

            if(connected >= 50){
                System.out.println(i + 1);
                print(robots);
                System.out.println();
                break;
            }
        }

        List<Robot> topLeft = new ArrayList<>();
        List<Robot> topRight = new ArrayList<>();
        List<Robot> bottomLeft = new ArrayList<>();
        List<Robot> bottomRight = new ArrayList<>();

        robots.forEach(r -> {
            if(r.x < width / 2 && r.y < height / 2){
                topLeft.add(r);
            }

            if(r.x > width / 2 && r.y < height / 2){
                topRight.add(r);
            }

            if(r.x < width / 2 && r.y > height / 2){
                bottomLeft.add(r);
            }

            if(r.x > width / 2 && r.y > height / 2){
                bottomRight.add(r);
            }
        });
        System.out.println(topLeft.size() * topRight.size() * bottomLeft.size() * bottomRight.size());
    }

    private static int countConnected(List<Robot> robots){
        Set<Robot> visited = new HashSet<>();
        int max = 0;
        for(Robot r : robots){
            if(visited.contains(r)){
                continue;
            }

            Queue<Robot> q = new LinkedList<>();
            q.add(r);
            int count = 0;
            while(!q.isEmpty()){
                count++;
                Robot cur = q.poll();
                Set<Robot> connected = getConnected(robots, cur).stream().filter(c -> visited.add(c)).collect(Collectors.toSet());
                q.addAll(connected);
            }
            max = Math.max(max, count);

            if(max >= 50){
                return max;
            }
        }

        return max;
    }

    private static Set<Robot> getConnected(List<Robot> robots, Robot rbt){
        return robots.stream().filter(r -> {
            return (r.x + 1 == rbt.x && r.y == rbt.y) ||
            (r.x - 1 == rbt.x && r.y == rbt.y) ||
            (r.x == rbt.x && r.y + 1 == rbt.y) ||
            (r.x == rbt.x && r.y - 1 == rbt.y) ||
            (r.x + 1 == rbt.x && r.y - 1 == rbt.y) ||
            (r.x - 1 == rbt.x && r.y - 1 == rbt.y) ||
            (r.x + 1 == rbt.x && r.y + 1 == rbt.y) ||
            (r.x - 1 == rbt.x && r.y + 1 == rbt.y);
        }).collect(Collectors.toSet());
    }
    private static void print(List<Robot> robots){
        char[][] map = new char[height][width];

        for(Robot r : robots){
            map[r.y][r.x] = '*';
        }

        for(int i = 20; i < map.length - 20; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] == '*'){
                    System.out.print(map[i][j]);
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }

    private static void move(Robot robot){
        robot.x += robot.vx;
        robot.y += robot.vy;

        if(robot.x >= width) {
            robot.x = robot.x - width;
        }

        if(robot.x < 0) {
            robot.x = width + robot.x;
        }

        if(robot.y >= height) {
            robot.y = robot.y - height;
        }

        if(robot.y < 0) {
            robot.y = height + robot.y;
        }
    }

    static class Robot {
        int x; int y; int vx; int vy;

        public Robot(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Robot robot = (Robot) o;
            return x == robot.x && y == robot.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
