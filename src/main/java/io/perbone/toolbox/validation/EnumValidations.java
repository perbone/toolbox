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
 * Enumeration Validations.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class EnumValidations
{
    /**
     * Validates a member name against a given enumeration type.
     * 
     * @param type
     *            the enumeration type
     * @param name
     *            the member name
     * 
     * @return <tt>true</tt> for a valid name; <tt>false</tt> otherwise
     */
    public static boolean isMember(final Class<?> type, String name)
    {
        if (type.isEnum())
        {
            for (Enum<?> e : (Enum[]) type.getEnumConstants())
            {
                if (e.toString().equalsIgnoreCase(name))
                    return true;
            }
        }

        return false;
    }
}