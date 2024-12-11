import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day2024_9 {
    public static void main(String[] args) throws Exception {
        List<String> discMap = parseData();
        //p1(discMap);
        p2(discMap);
        calculateChecksum(discMap);
    }

    private static void calculateChecksum(List<String> discMap) {
        long checksum = 0;
        for (int i = 0; i < discMap.size(); i++) {
            String s = discMap.get(i);
            if (s.equals(".")) {
                continue;
            }

            long val = Long.parseLong(s);
            checksum += val * i;
        }

        System.out.println(checksum);
    }

    private static List<String> parseData() throws IOException {
        String input = Files.readAllLines(Paths.get("resources/Day2024_9.txt")).get(0);

        List<String> discMap = new ArrayList<>();

        int id = 0;
        for (int i = 0; i < input.length(); i++) {
            int times = (input.charAt(i)) - '0';
            boolean freeSpace = i % 2 != 0;

            if (freeSpace) {
                for (int x = 0; x < times; x++) {
                    discMap.add(".");
                }
            } else {
                for (int x = 0; x < times; x++) {
                    discMap.add(Integer.toString(id));
                }
                id++;
            }
        }
        return discMap;
    }

    private static void p2(List<String> discMap) {
        int searchFrom = discMap.size() - 1;
        String idNumber = discMap.get(discMap.size() - 1);

        while(idNumber.equals(".")){
            searchFrom--;
            idNumber = discMap.get(searchFrom);
        }

        while (!idNumber.equals("0")) {
            int spaceNeeded = 0;

            while (discMap.get(searchFrom).equals(idNumber)) {
                spaceNeeded++;
                searchFrom--;
            }

            int spaceFound = 0;
            for (int i = 0; i < searchFrom + spaceNeeded - 1; i++) {
                String t = discMap.get(i);
                if (t.equals(".")) {
                    spaceFound++;
                } else {
                    spaceFound = 0;
                }

                if (spaceFound == spaceNeeded) {
                    int swapFrom = i - spaceNeeded + 1;
                    int swapTo = searchFrom + 1;

                    for (int x = 0; x < spaceNeeded; x++) {
                        discMap.set(swapFrom++, idNumber);
                        discMap.set(swapTo++, ".");
                    }

                    break;
                }
            }

            while (discMap.get(searchFrom).equals(".")) {
                searchFrom--;
            }

            idNumber = discMap.get(searchFrom);
        }
    }

    private static void p1(List<String> discMap) {
        for(int i = 0; i < discMap.size(); i++){
            String s1 = discMap.get(i);

            if(s1.equals(".")){
                //Find non empty from the end and swap them;
                for(int j = discMap.size() - 1; j > i; j--){
                    String s2 = discMap.get(j);
                    if(!s2.equals(".")){
                        discMap.set(i, s2);
                        discMap.set(j, s1);
                        break;
                    }
                }
            }
        }
    }
}
