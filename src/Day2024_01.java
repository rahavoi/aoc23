import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day2024_01 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day2024_1.txt"));

        Set<Integer> left = new HashSet<>();
        List<Integer> right = new ArrayList<>();

        for(String l : lines){
            String[] parts = l.split("   ");
            int lft = Integer.parseInt(parts[0]);
            int rght = Integer.parseInt(parts[1]);

            left.add(lft);
            right.add(rght);
        }

        /*
        Collections.sort(left);
        Collections.sort(right);

        long sum = 0;
        for(int i = 0; i < left.size(); i++){
            int l = left.get(i);
            int r = right.get(i);

            System.out.println(l);

            int distance = Math.abs(l - r);
            System.out.println(distance);
            sum+= distance;
        }

        System.out.println(sum);
         */

        Map<Integer, Integer> occ = new HashMap<>();

        for(int l : left){
            int count = 0;

            for(int r : right){
                if (l == r) {
                    count++;
                }
            }

            occ.put(l, count);
        }

        long sum = 0;

        for(int l : left){
            int score = l * occ.get(l);
            sum += score;
        }

        System.out.println(sum);
    }
}
