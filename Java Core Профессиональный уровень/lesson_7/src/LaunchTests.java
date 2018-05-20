import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.LinkedList;

class LaunchTests {

    static void start(Class obClass) {
        Method[] classMethods = obClass.getDeclaredMethods();
        LinkedList<Method> methodList = sortedMethod(classMethods);
        for (Method classMethod : classMethods) {
            if (classMethod.isAnnotationPresent(BeforeSuite.class)) {
                if (methodList.getFirst().isAnnotationPresent(BeforeSuite.class))
                    throw new RuntimeException("методов с аннотацией @BeforeSuite более одного");
                methodList.addFirst(classMethod);
            }
            if (classMethod.isAnnotationPresent(AfterSuite.class)) {
                if (methodList.getLast().isAnnotationPresent(AfterSuite.class))
                    throw new RuntimeException("методов с аннотацией @AfterSuite более одного");
                methodList.addLast(classMethod);
            }
        }
        try {
            Object object = obClass.newInstance();
            for (Method method : methodList) {
                if (Modifier.isPrivate(method.getModifiers())) method.setAccessible(true);
                method.invoke(object, (Object[]) method.getParameterTypes());
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static LinkedList<Method> sortedMethod(Method[] methods){
        LinkedList<Method> methodList = new LinkedList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class) && method.getAnnotation(Test.class).priority() > 0){
                methodList.add(method);
            }
        }
        methodList.sort(new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getAnnotation(Test.class).priority() - o2.getAnnotation(Test.class).priority();
            }
        });
        return methodList;
    }

}