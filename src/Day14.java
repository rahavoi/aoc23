import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day14 {

    static Character ROUND_ROCK = 'O';
    static Character SQUARE_ROCK = '#';
    static Character EMPTY = '.';

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day14.txt"));
        long result = 0;

        char[][] map = new char[lines.size()][lines.get(0).length()];

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(0).length(); j++){
                map[i][j] = lines.get(i).charAt(j);
            }
        }

        System.out.println(result);

        Set<Long> totalLoads = new HashSet<>();
        // every 11 cycles 83502. last one 10779
        //999989221 cycles to go.
        //999989221 mod 11 =
        for(int i = 0 ; i < 1000000000; i++){
            fullCycleTilt(map);
            long totalLoad = count(map);

            System.out.println("####Total Load: " + totalLoad + " after " + i + " cycles.");

            if(totalLoad == 83502){
                System.out.println("83502 after " + i + " cycles");
                System.out.println();
            }

            if(totalLoads.add(totalLoad)){
                System.out.println("Total Load: " + totalLoad + " after " + i + " cycles.");
            }
        }
        print(map);

    }

    enum Direction {
        SOUTH,
        NORTH,
        EAST,
        WEST;
    }

    private static void fullCycleTilt(char[][] map){
        tilt(Direction.NORTH, map);
        tilt(Direction.WEST, map);
        tilt(Direction.SOUTH, map);
        tilt(Direction.EAST, map);
    }

    private static void tilt(Direction direction, char[][] map){
        switch (direction){
            case NORTH:
                tilt(map);
                break;
            case SOUTH:
                rotate90(map);
                rotate90(map);
                tilt(map);
                rotate90(map);
                rotate90(map);
                break;
            case WEST:
                rotate90(map);
                tilt(map);
                rotate90(map);
                rotate90(map);
                rotate90(map);
                break;
            case EAST:
                rotate90(map);
                rotate90(map);
                rotate90(map);
                tilt(map);
                rotate90(map);
                break;
        }
    }

    private static void tilt(char[][] map){
            iterating_columns:
            for(int column = 0; column < map[0].length; column++){
                iterating_rows:
                for(int row = 0; row < map.length; row++){
                    while(row < map.length && map[row][column] != EMPTY){
                        row++;
                    }

                    if(row == map.length){
                        continue;
                    }

                    if(map[row][column] == EMPTY){
                        int emptyRow = row;

                        while(map[row][column] == EMPTY){
                            row++;
                            if(row == map.length){
                                continue iterating_rows;
                            }
                        }

                        if(row == map.length){
                            continue iterating_rows;
                        }

                        if(map[row][column] == SQUARE_ROCK){
                            continue iterating_rows;
                        }

                        if(map[row][column] == ROUND_ROCK){
                            map[row][column] = EMPTY;
                            map[emptyRow][column] = ROUND_ROCK;
                            row = emptyRow;

                        }
                    }
                }
            }
    }

    private static void print(char[][] map){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static long count(char[][] map) {
        long count = 0;
        for (int i = 0; i < map.length; i++) {
            int weight = map.length - i;
            int rocks = 0;
            for (int j = 0; j < map[0].length; j++) {
                char c = map[i][j];

                if(c == ROUND_ROCK){
                    rocks++;
                }
            }

            count += weight * rocks;
        }

        return count;
    }

    static void reverseRows (char mat[][])
    {
        int n = mat.length;
        for (int i = 0; i < mat.length; i++){
            for (int j = 0; j <  mat.length/ 2; j++){
                char temp = mat[i][j];
                mat[i][j] = mat[i][n - j - 1];
                mat[i][n - j - 1] = temp;
            }
        }

    }

    static void transpose (char arr[][])
    {
        for (int i = 0; i < arr.length; i++)
            for (int j = i; j < arr[0].length; j++){
                char temp = arr[j][i];
                arr[j][i] = arr[i][j];
                arr[i][j] = temp;
            }
    }

    static void rotate90 (char arr[][])
    {
        transpose (arr);
        reverseRows (arr);
    }
}
