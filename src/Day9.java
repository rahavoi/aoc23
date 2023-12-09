import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {
    public static void main(String [] args) throws  Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day9.txt"));

        long result = lines.stream().mapToLong(l -> {
            List<Long> numbers = Arrays.stream(l.split(" ")).map(Long::parseLong).toList();
            List<List<Long>> allDiffs = new ArrayList<>();
            List<Long> cur = numbers;

            while(!isAllZeroes(cur)){
                List<Long> diffs = getDifferences(cur);
                allDiffs.add(diffs);
                cur = diffs;
            }

            //return numbers.get(numbers.size() - 1) + allDiffs.stream().mapToLong(d -> d.get(d.size() - 1)).sum();

            long diff = 0;
            for(int i = allDiffs.size() - 1; i >= 0; i--){
                long higher = allDiffs.get(i).get(0);



                long adiff = higher - diff;

                diff = adiff;

            }
            return numbers.get(0) - diff;



        }).sum();

        System.out.println(result);
    }

    private static boolean isAllZeroes(List<Long> nums){
        for(Long n : nums){
            if(n != 0){
                return false;
            }
        }

        return true;
    }

    private static List<Long> getDifferences(List<Long> nums){
        List<Long> result = new ArrayList<>();

        for(int i = 1; i < nums.size(); i++){
            long prev = nums.get(i  - 1);
            long cur = nums.get(i);
            long diff = cur - prev;

            result.add(diff);
        }

        return result;
    }


}
