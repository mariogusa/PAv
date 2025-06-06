import java.lang.reflect.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.lang.annotation.Annotation;

public class ClassPrototyper {
    public static void printClassPrototype(Class<?> clazz) {
        if (clazz.isAnnotation()) return;
        
        System.out.println("\n" + getClassSignature(clazz) + " {");
        printMembers(clazz);
        System.out.println("}");
    }

    private static String getClassSignature(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(Modifier.toString(clazz.getModifiers()));
        if (sb.length() > 0) sb.append(" ");
        sb.append(clazz.isInterface() ? "interface " : "class ").append(clazz.getSimpleName());
        
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && superclass != Object.class) {
            sb.append(" extends ").append(superclass.getSimpleName());
        }
        
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            sb.append(clazz.isInterface() ? " extends " : " implements ")
              .append(Arrays.stream(interfaces)
                      .map(Class::getSimpleName)
                      .collect(Collectors.joining(", ")));
        }
        
        return sb.toString();
    }

    private static void printMembers(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("  " + getFieldSignature(field));
        }
        
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            System.out.println("  " + getConstructorSignature(ctor));
        }
        
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("  " + getMethodSignature(method));
        }
    }

    private static String getFieldSignature(Field field) {
        return String.format("%s %s %s;",
                Modifier.toString(field.getModifiers()),
                field.getType().getSimpleName(),
                field.getName());
    }

    private static String getConstructorSignature(Constructor<?> ctor) {
        return String.format("%s %s(%s);",
                Modifier.toString(ctor.getModifiers()),
                ctor.getDeclaringClass().getSimpleName(),
                getParameterTypes(ctor.getParameterTypes()));
    }

    private static String getMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        
        for (Annotation ann : method.getAnnotations()) {
            sb.append('@').append(ann.annotationType().getSimpleName()).append(' ');
        }
        
        sb.append(Modifier.toString(method.getModifiers()));
        if (sb.length() > 0) sb.append(" ");
        sb.append(method.getReturnType().getSimpleName())
          .append(" ")
          .append(method.getName())
          .append("(")
          .append(getParameterTypes(method.getParameterTypes()))
          .append(");");
        
        return sb.toString();
    }

    private static String getParameterTypes(Class<?>[] params) {
        return Arrays.stream(params)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
    }
}