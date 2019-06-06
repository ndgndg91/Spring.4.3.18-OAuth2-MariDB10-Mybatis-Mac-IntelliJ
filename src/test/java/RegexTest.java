import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    private static String testCase1 = "ndgndg91@gmail.com";
    private static String testCase2 = "aa$bb@cc!dd*";
    private static Pattern notStringPattern = Pattern.compile("([^a-zA-Z0-9])");

    @Test
    public void regTest(){
        Matcher notStringMatcher = notStringPattern.matcher(testCase1);
        System.out.println("1 : " + testCase1);
        testCase1 = notStringMatcher.replaceAll("");
        System.out.println("2 : " + testCase1);
        if (notStringMatcher.find())
            System.out.println(notStringMatcher.group());

        System.out.println("완료");
    }

    @Test
    public void regTest2(){
        Matcher notStringMatcher = notStringPattern.matcher(testCase2);
        System.out.println("1 : " + testCase2);
        testCase2 = notStringMatcher.replaceAll("");
        System.out.println("2 : " + testCase2);
        if (notStringMatcher.find())
            System.out.println(notStringMatcher.group());
        System.out.println("완료");
    }
}
