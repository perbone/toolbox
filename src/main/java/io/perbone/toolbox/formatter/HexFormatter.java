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

package io.perbone.toolbox.formatter;

/**
 * Hex formatter.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class HexFormatter
{
    /**
     * Returns the hexadecimal value of the supplied byte array. The resulting string always uses
     * two hexadecimals per byte. As a result, the length of the resulting string is guaranteed to
     * be twice the length of the supplied byte array.
     * 
     * @param data
     *            An arbitrary binary data
     * @return An hexadecimal String representation of the binary data
     */
    public static String encode(final byte[] data)
    {
        StringBuffer sb = new StringBuffer(data.length * 2);
        for (short b : data)
        {
            int d1 = (b >> 4) & 0xF;
            int d2 = b & 0xF;
            sb.append(Character.forDigit(d1, 16));
            sb.append(Character.forDigit(d2, 16));
        }

        return sb.toString();
    }

    /**
     * Transform an hexadecimal value into an array of bytes.
     * 
     * @param data
     *            An hexadecimal String representation of a binary data
     * @return The binary data
     */
    public static byte[] decode(final String data)
    {
        char[] c = data.toCharArray();

        byte[] decoded = new byte[c.length / 2];

        for (int i = 0, j = 0; i < c.length - 1; i++, j++)
        {
            int d1 = Character.digit(c[i], 16);
            int d2 = Character.digit(c[++i], 16);

            decoded[j] = (byte) ((byte) (d1 << 4) + (byte) d2);
        }

        return decoded;
    }
}