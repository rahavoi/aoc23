import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2024_07 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_7.txt"));
        long result = 0l;
        for(String l : lines){
            String[] parts = l.split(":");
            long output = Long.parseLong(parts[0]);

            List<Long> nums = Arrays.stream(parts[1].trim().split(" ")).map(e -> Long.parseLong(e)).collect(Collectors.toList());

            if(isValid(output, nums)) {
                result+=output;
            }

        }

        System.out.println(result);
    }

    private static boolean isValid(long output, List<Long> nums) {
        return isValid(output, nums, 0, nums.get(0));
    }

    private static boolean isValid(long output, List<Long> nums, int pos, long cur){
        pos++;
        if(pos >= nums.size()){
            return output == cur;
        }


        if(cur > output){
            return false;
        }

        return isValid(output, nums, pos, cur + nums.get(pos)) ||
                isValid(output, nums, pos, cur * nums.get(pos)) ||
                isValid(output, nums, pos, Long.parseLong(cur + "" + nums.get(pos)));
    }
}

