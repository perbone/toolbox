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

/**
 * Boolean Validations.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class BooleanValidations
{
    /**
     * Validates a string value for a boolean value.
     * <p>
     * Checks case insensitively for <tt>true</tt> , <tt>false</tt> , <tt>yes</tt>, <tt>no</tt>,
     * <tt>on</tt>, <tt>off</tt>.
     * 
     * @param value
     *            The string value to validate
     * 
     * @return <tt>true</tt> for a valid boolean; <tt>false</tt> otherwise
     */
    public static Boolean isBoolean(final String value)
    {
        return value == null ? false : value.toLowerCase().matches("true|false|yes|no|on|off");
    }
}