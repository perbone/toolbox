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
 * Resource interface.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public interface Resource extends AutoCloseable
{
    /**
     * Opens this resource.
     * 
     * @throws IllegalStateException
     *             if this resource is already open
     * @throws OperationTimeoutException
     *             if the operation is timed out
     * @throws NotEnoughResourceException
     *             if the is not enough resources to complete this operation
     * @throws ProviderException
     *             if an error occurs during this operation
     * 
     * @see #close
     * @see #isOpen
     */
    void open() throws IllegalStateException, OperationTimeoutException, NotEnoughResourceException, ProviderException;

    /**
     * Closes this resource.
     * <p>
     * Invocation has no additional effect if already closed and once closed it remains closed.
     * 
     * @throws ProviderException
     *             if an error occurs during this operation
     * 
     * @see #isOpen
     */
    void close() throws ProviderException;

    /**
     * Tells whether or not this resource is open.
     * <p>
     * It is assumed that after a successful object instantiation this method will return
     * <tt>true</tt>. Conversely for fail object instantiation this method should return
     * <tt>false</tt> despite the fact that this object may still be valid.
     * <p>
     * 
     * @return <tt>true</tt> if it is active; <tt>false</tt> otherwise
     * 
     * @see #close
     */
    boolean isOpen();
}
