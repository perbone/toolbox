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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class provides a skeletal implementation of the {@code Provider} interface, to minimize the
 * effort required to implement this interface.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractProvider implements Provider
{
    protected String MESSAGE_FAIL_CANNOT_ACTIVATE = "Cannot activate this provider";

    protected String MESSAGE_FAIL_CANNOT_SHUTDOWN = "Cannot shutdown this provider";

    protected String MESSAGE_FAIL_ALREADY_ACTIVE = "Attempt to activate already active provider object";

    protected String MESSAGE_FAIL_CHECK_ACTIVE = "Attempt to use non-active provider object";

    protected String MESSAGE_ABORT_OPERATION = "Aborting execution; this provider was deactivated";

    protected String MESSAGE_FAIL_CHECK_SHUTDOWN_IN_PROGRESS = "Illegal invocation; shutdown is already in progress";

    protected String MESSAGE_INVALID_ARGUMENT_GRACE_TIME = "Invalid grace time value; cannot be negative";

    protected String MESSAGE_INVALID_ARGUMENT_UNIT = "Invalid unit value; cannot be null";

    protected String MESSAGE_WARN_ALREADY_RUNNING = "Skipped invocation; this provider execution is already running";

    protected String MESSAGE_FAIL_UNSUPPORTED_OPERATION = "Provider feature not supported by this implementation";

    protected final AtomicBoolean active = new AtomicBoolean(false);

    protected final AtomicBoolean shutdownInProgress = new AtomicBoolean(false);

    protected String providerId = this.getClass().getName();

    public AbstractProvider()
    {
        // do nothing
    }

    @Override
    public String id()
    {
        return providerId;
    }

    @Override
    public <P extends Provider> P activate() throws IllegalStateException, NotEnoughResourceException, ProviderException
    {
        checkShutdownInProgress();

        if (active.compareAndSet(false, true))
        {
            try
            {
                this.onActivate();
            }
            catch (final IllegalStateException | ProviderException e)
            {
                active.set(false); // Activation roll back
                throw e;
            }
            catch (final Exception e)
            {
                active.set(false); // Activation roll back
                throw new ProviderException(e);
            }
        }
        else
        {
            throw new IllegalStateException(MESSAGE_FAIL_ALREADY_ACTIVE);
        }

        return (P) this;
    }

    @Override
    public <P extends Provider> P shutdown(long graceTime, TimeUnit unit)
            throws IllegalArgumentException, IllegalStateException, ProviderException
    {
        checkActive();

        if (graceTime < 0)
            throw new IllegalArgumentException(MESSAGE_INVALID_ARGUMENT_GRACE_TIME);
        if (unit == null)
            throw new IllegalArgumentException(MESSAGE_INVALID_ARGUMENT_UNIT);

        if (shutdownInProgress.compareAndSet(false, true))
        {
            try
            {
                this.onShutdown(graceTime, unit);
                active.set(false);
            }
            catch (final IllegalArgumentException | IllegalStateException | ProviderException e)
            {
                shutdownInProgress.set(false); // Abort shutdown
                throw e;
            }
            catch (final Exception e)
            {
                shutdownInProgress.set(false); // Abort shutdown
                throw new ProviderException(e);
            }
        }

        return (P) this;
    }

    @Override
    public boolean isShutdownInProgress()
    {
        return shutdownInProgress.get();
    }

    @Override
    public boolean isActive()
    {
        return active.get();
    }

    @Override
    public Resource openResource() throws IllegalStateException, IllegalArgumentException, OperationTimeoutException,
            NotEnoughResourceException, ProviderException
    {
        throw new UnsupportedOperationException(MESSAGE_FAIL_UNSUPPORTED_OPERATION);
    }

    @Override
    public <T> Resource openResource(T value) throws IllegalStateException, IllegalArgumentException,
            OperationTimeoutException, NotEnoughResourceException, ProviderException
    {
        throw new UnsupportedOperationException(MESSAGE_FAIL_UNSUPPORTED_OPERATION);
    }

    /**
     * Checks if this provider is currently active.
     * 
     * @throws IllegalStateException
     *             if it is not active
     */
    protected void checkActive() throws IllegalStateException
    {
        if (!active.get())
            throw new IllegalStateException(MESSAGE_FAIL_CHECK_ACTIVE);
    }

    /**
     * Checks if this provider execution was aborted.
     * 
     * @throws AbortOperationException
     *             if it was aborted
     */
    protected void checkAbortOperation() throws AbortOperationException
    {
        if (!active.get())
            throw new AbortOperationException(MESSAGE_ABORT_OPERATION);
    }

    /**
     * Checks if the shutdown is already in progress.
     * 
     * @throws IllegalStateException
     *             if the shutdown is already in progress
     */
    protected void checkShutdownInProgress() throws IllegalStateException
    {
        if (shutdownInProgress.get())
            throw new IllegalStateException(MESSAGE_FAIL_CHECK_SHUTDOWN_IN_PROGRESS);
    }

    /**
     * Activates the concrete provider implementation.
     * 
     * @throws IllegalStateException
     *             if shutdown is in progress
     * @throws IllegalStateException
     *             if this provider is already active
     * @throws NotEnoughResourceException
     *             if there is no enough resources to activate it
     * @throws ProviderException
     *             if cannot activate this provider
     * 
     * @see #isShutdown
     * @see #isActive
     */
    protected abstract void onActivate() throws IllegalStateException, NotEnoughResourceException, ProviderException;

    /**
     * Shuts down the concrete provider implementation.
     * <p>
     * Initiates an orderly shutdown in which previously opened resources will all be closed and no
     * new resources can be requested for this provider.
     * <p>
     * Invocation has no additional effect if shutdown is already in progress but will raise an
     * exception if this provider is inactive. Once inactive it remains inactive until
     * {@link #activate} is invoked again.
     * 
     * @param graceTime
     *            The period allowed for housekeeping before forced shutdown is assumed
     * @param unit
     *            The grace time unit
     * 
     * @throws IllegalArgumentException
     *             if either graceTime or unit are invalid
     * @throws IllegalStateException
     *             if this provider is inactive
     * @throws ProviderException
     *             if cannot shutdown this provider
     * 
     * @see #isShutdown
     * @see #isActive
     */
    protected abstract void onShutdown(final long graceTime, final TimeUnit unit)
            throws IllegalArgumentException, IllegalStateException, ProviderException;
}