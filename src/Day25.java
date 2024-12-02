import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day25 {
    public static void main(String[] args) throws Exception {
        System.out.println("Go");
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day25.txt"));

        for(String l: lines){
            String[] parts = l.split(": ");
            String from = parts[0];
            String[] to = parts[1].split(" ");

            for(String s : to){
                System.out.println(from + " -> " + s + " [dir = both];");
            }
        }
    }
}
