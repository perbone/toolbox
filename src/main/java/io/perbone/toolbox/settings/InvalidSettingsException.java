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
 * Invalid settings exception.
 * <p>
 * It's an specialization of {@link SettingsException} thrown to indicate that an operation could
 * not complete because the input did not conform to the appropriate XML document type for a
 * collection of settings, as per the {@link Settings} specification.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 * 
 * @see #SettingsException
 */
public final class InvalidSettingsException extends SettingsException
{
    /** Class {@code InvalidPreferenceException} serial version identifier. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a default {@code InvalidPreferenceException} object without neither a message nor a
     * root exception.
     */
    public InvalidSettingsException()
    {
        super();
    }

    /**
     * Creates an {@code InvalidPreferenceException} object with a custom message.
     * 
     * @param message
     *            The exception message
     */
    public InvalidSettingsException(final String message)
    {
        super(message);
    }

    /**
     * Creates an {@code InvalidPreferenceException} object with a custom root exception.
     * 
     * @param cause
     */
    public InvalidSettingsException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates an {@code InvalidPreferenceException} object with a custom message and a custom root
     * exception.
     * 
     * @param message
     * @param cause
     */
    public InvalidSettingsException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}