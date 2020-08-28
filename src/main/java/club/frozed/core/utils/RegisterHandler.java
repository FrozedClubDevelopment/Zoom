package club.frozed.core.utils;

import com.google.common.collect.ImmutableSet;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RegisterHandler {
    public static void loadListenersFromPackage(Plugin plugin, String packageName) {
        for (Class<?> clazz : getClassesInPackage(plugin, packageName)) {
            if (isListener(clazz)) {
                try {
                    plugin.getServer().getPluginManager().registerEvents((Listener)clazz.newInstance(), plugin);
                } catch (Exception exception) {}
                continue;
            }
            try {
                clazz.newInstance();
            } catch (Exception exception) {}
        }
    }
    public static void loadCommandsFromPackage(Plugin plugin, String packageName) {
        for (Class<?> clazz : getClassesInPackage(plugin, packageName)) {
            try {
                clazz.newInstance();
            } catch (Exception exception) {}
        }
    }
    // Check is listener
    public static boolean isListener(Class<?> clazz) {
        for (Class<?> interfaze : clazz.getInterfaces()) {
            if (interfaze == Listener.class)
                return true;
        }
        return false;
    }
    public static Collection<Class<?>> getClassesInPackage(Plugin plugin, String packageName) {
        JarFile jarFile;
        Collection<Class<?>> classes = new ArrayList<>();
        CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        URL resource = codeSource.getLocation();
        String relPath = packageName.replace('.', '/');
        String resPath = resource.getPath().replace("%20", " ");
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length())
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            if (className != null) {
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz != null)
                    classes.add(clazz);
            }
        }
        try {
            jarFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Collection<Class<?>>)ImmutableSet.copyOf(classes);
    }
}
