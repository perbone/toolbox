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

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class provides a skeletal implementation of the {@code ProviderFactory} interface, to
 * minimize the effort required to implement this interface.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public abstract class AbstractProviderFactory<P extends Provider> implements ProviderFactory<P>
{
    protected String MESSAGE_FAIL_CANNOT_ACTIVATE = "Cannot activate this factory";

    protected String MESSAGE_FAIL_CANNOT_SHUTDOWN = "Cannot shutdown this factory";

    protected String MESSAGE_FAIL_ALREADY_ACTIVE = "Attempt to activate already active factory object";

    protected String MESSAGE_FAIL_CHECK_ACTIVE = "Attempt to use non-active factory object";

    protected String MESSAGE_ABORT_OPERATION = "Aborting execution; this factory was deactivated";

    protected String MESSAGE_FAIL_CHECK_SHUTDOWN_IN_PROGRESS = "Illegal invocation; shutdown is already in progress";

    protected String MESSAGE_INVALID_ARGUMENT_GRACE_TIME = "Invalid grace time value; cannot be negative";

    protected String MESSAGE_INVALID_ARGUMENT_UNIT = "Invalid unit value; cannot be null";

    protected String MESSAGE_WARN_ALREADY_RUNNING = "Skipped invocation; this provider execution is already running";

    protected String MESSAGE_FAIL_UNSUPPORTED_OPERATION = "Factory feature not supported by this implementation";

    protected final AtomicBoolean active = new AtomicBoolean(false);

    protected final AtomicBoolean shutdownInProgress = new AtomicBoolean(false);

    @SuppressWarnings("unchecked")
    protected final Class<P> type = (Class<P>) ((ParameterizedType) this.getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];

    protected String factoryId = this.getClass().getName();

    public AbstractProviderFactory()
    {
        // do nothing
    }

    @Override
    public Class<P> getType()
    {
        return type;
    }

    @Override
    public String id()
    {
        return factoryId;
    }

    @Override
    public <F extends ProviderFactory<P>> F loadDefaultSettings() throws IllegalStateException, ProviderException
    {
        throw new UnsupportedOperationException(MESSAGE_FAIL_UNSUPPORTED_OPERATION);
    }

    @Override
    public <F extends ProviderFactory<P>> F loadSettings(String path)
            throws IllegalStateException, IllegalArgumentException, ProviderException
    {
        throw new UnsupportedOperationException(MESSAGE_FAIL_UNSUPPORTED_OPERATION);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ProviderFactory<P>> F activate()
            throws IllegalStateException, NotEnoughResourceException, ProviderException
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
                active.set(false); // Abort activation
                throw e;
            }
            catch (final Exception e)
            {
                active.set(false); // Abort activation
                throw new ProviderException(e);
            }
        }
        else
        {
            throw new IllegalStateException(MESSAGE_FAIL_ALREADY_ACTIVE);
        }

        return (F) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends ProviderFactory<P>> F shutdown(long graceTime, TimeUnit unit)
            throws IllegalStateException, IllegalArgumentException, ProviderException
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

        return (F) this;
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
    public P create()
            throws IllegalStateException, OperationTimeoutException, NotEnoughResourceException, ProviderException
    {
        throw new UnsupportedOperationException(MESSAGE_FAIL_UNSUPPORTED_OPERATION);
    }

    @Override
    public <T> P create(T value)
            throws IllegalStateException, OperationTimeoutException, NotEnoughResourceException, ProviderException
    {
        throw new UnsupportedOperationException(MESSAGE_FAIL_UNSUPPORTED_OPERATION);
    }

    @Override
    public void destroy(P provider) throws IllegalStateException, IllegalArgumentException, ProviderException
    {
        throw new UnsupportedOperationException(MESSAGE_FAIL_UNSUPPORTED_OPERATION);
    }

    @Override
    public void destroy(P provider, long graceTime, TimeUnit unit)
            throws IllegalStateException, IllegalArgumentException, ProviderException
    {
        throw new UnsupportedOperationException(MESSAGE_FAIL_UNSUPPORTED_OPERATION);
    }

    /**
     * Checks if this factory is currently inactive.
     * 
     * @throws IllegalStateException
     *             if it is active
     */
    protected void checkInactive() throws IllegalStateException
    {
        if (active.get())
            throw new IllegalStateException("Illegal invocation; this factory is already active");
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
     * Checks if this factory execution was aborted.
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
     * Activates the concrete factory implementation.
     * 
     * @throws IllegalStateException
     *             if shutdown is in progress
     * @throws IllegalStateException
     *             if this provider is already active
     * @throws NotEnoughResourceException
     *             if there is no enough resources to activate it
     * @throws ProviderException
     *             if cannot activate this factory
     * 
     * @see #isShutdown
     * @see #isActive
     */
    protected abstract void onActivate() throws IllegalStateException, NotEnoughResourceException, ProviderException;

    /**
     * Shuts down the concrete factory implementation.
     * <p>
     * Initiates an orderly shutdown in which previously opened providers will all be closed and no
     * new providers can be requested from this factory.
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
     *             if cannot shutdown this factory
     * 
     * @see #isShutdown
     * @see #isActive
     */
    protected abstract void onShutdown(final long graceTime, final TimeUnit unit)
            throws IllegalArgumentException, IllegalStateException, ProviderException;
}
