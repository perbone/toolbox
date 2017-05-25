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

package io.perbone.toolbox.id;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import io.perbone.toolbox.security.Fortuna;

/**
 * Uuid factory.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class UuidFactory
{
    private static volatile Random ng = Fortuna.newInstance();
    private static final long MSB = 0x8000000000000000L;

    public static final long DEFAULT_SIZE = 1;
    public static final long MAX_SIZE = 32767;

    private static final Pattern VALIDATION_PATTERN = Pattern.compile("[0-9a-fA-F]{32}");

    private static final Map<Long, String> cache = new ConcurrentHashMap<Long, String>(1024);

    private final long size;

    /**
     * 
     */
    public UuidFactory()
    {
        this(DEFAULT_SIZE);
    }

    /**
     * 
     * @param size
     */
    public UuidFactory(final long size)
    {
        this.size = size;
    }

    /**
     * 
     * @param target
     * @return
     */
    public Uuid generate(final long target)
    {
        return UuidFactory.generate(size, target);
    }

    /**
     * 
     * @param size
     * @param target
     * @return
     */
    public static Uuid generate(final long size, final long target)
    {
        if (size <= 0 || size > MAX_SIZE)
            throw new IllegalArgumentException(
                    String.format("size cannot be less than zero or greater than %d", MAX_SIZE));

        if (target < 0)
            throw new IllegalArgumentException("target cannot be less than zero");

        if (target >= size)
            throw new IllegalArgumentException("target cannot be greater than or equal to size");

        String s = cache.remove(target);
        if (s != null)
            return new Uuid(toByteArray(s));

        for (long i = 0; i < MAX_SIZE; i++)
        {
            String tmp = Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
            long shard = Integer.decode("0x" + tmp.substring(0, 4)) % (size);
            if (shard == target)
                return new Uuid(toByteArray(tmp));
            else
                cache.put(shard, tmp);
        }

        return null;
    }

    /**
     * 
     * @param uuid
     * @return
     */
    public long target(final String uuid)
    {
        return UuidFactory.target(size, uuid);
    }

    /**
     * 
     * @param size
     * @param uuid
     * @return
     */
    public static long target(final long size, final String uuid)
    {
        return Integer.decode("0x" + uuid.substring(0, 4)) % (size);
    }

    /**
     * 
     * @param uuid
     * @return
     */
    public static boolean isValid(final String uuid)
    {
        return uuid == null ? false : VALIDATION_PATTERN.matcher(uuid).matches();
    }

    /**
     * 
     * @param s
     * @return
     */
    private static byte[] toByteArray(final String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}