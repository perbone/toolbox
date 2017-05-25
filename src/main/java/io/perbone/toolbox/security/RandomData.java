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

package io.perbone.toolbox.security;

/**
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class RandomData
{
    public static final int ASCII_TABLE_SIZE = 256;
    public static final byte[] BYTE_ASCII_TABLE = byteAsciiTable();

    /**
     * 
     * @param length
     * @return
     */
    public static byte[] generate(final int length)
    {
        return generate(length, BYTE_ASCII_TABLE);
    }

    /**
     * 
     * @param length
     * @param samples
     * @return
     */
    public static char[] generate(final int length, final char[] samples)
    {
        Shuffler.shuffle(samples);
        char[] data = new char[length];
        for (int i = 0; i < length; i++)
            data[i] = samples[Fortuna.random().nextInt(samples.length)];
        return data;
    }

    /**
     * 
     * @param length
     * @param samples
     * @return
     */
    public static byte[] generate(final int length, final byte[] samples)
    {
        Shuffler.shuffle(samples);
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++)
            data[i] = samples[Fortuna.random().nextInt(samples.length)];
        return data;
    }

    /**
     * 
     * @return
     */
    private static byte[] byteAsciiTable()
    {
        byte[] table = new byte[ASCII_TABLE_SIZE];
        for (int i = 0; i < ASCII_TABLE_SIZE; i++)
            table[i] = (byte) i;
        return table;
    }
}