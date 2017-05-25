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

package io.perbone.toolbox.hash;

/**
 * CRC64 checksum calculator based on the polynom specified in ISO 3309. The implementation is based
 * on the following publication:
 * 
 * <ul>
 * <li>http://en.wikipedia.org/wiki/Cyclic_redundancy_check</li>
 * </ul>
 * 
 * @since 0.1.0
 */
public final class CRC64
{
    private static final long POLY64REV = 0xd800000000000000L;

    private static final long[] LOOKUPTABLE;

    static
    {
        LOOKUPTABLE = new long[0x100];
        for (int i = 0; i < 0x100; i++)
        {
            long v = i;
            for (int j = 0; j < 8; j++)
            {
                if ((v & 1) == 1)
                {
                    v = (v >>> 1) ^ POLY64REV;
                }
                else
                {
                    v = (v >>> 1);
                }
            }
            LOOKUPTABLE[i] = v;
        }
    }

    /**
     * Calculates the CRC64 checksum for the given data array.
     * 
     * @param data
     *            data to calculate checksum for
     * 
     * @return checksum value
     */
    public static long checksum(final byte[] data)
    {
        long sum = 0;
        for (int i = 0; i < data.length; i++)
        {
            final int lookupidx = ((int) sum ^ data[i]) & 0xff;
            sum = (sum >>> 8) ^ LOOKUPTABLE[lookupidx];
        }
        return sum;
    }
}