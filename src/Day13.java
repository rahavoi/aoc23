import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("/Users/irahavoi/IdeaProjects/aoc2023/resources/Day13.txt"));

        long result = 0;

        List<String> currentImg = new ArrayList<>();
        int countLines = 0;
        outer:
        for(String l : lines){
            if(l.trim().isEmpty()){
                char[][] img = new char[currentImg.size()][currentImg.get(0).length()];

                for(int i = 0; i < img.length; i++){
                    for(int j = 0; j < img[0].length; j++){
                        img[i][j] = currentImg.get(i).charAt(j);
                    }
                }

                search_reflection_in_lines:
                for(int i = 1; i < img.length; i++){
                    List<Character> line = currentImg.get(i).chars().mapToObj(c -> (char) c).toList();
                    List<Character> prevLine = currentImg.get(i - 1).chars().mapToObj(c -> (char) c).toList();

                    int diffs = countDifferences(line, prevLine);

                    if(diffs <= 1){
                        int leftIdx = i - 2;
                        int rightIdx = i + 1;
                        while(leftIdx >= 0 && rightIdx < img.length){
                            List<Character> left = currentImg.get(leftIdx).chars().mapToObj(c -> (char) c).toList();
                            List<Character> right = currentImg.get(rightIdx).chars().mapToObj(c -> (char) c).toList();

                            diffs += countDifferences(left, right);
                            if(diffs <= 1){
                                leftIdx--;
                                rightIdx++;
                            } else {
                                continue search_reflection_in_lines;
                            }
                        }

                        //Reached end of img. Line of reflection is found!
                        if(diffs == 1){
                            result+= 100 * i;
                            currentImg = new ArrayList<>();
                            continue outer;
                        }
                    }
                }

                //System.out.println("Smudged Line of reflection is not in rows. Search columns instead");

                search_reflection_in_rows:
                for(int i = 1; i < img[0].length; i++){
                    List<Character> row = new ArrayList<>();
                    List<Character> prevRow = new ArrayList<>();

                    for(char[] col : img){
                        row.add(col[i]);
                        prevRow.add(col[i - 1]);
                    }

                    int diffs = countDifferences(row, prevRow);

                    if(diffs <= 1){
                        int leftIdx = i - 2;
                        int rightIdx = i + 1;

                        while(leftIdx >= 0 && rightIdx < img[0].length){
                            List<Character> left = new ArrayList<>();
                            List<Character> right = new ArrayList<>();

                            for(char[] c : img){
                                left.add(c[leftIdx]);
                                right.add(c[rightIdx]);
                            }

                            diffs += countDifferences(left, right);

                            if(diffs <= 1){
                                leftIdx--;
                                rightIdx++;
                            } else {
                                continue search_reflection_in_rows;
                            }
                        }

                        //Reached end of img. Line of reflection is found!
                        if(diffs == 1){
                            result += i;
                            currentImg = new ArrayList<>();
                            continue outer;
                        }
                    }
                }

                currentImg = new ArrayList<>();
            } else {
                currentImg.add(l);
            }

        }

        System.out.println(result);
    }

    private static int countDifferences(List<Character> left, List<Character> right){
        int diffs = 0;
        for(int i = 0; i < left.size(); i++){
            if(left.get(i) != right.get(i)){
                diffs++;
            }
        }

        return diffs;
    }
}
