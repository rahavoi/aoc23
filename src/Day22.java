import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day22 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day22.txt"));
        List<Brick> bricks = parse(lines);

        Collections.shuffle(bricks);

        applyGravity(bricks);

        long okToDisintegrate = bricks.stream().map(b -> getDirectlyImpactedBricksIfRemoved(b, bricks)).filter(Set::isEmpty).count();
        Map<Brick, List<Brick>> dependencies = buildDependencies(bricks);
        AtomicInteger impact = new AtomicInteger();

        bricks.forEach(b -> {
            Map<Brick, List<Brick>> deps = new HashMap<>();
            dependencies.forEach((key, value) -> deps.put(key, new ArrayList<>(value)));
            impact.addAndGet(getAllImpactedIfRemoved(b, deps).size());
        });

        System.out.println(okToDisintegrate);
        System.out.println(impact.get());
    }

    static void applyGravity(List<Brick> bricks){
        bricks.sort(Comparator.comparingInt(b -> b.sideA.getZ()));
        Map<XYPoint, Integer> layers = new HashMap<>();

        bricks.forEach(b -> {
            Set<XYPoint> cubes = toCubes(b);

            Integer layer = null;

            OptionalInt bottom = cubes.stream().map(layers::get)
                    .filter(Objects::nonNull)
                    .mapToInt(i -> i)
                    .max();

            if(bottom.isPresent()){
                layer = bottom.getAsInt() + 1;
            }

            if(layer == null){
                //The brick has fallen on an unoccupied space
                layer = 1;
            }

            int diff = b.sideB.z - b.sideA.z;

            b.sideA.z = layer;
            b.sideB.z = layer + diff;

            //Updating layers map
            cubes.forEach(c -> layers.put(c, b.sideB.z));
        });
    }

    static Map<Brick, List<Brick>> buildDependencies(List<Brick> bricks){
        Map<Brick, List<Brick>> dependencies = new HashMap<>();

        bricks.forEach(b -> {
            Set<XYPoint> curBrickCubes = toCubes(b);
            List<Brick> supporters = bricks.stream()
                    .filter(br -> b != br && br.sideB.z == b.sideA.z - 1)
                    .filter(br -> !Collections.disjoint(curBrickCubes, toCubes(br)))
                    .toList();

            dependencies.put(b, new ArrayList<>(supporters));
        });

        return dependencies;
    }

    static Set<Brick> getAllImpactedIfRemoved(Brick b, Map<Brick, List<Brick>> dependencies){
        Set<Brick> impact = new HashSet<>();

        Queue<Brick> q = new LinkedList<>();
        q.add(b);

        while (!q.isEmpty()) {
            Brick cur = q.poll();
            dependencies.forEach((br, supporters) -> {
                if (supporters.contains(cur)) {
                    supporters.remove(cur);
                    if (supporters.isEmpty()) {
                        impact.add(br);
                        q.add(br);
                    }
                }
            });
        }

        return impact;
    }

    static Set<Brick> getDirectlyImpactedBricksIfRemoved(Brick b, List<Brick> bricks){
        Set<Brick> result = new HashSet<>();
        Set<XYPoint> curBrickCubes = toCubes(b);

        List<Brick> bricksSameLevel = bricks.stream().filter(br -> b != br && br.sideB.z == b.sideB.z).toList();
        List<Brick> bricksSupportedByCurrentBrick = bricks.stream().filter(br -> b != br && br.sideA.z == b.sideB.z + 1)
                .filter(brickAbove -> !Collections.disjoint(toCubes(brickAbove), curBrickCubes))
                .toList();

        if(bricksSupportedByCurrentBrick.isEmpty()){
            return result;
        }

        for(Brick brickAbove : bricksSupportedByCurrentBrick){
            Set<XYPoint> brickAboveCubes = toCubes(brickAbove);

            Optional<Brick>  supportedByOther = bricksSameLevel.stream()
                    .filter(bsl -> !Collections.disjoint(toCubes(bsl), brickAboveCubes))
                    .findFirst();

            if(supportedByOther.isEmpty()){
                result.add(brickAbove);
            }
        }

        return result;
    }

    static Set<XYPoint> toCubes(Brick b){
        Set<XYPoint> cubes = new HashSet<>();
        int minX = Math.min(b.sideA.x, b.sideB.x);
        int maxX = Math.max(b.sideA.x, b.sideB.x);

        int minY = Math.min(b.sideA.y, b.sideB.y);
        int maxY = Math.max(b.sideA.y, b.sideB.y);

        for(int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                cubes.add(new XYPoint(x, y));
            }
        }

        return cubes;
    }

    static class XYPoint{
        int x;
        int y;

        public XYPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            XYPoint xyPoint = (XYPoint) o;
            return x == xyPoint.x && y == xyPoint.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static List<Brick> parse(List<String> data){
        return new ArrayList<>(data.stream().map(l -> {
            String[] parts = l.split("~");
            String[] partA = parts[0].split(",");
            String[] partB = parts[1].split(",");

            Point sideA = new Point(
                    Integer.parseInt(partA[0]),
                    Integer.parseInt(partA[1]),
                    Integer.parseInt(partA[2])
            );

            Point sideB = new Point(
                    Integer.parseInt(partB[0]),
                    Integer.parseInt(partB[1]),
                    Integer.parseInt(partB[2])
            );

            return new Brick(sideA, sideB);
        }).toList());
    }

    static class Brick {
        Point sideA;
        Point sideB;

        public Brick(Point sideA, Point sideB) {
            this.sideA = sideA;
            this.sideB = sideB;
        }
    }

    static class Point {
        int x;
        int y;
        int z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getZ() {
            return z;
        }
    }
}
