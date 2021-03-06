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

package io.perbone.toolbox.provider;

/**
 * Defines the exception thrown when an operation aborts.
 * <p>
 * Generally this exception occurs when the provider have been shut down. Other situation than that
 * should be used with care.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 * 
 * @see #ProviderException
 */
public class AbortOperationException extends ProviderException
{
    /** Class {@code OperationTimeoutException} serial version identifier. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a default {@code OperationTimeoutException} object without neither a message nor a
     * root exception.
     */
    public AbortOperationException()
    {
        super();
    }

    /**
     * Creates an {@code OperationTimeoutException} object with a custom message.
     * 
     * @param message
     *            The exception message
     */
    public AbortOperationException(final String message)
    {
        super(message);
    }

    /**
     * Creates an {@code OperationTimeoutException} object with a custom root exception.
     * 
     * @param cause
     */
    public AbortOperationException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates an {@code OperationTimeoutException} object with a custom message and a custom root
     * exception.
     * 
     * @param message
     * @param cause
     */
    public AbortOperationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}