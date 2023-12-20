import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day20 {
    static String FLIP_FLOP = "%";
    static String CONJUNCTION = "&";
    static String BROADCASTER = "broadcaster";

    static Map<String, Module> modules = new HashMap<>();

    static Queue<ModulePulse> processingQueue = new LinkedList<>();

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day20.txt"));

        for(String l : lines){
            String[] parts = l.split(" -> ");
            String typeAndName = parts[0];
            List<String> outputs = Arrays.asList(parts[1].split(", "));

            Module module = null;
            String name = "";
            String type = "";

            if(typeAndName.equals(BROADCASTER)){
                name = BROADCASTER;
                type = BROADCASTER;
            } else {
                name = typeAndName.substring(1);

                if(typeAndName.startsWith("%")){
                    type = FLIP_FLOP;
                } else if(typeAndName.startsWith("&")){
                    type = CONJUNCTION;
                }
            }

            if(type.equals(BROADCASTER)){
                module = new BroadcastModule(BROADCASTER, outputs);
            } else if(type.equals(FLIP_FLOP)){
                module = new FlipFlopModule(name, outputs);
            } else if(type.equals(CONJUNCTION)){
                module = new ConjunctionModule(name, outputs);
            }

            modules.put(name, module);

            //Init inputs for conjunction module:
            modules.values().stream()
                    .filter(m -> m instanceof ConjunctionModule)
                    .forEach(cm -> modules.values().stream().filter(m -> m.getOutputs().contains(cm.getName()))
                            .forEach(m -> ((ConjunctionModule)cm).addInput(m.getName())));
        }

        long countLow = 0;
        long countHigh = 0;

        //Part 1 Push button 1000 times:
        for(int i = 0; i < 1000; i++){
            //System.out.println("Pushing button. Round " + (i + 1) + ":");
            processingQueue.add(new ModulePulse(modules.get(BROADCASTER), false, "button"));

            while (!processingQueue.isEmpty()){
                ModulePulse mp = processingQueue.poll();
                Module m = mp.module;
                boolean pulse = mp.pulse;

                if(pulse){
                    countHigh++;
                } else {
                    countLow++;
                }

                String n = mp.module != null ? mp.module.getName() : "output";
                //System.out.println(mp.inputName + " -" + (mp.pulse? "high" : "low") + "-> " + n);


                //ALRIGHT STOP!
                //HAMMER TIME!
                //For part 2 I noticed that the only to way reach the final module (rx)
                //is via the Conjunction module (bb), which, turn receives inputs from
                // modules xc,ct,kp,ks.
                //Which means that bb will send low pulse to xp only when it receives high pulse from all inputs.
                //Being lazy, I just increased the number of button pushes to 1million iterations
                //And counted how often each input of bb is sending a high pulse (they are doing so in constant loops, yay):

                //xc loop is 3823
                //ct loop is 3797
                //kp loop is 3733
                //ks loop is 3907
                // After that I just needed to find an lcm for these numbers, which, again, I was too lazy to code.
                //Maybe later
                if(mp.module!= null){
                    if(mp.inputName.equals("ks") && pulse){
                        System.out.println("Sends high pulse after " + (i + 1) + " pushes");
                    }
                    m.receive(mp.inputName, pulse);
                }
            }

            //System.out.println();
            //System.out.println();
        }

        System.out.println(countLow);
        System.out.println(countHigh);
        System.out.println(countLow * countHigh);
    }

    interface Module {
        //false - low, true - high
        void receive(String inputName, boolean pulseHigh);

        void send();

        List<String> getOutputs();
        String getName();
    }

    static class FlipFlopModule implements Module {
        String name;
        List<String> outputs;

        //Initially off
        boolean on;

        public FlipFlopModule(String name, List<String> outputs) {
            this.name = name;
            this.outputs = outputs;
        }

        @Override
        public void receive(String inputName, boolean pulseHigh){
            //If a flip-flop module receives a high pulse, it is ignored and nothing happens.
            //if a flip-flop module receives a low pulse, it flips between on and off.
            if(!pulseHigh){
                on = !on;

                send();
            }
        }

        public void send() {
            //On - high pulse, off - low pulse
            outputs.forEach(o -> {
                Module outModule = modules.get(o);
                ModulePulse modulePulse = new ModulePulse(outModule, on, name);
                processingQueue.add(modulePulse);
            });
        }

        @Override
        public List<String> getOutputs() {
            return outputs;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    static class ConjunctionModule implements Module {
        String name;
        List<String> outputs;

        Map<String, Boolean> inputsMemory = new HashMap<>();

        public ConjunctionModule(String name, List<String> outputs) {
            this.name = name;
            this.outputs = outputs;
        }


        @Override
        public void receive(String inputName, boolean pulseHigh) {
            inputsMemory.put(inputName, pulseHigh);
            send();
        }

        @Override
        public void send() {
            boolean signal = inputsMemory.values().stream().allMatch(v -> v);
            outputs.forEach(o -> {
                Module outModule = modules.get(o);
                ModulePulse modulePulse = new ModulePulse(outModule, !signal, name);
                processingQueue.add(modulePulse);
            });
        }

        @Override
        public List<String> getOutputs() {
            return outputs;
        }

        @Override
        public String getName() {
            return name;
        }

        public void addInput(String name){
            //Low pulse in the beginning
            inputsMemory.put(name, false);
        }
    }

    static class BroadcastModule implements Module {
        String name;

        List<String> outputs;

        public BroadcastModule(String name, List<String> outputs) {
            this.name = name;
            this.outputs = outputs;
        }

        @Override
        public void receive(String inputName, boolean pulseHigh) {
            outputs.forEach(o -> {
                Module outModule = modules.get(o);
                ModulePulse mp = new ModulePulse(outModule, pulseHigh, name);
                processingQueue.add(mp);
            });
        }

        @Override
        public void send() {
            //nothing is done here: Broadcast sends as soon as it receives.
        }

        @Override
        public List<String> getOutputs() {
            return outputs;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    static class ModulePulse {
        Module module;
        boolean pulse;

        String inputName;

        public ModulePulse(Module module, boolean pulse, String inputName) {
            this.module = module;
            this.pulse = pulse;
            this.inputName = inputName;
        }
    }
}
