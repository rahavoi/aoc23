import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day2024_02 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_2.txt"));

        long count = lines.stream().mapToInt(l -> {
            List<Integer> nums = Arrays.stream(l.split(" ")).map(Integer::parseInt).collect(Collectors.toList());

            if((isAllDecreasing(nums) || isAllIncreasing(nums)) && isChangeWithinRange(nums)){
                return 1;
            }

            for(int i = 0; i < nums.size(); i++){
                List<Integer> copy = new ArrayList<>(nums);
                copy.remove(i);

                if((isAllDecreasing(copy) || isAllIncreasing(copy)) && isChangeWithinRange(copy)){
                    return 1;
                }
            }

            return 0;
        }).sum();

        System.out.println(count);
    }

    private static boolean isAllIncreasing(List<Integer> nums){
        List<Integer> copy = new ArrayList<>(nums);
        Collections.sort(copy);
        return nums.equals(copy);
    }

    private static boolean isAllDecreasing(List<Integer> nums){
        List<Integer> copy = new ArrayList<>(nums);
        Collections.sort(copy, Collections.reverseOrder());
        return nums.equals(copy);
    }

    private static boolean isChangeWithinRange(List<Integer> nums){
        for(int i = 0; i < nums.size() - 1; i++){
            if (Math.abs(nums.get(i) - nums.get(i + 1)) > 3 || Math.abs(nums.get(i) - nums.get(i + 1)) < 1){
                return false;
            }
        }

        return true;
    }
}