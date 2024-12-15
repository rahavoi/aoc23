import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2024_03 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("resources/Day2024_3.txt"));

        long result = 0;
        boolean enabled = true;
        Pattern pattern = Pattern.compile("(mul\\(\\d+,\\d+\\))|do\\(\\)|don't\\(\\)");

        for(String l : lines){
            Matcher matcher = pattern.matcher(l);

            List<String> valid = new ArrayList<>();

            while (matcher.find()){
                valid.add(matcher.group());
            }

            for(String s : valid){
                if(s.equals("don't()") ){
                    enabled = false;
                    continue;
                }
                if(s.equals("do()") ){
                    enabled = true;
                    continue;
                }

                if(enabled){
                    String[] parts = s.replaceAll("mul\\(", "").replaceAll("\\)", "").split(",");
                    result += Integer.parseInt(parts[0]) * Integer.parseInt(parts[1]);
                }
            }
        }

        System.out.println(result);
    }
}
