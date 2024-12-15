import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day2024_15 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part2() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_15a.txt"));
        List<List<Character>> map = new ArrayList<>();
        Point robotPos = null;
        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(0).length(); j++){
                if(map.size() == i){
                    map.add(new ArrayList<>());
                }
                List<Character> row = map.get(i);

                char c = lines.get(i).charAt(j);

                if(c == '@'){
                    robotPos = new Point(row.size(), i);
                    row.add('.');
                    row.add('.');
                }

                if(c == '.'){
                    row.add('.');
                    row.add('.');
                }

                if(c == '#'){
                    row.add('#');
                    row.add('#');
                }

                if(c == 'O'){
                    row.add('[');
                    row.add(']');
                }
            }
        }

        char[][] mapArr = new char[map.size()][map.get(0).size()];

        for(int i = 0; i < mapArr.length; i++) {
            for (int j = 0; j < mapArr[0].length; j++) {
                mapArr[i][j] = map.get(i).get(j);
            }
        }

        List<Character> moves = new ArrayList<>();
        Files.readAllLines(Paths.get("resources/Day2024_15b.txt"))
                .stream()
                .forEach(l -> {
                    for(char c : l.toCharArray()){
                        moves.add(c);
                    }
                } );

        for(char c : moves){
            robotPos = moveP2(robotPos, mapArr, c);
            //print(mapArr);
        }


        //100 times its distance from the top edge of the map plus its distance from the left edge of the map
        long sum = 0;
        for(int i = 0; i < mapArr.length; i++){
            for(int j = 0; j < mapArr[0].length; j++){
                if(mapArr[i][j] == '['){
                    sum += 100 * i + j;
                }
            }
        }

        System.out.println(sum);
    }

    private static Point moveP2(Point cur, char[][] map, char direction){
        Point nextPos = getNextPos(cur, direction);
        //System.out.println("Moving from " + cur.x + "," + cur.y +" to " + nextPos.x + "," + nextPos.y + " Direction: " + direction);
        int x = nextPos.x;
        int y = nextPos.y;

        char next = map[y][x];

        if(next == '#'){
            //Can't move
            return cur;
        }

        if(next == '.'){
            return new Point(x, y);
        }

        if(next == '[' || next == ']'){
            if(direction == '>' || direction == '<'){
                //moving horizontally
                Point n = nextPos;
                while(map[n.y][n.x] == '[' || map[n.y][n.x] == ']'){
                    n = direction == '>' ? new Point(n.x + 1,  n.y) : new Point(n.x - 1,  n.y);
                }

                if(map[n.y][n.x] == '#'){
                    //Hit the wall
                    return cur;
                }

                if(map[n.y][n.x] == '.'){
                    while (!n.equals(cur)){
                        //moving backwards
                        Point tmp = direction == '>' ? new Point(n.x - 1, n.y) : new Point(n.x + 1, n.y);
                        map[n.y][n.x] = map[tmp.y][tmp.x];
                        n = tmp;
                    }


                    cur = direction == '>' ? new Point(cur.x + 1, y) : new Point(cur.x - 1, y);
                    //print(map, cur);
                    return cur;

                }

                nextPos = direction == '>' ? new Point(n.x + 1,  n.y) : new Point(n.x - 1,  n.y);

            } else {
                //moving vertically
                Queue<Point> q = new LinkedList<>();
                q.add(nextPos);
                Set<Box> boxesToMove = new HashSet<>();

                while (!q.isEmpty()){
                    Point p = q.poll();

                    if(!boxesToMove.add(new Box(p.x, p.y, map[p.y][p.x]))) {
                        continue;
                    }

                    Point otherHalf = map[p.y][p.x] == '[' ? new Point(p.x + 1, p.y) : new Point(p.x - 1, p.y);
                    q.add(otherHalf);

                    Point adj = direction == '^' ? new Point(p.x, p.y - 1) : new Point(p.x, p.y + 1);

                    if(map[adj.y][adj.x] == '.'){
                        continue;
                    }

                    if(map[adj.y][adj.x] == '#'){
                        //Hit the wall
                        return cur;
                    }

                    q.add(adj);
                }

                //Just fill with empty temporarily.. Too lazy for a better solution
                boxesToMove.forEach(b -> {
                    map[b.y][b.x] = '.';
                });

                boxesToMove.forEach(b -> {
                    int by = direction == '^' ? b.y - 1 : b.y + 1;
                    map[by][b.x] = b.c;
                });
                nextPos = direction == '^' ? new Point(cur.x,  cur.y - 1) : new Point(cur.x,  cur.y + 1);
            }

            //print(map, nextPos);
            return nextPos;
        }

        throw new IllegalArgumentException("WTF");
    }

    private static void part1() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_15a.txt"));
        char[][] map = new char[lines.size()][lines.get(0).length()];

        Point robotPos = null;

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j);

                if(map[i][j] == '@'){
                    robotPos = new Point(j, i);
                    map[i][j] = '.';
                }
            }
        }

        List<Character> moves = new ArrayList<>();

        Files.readAllLines(Paths.get("resources/Day2024_15b.txt"))
                .stream()
                .forEach(l -> {
                    for(char c : l.toCharArray()){
                        moves.add(c);
                    }
                } );

        for(char c : moves){
            robotPos = moveP1(robotPos, map, c);
        }


        //100 times its distance from the top edge of the map plus its distance from the left edge of the map
        long sum = 0;
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] == 'O'){
                    sum += 100 * i + j;
                }
            }
        }

        System.out.println(sum);
    }

    private static void print(char[][] map, Point robot){
        for(int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(robot.x == j && robot.y == i){
                    System.out.print('@');
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static Point moveP1(Point cur, char[][] map, char direction){
        Point nextPos = getNextPos(cur, direction);
        int x = nextPos.x;
        int y = nextPos.y;

        char next = map[y][x];

        if(next == '#'){
            //Can't move
            return cur;
        }

        if(next == '.'){
            return new Point(x, y);
        }

        if(next == 'O'){
            Point n = nextPos;
            while(map[n.y][n.x] == 'O'){
                n = getNextPos(n, direction);
            }

            if(map[n.y][n.x] == '.'){
                //Shift boxes
                map[n.y][n.x] = 'O';
                map[nextPos.y][nextPos.x] = '.';
            }

            if(map[n.y][n.x] == '#'){
                //Can't move
                return cur;
            }

            return nextPos;
        }

        throw new IllegalArgumentException("WTF");



    }

    private static Point getNextPos(Point cur, char direction){
        int x = cur.x;
        int y = cur.y;

        switch (direction){
            case '^': //Up
                y--;
                break;
            case 'v': //Down
                y++;
                break;
            case '<': //Left
                x--;
                break;
            case '>': //Right
                x++;
                break;
            default:
                throw new IllegalArgumentException("Invalid movement: " + direction);
        }

        return new Point(x, y);
    }

    record Point(int x, int y) {}
    record Box(int x, int y, char c) {}
}
