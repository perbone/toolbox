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

package io.perbone.toolbox.validation;

import java.util.Date;

import io.perbone.toolbox.formatter.DateTimeFormatter;

/**
 * Date validations.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class DateValidations
{
    /**
     * 
     * @param source
     * @return
     */
    public static boolean isValidISO8601Date(final String source)
    {
        return DateTimeFormatter.fromISO8601(source) == null ? false : true;
    }

    /**
     * Validates a string value for a valid date.
     * 
     * @param value
     *            The string value to validate
     * @return <tt>true</tt> for a valid date; <tt>false</tt> otherwise
     */
    public static boolean isValid(final String value)
    {
        if (value == null)
            return false;

        try
        {
            if (Long.parseLong(value) < 0)
                return false;
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Validates an object value for a valid date.
     * 
     * @param value
     *            The string value to validate
     * @return <tt>true</tt> for a valid date; <tt>false</tt> otherwise
     */
    public static boolean isValid(final Object value)
    {
        if (value == null)
            return false;
        else if (value instanceof Date)
            return true;
        else if (value instanceof Long && ((Long) value) >= 0)
            return true;
        else
            return false;
    }

    public static boolean isFuture(final String value)
    {
        if (isValid(value) && Long.parseLong(value) > System.currentTimeMillis())
            return true;
        else
            return false;
    }
}