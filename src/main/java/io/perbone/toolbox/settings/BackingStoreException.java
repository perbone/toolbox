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

package io.perbone.toolbox.settings;

/**
 * Backing store exception.
 * <p>
 * It's an specialization of {@link SettingsException} thrown to indicate that a settings operation
 * could not complete because of a failure in the backing store, or a failure to contact the backing
 * store.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 * 
 * @see #SettingsException
 */
public final class BackingStoreException extends SettingsException
{
    /** Class {@code BackingStoreException} serial version identifier. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a default {@code BackingStoreException} object without neither a message nor a root
     * exception.
     */
    public BackingStoreException()
    {
        super();
    }

    /**
     * Creates an {@code BackingStoreException} object with a custom message.
     * 
     * @param message
     *            The exception message
     */
    public BackingStoreException(final String message)
    {
        super(message);
    }

    /**
     * Creates an {@code BackingStoreException} object with a custom root exception.
     * 
     * @param cause
     */
    public BackingStoreException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates an {@code BackingStoreException} object with a custom message and a custom root
     * exception.
     * 
     * @param message
     * @param cause
     */
    public BackingStoreException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}