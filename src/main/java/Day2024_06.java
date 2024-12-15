import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day2024_06 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_06.txt"));

        char[][] map = new char[lines.size()][lines.get(0).length()];

        int y = 0;
        int x = 0;
        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(0).length(); j++){
                map[i][j] = lines.get(i).charAt(j);
                if(map[i][j] == '^') {
                    y = i;
                    x = j;
                    map[i][j] = '.';
                }
            }
        }

        Point p = new Point();
        p.direction = 'n';
        p.x = x;
        p.y = y;

        /*
        P1:
        try {
            while (true) {
                visited.add(p.x + "," + p.y);
                move(p, map);
            }
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println(visited.size());
        }
         */

        int count = 0;

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(0).length(); j++){
                char[][] copy = Arrays.stream(map).map(el -> el.clone()).toArray($ -> map.clone());

                if(copy[i][j] == '#'){
                    continue;
                }

                copy[i][j] = '#';

                Point cur = new Point();
                cur.x = p.x;
                cur.y = p.y;
                cur.direction = p.direction;

                Set<String> visited = new HashSet<>();
                visited.add(p.x + "," + p.y + "," + p.direction);

                while (true){
                    try {
                        move(cur, copy);
                        if(visited.contains(cur.x + "," + cur.y + "," + cur.direction)){
                            count++;
                            break;
                        }
                        visited.add(cur.x + "," + cur.y + "," + cur.direction);
                    } catch (ArrayIndexOutOfBoundsException e){
                        break;
                    }
                }

            }
        }

        System.out.println(count);
    }

    private static void move(Point p, char[][] map){
        switch (p.direction){
            case 'n': {
                if(map[p.y - 1][p.x] == '.'){
                    p.y--;
                } else {
                    p.direction = 'e';
                }
                break;
            }
            case 's': {
                if(map[p.y + 1][p.x] == '.'){
                    p.y++;
                } else {
                    p.direction = 'w';
                }
                break;
            }
            case 'e': {
                if(map[p.y][p.x + 1] == '.'){
                    p.x++;
                } else {
                    p.direction = 's';
                }
                break;
            }
            case 'w': {
                if(map[p.y][p.x - 1] == '.'){
                    p.x--;
                } else {
                    p.direction = 'n';
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("WTF" + p.direction);
            }
        }
    }

    static class Point {
        char direction;
        int x;
        int y;
    }
}
