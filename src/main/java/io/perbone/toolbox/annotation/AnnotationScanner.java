/*
 * This file is part of ToolBox
 * https://github.com/perbone/toolbox/
 * 
 * Copyright 2013-2018 Paulo Perbone
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package io.perbone.toolbox.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Annotation scanner helper class.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public class AnnotationScanner
{
    /** Cache of scanned types */
    private static final Map<Class<?>, Set<Annotation>> markedTypes = new ConcurrentHashMap<Class<?>, Set<Annotation>>();

    /** Cache of scanned fields */
    private static final Map<Class<?>, Map<Class<? extends Annotation>, List<Field>>> annotatedFields = new ConcurrentHashMap<Class<?>, Map<Class<? extends Annotation>, List<Field>>>();

    /** Cache of scanned methods */
    private static final Map<Class<?>, Map<Class<? extends Annotation>, List<Method>>> annotatedMethods = new ConcurrentHashMap<Class<?>, Map<Class<? extends Annotation>, List<Method>>>();

    /**
     * 
     * @param annotationClass
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isAnnotationPresent(final Class<?> type, final Class<? extends Annotation> annotationClass)
    {
        return isAnnotationPresent(type, annotationClass, false);
    }

    /**
     * 
     * @param annotationClass
     * @param deep
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isAnnotationPresent(final Class<?> type, final Class<? extends Annotation> annotationClass,
            final boolean deep) throws IllegalArgumentException
    {
        if (type == null)
            throw new IllegalArgumentException("type parameter cannot be null");
        if (annotationClass == null)
            throw new IllegalArgumentException("annotationClass parameter cannot be null");

        if (!deep)
            return type.isAnnotationPresent(annotationClass);

        Class<?> clazz = type;
        while (clazz != null)
        {
            Set<Annotation> ann = markedTypes.get(clazz);
            if (ann != null)
            {
                for (Annotation a : ann)
                {
                    if (a.annotationType().equals(annotationClass))
                        return true;
                }
            }

            if (clazz.isAnnotationPresent(annotationClass))
            {
                Set<Annotation> s = markedTypes.containsKey(clazz) ? markedTypes.get(clazz) : new HashSet<Annotation>();
                s.add(clazz.getAnnotation(annotationClass));
                markedTypes.put(clazz, s);
                return true;
            }

            clazz = clazz.getSuperclass();
        }

        return false;
    }

    /**
     * 
     * @param type
     * @param annotationClass
     * @return
     * @throws IllegalArgumentException
     */
    public static <T extends Annotation> T getAnnotation(final Class<?> type, Class<T> annotationClass)
            throws IllegalArgumentException
    {
        return getAnnotation(type, annotationClass, false);
    }

    /**
     * 
     * @param type
     * @param annotationClass
     * @param deep
     * @return
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(final Class<?> type, final Class<T> annotationClass,
            boolean deep) throws IllegalArgumentException
    {
        if (type == null)
            throw new IllegalArgumentException("type parameter cannot be null");
        if (annotationClass == null)
            throw new IllegalArgumentException("annotationClass parameter cannot be null");

        if (!deep)
            return type.getAnnotation(annotationClass);

        if (isAnnotationPresent(type, annotationClass, deep))
        {
            Class<?> clazz = type;
            while (clazz != null)
            {
                for (Annotation a : markedTypes.get(clazz))
                {
                    if (a.annotationType().equals(annotationClass))
                        return (T) a;
                }

                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }

    /**
     * 
     * Scan Field (goes inside superclass too)
     * 
     * @param type
     * @param annotationClass
     * @return
     * @throws IllegalArgumentException
     */
    public static List<Field> scanFields(final Class<?> type, final Class<? extends Annotation> annotationClass)
            throws IllegalArgumentException
    {
        if (type == null)
            throw new IllegalArgumentException("type parameter cannot be null");
        if (annotationClass == null)
            throw new IllegalArgumentException("annotationClass parameter cannot be null");

        List<Field> fields = null;

        Map<Class<? extends Annotation>, List<Field>> map = annotatedFields.get(type);
        if (map != null)
            fields = map.get(annotationClass);

        /** Cache miss, scan all fields (expensive operation) */
        if (fields == null)
        {
            /** Scan types fields */
            fields = new ArrayList<Field>();

            for (Field f : type.getDeclaredFields())
            {
                if (f.isAnnotationPresent(annotationClass))
                {
                    f.setAccessible(true);

                    fields.add(f);
                }
            }

            final Class<?> superClazz = type.getSuperclass();
            if (superClazz != null)
            {
                fields.addAll(scanFields(superClazz, annotationClass));
            }

            /** Cache it */
            if (!fields.isEmpty() && map == null)
            {
                map = new HashMap<Class<? extends Annotation>, List<Field>>();
                map.put(annotationClass, fields);
                annotatedFields.put(type, map);
            }
        }

        return fields;
    }

    /**
     * Scan Field (goes inside superclass too)
     * 
     * @param type
     * @param annotationClass
     * @param assignableClass
     * @return
     * @throws IllegalArgumentException
     */
    public static List<Field> scanFields(final Class<?> type, final Class<? extends Annotation> annotationClass,
            final Class<?> assignableClass) throws IllegalArgumentException
    {
        if (type == null)
            throw new IllegalArgumentException("type parameter cannot be null");
        if (annotationClass == null)
            throw new IllegalArgumentException("annotationClass parameter cannot be null");
        if (assignableClass == null)
            throw new IllegalArgumentException("assignableClass parameter cannot be null");

        List<Field> fields = new ArrayList<Field>();

        List<Field> cached = scanFields(type, annotationClass);
        if (!cached.isEmpty())
        {
            for (Field f : cached)
            {
                if (f.getType().isAssignableFrom(assignableClass))
                    fields.add(f);
            }
        }

        return fields;
    }

    /**
     * 
     * @param field
     * @param annotationClass
     * @return
     * @throws IllegalArgumentException
     */
    public static <T extends Annotation> T getDeepAnnotation(final Field field, final Class<T> annotationClass)
            throws IllegalArgumentException
    {
        if (field == null)
            throw new IllegalArgumentException("field parameter cannot be null");
        if (annotationClass == null)
            throw new IllegalArgumentException("annotationClass parameter cannot be null");

        T annotation = field.getAnnotation(annotationClass);

        if (annotation == null)
        {
            for (Annotation a : field.getAnnotations())
            {
                if (a.annotationType().isAnnotationPresent(annotationClass))
                {
                    annotation = a.annotationType().getAnnotation(annotationClass);
                    break;
                }
            }

        }

        return annotation;
    }

    /**
     * 
     * @param type
     * @param annotationClass
     * @return
     * @throws IllegalArgumentException
     */
    public static List<Method> scanMethods(final Class<?> type, final Class<? extends Annotation> annotationClass)
            throws IllegalArgumentException
    {
        if (type == null)
            throw new IllegalArgumentException("type parameter cannot be null");
        if (annotationClass == null)
            throw new IllegalArgumentException("annotationClass parameter cannot be null");

        List<Method> methods = null;

        Map<Class<? extends Annotation>, List<Method>> map = annotatedMethods.get(type);
        if (map != null)
            methods = map.get(annotationClass);

        /** Cache miss, scan all methods(expensive operation) */
        if (methods == null)
        {
            /** Scan type's methods */
            methods = new ArrayList<Method>();
            for (Method m : type.getDeclaredMethods())
            {
                if (m.isAnnotationPresent(annotationClass))
                {
                    m.setAccessible(true);

                    methods.add(m);
                }
            }

            final Class<?> superClazz = type.getSuperclass();
            if (superClazz != null)
            {
                methods.addAll(scanMethods(superClazz, annotationClass));
            }

            /** Cache it */
            if (!methods.isEmpty() && map == null)
            {
                map = new HashMap<Class<? extends Annotation>, List<Method>>();
                map.put(annotationClass, methods);
                annotatedMethods.put(type, map);
            }
        }

        return methods;
    }
}