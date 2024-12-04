import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class Day2024_04 {
    private static char[] xmas = new char[]{'X', 'M', 'A', 'S'};
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_4.txt"));
        long p1 = 0;
        long p2 = 0;
        char[][] map = new char[lines.size()][lines.get(0).length()];

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(0).length(); j++){
                map[i][j] = lines.get(i).charAt(j);
            }
        }

        for(int y = 0; y < lines.size(); y++){
            for(int x = 0; x < lines.get(0).length(); x++){
                char c = map[y][x];

                if(c == 'X'){
                    p1 += searchP1(map, y, x, (a) -> a - 1, (a) -> a);
                    p1 += searchP1(map, y, x, (a) -> a + 1, (a) -> a);
                    p1 += searchP1(map, y, x, (a) -> a, (a) -> a + 1);
                    p1 += searchP1(map, y, x, (a) -> a, (a) -> a - 1);
                    p1 += searchP1(map, y, x, (a) -> a - 1, (a) -> a - 1);
                    p1 += searchP1(map, y, x, (a) -> a + 1, (a) -> a - 1);
                    p1 += searchP1(map, y, x, (a) -> a - 1, (a) -> a + 1);
                    p1 += searchP1(map, y, x, (a) -> a + 1, (a) -> a + 1);
                }

                if(c == 'A') {
                    p2 += searchP2(map, y, x);
                }
            }
        }

        System.out.println(p1);
        System.out.println(p2);
    }

    private static int searchP2(char[][] map, int y, int x){
        try {
            if(((map[y + 1][x - 1] == 'M' && map[y - 1][x + 1] == 'S') || (map[y + 1][x - 1] == 'S' && map[y - 1][x + 1] == 'M')) &&
                    ((map[y - 1][x - 1] == 'M' && map[y + 1][x + 1] == 'S') || (map[y - 1][x - 1] == 'S' && map[y + 1][x + 1] == 'M'))){
                return 1;
            }
        } catch (ArrayIndexOutOfBoundsException e){
            // ¯\_(ツ)_/¯
        }

        return 0;
    }

    private static int searchP1(char[][] map, int y, int x, Function<Integer, Integer> updateX, Function<Integer, Integer> updateY){
        int pos = 0;

        try {
            while(map[y][x] == xmas[pos]){
                x = updateX.apply(x);
                y = updateY.apply(y);
                pos++;

                if(pos == xmas.length){
                    return 1;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            // ¯\_(ツ)_/¯
        }

        return 0;
    }
}
