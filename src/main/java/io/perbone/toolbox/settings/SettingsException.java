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

package io.perbone.toolbox.settings;

/**
 * Settings root exception.
 * <p>
 * It's an specialization of {@link RuntimeException} thrown to indicate all sort of settings
 * operation exceptions. Any other exception from this framework related to settings <em>should</em>
 * extends this class.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public class SettingsException extends RuntimeException
{
    /** Class {@code SettingsException} serial version identifier. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a default {@code SettingsException} object without neither a message nor a root
     * exception.
     */
    public SettingsException()
    {
        super();
    }

    /**
     * Creates an {@code SettingsException} object with a custom message.
     * 
     * @param message
     *            The exception message
     */
    public SettingsException(final String message)
    {
        super(message);
    }

    /**
     * Creates an {@code SettingsException} object with a custom root exception.
     * 
     * @param cause
     */
    public SettingsException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates an {@code SettingsException} object with a custom message and a custom root
     * exception.
     * 
     * @param message
     * @param cause
     */
    public SettingsException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}