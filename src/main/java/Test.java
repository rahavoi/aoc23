import java.math.BigDecimal;

public class Test {
    public static void main(String[] args){
        //16897
        //21883
        //13019
        //19667
        //20221
        //16343
        BigDecimal test = new BigDecimal(16897);
        test = test.multiply(new BigDecimal(21883));
        test = test.multiply(new BigDecimal(13019));
        test = test.multiply(new BigDecimal(19667));
        test = test.multiply(new BigDecimal(20221));
        test = test.multiply(new BigDecimal(16343));


        System.out.println(test);
    }
}
