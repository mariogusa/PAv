public class ExampleTests {
    @Test
    public static void staticTest() {
        System.out.println("Static test running");
    }
    
    @Test
    public void instanceTest() {
        System.out.println("Instance test running");
    }
    
    @Test
    public void testWithParams(int num, String text) {
        System.out.println("Test with parameters: " + num + ", " + text);
    }
    
    public void helperMethod() {}
}