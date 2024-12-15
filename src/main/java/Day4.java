import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day4 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day4.txt"));

        Map<Integer, Integer> cardCount = new HashMap<>();
        for(int i = 0; i < lines.size(); i++){
            cardCount.put(i, 1);
        }

        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);

            String[] parts = line.split(":")[1].split("\\|");

            String[] w = parts[0].trim().split(" ");
            String[] y = parts[1].trim().split(" ");
            Set<Integer> winning = new HashSet<>();
            Set<Integer> yours = new HashSet<>();

            for(String n : w){
                if(!n.trim().isEmpty()){
                    winning.add(Integer.parseInt(n));
                }
            }

            for(String n : y){
                if(!n.trim().isEmpty()){
                    yours.add(Integer.parseInt(n));
                }
            }

            int points = 0;
            for(int n : yours){
                if(winning.contains(n)){
                    points++;
                }
            }

            int copiesOfCurrentCard = cardCount.getOrDefault(i, 1);

            for(int n = 0; n < points; n++){
                int cardId = i + n + 1;
                int prizeCopies = cardCount.getOrDefault(cardId, 1) + copiesOfCurrentCard;
                cardCount.put(cardId, prizeCopies);
            }
        }

        System.out.println(cardCount.values().stream().mapToInt(i -> i).sum());
    }
}
