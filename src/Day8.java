import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) throws Exception {
        long result = 0;
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day8.txt"));

        String instructions = "LRRLRRRLLRRLRRLRRRLRLRRLRRLRRRLRRRLRRLRLLRRLRLRRLRRLRLRLRRLRRLRRRLLRLLRRLRLRRRLRRRLLRRRLRRLRLLRRLRRRLRLLRLRLLRRRLRLRRRLLRRRLRRRLRRLLRLRLLRRLRRLLRRRLLRLLRRLRRRLRLRRRLRLRRLRLRLRRLRRLRRLLLRRRLRLRLLLRRRLLRLRRLRRRLRRLRRLRRRLRRRLRRLLRLLRRLRRRLLRRRLRLRLRRRLRRRLRRLRRLRLLRLRRLLRRLLRRRR";

        Map<String, Node> nodes = new HashMap<>();
        long steps = 0;
        String end = "ZZZ";

        for(String l : lines){
            String[] parts = l.split(" = ");
            String name = parts[0];
            String[] neighbors =  parts[1].replaceAll("\\(", "").replaceAll("\\)", "").split(", ");
            String left = neighbors[0];
            String right = neighbors[1];

            Node n = new Node();
            n.name = name;
            n.left = left;
            n.right = right;

            nodes.put(name, n);
        }

        List<Node> curNodes = nodes.keySet().stream().filter(n -> n.endsWith("A")).map(n -> nodes.get(n))
                        .collect(Collectors.toList());


        //TODO: LCM for each.
        //16897
        //21883
        //13019
        //19667
        //20221
        //16343


        List<Long> allPaths = curNodes.stream().map(n -> {
            String startName = n.name;
            long path = 0;
            int instructionPos = 0;

            while(!n.name.endsWith("Z123")){
                if(instructionPos >= instructions.length()){
                    instructionPos = 0;
                }

                List<Node> newNodes = new ArrayList<>();
                char instruction = instructions.charAt(instructionPos++);

                Node cur;
                if(instruction == 'L'){
                    cur = nodes.get(n.left);
                } else {
                    cur = nodes.get(n.right);
                }

                if(n.name.endsWith("Z")){
                    System.out.println(startName + "-> " + n.name + " : " + path);
                    return path;
                }

                n = cur;
                path++;
            }
            //Unreachable code
            return - 1L;
        }).toList();

        System.out.println(lcm(allPaths));
    }

    private static long gcd(long a, long b)
    {
        while (b > 0)
        {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private static long gcd(long[] input)
    {
        long result = input[0];
        for(int i = 1; i < input.length; i++) result = gcd(result, input[i]);
        return result;
    }

    private static long lcm(long a, long b)
    {
        return a * (b / gcd(a, b));
    }

    private static long lcm(List<Long> input)
    {
        long result = input.get(0);
        for(int i = 1; i < input.size(); i++) result = lcm(result, input.get(i));
        return result;
    }

    static class Node {
        String name;
        String left;
        String right;
    }
}
