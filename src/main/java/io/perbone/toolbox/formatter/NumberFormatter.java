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

package io.perbone.toolbox.formatter;

/**
 * Number formatter (high performance version).
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class NumberFormatter
{
    public static Long asLong(final Object value)
    {
        if (value == null)
            return null;
        if (value instanceof Long)
            return (Long) value;
        if (value instanceof Integer)
            return ((Integer) value).longValue();
        if (value instanceof String)
            return Long.parseLong((String) value);

        return null;
    }

    /**
     * Converts the long argument to an array of bytes.
     * 
     * @param value
     *            the long value
     * 
     * @return the array of bytes
     */
    public static byte[] longToByteArray(final long value)
    {
        return new byte[] { (byte) (value & 0xFF), (byte) ((value >> 8) & 0xFF), (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF), (byte) ((value >> 32) & 0xFF), (byte) ((value >> 40) & 0xFF),
                (byte) ((value >> 48) & 0xFF), (byte) ((value >> 56) & 0xFF), };
    }

    /**
     * Converts the byte array argument to a long value.
     * 
     * @param value
     *            the byte array
     * 
     * @return the long value
     */
    public static long byteArrayToLong(byte[] ba)
    {
        return (long) ba[7] << 56 | ((long) ba[6] & 0xff) << 48 | ((long) ba[5] & 0xff) << 40
                | ((long) ba[4] & 0xff) << 32 | ((long) ba[3] & 0xff) << 24 | ((long) ba[2] & 0xff) << 16
                | ((long) ba[1] & 0xff) << 8 | (long) ba[0] & 0xff;
    }

    /**
     * Converts the byte array argument to an integer value.
     * 
     * @param value
     *            the byte array
     * 
     * @return the integer value
     */
    public static int byteArrayToInt(byte[] ba)
    {
        return ba[3] & 0xFF | (ba[2] & 0xFF) << 8 | (ba[1] & 0xFF) << 16 | (ba[0] & 0xFF) << 24;
    }

    /**
     * Converts the int argument to an array of bytes.
     * 
     * @param value
     *            the int value
     * 
     * @return the array of bytes
     */
    public static byte[] intToByteArray(int value)
    {
        return new byte[] { (byte) ((value >> 24) & 0xFF), (byte) ((value >> 16) & 0xFF), (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF) };
    }
}
