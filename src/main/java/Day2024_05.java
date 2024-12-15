import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day2024_05 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_5a.txt"));

        Map<Integer, List<Integer>> pagesBefore = new HashMap<>();

        Set<Integer> allNumbers = new HashSet<>();

        for(String l : lines) {
            String[] parts = l.split("\\|");

            Integer first = Integer.parseInt(parts[0]);
            Integer second = Integer.parseInt(parts[1]);

            List<Integer> allBefore = pagesBefore.getOrDefault(second, new ArrayList<>());
            allBefore.add(first);
            pagesBefore.put(second, allBefore);

            allNumbers.add(first);
            allNumbers.add(second);
        }

        List<String> input2 = Files.readAllLines(Paths.get("resources/Day2024_5b.txt"));
        List<List<Integer>> updates = new ArrayList<>();

        for(String l : input2) {
            updates.add(Arrays.stream(l.split(",")).map(e -> {
                int n = Integer.parseInt(e);
                allNumbers.add(n);
                return n;
            }).collect(Collectors.toList()));
        }

        int sum = 0;

        List<List<Integer>> incorrect = new ArrayList<>();
        outer:
        for(List<Integer> update : updates){
            for(int i = 0; i < update.size(); i++){
                int cur = update.get(i);
                List<Integer> before = pagesBefore.get(cur);

                if(before == null){
                    continue ;
                }

                for(int j = i + 1; j < update.size(); j++){
                  if(before.contains(update.get(j))){
                      incorrect.add(update);
                      continue outer;
                  }
                }
            }

            sum += update.get(update.size() / 2);
        }

        int sumP2 = 0;

        for(List<Integer> badUpdate : incorrect) {
            int origSize = badUpdate.size();
            List<Integer> corrected = new ArrayList<>();

            while(corrected.size() != origSize){
                Integer first = badUpdate.stream().filter(n -> pagesBefore.get(n).stream().noneMatch(before -> badUpdate.contains(before))).findFirst().get();
                corrected.add(first);
                badUpdate.remove(first);
            }

            sumP2 += corrected.get(corrected.size() / 2);

        }

        System.out.println(sum);
        System.out.println(sumP2);
    }
}