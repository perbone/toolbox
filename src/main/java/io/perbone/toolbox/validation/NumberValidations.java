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

package io.perbone.toolbox.validation;

/**
 * Numbers validations.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class NumberValidations
{
    /**
     * Validates a string value for a integer number
     * 
     * @param value
     *            The string value to validate
     * 
     * @return <tt>true</tt> for a valid number; <tt>false</tt> otherwise
     */
    public static boolean isInteger(final String value)
    {
        if (value == null)
            return false;

        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Validates an object value for a long number
     * 
     * @param value
     *            The string value to validate
     * 
     * @return <tt>true</tt> for a valid number; <tt>false</tt> otherwise
     */
    public static boolean isLong(final Object value)
    {
        if (value == null)
            return false;
        else if (value instanceof Long)
            return true;
        else if (value instanceof String)
        {
            try
            {
                Long.parseLong((String) value);
                return true;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        else
            return false;
    }
}