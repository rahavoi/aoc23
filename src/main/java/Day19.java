import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Day19 {
    public static void main(String[] args) throws Exception{
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day19.txt"));

        Map<String, String[]> flows = new HashMap<>();

        int idx = 0;
        for(int i = 0; i < lines.size(); i++){
            if(lines.get(i).isEmpty()){
                idx = i + 1;
                break;
            }

            String[] parts = lines.get(i).split("\\{");
            String name = parts[0];
            String[] rules = parts[1].replace("}", "").split(",");

            flows.put(name, rules);
        }

        PartRange startRange = new PartRange(
                new Range(1, 4000),
                new Range(1, 4000),
                new Range(1, 4000),
                new Range(1, 4000)
        );

        String[] startFlow = flows.get("in");

        Queue<PartRangeAndFlow> q = new LinkedList<>();
        q.add(new PartRangeAndFlow(startRange, startFlow));
        List<PartRange> accepted = new ArrayList<>();

        while(!q.isEmpty()){
            PartRangeAndFlow partRangeAndFlow = q.poll();
            PartRange pr = partRangeAndFlow.partRange;

            for(String rule : partRangeAndFlow.flow){
                if(rule.equals("R")){
                    break;
                }

                if(rule.equals("A")){
                    accepted.add(pr);
                    break;
                }

                if(!rule.contains(":")){
                    q.add(new PartRangeAndFlow(pr, flows.get(rule)));
                    break;
                }

                String[] condition = rule.split(":");
                String resultingFlow = condition[1];
                String input = condition[0];

                boolean hasLessThanOperator = input.contains("<");

                String attributeName = input.split(hasLessThanOperator ? "<" : ">")[0];
                int value = Integer.parseInt(input.split(hasLessThanOperator ? "<" : ">")[1]);

                Range range = pr.getByName(attributeName);

                boolean match = hasLessThanOperator ?
                        range.min < value :
                        range.max > value;

                if(match){
                    //now we need to split range into 2: one matching, other not, depending on the operator used.
                    Range newRange = hasLessThanOperator ? new Range(range.min, value - 1) : new Range(value + 1, range.max);
                    PartRange newPartRange = new PartRange(pr.x, pr.m, pr.a, pr.s);
                    newPartRange.setByName(attributeName, newRange);

                    //Updating current range to continue evaluation:
                    Range curRange = hasLessThanOperator ? new Range(value, range.max) : new Range(range.min, value);
                    pr.setByName(attributeName, curRange);


                    if(resultingFlow.equals("A")){
                        accepted.add(newPartRange);
                    } else if(resultingFlow.equals("R")){
                        //break;
                        //Carry on?
                    } else {
                        q.add(new PartRangeAndFlow(newPartRange, flows.get(resultingFlow)));
                    }


                } else {
                    //carry on
                }
            }
        }

        Long result = accepted.stream().mapToLong(pr ->
                (long) (pr.x.max - pr.x.min + 1) *
        (pr.m.max - pr.m.min + 1) *
        (pr.a.max - pr.a.min + 1) *
        (pr.s.max - pr.s.min + 1)).sum();

        System.out.println("Part2: " + result);

        //167409079868000
        //205670790028854

        //part1(lines, idx, flows);

    }

    static void part1(List<String> lines, int idx, Map<String, String[]> flows){
        List<Part> parts = new ArrayList<>();

        for(int i = idx; i < lines.size(); i++){
            String[] input = lines.get(i).replace("\\{", "").replace("}", "").split(",");

            int x = Integer.parseInt(input[0].split("=")[1]);
            int m = Integer.parseInt(input[1].split("=")[1]);
            int a = Integer.parseInt(input[2].split("=")[1]);
            int s = Integer.parseInt(input[3].split("=")[1]);

            parts.add(new Part(x, m, a, s));
        }

        String[] initialRules = flows.get("in");

        Queue<PartAndFlow> q = new LinkedList<>();

        for(Part p : parts){
            q.add(new PartAndFlow(p, initialRules));
        }

        List<Part> accepted = new ArrayList<>();

        while(!q.isEmpty()){
            PartAndFlow pf = q.poll();
            Part p = pf.part;

            for(String rule : pf.flow){
                if(rule.equals("R")){
                    break;
                }

                if(rule.equals("A")){
                    accepted.add(p);
                    break;
                }

                if(!rule.contains(":")){
                    q.add(new PartAndFlow(p, flows.get(rule)));
                    break;
                }

                String[] condition = rule.split(":");
                String input = condition[0];

                boolean hasLessThanOperator = input.contains("<");

                String attributeName = input.split(hasLessThanOperator ? "<" : ">")[0];
                int value = Integer.parseInt(input.split(hasLessThanOperator ? "<" : ">")[1]);

                boolean match = hasLessThanOperator ?
                        p.getByName(attributeName) < value :
                        p.getByName(attributeName) > value;

                String resultingFlow = condition[1];

                if(match){
                    if(resultingFlow.equals("A")){
                        accepted.add(p);
                        break;
                    }

                    if(resultingFlow.equals("R")){
                        break;
                    }

                    q.add(new PartAndFlow(p, flows.get(resultingFlow)));
                    break;
                }
            }

        }

        accepted.stream().mapToLong(p -> p.x + p.m + p.a + p.s).sum();

        System.out.println(accepted.stream().mapToLong(p -> p.x + p.m + p.a + p.s).sum());
    }

    static class PartAndFlow {
        Part part;
        String[] flow;

        public PartAndFlow(Part part, String[] flow) {
            this.part = part;
            this.flow = flow;
        }
    }

    static class PartRangeAndFlow {
        PartRange partRange;
        String[] flow;

        public PartRangeAndFlow(PartRange partRange, String[] flow) {
            this.partRange = partRange;
            this.flow = flow;
        }
    }

    static class PartRange {
        Range x;
        Range m;
        Range a;
        Range s;

        public PartRange(Range x, Range m, Range a, Range s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;
        }

        public Range getByName(String name){
            return switch (name){
                case "x" -> x;
                case "m" -> m;
                case "a" -> a;
                case "s" -> s;
                default -> throw new IllegalArgumentException("wrong attribute name");
            };
        }

        public void setByName(String name, Range range){
            switch (name){
                case "x" -> x = range;
                case "m" -> m = range;
                case "a" -> a = range;
                case "s" -> s = range;
                default -> throw new IllegalArgumentException("wrong attribute name");
            };
        }
    }

    static class Range {
        int min;
        int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    static class Part {
        int x;
        int m;
        int a;
        int s;

        public Part(int x, int m, int a, int s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;
        }

        public int getByName(String name){
            return switch (name){
                case "x" -> x;
                case "m" -> m;
                case "a" -> a;
                case "s" -> s;
                default -> throw new IllegalArgumentException("wrong attribute name");
            };
        }
    }
}
