import java.lang.reflect.*;
import java.util.*;

public class TestRunner {
    public static void runTests(Class<?> clazz, TestStatistics stats) {
        System.out.println("\nRunning tests for: " + clazz.getSimpleName());
        
        Object instance = null;
        try {
            if (hasInstanceTests(clazz)) {
                instance = createInstance(clazz);
            }
        } catch (Exception e) {
            System.err.println("Failed to create instance: " + e.getMessage());
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (isTestMethod(method)) {
                stats.testMethods++;
                runTest(method, instance, stats);
            }
        }
    }

    private static boolean hasInstanceTests(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .anyMatch(m -> isTestMethod(m) && !Modifier.isStatic(m.getModifiers()));
    }

    private static Object createInstance(Class<?> clazz) throws Exception {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            System.err.println("No default constructor available, using first constructor");
            Constructor<?> ctor = clazz.getDeclaredConstructors()[0];
            ctor.setAccessible(true);
            return ctor.newInstance(generateMockArguments(ctor.getParameterTypes()));
        }
    }

    private static boolean isTestMethod(Method method) {
        return method.isAnnotationPresent(Test.class) &&
               Modifier.isPublic(method.getModifiers());
    }

    private static void runTest(Method method, Object instance, TestStatistics stats) {
        String testName = method.getName();
        try {
            Object[] args = generateMockArguments(method.getParameterTypes());
            System.out.printf("Running test: %s(%s)%n", testName, Arrays.toString(args));
            
            if (Modifier.isStatic(method.getModifiers())) {
                method.invoke(null, args);
            } else {
                method.invoke(instance, args);
            }
            
            System.out.println("PASSED: " + testName);
            stats.passedTests++;
        } catch (Throwable t) {
            System.out.println("FAILED: " + testName + " - " + getRootCause(t));
            stats.failedTests++;
        }
    }

    private static Object[] generateMockArguments(Class<?>[] paramTypes) {
        return Arrays.stream(paramTypes)
                .map(TestRunner::generateMockValue)
                .toArray();
    }

    private static Object generateMockValue(Class<?> type) {
        if (type == int.class || type == Integer.class) return 42;
        if (type == String.class) return "test";
        if (type == boolean.class || type == Boolean.class) return true;
        if (type == long.class || type == Long.class) return 123L;
        if (type == double.class || type == Double.class) return 3.14;
        if (type == float.class || type == Float.class) return 1.23f;
        if (type == char.class || type == Character.class) return 'x';
        if (type == byte.class || type == Byte.class) return (byte)1;
        if (type == short.class || type == Short.class) return (short)2;
        return null;
    }

    private static String getRootCause(Throwable t) {
        Throwable root = t;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root.getMessage() != null ? root.getMessage() : root.toString();
    }
}