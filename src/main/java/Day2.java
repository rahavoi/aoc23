import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day2 {

    public static void main(String[] args) throws Exception{
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day2.txt"));
        List<List<Map<String, Integer>>> data = parse(lines);
        System.out.println(part1(data));
        System.out.println(part2(data));
    }
    private static List<List<Map<String, Integer>>> parse(List<String> data) {
        List<List<Map<String, Integer>>> games = new ArrayList<>();

        data.forEach(l -> {
            List<Map<String, Integer>> game = new ArrayList<>();
            String[] sets = l.split(":")[1].trim().split(";");

            for(String s : sets){
                String[] colors = s.trim().split(",");
                Map<String, Integer>  set = new HashMap<>();

                for(String color : colors){
                    String[] p = color.trim().split(" ");
                    int num = Integer.parseInt(p[0].trim());
                    color = p[1].trim();
                    set.put(color, num);
                }

                game.add(set);
            }

            games.add(game);
        });

        return games;
    }

    public static int part1(List<List<Map<String, Integer>>> games){
        int result = 0;
        int red_max = 12;
        int green_max = 13;
        int blue_max = 14;

        for(int i = 0; i < games.size(); i++){
            List<Map<String, Integer>> game = games.get(i);

            boolean valid = true;
            outer:
            for(Map<String, Integer> set : game){
                for(Map.Entry<String, Integer> e : set.entrySet()) {
                    String color = e.getKey();
                    int num = e.getValue();

                    valid = !(
                        (color.equals("red") && num > red_max) ||
                        (color.equals("green") && num > green_max) ||
                        (color.equals("blue") && num > blue_max)
                    );

                    if(!valid){
                        break outer;
                    }
                }
            }

            if(valid){
                result += i + 1;
            }
        }

        return result;
    }

    private static int part2(List<List<Map<String, Integer>>> games){
        int result = 0;

        for(List<Map<String, Integer>> game : games){
            long reds = 0;
            long greens = 0;
            long blues = 0;

            for(Map<String, Integer> set : game){
                for(Map.Entry<String, Integer> e : set.entrySet()) {
                    String color = e.getKey();
                    int num = e.getValue();

                    if(color.equals("red")){
                        reds = Math.max(reds, num);
                    } else if(color.equals("green")){
                        greens = Math.max(greens, num);
                    } else if(color.equals("blue")){
                        blues = Math.max(blues, num);
                    }
                }
            }

            result += reds * greens * blues;
        }

        return result;
    }
}