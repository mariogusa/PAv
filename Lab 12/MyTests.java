import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Define our own Test annotation
@Retention(RetentionPolicy.RUNTIME)
@interface Test {}

public class MyTests {
    @Test
    public static void testSuccess() {
        System.out.println("Test passed successfully!");
    }

    public static void helperMethod() {
        System.out.println("Helper method should not run");
    }

    @Test
    public static void testFailure() {
        throw new RuntimeException("Test failed intentionally");
    }
}