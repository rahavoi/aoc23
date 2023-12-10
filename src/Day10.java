import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day10 {
    static char EMPTY = ' ';
    static Set<Point> visited = new HashSet<>();

    static  int MAX_STEPS_AWAY = 0;
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day10.txt"));
        char[][] map = new char[lines.size()][lines.get(0).length()];


        int x = -1;
        int y = -1;

        //Init map:
        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);

            for(int j = 0; j < line.length(); j++){
                char c = line.charAt(j);

                if(c == 'S'){
                    x = j;
                    y = i;
                }
                map[i][j] = c;
            }
        }

        Point start = new Point(0, x, y);
        Queue<Point> connections = getValidConnections(start, map);
        visited.add(start);


        while(!connections.isEmpty()){
            Point c = connections.poll();
            connections.addAll(getValidConnections(c, map));
            visited.add(c);
        }

        System.out.println(MAX_STEPS_AWAY);
        System.out.println(countInsiders(map));

    }

    private static int countInsiders(char[][] map){
        int result = 0;
        Set<Character> pipes = Set.of('|', 'L', 'J', 'S');
        for(int i = 0; i < map.length; i++){

            for(int j = 0; j < map[0].length; j++){
                Point p = new Point(-1, j, i);
                if(visited.contains(p)){
                    continue;
                }

                int cur = p.x - 1;

                int pipeCount = 0;
                while(cur >= 0){
                    char c = map[p.y][cur];

                    if(pipes.contains(c) && visited.contains(new Point(-1, cur, p.y))){
                        pipeCount++;
                    }

                    cur--;
                }

                if(pipeCount % 2 != 0){
                    map[p.y][p.x] = '0';
                    result++;
                }
            }
        }

        return result;

    }

    /**
     | is a vertical pipe connecting north and south.
     - is a horizontal pipe connecting east and west.
     L is a 90-degree bend connecting north and east.
     J is a 90-degree bend connecting north and west.
     7 is a 90-degree bend connecting south and west.
     F is a 90-degree bend connecting south and east.
     . is ground; there is no pipe in this tile.
     S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
     */
    private static Queue<Point> getValidConnections(Point p, char[][] map){
        int x = p.x;
        int y = p.y;

        Queue<Point> connections = new LinkedList<>();
        char current = map[y][x];
        char north = get(x, y - 1, map);
        char south = get(x, y + 1, map);
        char east = get(x + 1, y, map);
        char west = get(x - 1, y, map);

        int n = p.stepsFromStart + 1;

        Point northPos = new Point(n, x, y - 1);
        Point southPos = new Point(n, x, y + 1);
        Point eastPos = new Point(n,x + 1, y);
        Point westPos = new Point(n,x - 1, y);

        //Pretty much anything can connect to the beginning
        if(current == 'S' && (north == '|' || north == '7' || north == 'F')){
            connections.add(northPos);
        }

        if(current == 'S' && (south == '|' || south == 'J' || south == 'L')) {
            connections.add(southPos);
        }

        if(current == 'S' && (east == '-' || east == 'J' || east == '7')) {
            connections.add(eastPos);
        }

        if(current == 'S' && (west == '-' || west == 'F' || west == 'L')) {
            connections.add(westPos);
        }

        if(current == '|' && (north == '|' || north == '7' || north == 'F')) {
            connections.add(northPos);
        }

        if(current == '|' && (south == '|' || south == 'J' || south == 'L')) {
            connections.add(southPos);
        }

        if(current == '-' && (east == '-' || east == 'J' || east == '7')) {
            connections.add(eastPos);
        }

        if(current == '-' && (west == '-' || west == 'F' || west == 'L')) {
            connections.add(westPos);
        }

        // L is a 90-degree bend connecting north and east.
        //For L only north and east connections are valid:
        if(current == 'L' && (north == '|' || north == 'F' || north == '7')){
            connections.add(northPos);
        }

        if(current == 'L' && (east == '-' || east == '7' || east == 'J')){
            connections.add(eastPos);
        }

        //J is a 90-degree bend connecting north and west.
        //For J only north and west connections are valid:
        if(current == 'J' && (north == '|' || north == 'F' || north == '7') ){
            connections.add(northPos);
        }

        if(current == 'J' && (west == '-' || west == 'F' || west == 'L') ){
            connections.add(westPos);
        }

        //7 is a 90-degree bend connecting south and west.
        //For 7 only west and south connections are valid:
        if(current == '7' && (west == '-' || west == 'F' || west == 'L') ){
            connections.add(westPos);
        }

        if(current == '7' && (south == '|' || south == 'J' || south == 'L')){
            connections.add(southPos);
        }

        //F is a 90-degree bend connecting south and east.
        //For F only east and south connections are valid:
        if(current == 'F' && (east == '-' || east == 'J' || east == '7')){
            connections.add(eastPos);
        }

        if(current == 'F' && (south == '|' || south == 'J' || south == 'L')){
            connections.add(southPos);
        }

        connections.removeAll(connections.stream().filter(c -> visited.contains(c)).toList());
        if(connections.size() > 0){
            MAX_STEPS_AWAY = Math.max(n, MAX_STEPS_AWAY);
        }

        return connections;
    }

    static class Point {
        int stepsFromStart;
        int x;
        int y;

        public Point(int stepsFromStart, int x, int y) {
            this.stepsFromStart = stepsFromStart;
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

    private static Character get(int x, int y, char[][] map){
        if(x < 0 || x >= map[0].length || y < 0 || y >= map.length){
            return '.';
        }

        return map[y][x];
    }
}
