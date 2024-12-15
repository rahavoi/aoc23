import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day16 {
    static Set<String> energized;
    static List<Point> points;
    static char[][] map;
    static char FORWARD_MIRROR = '/';
    static char BACKWARD_MIRROR = '\\';
    static char HORIZONTAL_SPLITTER = '-';
    static char VERTICAL_SPLITTER = '|';

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day16.txt"));
        map = new char[lines.size()][lines.get(0).length()];

        List<Point> options = new ArrayList<>();

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j);

                if(i == 0 || i == map.length - 1 || j == 0 || j == map[0].length - 1){
                    options.add(new Point(j, i, Direction.EAST));
                    options.add(new Point(j, i, Direction.WEST));
                    options.add(new Point(j, i, Direction.SOUTH));
                    options.add(new Point(j, i, Direction.NORTH));
                }
            }
        }

        long maxEnergized = 0;
        for (Point option : options) {
            //If light passed through the same spot in the same direction, no need to repeat it..
            Set<String> loopedBeams = new HashSet<>();
            //System.out.println((x + 1) + " out of " + options.size());
            points = new ArrayList<>();
            points.add(option);
            energized = new HashSet<>();

            while (!points.isEmpty()) {
                for (int i = 0; i < points.size(); i++) {
                    loopedBeams.add(points.get(i).toString() + points.get(i).direction);
                    points.get(i).move();
                }


                points = new ArrayList<>(
                        points.stream().filter(Point::isInBounds)
                                .filter(p -> !loopedBeams.contains(p.toString() + p.direction))
                                .toList()
                );
            }

            maxEnergized = Math.max(maxEnergized, energized.size());
        }

        System.out.println(maxEnergized);
    }

    static class Point {
        int x;
        int y;

        Direction direction;

        public Point(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        public String toString(){
            return x + "," + y;
        }

        private boolean isInBounds(){
            return x >= 0 && x < map[0].length && y >= 0 && y < map.length;
        }

        public void move(){
            if(isInBounds()){
                energized.add(toString());
                char tile = map[y][x];

                if(tile == FORWARD_MIRROR){
                    if(direction == Direction.EAST){
                        direction = Direction.NORTH;
                    } else if(direction == Direction.WEST){
                        direction = Direction.SOUTH;
                    } else if(direction == Direction.NORTH){
                        direction = Direction.EAST;
                    } else if(direction == Direction.SOUTH){
                        direction = Direction.WEST;
                    }
                } else if(tile == BACKWARD_MIRROR){
                    if(direction == Direction.EAST){
                        direction = Direction.SOUTH;
                    } else if(direction == Direction.WEST){
                        direction = Direction.NORTH;
                    } else if(direction == Direction.NORTH){
                        direction = Direction.WEST;
                    } else if(direction == Direction.SOUTH){
                        direction = Direction.EAST;
                    }
                } else if(tile == HORIZONTAL_SPLITTER){
                    if(direction == Direction.NORTH || direction == Direction.SOUTH){
                        direction = Direction.EAST;
                        Point p2 = new Point(x, y, Direction.WEST);
                        points.add(p2);
                    }
                } else if(tile == VERTICAL_SPLITTER){
                    if(direction == Direction.EAST || direction == Direction.WEST){
                        direction = Direction.NORTH;
                        Point p2 = new Point(x, y, Direction.SOUTH);
                        points.add(p2);
                    }
                }
            }

            switch (direction){
                case EAST:
                    x++;
                    break;
                case WEST:
                    x--;
                    break;
                case NORTH:
                    y--;
                    break;
                case SOUTH:
                    y++;
                    break;
            }
        }
    }

    enum Direction {
        EAST,
        WEST,
        SOUTH,
        NORTH;
    }
}
