import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;

public class ClassAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ClassAnalyzer <fully-qualified-class-name>");
            System.err.println("Example: java ClassAnalyzer MyTests");
            return;
        }

        String className = args[0];
        try {
            Class<?> clazz = Class.forName(className);
            printClassPrototype(clazz);
            runTestMethods(clazz);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Class not found - " + className);
            System.err.println("Classpath: " + System.getProperty("java.class.path"));
            System.err.println("Hint: Run from directory containing your class file");
        }
    }

    private static void printClassPrototype(Class<?> clazz) {
        System.out.println("Class Prototype:");
        StringBuilder classSignature = new StringBuilder();
        String mods = Modifier.toString(clazz.getModifiers());
        if (!mods.isEmpty()) {
            classSignature.append(mods).append(" ");
        }
        classSignature.append("class ").append(clazz.getSimpleName());

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            classSignature.append(" extends ").append(superclass.getSimpleName());
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            classSignature.append(" implements ");
            for (int i = 0; i < interfaces.length; i++) {
                if (i > 0) classSignature.append(", ");
                classSignature.append(interfaces[i].getSimpleName());
            }
        }
        System.out.println(classSignature + " {");

        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("  " + getMethodSignature(method));
        }
        System.out.println("}");
    }

    private static String getMethodSignature(Method method) {
        StringBuilder signature = new StringBuilder();
        
        // annotations
        for (Annotation ann : method.getAnnotations()) {
            signature.append('@').append(ann.annotationType().getSimpleName()).append(' ');
        }
        
        String mods = Modifier.toString(method.getModifiers());
        if (!mods.isEmpty()) {
            signature.append(mods).append(" ");
        }
        signature.append(method.getReturnType().getSimpleName()).append(" ");
        signature.append(method.getName()).append("(");

        Class<?>[] params = method.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) signature.append(", ");
            signature.append(params[i].getSimpleName());
        }
        signature.append(");");
        return signature.toString();
    }

    private static void runTestMethods(Class<?> clazz) {
        System.out.println("\nRunning Tests:");
        int testCount = 0;
        int passed = 0;
        int failed = 0;

        for (Method method : clazz.getDeclaredMethods()) {
            if (isTestMethod(method)) {
                testCount++;
                String methodName = method.getName();
                try {
                    method.invoke(null);
                    System.out.println("[" + methodName + "] PASSED");
                    passed++;
                } catch (Exception e) {
                    Throwable cause = e.getCause();
                    System.out.println("[" + methodName + "] FAILED: " + 
                                       (cause != null ? cause : e));
                    failed++;
                }
            }
        }

        System.out.println("\nTest Results: " + testCount + " tests run, " +
                           passed + " passed, " + failed + " failed.");
    }

    private static boolean isTestMethod(Method method) {
        int mods = method.getModifiers();
        return Modifier.isStatic(mods) && 
               method.getParameterCount() == 0 && 
               hasTestAnnotation(method);
    }

    private static boolean hasTestAnnotation(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("Test")) {
                return true;
            }
        }
        return false;
    }
}