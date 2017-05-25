/*
 * This file is part of ToolBox
 * https://github.com/perbone/toolbox/
 * 
 * Copyright 2013-2017 Paulo Perbone
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

package io.perbone.toolbox.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import io.perbone.toolbox.annotation.AnnotationScanner;
import io.perbone.toolbox.validation.StringValidations;

/**
 * Settings utility class.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class Settings
{
    private static final String DEFAULT_DELIMETERS = " ,;:\n";

    private Map<String, String> properties = new ConcurrentHashMap<String, String>();

    /**
     * Injects the loaded properties into the given injectee.
     * <p>
     * TODO support for list of all types not only Strings
     * 
     * @param injectee
     *            the point of injection
     * 
     * @return the injectee object
     * 
     * @throws IllegalArgumentException
     *             if the injectee is invalid
     * @throws IllegalArgumentException
     *             if the value can not be set into the field
     * @throws IllegalStateException
     *             if there is no value for a not null field
     */
    public <T> T inject(final T injectee) throws IllegalArgumentException, IllegalStateException
    {
        if (injectee == null)
            throw new IllegalArgumentException("Invalid injectee object");

        List<Field> annotatedFields = AnnotationScanner.scanFields(injectee.getClass(), Property.class);

        /**
         * Set field values
         */
        for (Field f : annotatedFields)
        {
            try
            {
                f.setAccessible(true);

                Property annotation = f.getAnnotation(Property.class);

                /* Property's name defaults to the field's name */
                String key = StringValidations.isValid(annotation.name()) ? annotation.name() : f.getName();
                /* Default value */
                String defaultValue = StringValidations.isValid(annotation.value()) ? annotation.value() : null;
                /* Nullable flag */
                boolean nullable = annotation.nullable();
                /* Collection elements supported separator delimiters */
                String delimiters = StringValidations.isValid(annotation.delimiters()) ? annotation.delimiters()
                        : DEFAULT_DELIMETERS;

                /* Parses the value to the field type */
                Object value = null;
                Class<?> type = f.getType();

                if (type == String.class)
                    value = getString(key, defaultValue);
                else if (type == Integer.class)
                    value = isValid(defaultValue) ? getInteger(key, Integer.parseInt(defaultValue)) : getInteger(key);
                else if (type == Long.class)
                    value = isValid(defaultValue) ? getLong(key, Long.parseLong(defaultValue)) : getLong(key);
                else if (type == Boolean.class)
                    value = isValid(defaultValue) ? getBoolean(key, Boolean.parseBoolean(defaultValue))
                            : getBoolean(key);
                else if (type.isEnum())
                    value = getEnum(type, key);
                else if (type == List.class && getString(key, defaultValue) != null)
                {
                    List<Object> list = new ArrayList<>();

                    StringTokenizer st = new StringTokenizer(getString(key, defaultValue), delimiters);
                    while (st.hasMoreElements())
                        list.add(st.nextElement());

                    value = list;
                }

                /* Nullable enforcement */
                if (value == null && !nullable)
                    throw new IllegalStateException(
                            String.format("Missing value for not null [%s] field ", f.getName()));

                /* Sets default values if the set contains no match */
                f.set(injectee, value);
            }
            catch (IllegalAccessException | IllegalArgumentException e)
            {
                throw new IllegalArgumentException(String.format("Cannot assign %s to a %s field",
                        injectee.getClass().getName(), f.getType().getName()), e);
            }
        }

        return injectee;
    }

    /**
     * Loads the given property file into this object.
     * <p>
     * It appends new properties and updates the existent ones.
     * 
     * @param path
     *            the property file path
     * 
     * @throws FileNotFoundException
     *             if the access to the file has failed
     */
    public Settings load(final String path) throws FileNotFoundException
    {
        Properties props = new Properties();

        try
        {
            File file = new java.io.File(new URI(path));
            FileInputStream fis = new FileInputStream(file);
            props.load(fis);
            load(props);
            props.clear();
            fis.close();
        }
        catch (Exception e)
        {
            throw new FileNotFoundException("Could not load [" + path + "]");
        }
        return this;
    }

    /**
     * Loads the given properties into this object.
     * <p>
     * It appends new properties and updates the existent ones.
     * 
     * @param props
     *            the property object to load
     * 
     * @throws IllegalArgumentException
     *             if the properties object is invalid
     */
    public Settings load(final Properties props) throws IllegalArgumentException
    {
        if (props == null)
            throw new IllegalArgumentException("Invalid properties");

        for (Map.Entry<Object, Object> entry : props.entrySet())
            properties.put((String) entry.getKey(), (String) entry.getValue());

        return this;
    }

    /**
     * Loads the given map of properties into this object.
     * <p>
     * It appends new properties and updates the existent ones.
     * 
     * @param props
     *            the map object to load
     * 
     * @throws IllegalArgumentException
     *             if the map object is invalid
     */
    public Settings load(final Map<String, String> props) throws IllegalArgumentException
    {
        if (props == null)
            throw new IllegalArgumentException("Invalid properties");

        for (Map.Entry<String, String> entry : props.entrySet())
            properties.put(entry.getKey(), entry.getValue());

        return this;
    }

    /**
     * Returns the specified key value as a string object.
     * 
     * @param key
     *            the property key
     * 
     * @return the value of the property or {@code null} if the property is not found
     */
    public String getString(final String key)
    {
        String value = properties.get(key);
        return value;
    }

    /**
     * Returns the specified key value as a string object.
     * 
     * @param key
     *            the property key
     * @param value
     *            the default value
     * 
     * @return the value of the property or the default value if the property is not found
     */
    public String getString(final String key, final String value)
    {
        return properties.containsKey(key) ? properties.get(key) : value;
    }

    /**
     * Returns the specified key value as an integer object.
     * 
     * @param key
     *            the property key
     * 
     * @return the value of the property or {@code null} if the property is not found
     * 
     * @throws IllegalStateException
     *             if the value cannot be converted to the requested type
     */
    public Integer getInteger(final String key) throws IllegalStateException
    {
        if (!properties.containsKey(key))
            return null;

        String raw = properties.get(key);
        try
        {
            return Integer.parseInt(raw);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalStateException(String.format("Could not convert string [%s] into a valid integer", raw));
        }
    }

    /**
     * Returns the specified key value as an integer object.
     * 
     * @param key
     *            the property key
     * @param value
     *            the default value
     * 
     * @return the value of the property or the default value if the property is not found
     */
    public Integer getInteger(final String key, final Integer value)
    {
        if (!properties.containsKey(key))
            return value;

        String raw = properties.get(key);
        try
        {
            return Integer.parseInt(raw);
        }
        catch (NumberFormatException e)
        {
            return value;
        }
    }

    /**
     * Returns the specified key value as a long object.
     * 
     * @param key
     *            the property key
     * 
     * @return the value of the property or {@code null} if the property is not found
     * 
     * @throws IllegalStateException
     *             if the value cannot be converted to the requested type
     */
    public Long getLong(final String key) throws IllegalStateException
    {
        if (!properties.containsKey(key))
            return null;

        String raw = properties.get(key);
        try
        {
            return Long.parseLong(raw);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalStateException(String.format("Could not convert string [%s] into a valid long", raw));
        }
    }

    /**
     * Returns the specified key value as a long object.
     * 
     * @param key
     *            the property key
     * @param value
     *            the default value
     * 
     * @return the value of the property or the default value if the property is not found
     */
    public Long getLong(final String key, final Long value)
    {
        if (!properties.containsKey(key))
            return value;

        String raw = properties.get(key);
        try
        {
            return Long.parseLong(raw);
        }
        catch (NumberFormatException e)
        {
            return value;
        }
    }

    /**
     * Returns the specified key value as a boolean object.
     * 
     * @param key
     *            the property key
     * 
     * @return the value of the property or {@code null} if the property is not found
     * 
     * @throws IllegalStateException
     *             if the value cannot be converted to the requested type
     */
    public Boolean getBoolean(final String key) throws IllegalStateException
    {
        if (!properties.containsKey(key))
            return false;

        String raw = properties.get(key);
        if (!raw.toLowerCase().matches("true|yes|false|no"))
            throw new IllegalStateException(String.format("Could not convert string [%s] into a valid integer", raw));
        else
            return Boolean.parseBoolean(raw);
    }

    /**
     * Returns the specified key value as a boolean object.
     * 
     * @param key
     *            the property key
     * @param value
     *            the default value
     * 
     * @return the value of the property or the default value if the property is not found
     */
    public Boolean getBoolean(final String key, final Boolean value)
    {
        if (!properties.containsKey(key))
            return value;

        String raw = properties.get(key);
        return raw.toLowerCase().matches("true|yes|false|no") ? Boolean.parseBoolean(raw) : value;
    }

    /**
     * Returns the specified key value as an integer object.
     * 
     * @param type
     *            the enumeration class type
     * @param key
     *            the property key
     * 
     * @return the value of the property or {@code null} if the property is not found
     * 
     * @throws IllegalArgumentException
     *             if the type is invalid
     * @throws IllegalArgumentException
     *             if the type is not an enumeration
     * @throws IllegalStateException
     *             if the value cannot be converted to the requested type
     */
    public Enum<?> getEnum(final Class<?> type, final String key) throws IllegalArgumentException, IllegalStateException
    {
        if (type == null)
            throw new IllegalArgumentException("Invalid type class");
        if (!type.isEnum())
            throw new IllegalArgumentException("Type class is not enum");

        if (!properties.containsKey(key))
            return null;

        String raw = properties.get(key);
        for (Enum<?> e : (Enum[]) type.getEnumConstants())
        {
            if (e.toString().equalsIgnoreCase(raw))
                return e;
        }
        throw new IllegalStateException(String.format("Could not convert string [%s] into a valid enum of type [%s]",
                raw, type.getCanonicalName()));
    }

    /**
     * Returns the specified key value as a boolean object.
     * 
     * @param type
     *            the enumeration class type
     * @param key
     *            the property key
     * @param value
     *            the default value
     * 
     * @return the value of the property or the default value if the property is not found
     */
    public Enum<?> getEnum(final Class<?> type, final String key, final Enum<?> value)
    {
        if (type == null)
            throw new IllegalArgumentException("Invalid type class");
        if (!type.isEnum())
            throw new IllegalArgumentException("Type class is not enum");

        if (!properties.containsKey(key))
            return value;

        String raw = properties.get(key);
        for (Enum<?> e : (Enum[]) type.getEnumConstants())
        {
            if (e.toString().equalsIgnoreCase(raw))
                return e;
        }
        return value;
    }

    /**
     * 
     * @param str
     * @return
     */
    private boolean isValid(final String str)
    {
        return (str != null && !str.isEmpty());
    }
}