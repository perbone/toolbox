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

import java.util.regex.Pattern;

/**
 * String validations.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class StringValidations
{
    /**
     * Check if a string is a combination of alphabetic and numeric
     * 
     * @param str
     * @return
     */
    public static boolean isAlphameric(final String str)
    {
        return isValid(str) && Pattern.matches("\\w*", str);
    }

    /**
     * Validates a given string.
     * <p>
     * Null, empty or blank only strings will be considered invalid.
     * 
     * @param str
     *            the string to validate
     * 
     * @return true if the string is valid. false otherwise.
     */
    public static boolean isValid(final String str)
    {
        return str == null ? false : !str.trim().isEmpty();
    }
}
