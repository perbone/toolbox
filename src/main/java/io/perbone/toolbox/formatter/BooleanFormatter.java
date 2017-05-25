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
 * Boolean formatter class.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class BooleanFormatter
{
    /**
     * Converts a string into its equivalent boolean value.
     * <p>
     * Converts <tt>true</tt> , <tt>false</tt> , <tt>yes</tt>, <tt>no</tt>, <tt>on</tt>,
     * <tt>off</tt>.
     * 
     * @param value
     *            the string value to convert
     * 
     * @return The boolean value for a valid boolean; <tt>null</tt> otherwise
     */
    public static Boolean toBoolean(final String value)
    {
        if (value == null)
            return false;
        else if (value.toLowerCase().matches("true|yes|on"))
            return true;
        else if (value.toLowerCase().matches("false|no|off"))
            return false;
        else
            return false;
    }
}