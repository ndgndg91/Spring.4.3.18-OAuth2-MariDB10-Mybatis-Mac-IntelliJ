import com.ndgndg91.common.CommonUtil;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class CommonUtilTest {

    @Test
    public void 오브젝트_인트_변환(){
        assertThat(CommonUtil.IntFromAssumedToBeIntegerObject("23423")).isEqualTo(0);
    }

    @Test
    public void 옵셔널_테스트(){
        Optional<String> nullable = Optional.empty();
        assertThat(nullable.equals(nullable.isPresent())).isEqualTo(false);
    }
}
