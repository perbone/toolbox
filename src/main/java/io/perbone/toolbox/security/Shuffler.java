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
public final class Shuffler
{
    /**
     * 
     * @param value
     * @return
     */
    public static String shuffle(final String value)
    {
        return new String(shuffle(value.toCharArray()));
    }

    /**
     * 
     * @param value
     * @return
     */
    public static char[] shuffle(final char[] value)
    {
        for (int i = 0; i < value.length; i++)
        {
            int rpos = Fortuna.random().nextInt(value.length);
            char temp = value[i];
            value[i] = value[rpos];
            value[rpos] = temp;
        }
        return value;
    }

    /**
     * 
     * @param value
     * @return
     */
    public static byte[] shuffle(final byte[] value)
    {
        for (int i = 0; i < value.length; i++)
        {
            int rpos = Fortuna.random().nextInt(value.length);
            byte temp = value[i];
            value[i] = value[rpos];
            value[rpos] = temp;
        }
        return value;
    }
}