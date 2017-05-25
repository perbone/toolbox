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

package io.perbone.toolbox.provider;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class provides a skeletal implementation of the {@code Resource} interface, to minimize the
 * effort required to implement this interface.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public abstract class AbstractResource implements Resource
{
    protected String MESSAGE_FAIL_CANNOT_OPEN = "Cannot open this data source";

    protected String MESSAGE_FAIL_CANNOT_CLOSE = "Cannot close this data source";

    protected String MESSAGE_FAIL_ALREADY_OPEN = "Attempt to open already open data source object";

    protected String MESSAGE_FAIL_CHECK_CLOSING = "Illegal invocation; closing is already in progress";

    protected String MESSAGE_FAIL_CHECK_OPEN = "Attempt to use non-open resource object";

    protected String MESSAGE_FAIL_UNSUPPORTED_OPERATION = "Data source feature not supported by this implementation";

    protected final AtomicBoolean open = new AtomicBoolean(false);

    protected final AtomicBoolean closing = new AtomicBoolean(false);

    public AbstractResource()
    {
        // do nothing
    }

    @Override
    public void open()
            throws IllegalStateException, OperationTimeoutException, NotEnoughResourceException, ProviderException
    {
        if (open.compareAndSet(false, true))
        {
            try
            {
                this.onOpen();
            }
            catch (final ProviderException e)
            {
                open.set(false); // Abort open
                throw e;
            }
            catch (final Exception e)
            {
                open.set(false); // Abort open
                throw new ProviderException(MESSAGE_FAIL_CANNOT_OPEN, e);
            }
        }
        else
        {
            throw new IllegalStateException(MESSAGE_FAIL_ALREADY_OPEN);
        }
    }

    @Override
    public void close() throws ProviderException
    {
        if (closing.compareAndSet(false, true))
        {
            try
            {
                this.onClose();
                open.set(false);
            }
            catch (final ProviderException e)
            {
                closing.set(true); // Abort close
                throw e;
            }
            catch (final Exception e)
            {
                closing.set(true); // Abort close
                throw new ProviderException(MESSAGE_FAIL_CANNOT_CLOSE, e);
            }
        }
    }

    @Override
    public boolean isOpen()
    {
        return open.get();
    }

    /**
     * Checks if this data source is currently open
     * 
     * @throws IllegalStateException
     *             when it is not open
     */
    protected void checkOpen() throws IllegalStateException
    {
        if (!open.get())
            throw new IllegalStateException(MESSAGE_FAIL_CHECK_OPEN);
    }

    /**
     * Checks if the shutdown is already in progress.
     * 
     * @throws IllegalStateException
     *             if the shutdown is already in progress
     */
    protected void checkClosing() throws IllegalStateException
    {
        if (closing.get())
            throw new IllegalStateException(MESSAGE_FAIL_CHECK_CLOSING);
    }

    /**
     * Open entry point for concrete implementations.
     * 
     * @throws IllegalStateException
     *             if this data source is already open
     * @throws OperationTimeoutException
     *             if the operation times out
     * @throws StorageProviderException
     *             if cannot open this data source
     */
    protected abstract void onOpen()
            throws IllegalStateException, OperationTimeoutException, NotEnoughResourceException, ProviderException;

    /**
     * Close entry point for concrete implementations.
     * 
     * @throws StorageProviderException
     *             if cannot closes the this data source
     */
    protected abstract void onClose() throws ProviderException;
}