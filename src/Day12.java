import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day12 {
    static Map<Integer, List<String>> naiveMemory = new HashMap<>();
    static Map<String, Long> notSoNaiveMemory = new HashMap<>();

    static int REPEAT_TIMES = 5;
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day12.txt"));

        long result = 0;

        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            String[] parts = line.split(" ");

            parts[0] = (parts[0]+"?").repeat(REPEAT_TIMES);
            parts[1] = (parts[1] + ",").repeat(REPEAT_TIMES);
            parts[1] = parts[1].substring(0, parts[1].length());

            String input = parts[0].substring(0, parts[0].length() - 1);
            List<Integer> nums = Arrays.stream(parts[1].split(",")).map(Integer::parseInt).toList();

            //result += naive(input, parts[1]);
            result += notSoNaive(input, new ArrayList<>(nums));
        }

        System.out.println("Memory in the end: " + notSoNaiveMemory.size());
        //notSoNaiveMemory.entrySet().forEach(e -> System.out.println(e.getKey() + "(" + e.getValue() + ")"));
        System.out.println("Result: " + result);
    }

    private static long notSoNaive(String input, List<Integer> nums){
        String pattern = nums.stream().map(n -> n.toString()).collect(Collectors.joining(","));
        String key = input + " : " + pattern;

        if(notSoNaiveMemory.containsKey(key)){
            return notSoNaiveMemory.get(key);
        }

        long count = count(input, nums);

        notSoNaiveMemory.put(key, count);

        return count;
    }

    private static long count(String input, List<Integer> nums){
        while(true){

            //Reached the end of validation. If there are springs left over, combination is invalid:
            if(nums.size() == 0){
                return input.contains("#") ? 0 : 1;
            }

            //There are still group lengths to verify but we've reached the end of input. Combination is invalid:
            if(input.isEmpty()){
                return 0;
            }

            //Cleanup: dots in the beginning can be ignored
            while(input.startsWith(".")){
                input = input.replaceFirst(".", "");
            }

            //If starts with ? we need to recurse and calculate both options:
            if(input.startsWith("?")){
                String optionA = "." + input.substring(1);
                String optionB = "#" + input.substring(1);
                return notSoNaive(optionA, new ArrayList<>(nums)) + notSoNaive(optionB, new ArrayList<>(nums));
            }

            if(input.startsWith("#")){
                if(nums.isEmpty() ||  //No more groups to validate against, but we still have springs left, invalid
                   input.length() < nums.get(0)  || //Not enough springs, invalid
                   input.substring(0, nums.get(0)).contains(".")  //Groups must be contiguous, no dots in the group are allowed. invalid
                ) {
                    return 0;
                }

                if(nums.size() > 1){

                    if(input.length() < nums.get(0) + 1 || //Input not long enough to validate the next group){
                       input.charAt(nums.get(0)) == '#'//Contiguous group of springs is longer than next group length
                    ) {
                        return 0;
                    }

                    input = input.substring(nums.get(0) + 1); //Skip group + one character after (.,? - does not matter)
                } else {
                    input = input.substring(nums.get(0));
                }

                nums.remove(0); //On to checking next group!
            }
        }
    }

    private static long naive(String input, String pattern){
        List<Integer> nums = Arrays.stream(pattern.split(",")).map(Integer::parseInt).toList();
        List<String> permutations = getAllPermutations(input.length());

        List<String> matchingInput = permutations.stream()
                .filter(p -> {
                    for(int j = 0; j < input.length(); j++){
                        char c = p.charAt(j);
                        char t = input.charAt(j);

                        if(t != '?' && c != t){
                            return false;
                        }
                    }

                    return true;
                }).toList();

        List<String> matchingNums = matchingInput.stream().filter(p -> {

            for(int x = 0; x < nums.size(); x++){
                int num = nums.get(x);

                String s = buildContiguousStr(num);

                if(x < nums.size() - 1){
                    s = s + ".";
                }

                int idx = p.indexOf(s);
                if(idx == -1 || p.indexOf("#") < idx){
                    return false;
                }

                p = p.substring(idx + s.length());
            }

            //IF there are still extra # chars, it is invalid!
            if(p.contains("#")){
                return false;
            }

            return true;
        }).toList();

        return matchingNums.size();

    }

    private static String buildContiguousStr(int n){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < n; i++){
            sb.append("#");
        }

        return sb.toString();
    }

    private static List<String> getAllPermutations(int length){

        List<String> cached = naiveMemory.get(length);

        if(cached != null){
            return cached;
        }

        int[] arr = new int[length];

        List<String> permutations = new ArrayList<>();
        generateAllPossibleStrings(length, arr, 0, permutations);

        naiveMemory.put(length, permutations);

        return permutations;
    }

    static String toString(int arr[], int n)
    {
        StringJoiner sj = new StringJoiner("");
        for (int i = 0; i < n; i++)
        {
            sj.add(arr[i] == 0 ? "." : "#");
        }

        return sj.toString();
    }

    // Function to generate all binary strings
    static void generateAllPossibleStrings(int n, int arr[], int i, List<String> result)
    {
        if (i == n)
        {

            result.add(toString(arr, n));
            return;
        }

        // First assign "0" at ith position
        // and try for all other permutations
        // for remaining positions
        arr[i] = 0;
        generateAllPossibleStrings(n, arr, i + 1, result);

        // And then assign "1" at ith position
        // and try for all other permutations
        // for remaining positions
        arr[i] = 1;
        generateAllPossibleStrings(n, arr, i + 1, result);
    }
}
