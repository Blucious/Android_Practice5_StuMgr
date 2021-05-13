package org.group9.stumgr.util.android;

import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 利用反射解析sdk找到对应的实现类
 */
public abstract class PlatformSupportManager<T> {

   private final Class<T> managedInterface;

   private final T defaultImplementation;

   private final SortedMap<Integer, String> implementations;

   protected PlatformSupportManager(Class<T> managedInterface, T defaultImplementation) {
      if (!managedInterface.isInterface()) {
         throw new IllegalArgumentException();
      }
      if (!managedInterface.isInstance(defaultImplementation)) {
         throw new IllegalArgumentException();
      }
      this.managedInterface = managedInterface;
      this.defaultImplementation = defaultImplementation;
      this.implementations = new TreeMap<Integer, String>(Collections.reverseOrder());
   }

   protected final void addImplementationClass(int minVersion, String className) {
      implementations.put(minVersion, className);
   }

   public final T build() {
      for (Integer minVersion : implementations.keySet()) {
         if (Build.VERSION.SDK_INT >= minVersion) {
            String className = implementations.get(minVersion);
            try {
               Class<? extends T> clazz = Class.forName(className).asSubclass(managedInterface);
               return clazz.getConstructor().newInstance();
            } catch (ClassNotFoundException cnfe) {
            } catch (IllegalAccessException iae) {
            } catch (InstantiationException ie) {
            } catch (NoSuchMethodException nsme) {
            } catch (InvocationTargetException ite) {
            }
         }
      }
      return defaultImplementation;
   }

}
