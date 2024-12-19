import java.util.*;
import java.util.stream.Collectors;

public class Day2024_17 {
    public static void main(String[] args) throws Exception {
        String input = "2,4,1,2,7,5,4,7,1,3,5,5,0,3,3,0";


        List<Integer> numsToMatch = new ArrayList<>(Arrays.stream(input.split(",")).map(Integer::parseInt).toList());
        Collections.reverse(numsToMatch);
        int matchIdx = numsToMatch.size() - 1;
        long regA = 0L;
        int matchCnt = 1;

        outer:
        while(matchIdx >= 0){
            State state = new State(regA);
            String output = runProgram(state, input);
            List<Integer> outNums = new ArrayList<>(Arrays.stream(output.split(",")).map(Integer::parseInt).toList());
            Collections.reverse(outNums);

            if(matchCnt == outNums.size()){
                for(int i = 0; i < outNums.size(); i++){
                    if(outNums.get(i) != numsToMatch.get(i)){
                        regA++;
                        continue outer;
                    }
                }

                System.out.println("Matched for " + regA);
                System.out.println(output);
                regA*=8;
                matchIdx--;
                matchCnt++;

            } else {
                regA++;
            }
        }
    }

    private static String runProgram(State state, String input) {
        List<Integer> program = Arrays.stream(input.split(",")).map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        int instructionPointer = 0;
        StringJoiner sj = new StringJoiner(",");

        try {
            while(true){
                int instruction = program.get(instructionPointer);
                int operand = program.get(instructionPointer + 1);
                switch (instruction){
                    case 0: //adv (division)
                        long numerator = state.registerA;
                        long denominator = (long) Math.pow(2, state.getComboOperand(operand)); //??
                        state.registerA = numerator / denominator;
                        instructionPointer+=2;
                        break;
                    case 1: //bxl (bitwise OR)
                        state.registerB = state.registerB ^ operand;
                        instructionPointer+=2;
                        break;
                    case 2: //bst
                        state.registerB = state.getComboOperand(operand) % 8;
                        instructionPointer+=2;
                        break;
                    case 3: //jnz
                        if(state.registerA != 0){
                            instructionPointer = operand;
                        } else {
                            instructionPointer+=2;
                        }
                        break;
                    case 4: //bxc
                        state.registerB = state.registerB ^ state.registerC;
                        instructionPointer+=2;
                        break;
                    case 5: //out
                        Long out = state.getComboOperand(operand) % 8;
                        sj.add(out.toString());
                        instructionPointer+=2;
                        break;
                    case 6: //bdv
                        long numeratorB = state.registerA;
                        long denominatorB = (long) Math.pow(2, state.getComboOperand(operand));
                        state.registerB = numeratorB / denominatorB;
                        instructionPointer+=2;
                        break;
                    case 7: //cdv
                        long numeratorC = state.registerA;
                        long denominatorC = (long) Math.pow(2, state.getComboOperand(operand));
                        state.registerC = numeratorC / denominatorC;
                        instructionPointer+=2;
                        break;
                }
            }
        } catch (IndexOutOfBoundsException e){
            //
            //System.out.println("Halt");
        }
        return sj.toString();
    }

    static class State {
        long registerA;
        long registerB = 0;
        long registerC = 0;

        public State(long registerA) {
            this.registerA = registerA;
        }

        public long getComboOperand(int val){
            if(val < 4){
                return val;
            }

            if(val == 4){
                return registerA;
            }

            if(val == 5){
                return registerB;
            }

            if(val == 6){
                return registerC;
            }

            throw new IllegalArgumentException("Invalid operand: " + val);
        }
    }

    record TestProgram(String opt, int matchLen){}
}
