package week5;
/*
Upload a couple of jar files to your Repl, one of them must be json-org.java, the jar file of the course project. 
Note that you should only upload the jars, not source files.

Write a program called JarClasses.java (inside Week5 folder) that takes a jar file as command line argument and prints out a listing of all the classes inside, 
as well as the number of declared public, private, protected, and static methods for each, 
and the number of declared fields for each. The listing should be sorted alphabetically on the name of the classes. Your program should use reflection to get the required information about the methods and fields of each class.

For example, the command line and output for json-java.jar is:
$ java JarClasses json-java.jar
*/
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClasses {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JarClasses <JAR file>");
            return;
        }
        
        String jarFileName = args[0];
        try {
            JarFile jarFile = new JarFile(new File(jarFileName));
            Enumeration<JarEntry> entries = jarFile.entries();
            ArrayList<String> classNames = new ArrayList<>();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class") && !entryName.contains("module-info")) {
                    String className = entryName.replace('/', '.').replace(".class", "");
                    classNames.add(className);
                }
            }            
            
            Collections.sort(classNames);
            for (String className : classNames) {
                try {
                    Class<?> cls = Class.forName(className, false, JarClasses.class.getClassLoader());
                    printClassDetails(cls);
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                }                
            }
            
            jarFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void printClassDetails(Class<?> cls) {
        System.out.println("----------" + cls.getName() + "----------");
        int publicMethodCount = 0, privateMethodCount = 0, protectedMethodCount = 0, staticMethodCount = 0;
        
        for (Method method : cls.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers)) publicMethodCount++;
            if (Modifier.isPrivate(modifiers)) privateMethodCount++;
            if (Modifier.isProtected(modifiers)) protectedMethodCount++;
            if (Modifier.isStatic(modifiers)) staticMethodCount++;
        }
        
        System.out.println("  Public methods: " + publicMethodCount);
        System.out.println("  Private methods: " + privateMethodCount);
        System.out.println("  Protected methods: " + protectedMethodCount);
        System.out.println("  Static methods: " + staticMethodCount);
        
        int fieldsCount = cls.getDeclaredFields().length;
        System.out.println("  Fields: " + fieldsCount);
    }
}
