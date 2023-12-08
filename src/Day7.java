import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {
    static  Map<Character, Integer> cardWeights = new HashMap<>();;
    public static void main(String[] args) throws Exception {
        String[] cards = "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2".split(",");


        int rank = cards.length;
        for(int i = 0; i < cards.length; i++){
            Character card = cards[i].trim().charAt(0);
            cardWeights.put(card, rank--);
        }

        cardWeights.put('J', -1);

        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day7.txt"));

        lines.sort((line1, line2) -> {
            String[] line1parts = line1.split(" ");
            String[] line2parts = line2.split(" ");

            String hand1 = line1parts[0];
            HandType type1 = getHandType(hand1);
            String hand2 = line2parts[0];
            HandType type2 = getHandType(hand2);

            if(type1 != type2){
                return type1.weight - type2.weight;
            } else {
                int pos = 0;
                while (pos < hand1.length()){
                    char c1 = hand1.charAt(pos);
                    char c2 = hand2.charAt(pos);

                    int weight1 = cardWeights.get(c1);
                    int weight2 = cardWeights.get(c2);

                    if(weight1 != weight2){
                        return  weight1 - weight2;
                    } else {
                        pos++;
                    }
                }
            }

            return 0;
        });

        Long sum = 0L;
        for(int i = 0; i < lines.size(); i++){
            Long bid = Long.parseLong(lines.get(i).split(" ")[1]);
            HandType handType = getHandType(lines.get(i).split(" ")[0]);
            System.out.println(lines.get(i) + " : " + handType);
            sum += ((i + 1) * bid);

        }

        System.out.println(sum);
    }

    public static HandType getHandType(String hand){
        char[] chars = hand.toCharArray();

        Map<Character, Integer> cardCounts = new HashMap();

        for(char c : chars){
            int count = cardCounts.getOrDefault(c, 0);
            cardCounts.put(c, count + 1);
        }

        int jokers = cardCounts.getOrDefault('J', 0);

        if(jokers > 0 && jokers != 5){
            Comparator<Map.Entry<Character, Integer>> byCount = Comparator.comparingInt(Map.Entry::getValue);
            Comparator<Map.Entry<Character, Integer>> byRank = Comparator.comparingInt(e -> cardWeights.get(e.getKey()));

            char pretend = cardCounts.entrySet().stream().filter(e -> e.getKey() != 'J')
                    .sorted(byCount.thenComparing(byRank).reversed())
                    .collect(Collectors.toList()).get(0).getKey();

            int countPretend = cardCounts.get(pretend);
            cardCounts.put(pretend, countPretend + jokers);
            cardCounts.remove('J');
        }

        HandType result;

        if(cardCounts.size() == 1){
            result = HandType.FIVE_OF_A_KIND;
        } else if(cardCounts.values().contains(4)){
            result = HandType.FOUR_OF_A_KIND;
        } else if(cardCounts.values().contains(3) && cardCounts.values().contains(2)){
            result = HandType.FULL_HOUSE;
        } else if(cardCounts.values().contains(3)){
            result = HandType.THREE_OF_AKIND;
        }else if(cardCounts.size() == 3 && cardCounts.values().contains(2) && cardCounts.values().contains(1)) {
            result = HandType.TWO_PAIR;
        } else if(cardCounts.values().contains(2)){
            result = HandType.PAIR;
        } else {
            result = HandType.HIGH_CARD;
        }

        //System.out.println(hand + " is a " + result);
        return result;
    }
}

enum HandType {
    FIVE_OF_A_KIND(6),
    FOUR_OF_A_KIND(5),
    FULL_HOUSE(4),
    THREE_OF_AKIND(3),
    TWO_PAIR(2),
    PAIR(1),
    HIGH_CARD(0);

    final int weight;

    HandType(int weight) {
        this.weight = weight;
    }
};
