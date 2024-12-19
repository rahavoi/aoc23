import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day2024_19 {
    static Map<String, Long> memo = new HashMap<>();
    public static void main(String[] args) throws Exception {
        List<String> available = Arrays.stream("rbrr, gbgrgrg, rgurwgb, grb, ggg, grbugw, brwggrw, rbbrub, grguuu, uuub, uw, urug, bru, wwrrb, rbwbb, urw, uwuw, ubg, uugug, wbub, guubr, rru, gwb, wggrg, gubbwww, wrr, wbw, grug, brrr, rgw, urg, gugg, rugwuwb, rwu, rgu, bbu, ubb, gr, wwwggw, ub, uggrb, bbwrru, wuwrww, gbrbwru, uubu, bub, uguub, b, uwgu, brggrr, rruuggrr, buuu, brbgw, wbugb, rbwru, wrgu, bbg, uwbg, ruwg, bgugub, urwrrb, bggugw, bg, rgrrb, guwwu, gbbr, rwb, urrgu, brw, gbrwg, gbgwwrb, grgb, ggug, ugw, wbwwbug, wrbru, uwww, wwuw, wubg, burubw, gwr, wwubu, ubggrr, uru, www, wwr, uwbw, ww, rbbrwbr, rrwgru, gwuwgug, wubwww, gwbbwwb, bwu, ubr, rrguwr, bu, g, wrwbr, ggwb, wbr, uubwbrrg, gubrurwu, ugbg, wrurwbwr, buu, wuwbgrbw, ruw, wwgrw, bbbr, wbg, grrrrbu, wguuggwb, wrrwr, rbrru, bur, wurgwbgw, wgr, burggw, grugug, uugbrg, bgurgrr, urbw, gurur, wggb, rrub, uwgwg, grrug, uuu, gurg, ugugg, rurb, bbb, rrggwwb, brwbb, rrbub, ugb, wwbgg, gbguw, wgwgbb, wgw, brrw, ugbwrg, wgub, uub, bwgrugwb, guwgg, gurw, rrruwr, wgg, bgw, wrub, bubrw, uwrbb, uubugbwg, bwr, ugg, gwrbg, gbg, rbuwr, gububbu, bbw, wggw, rwrww, bbggbr, urgburg, rwbgr, rgr, wbugu, uug, uwg, ubgruurb, r, uu, rrw, uuwgu, gbu, urb, ugwg, bgrgrrg, rub, wubgwbg, brgubr, uuuburb, rbrwgr, wgwur, wbu, buurrgw, uwu, wbgb, rgru, wgggg, gubbr, uubwwg, uurbbggg, bgrwg, wggburr, ubwrg, wu, urbu, wbwrur, urgu, bggub, gbuwbb, uruu, gwwggwu, wugubrbr, bgrb, rgrbrb, guw, brr, rbguu, bgb, rug, gbur, gbb, rbrwbbb, ruuw, wrug, bbrwbgu, wwuwwbr, bgugur, rgg, brbw, wru, burrg, gu, ggwuww, wuw, uurg, wuwwbrwr, bbgbr, wb, ug, rrru, gwbg, ubwrug, rgur, rwg, ggrr, rgbub, uwubub, rwugg, wgbw, wburr, rgbu, ugr, ggwwb, gg, bbrbwgrw, gwu, brgwrwb, bguw, bbwwurr, ruu, ggbbu, ggr, bgwbgr, gru, gguuw, rbg, urbb, grw, wrwubbg, wbuu, ubwuw, gug, rbbr, wwb, buw, gw, buuw, wurgbuu, u, uuw, rwbw, wbbguw, bww, urguuwr, buwb, rugguggb, wwub, wrgubb, wrbr, gubbrw, guugr, ubbu, rwwbrb, bw, uwur, uugugw, bb, grr, wwuu, grrggg, rur, burugrr, ggb, rr, grg, brgw, wbwgu, uruwb, wuu, bwwgwrb, urgg, rrwuru, wurbur, uwb, bwrb, wur, ruwbwug, gb, bbr, uuwguu, ggwgwggw, bgurbb, wrw, rrbu, ubu, wbrgr, ruug, rbrg, rbr, bubwb, wbwgbwb, urugww, wrgbw, wgb, grub, gwrbuu, rbb, rrb, wbb, gguw, ggurrwg, bgwg, uuurw, wrwuww, brg, gbuub, uuur, rwrwu, wgu, rrrw, rbw, bwrbr, gwrgwu, ubbruu, rurrbrg, wwbwb, ubrg, wbbb, brb, rgwgr, gwg, brrgb, rwub, guwburb, buwgbru, ugbug, bwb, wgbwuu, wrbg, rrwugb, guuub, burw, brgwbwb, ubrub, ggbb, wggww, buwwwwuw, gur, ugu, guu, gwrbbrug, wbggu, gbgbuu, gbr, ggub, bbwg, ugwuur, grgr, ubguu, urgw, ru, bgg, wwu, buggg, gbuu, uuwu, rrww, ugrw, urr, rbu, bbugb, rgb, uwrbbu, wrwguu, rw, wwwu, gbbbuguw, wbrwwg, gbbrwrwr, wugw, rrbb, wurg, wgbubg, wug, wwwwg, br, rwbg, ur, rrrggg, gbw, ggu, bwrwb, rrr, rguuww, wuwbgww, bug, wgwru, wgbggw, uww, bwg, ggw, ubw, rrg, wg, uur, uwr, wrb, bwurbg, ugur, rb, gww, ubuu, burr, ggwg, rwr, gwuww, bgbwu, gub, bgu, wwgr, wwgb, bbrug".split(", ")).collect(Collectors.toList());

        List<String> towels = Files.readAllLines(Paths.get("resources/Day2024_19.txt"));

        int count = towels.stream().map(t -> isPossible(t, available)).mapToInt(p -> p ? 1 : 0).sum();
        System.out.println(count);

        long countPossible = towels.stream().mapToLong(t -> countPossible(t, available)).sum();
        System.out.println(countPossible);
    }

    private static Long countPossible(String towel, List<String> patterns) {
        if(memo.containsKey(towel)){
            return memo.get(towel);
        }

        long count = patterns.stream().mapToLong(p -> {
            Long result = 0L;
            if(towel.equals(p)){
                result++;
            } else if(towel.startsWith(p)){
                result+= countPossible(towel.substring(p.length()), patterns);
            }

            return result;
        }).sum();

        memo.put(towel, count);
        return count;
    }

    private static boolean isPossible(String towel, List<String> available) {
        PriorityQueue<String> q = new PriorityQueue<>(Comparator.comparingInt(String::length));
        q.add(towel);

        while(!q.isEmpty()){
            String cur = q.poll();

            if(cur.length() == 0){
                return true;
            }

            List<String> match = available.stream().filter(a -> cur.startsWith(a)).collect(Collectors.toList());

            match.forEach(m -> q.add(cur.substring(m.length())));
        }

        return false;
    }
}
