import java.util.*;
import java.util.stream.Collectors;

public class Day2024_11 {
    public static void main(String[] args) throws Exception {

       Map<Long, Long> memory =
                Arrays.stream("6563348 67 395 0 6 4425 89567 739318".split(" ")).map(Long::parseLong)
                        .collect(Collectors.toMap(s -> s, s -> 1L ));

        for(int i = 0; i < 75; i ++){
            Map<Long, Long> newCache = new HashMap<>();

            memory.entrySet().stream().forEach(e -> {
                Long stone = e.getKey();
                Long count = e.getValue();
                BlinkResult br = blink(stone);

                Long cachedCount1 = newCache.getOrDefault(br.v1, 0L);
                newCache.put(br.v1, cachedCount1 + count);
                br.v2.ifPresent(v2 -> newCache.put(v2, newCache.getOrDefault(v2, 0L) + count));
            });

            memory = newCache;

            //p1:
            if(i + 1 == 25){
                System.out.println(memory.values().stream().mapToLong(v -> v).sum());
            }

            //p2:
            if(i + 1 == 75){
                System.out.println(memory.values().stream().mapToLong(v -> v).sum());
            }
        }
    }

    private static BlinkResult blink(Long v){
        String sv = v.toString();
        if(v == 0){
            return new BlinkResult(1L, null);
        } else if(sv.length() % 2 == 0){
            int mid = sv.length() / 2;
            Long l1 = Long.parseLong(sv.substring(0, mid));
            Long l2 = Long.parseLong(sv.substring(mid));
            return new BlinkResult(l1, Optional.of(l2));
        } else {
            return new BlinkResult(v * 2024, Optional.empty());
        }
    }

    public record BlinkResult(Long v1, Optional<Long> v2){ }
}
