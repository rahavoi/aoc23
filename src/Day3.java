import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day3 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day3.txt"));

        int result = 0;

        char[][] map = new char[lines.size()][lines.get(0).length()];

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(0).length(); j++){
                //System.out.println("Getting line " + i + "Position: " + j);
                map[i][j] = lines
                        .get(i)
                        .charAt(j);
            }
        }

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(0).length(); j++){
                if(map[i][j] == '*'){
                    //Find all numbers that are adjacent
                    List<Integer> adjNums = findAdjacentNums(map, i, j);

                    if(adjNums.size() == 2){
                        result += adjNums.get(0) * adjNums.get(1);
                    }

                }
            }
        }

        System.out.println(result);
    }

    private static List<Integer> findAdjacentNums(char[][] map, int y, int x){
        List<Integer> result = new ArrayList<>();

        getNumber(map, y - 1, x - 1).ifPresent(result::add);
        getNumber(map, y - 1, x).ifPresent(result::add);
        getNumber(map, y - 1, x + 1).ifPresent(result::add);
        getNumber(map, y, x + 1).ifPresent(result::add);
        getNumber(map, y, x - 1).ifPresent(result::add);
        getNumber(map, y + 1, x - 1).ifPresent(result::add);
        getNumber(map, y + 1, x).ifPresent(result::add);
        getNumber(map, y + 1, x + 1).ifPresent(result::add);
        return result;
    }

    private static Optional<Integer> getNumber(char[][] map, int y, int x){
        if(!isValid(map, y, x)){
            return Optional.empty();
        }

        if(Character.isDigit(map[y][x])){
            //Find the whole number, erase positions.
            int minx = x;
            int maxx = x;
            while(isValid(map, y, minx - 1) && Character.isDigit(map[y][minx - 1])){
                minx -= 1;
            }

            while(isValid(map, y, maxx + 1) && Character.isDigit(map[y][maxx + 1])){
                maxx += 1;
            }

            StringBuilder sb = new StringBuilder();

            for(int curX = minx; curX < maxx + 1; curX++){
                sb.append(map[y][curX]);
                map[y][curX] = '.';
            }

            System.out.println("Found: " + sb);
            return Optional.of(Integer.parseInt(sb.toString()));
        } else {
            return Optional.empty();
        }
    }

    private static boolean isValid(char[][] map, int y, int x){
        return y < map.length && y >= 0 && x < map[0].length && x >=0;
    }
}
