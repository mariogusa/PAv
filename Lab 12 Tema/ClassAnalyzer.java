import java.lang.reflect.*;
import java.util.*;

public class ClassAnalyzer {
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java ClassAnalyzer <className>");
            return;
        }
        
        try {
            Class<?> clazz = Class.forName(args[0]);
            new ClassAnalyzer().processClass(clazz);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + args[0]);
        }
    }

    public void processClass(Class<?> clazz) {
        System.out.println("Analyzing class: " + clazz.getName());
        printClassPrototype(clazz);
        runTests(clazz);
        printStatistics();
    }

    private void printClassPrototype(Class<?> clazz) {
        System.out.println("\nClass Prototype:");
        System.out.println(Modifier.toString(clazz.getModifiers()) + " class " + clazz.getSimpleName() + " {");
        
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            System.out.println("  " + Modifier.toString(c.getModifiers()) + " " + 
                             clazz.getSimpleName() + "(" + 
                             getParameterTypes(c.getParameterTypes()) + ");");
        }
        
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println("  " + Modifier.toString(m.getModifiers()) + " " + 
                             m.getReturnType().getSimpleName() + " " + 
                             m.getName() + "(" + 
                             getParameterTypes(m.getParameterTypes()) + ");");
        }
        
        System.out.println("}");
    }

    private String getParameterTypes(Class<?>[] types) {
        StringJoiner sj = new StringJoiner(", ");
        for (Class<?> type : types) {
            sj.add(type.getSimpleName());
        }
        return sj.toString();
    }

    private void runTests(Class<?> clazz) {
        Object instance = null;
        
        if (hasAnnotatedMethods(clazz)) {
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.out.println("Could not create instance: " + e.getMessage());
            }
        }
        
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                totalTests++;
                try {
                    System.out.println("\nRunning test: " + method.getName());
                    Object[] params = generateMockArguments(method.getParameterTypes());
                    
                    if (Modifier.isStatic(method.getModifiers())) {
                        method.invoke(null, params);
                    } else {
                        method.invoke(instance, params);
                    }
                    
                    passedTests++;
                    System.out.println("Test passed");
                } catch (Exception e) {
                    failedTests++;
                    System.out.println("Test failed: " + e.getCause());
                }
            }
        }
    }

    private boolean hasAnnotatedMethods(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class) && 
                !Modifier.isStatic(method.getModifiers())) {
                return true;
            }
        }
        return false;
    }

    private Object[] generateMockArguments(Class<?>[] paramTypes) {
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = getDefaultValue(paramTypes[i]);
        }
        return args;
    }

    private Object getDefaultValue(Class<?> type) {
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == boolean.class) return false;
        if (type == double.class) return 0.0;
        if (type == float.class) return 0.0f;
        if (type == byte.class) return (byte)0;
        if (type == short.class) return (short)0;
        if (type == char.class) return '\0';
        if (type == String.class) return "test";
        return null;
    }

    private void printStatistics() {
        System.out.println("\nOverall Statistics:");
        System.out.println("Test methods executed: " + totalTests);
        System.out.println("Passed tests: " + passedTests);
        System.out.println("Failed tests: " + failedTests);
    }
}