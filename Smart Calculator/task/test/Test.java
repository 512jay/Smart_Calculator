import calculator.MainKt;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.PredefinedIOTestCase;

import java.util.Arrays;
import java.util.List;

public class Test extends BaseStageTest {

    public Test() {
        super(MainKt.class);
    }

    @Override
    public List<PredefinedIOTestCase> generate() {
        return Arrays.asList(
            /* Check expressions with variables */
            new PredefinedIOTestCase(
                "a = 4\nb = 5\nc = 6\na*2+b*3+c*(2+3)\n/exit",
                "53\nBye!"
            ),
            /* Check reassignment */
            new PredefinedIOTestCase(
                "a = 1\na = 2\na = 3\na\n/exit",
                "3\nBye!"
            ),
            /* Check handling unknown commands */
            new PredefinedIOTestCase(
                "/command\n/exit",
                "Unknown command\nBye!"
            ),
            /* Check all operations */
            new PredefinedIOTestCase(
                "3 + 8 * ((4 + 3) * 2 + 1) - 6 / (2 + 1)\n/exit",
                "121\nBye!"
            ),
            /* Check with an invalid expressions */
            new PredefinedIOTestCase(
                "8 * 3 + 12 * (4 - 2)\n4 * (2 + 3\n4 + 3)\n/exit",
                "48\nInvalid expression\nInvalid expression\nBye!"
            ),
            /* Check expressions with large numbers */
            new PredefinedIOTestCase(
                "112234567890 + 112234567890 * (10000000999 - 999)\n/exit",
                "1122345679012234567890\nBye!"
            ),
            /* Check expressions with large numbers and variables */
            new PredefinedIOTestCase(
                "a = 800000000000000000000000\nb = 100000000000000000000000\na + b\n/exit",
                "900000000000000000000000\nBye!"
            ),
            /* Check reassignment with large numbers */
            new PredefinedIOTestCase(
                "n = 3\nn = 500000000000\nn = 20000000000000000000000\nn\n/exit",
                "20000000000000000000000\nBye!"
            )
        );
    }
}
