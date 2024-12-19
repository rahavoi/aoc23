import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day2024_Day16 {
    public static void main(String[] args ) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_16.txt"));
        char[][] map = new char[lines.size()][lines.get(0).length()];

        Point robot = null;
        Point dest = null;

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j);
                if(map[i][j] == 'S'){
                    robot = new Point(j, i);
                }
                if(map[i][j] == 'E'){
                    dest = new Point(j, i);
                }
            }
        }

        PriorityQueue<State> q = new PriorityQueue<>((a,b) -> (int) (a.score - b.score));
        Set<Point> visited = new HashSet<>();
        visited.add(robot);
        q.add(new State(robot, 0L, 'E', visited));
        Map<Point, Long> costToDest = new HashMap<>();

        Long minCost = Long.MAX_VALUE;
        Set<Point> bestTiles = new HashSet<>();
        while (!q.isEmpty()){
            State cur = q.poll();
            if(cur.score > minCost){
                continue;
            }

            Long cost = costToDest.get(cur.p);
            if(cost == null){
                costToDest.put(cur.p, cur.score);
            } else if (cost + 1000 < cur.score){
                //System.out.println("Aha!");
               continue;
            }

            //System.out.println(cur);

            //System.out.println(cur);
            map[cur.p.y][cur.p.x] = cur.direction;
            //print(map);

            if(cur.p.equals(dest)){
                System.out.println("Done: " + cur.score);
                System.out.println("Done: " + cur.visited.size());
                bestTiles.addAll(cur.visited);
                minCost = Math.min(minCost, cur.score);
            } else {
                q.addAll(getNextStates(cur, map));
            }
        }

        System.out.println(minCost);
        System.out.println("Best seats: " + bestTiles.size());
    }

    private static List<State> getNextStates(State state, char[][] map) {
        List<Character> directions = getDirections(state.direction).stream()
                .filter(direction -> {
                    switch (direction){
                        case 'S': return map[state.p.y + 1][state.p.x] != '#';
                        case 'N': return map[state.p.y - 1][state.p.x] != '#';
                        case 'E': return map[state.p.y][state.p.x + 1] != '#';
                        case 'W': return map[state.p.y][state.p.x - 1] != '#';
                        default:
                            throw new IllegalArgumentException("Invalid dir");
                    }
                }).toList();

        return directions
                .stream().map(d -> {
                    Long score = d == state.direction ? state.score + 1 : state.score + 1001;
                    Point p;

                    switch (d){
                        case 'S': p = new Point(state.p.x, state.p.y + 1);
                            break;
                        case 'N': p = new Point(state.p.x, state.p.y - 1);
                            break;
                        case 'E': p = new Point(state.p.x + 1, state.p.y);
                            break;
                        case 'W': p = new Point(state.p.x - 1, state.p.y);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid direction");
                    }

                    Set<Point> visited = new HashSet<>(state.visited);

                    if(!visited.add(p)){
                        return null;
                    }
                    return new State(p, score, d, visited);
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static void print(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static List<Character> getDirections(char dir){
        switch (dir){
            case 'E':
                return List.of('N', 'S', 'E');
            case 'W':
                return List.of('W', 'N', 'S');
            case 'N':
                return List.of('W', 'E', 'N');
            case 'S':
                return List.of('W', 'E', 'S');
            default:
                throw new IllegalArgumentException("invalid direction");
        }
    }

    public record Point(int x, int y){}
    public record State(Point p , Long score, char direction, Set<Point> visited) {}
}
