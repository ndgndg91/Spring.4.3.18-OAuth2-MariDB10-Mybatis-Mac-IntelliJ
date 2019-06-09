import com.ndgndg91.common.MemberUtils;
import org.junit.Test;

public class MemberUtilsTest {

    @Test
    public void ageTest(){
        System.out.println(MemberUtils.getMemberKoreanAge("1991-11-07"));
        System.out.println(MemberUtils.getMemberKoreanAge("1989-10-03"));
    }
}
