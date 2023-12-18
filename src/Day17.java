import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day17 {
    static char[][] map;

    static Map<String, Integer> minCosts = new HashMap<>();
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day17.txt"));
        long result = Long.MAX_VALUE;

        map = new char[lines.size()][lines.get(0).length()];

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = lines.get(i).charAt(j);
            }
        }

        Point destination = new Point(map[0].length - 1, map.length - 1, null, new ArrayList<>());

        PriorityQueue<Point> points = new PriorityQueue<>(Comparator.comparing(Point::cost));
        points.add(new Point(0, 0, "", new ArrayList<>()));

        while(!points.isEmpty()){
            Point cur = points.poll();
            //System.out.println(cur.x + "," + cur.y + ". Cost: " + cur.cost());

            if(!cur.directionsHistory.isEmpty()){
                char direction = cur.directionsHistory.charAt(cur.directionsHistory.length() - 1);
                int count = 0;
                int pos = cur.directionsHistory.length() - 1;

                while(pos >= 0 && direction == cur.directionsHistory.charAt(pos)){
                    if(direction == cur.directionsHistory.charAt(pos)){
                        count++;
                        pos--;
                    }
                }
                String key = cur.x + "," + cur.y + direction + count;

                if(minCosts.getOrDefault(key, Integer.MAX_VALUE) <= cur.cost()){
                    continue;
                }

                minCosts.put(key, cur.cost());
            }

            if(cur.x == destination.x && cur.y == destination.y && cur.isAllowedToStop()){
                result = Math.min(result, cur.cost());

                System.out.println(cur.directionsHistory + ": " + (cur.cost() - Character.getNumericValue(map[0][0])));
                //break;
            } else {
                points.addAll(cur.getNeighbors());
            }

        }

        System.out.println(result - Character.getNumericValue(map[0][0]));
    }

    static class Point {
        int heat;

        int x;
        int y;

        String directionsHistory;
        List<Point> path;

        public Point(int x, int y, String directionsHistory, List<Point> path) {
            this.x = x;
            this.y = y;
            this.directionsHistory = directionsHistory;
            this.path = path;

            if(isInBound()){
                this.heat = Character.getNumericValue(map[y][x]);
            }
        }

        int cost(){
            int result = path.stream().mapToInt(p -> p.heat).sum() + heat;
            return result;
        }

        boolean isInBound(){
            return x >= 0 && y >= 0 && x < map[0].length && y < map.length;
        }

        List<Point> getNeighbors(){
            List<Point> result =  List.of(
                            new Point(x + 1, y, directionsHistory + 'e', new ArrayList<>(path)),
                            new Point(x - 1, y, directionsHistory + 'w', new ArrayList<>(path)),
                            new Point(x, y - 1, directionsHistory + 'n', new ArrayList<>(path)),
                            new Point(x, y + 1, directionsHistory + 's', new ArrayList<>(path))
                    ).stream()
                    .filter(p -> {
                        if(directionsHistory.isEmpty()){
                            return true;
                        }

                        char next = p.directionsHistory.charAt(p.directionsHistory.length() - 1);
                        char prev = directionsHistory.charAt(directionsHistory.length() - 1);

                        return (next == 'n' && prev != 's') ||
                                (next == 's' && prev != 'n') ||
                                (next == 'e' && prev != 'w') ||
                                (next == 'w' && prev != 'e');

                    })
                    .filter(Point::isInBound)
                    .filter(this::isMatchingRulesForPart2)
                    //.filter(p-> !path.contains(p))
                    //.filter(this::isMatchingRulesForPart1)
                    .toList();

            result.forEach(p -> p.path.add(this));
            return result;
        }

        private boolean isMatchingRulesForPart2(Point p){
            String dirHistory = p.directionsHistory;
            if(dirHistory.length() > 10){
                char[] last11Moves = dirHistory.substring(dirHistory.length() - 11).toCharArray();
                Set<Character> unique = new HashSet<>();
                for(char d : last11Moves){
                    unique.add(d);
                }

                //an ultra crucible can move a maximum of ten consecutive blocks without turning.
                if(unique.size() == 1){
                    return false;
                }
            }

            //once it starts moving, it needs to move a minimum of four blocks
            char currentDir = dirHistory.charAt(dirHistory.length() - 1);

            int countCurrentDir = 0;
            int pos = dirHistory.length() - 1;
            while(currentDir == dirHistory.charAt(pos)){
                countCurrentDir++;
                pos--;

                //Reached the end
                if(pos < 0){
                    return true;
                }
            }

            if(countCurrentDir >= 4){
                return true;
            }

            char prevDir = dirHistory.charAt(pos);
            int countPrevDir = 0;

            while(pos >= 0 && prevDir == dirHistory.charAt(pos)){
                countPrevDir++;
                pos--;
            }

            return countPrevDir >= 4;
        }

        private boolean isAllowedToStop(){
            if(directionsHistory.length() < 4){
                return false;
            }

            int count = 0;
            char currentDir = directionsHistory.charAt(directionsHistory.length() - 1);
            int pos = directionsHistory.length() - 1;
            while(pos >= 0 && currentDir == directionsHistory.charAt(pos)){
                count++;
                pos--;

                if(count >= 4){
                    return true;
                }
            }

            return false;

        }

        private boolean isMatchingRulesForPart1(Point p){
            if(p.directionsHistory.length() < 4){
                return true;
            }
            char[] dirs = p.directionsHistory.substring(p.directionsHistory.length() - 4).toCharArray();
            Set<Character> unique = new HashSet<>();
            for(char d : dirs){
                unique.add(d);
            }

            return unique.size() != 1;
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
