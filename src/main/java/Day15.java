import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day15.txt"));
        Map<Integer, List<String>> boxes = new HashMap<>();

        long part1 = 0;
        for(String s : lines.get(0).split(",")){
            String label;
            if(s.contains("-")){
                label = s.substring(0, s.length() - 1);

                int hash = hash(label);

                List<String> lenses = boxes.getOrDefault(hash, new ArrayList<>());

                for(String l : lenses){
                    if(l.startsWith(label)){
                        lenses.remove(l);
                        break;
                    }
                }
                boxes.put(hash, lenses);
            } else if(s.contains("=")) {
                String[] parts = s.split("=");
                label = parts[0];
                int focalLength = Integer.parseInt(parts[1]);
                int hash = hash(label);
                List<String> lenses = boxes.getOrDefault(hash, new ArrayList<>());
                String fullName = label + ":" + focalLength;

                for(int i = 0; i < lenses.size(); i++){
                    String l = lenses.get(i);
                    if(l.startsWith(label)){
                        lenses.set(i, fullName);
                        break;
                    }
                }

                if(!lenses.contains(fullName)){
                    lenses.add(fullName);
                }

                boxes.put(hash, lenses);
            }

            part1 += hash(s);
        }

        long part2 = getFocusingPower(boxes);

        System.out.println(part1);
        System.out.println(part2);
    }

    private static long getFocusingPower(Map<Integer, List<String>> boxes){
        long result = 0;
        for(int i = 0; i < 255; i++){
            List<String> lenses = boxes.getOrDefault(i, new ArrayList<>());

            for(int j = 0; j < lenses.size(); j++){
                long one_plus_box_num = 1 + i;
                long slot_num = 1 + j;
                long focal_len = Long.parseLong(lenses.get(j).split(":")[1]);

                result += one_plus_box_num * slot_num * focal_len;
            }
        }

        return result;
    }

    private static int hash(String input){
        int value = 0;
        for(char c : input.toCharArray()){
            int code = 0 + c;
            value += code;
            value *= 17;
            value = value % 256;
        }

        return value;
    }
}
