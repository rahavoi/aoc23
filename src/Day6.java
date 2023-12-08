import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day6 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day6.txt"));

        String[] time = "41777096".split(" ");
        String[] distance = "249136211271011".split(" ");


        List<Integer> wins = new ArrayList<>();

        for(int i = 0; i < time.length; i++){
            long t = Long.parseLong(time[i]);
            long d = Long.parseLong(distance[i]);

            int countWins = 0;

            for(int j = 0; j < t; j++){
                if(j %10000 == 0){
                    System.out.println("Remaining: " + (t - j));
                }
                long speed = j;
                long goingTime = t - j;

                long resultingDistance = goingTime * speed;

                if(resultingDistance > d){
                    countWins++;
                }
            }

            wins.add(countWins);
        }

        long result = wins.stream().mapToLong(i -> i).reduce(1, (a, b) -> a * b);
        System.out.println(result);

    }
}
