import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

import static com.rahavoi.constants.Day7Constants.*;

public class Day5 {
    public static void main(String[] args) {
        List<Rule> seedToSoilRules = getRules(SEED_TO_SOIL);
        List<Rule> soilToFertilizerRules = getRules(SOIL_FO_FERTILIZER);
        List<Rule> fertilizerToWaterRules = getRules(FERTILIZER_TO_WATER);
        List<Rule> waterToLightRules = getRules(WATER_TO_LIGHT);
        List<Rule> lightToTempRules = getRules(LIGHT_TO_TEMP);
        List<Rule> tempToHumidityRules = getRules(TEMP_TO_HUMIDITY);
        List<Rule> humidityToLocationRules = getRules(HUMIDITY_TO_LOCATION);

        List<LongStream> streams = new ArrayList<>();

        for(String s : SEEDS.split(";")){
            LongStream stream = getStream(s);
            streams.add(stream);
        }

        LongStream mainStream = streams.get(0);

        for(int i = 1; i < streams.size(); i++){
            mainStream = LongStream.concat(mainStream, streams.get(i));
        }

        AtomicLong minLocation = new AtomicLong(Long.MAX_VALUE);
        AtomicLong iterations = new AtomicLong(0);

        mainStream.parallel().forEach(seed -> {
            Long soil = getDestinationFromRules(seed, seedToSoilRules);
            Long fertilizer = getDestinationFromRules(soil, soilToFertilizerRules);
            Long water = getDestinationFromRules(fertilizer, fertilizerToWaterRules);
            Long light = getDestinationFromRules(water, waterToLightRules);
            Long temp = getDestinationFromRules(light, lightToTempRules);
            Long humidity = getDestinationFromRules(temp, tempToHumidityRules);
            Long location = getDestinationFromRules(humidity, humidityToLocationRules);
            minLocation.set(Math.min(minLocation.get(), location));

            if(iterations.addAndGet(1) % 10000000 == 0){
                System.out.println("After " +iterations.get() + " iterations Local minimum: " + minLocation +  "with seed : " + seed);
            }
        });

        System.out.println("Local minimum: " + minLocation);
    }

    private static LongStream getStream(String s) {
        String[] parts = s.trim().split(" ");
        Long start = Long.parseLong(parts[0]);
        Long range = Long.parseLong(parts[1]);

        return LongStream.range(start, start + range - 1);
    }

    private static Long getDestinationFromRules(long source, List<Rule> rules){
        for(Rule rule : rules){
            if(rule.sourceRange <= source && rule.sourceRange + rule.rangeLength >= source ){
                long offset = source - rule.sourceRange;
                return rule.destRange + offset;
            }
        }

        return source;
    }

    static class Rule {
        long sourceRange;
        long destRange;
        long rangeLength;
    }

    private static List<Rule> getRules(String data){
        return Arrays.stream(data.split("\n")).map(s -> {
            String[] parts = s.split(" ");
            long destRange = Long.parseLong(parts[0]);
            long sourceRange = Long.parseLong(parts[1]);
            long rangeLength = Long.parseLong(parts[2]);

            Rule rule = new Rule();
            rule.sourceRange = sourceRange;
            rule.destRange = destRange;
            rule.rangeLength = rangeLength;

            return rule;
        }).toList();
    }
}
