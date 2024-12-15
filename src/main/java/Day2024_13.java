import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day2024_13 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_13.txt"));

        //List<Machine> machines = new ArrayList<>();
        long tokens = 0;
        for(int i = 0; i < lines.size(); i+=4){
            String[] btnAInput = lines.get(i).replaceAll("Button A: X\\+", "").replaceAll(" Y\\+", "").split(",");
            String[] btnBInput = lines.get(i + 1).replaceAll("Button B: X\\+", "").replaceAll(" Y\\+", "").split(",");
            String[] prizeInput = lines.get(i + 2).replaceAll("Prize: X=", "").replaceAll(" Y=", "").split(",");

            Button btnA = new Button(Long.parseLong(btnAInput[0]), Long.parseLong(btnAInput[1]), 3);
            Button btnB = new Button(Long.parseLong(btnBInput[0]), Long.parseLong(btnBInput[1]), 1);

            //Prize prize = new Prize(Long.parseLong(prizeInput[0]), Long.parseLong(prizeInput[1]));
            Prize prize = new Prize(Long.parseLong(prizeInput[0]) + 10000000000000L, Long.parseLong(prizeInput[1]) + 10000000000000L);

            //Machine machine = new Machine(btnA, btnB, prize);
            //machines.add(machine);
            long prizeDiff = prize.x * btnB.y - prize.y * btnB.x;
            long xTmp = (btnA.x * btnB.y - btnA.y * btnB.x);

            if(prizeDiff % xTmp == 0) {

                long btnACnt = prizeDiff / xTmp;
                long btnBCnt = (prize.x - btnACnt * btnA.x) / btnB.x;

                if(!(btnBCnt * btnB.x + btnACnt * btnA.x == prize.x)){
                    System.out.println("WTF"); // Spent too much time here..
                } else {
                    tokens += (btnACnt * 3) + btnBCnt;
                }

            }
        }

        System.out.println("Tokens: " + tokens);
        /*
        Part 1 brute force
        int tokens = 0;
        for(Machine m : machines){
            List<Long> games = getGames(m);

            if(games.size() > 0){
                long min = games.stream().mapToLong(l -> l).min().getAsLong();
                tokens += min;
            }
        }
         */
    }

    private static List<Long> getGames(Machine m){
        //System.out.println("Let's go");
        List<Long> games = new ArrayList<>();

        Set<GameConfig> gameConfigs = new HashSet<>();

        play(games, m, new Prize(0,0), 0L, 0, 0, gameConfigs);

        return games;
    }

    private static void play(List<Long> games, Machine m, Prize currentLocation, Long tokens, int btnACount, int btnBCount, Set<GameConfig> gameConfigs){
        if(!gameConfigs.add(new GameConfig(btnACount, btnBCount))){
            return;
        }

        if(btnACount > 100 || btnBCount > 100){
            return;
        }
        if(currentLocation.x > m.prize.x || currentLocation.y > m.prize.y){
            return;
        }

        if(currentLocation.x == m.prize.x && currentLocation.y == m.prize.y){
            games.add(tokens);
            return;
        }
        //Push A:
        play(games, m, new Prize(currentLocation.x + m.btnA.x, currentLocation.y + m.btnA.y), tokens + m.btnA.cost, btnACount + 1, btnBCount, gameConfigs);
        //Push B:
        play(games, m, new Prize(currentLocation.x + m.btnB.x, currentLocation.y + m.btnB.y), tokens + m.btnB.cost, btnACount, btnBCount + 1, gameConfigs);
    }

    public record Button(long x, long y, int cost){}
    public record Prize(long x, long y) {}
    public record Machine(Button btnA, Button btnB, Prize prize) {}

    public record GameConfig(int btnACount, int btnBCount){}
}
