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

package io.perbone.toolbox.serialization;

/**
 * It's an specialization of {@link RuntimeException} thrown to indicate all kind of serialization
 * exception events.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public class SerializationException extends RuntimeException
{
    /** Class {@code StorageException} serial version identifier. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a default {@code StorageException} object without neither a message nor a root
     * exception.
     */
    public SerializationException()
    {
        super();
    }

    /**
     * Creates an {@code StorageException} object with a custom message.
     * 
     * @param message
     *            The exception message
     */
    public SerializationException(String message)
    {
        super(message);
    }

    /**
     * Creates an {@code StorageException} object with a custom root exception.
     * 
     * @param cause
     */
    public SerializationException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates an {@code StorageException} object with a custom message and a custom root exception.
     * 
     * @param message
     * @param cause
     */
    public SerializationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}