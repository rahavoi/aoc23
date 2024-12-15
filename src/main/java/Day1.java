import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day1.txt"));

        int part1 = 0;
        int part2 = 0;


        for(String line : lines){
            line = line.replace("zero", "zero0zero");
            line = line.replace("one", "one1one");
            line = line.replace("two", "two2two");
            line = line.replace("three", "three3three");
            line = line.replace("four", "four4four");
            line = line.replace("five", "five5five");
            line = line.replace("six", "six6six");
            line = line.replace("seven", "seven7seven");
            line = line.replace("eight", "eight8eight");
            line = line.replace("nine", "nine9nine");

            System.out.println(line);

            line = line.replaceAll("[^\\d.]", "");

            String first = line.substring(0, 1);
            String last = line.substring(line.length() - 1);

            part2 += Integer.parseInt(first + last);

        }


        System.out.println(part1);
        System.out.println(part2);
    }
}
