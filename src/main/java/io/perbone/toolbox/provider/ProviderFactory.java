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

import java.util.concurrent.TimeUnit;

/**
 * Provider factory interface.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public interface ProviderFactory<P extends Provider>
{
    /**
     * Returns this interface parameterized type.
     * 
     * @return the parameterized type
     */
    Class<P> getType();

    /**
     * The provider factory id.
     * <p>
     * This id should to be unique application wise.
     * 
     * @return the provider factory id; null if this factory is not active
     */
    String id();

    /**
     * Loads the settings from all default stores.
     * 
     * @return this instance
     * 
     * @throws IllegalStateException
     *             if either shutdown is in progress or this factory is inactive
     * @throws ProviderException
     *             if an error occurs during this operation
     */
    <F extends ProviderFactory<P>> F loadDefaultSettings() throws IllegalStateException, ProviderException;

    /**
     * Loads the settings from the given store.
     * 
     * @param path
     *            the backing store path
     * 
     * @return this instance
     * 
     * @throws IllegalStateException
     *             if either shutdown is in progress or this factory is inactive
     * @throws IllegalArgumentException
     *             if the path is invalid
     * @throws ProviderException
     *             if an error occurs during this operation
     */
    <F extends ProviderFactory<P>> F loadSettings(final String path)
            throws IllegalStateException, IllegalArgumentException, ProviderException;

    /**
     * Activates this provider.
     * 
     * @return this concrete {@link ProviderFactory} implementation
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
     * @see #isShutdownInProgress
     * @see #isActive
     */
    <F extends ProviderFactory<P>> F activate()
            throws IllegalStateException, NotEnoughResourceException, ProviderException;

    /**
     * Shuts down this provider.
     * <p>
     * Initiates an orderly shutdown in which previously opened data sources will all be closed and
     * no new data sources can be requested for this provider.
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
     * @return this concrete {@link ProviderFactory} implementation
     * 
     * @throws IllegalStateException
     *             if this provider is inactive
     * @throws IllegalArgumentException
     *             if either graceTime or unit are invalid
     * @throws ProviderException
     *             if cannot shutdown this provider
     * 
     * @see #isShutdownInProgress
     * @see #isActive
     */
    <F extends ProviderFactory<P>> F shutdown(final long graceTime, final TimeUnit unit)
            throws IllegalStateException, IllegalArgumentException, ProviderException;

    /**
     * Returns the status of the shutdown process.
     * 
     * @return <tt>true</tt> if the shutdown is in progress; <tt>false</tt> otherwise
     * 
     * @see #isActive
     */
    boolean isShutdownInProgress();

    /**
     * Tells whether or not this provider is active.
     * <p>
     * It is assumed that after a successful object instantiation this method will return
     * <tt>true</tt>. Conversely for fail object instantiation this method should return
     * <tt>false</tt> despite the fact that this object may still be valid.
     * <p>
     * For an active provider asked for shutdown, it will return <tt>true</tt> until
     * {@link #isShutdownInProgress} returns <tt>true</tt>; after that it will returns
     * <tt>false</tt>.
     * 
     * @return <tt>true</tt> if it is active; <tt>false</tt> otherwise
     * 
     * @see #isShutdownInProgress
     */
    boolean isActive();

    /**
     * Create a new provider instance.
     * 
     * @return the new provider instance
     * 
     * @throws IllegalStateException
     *             if this factory is not active
     * @throws IllegalStateException
     *             if shutdown is in progress
     * @throws OperationTimeoutException
     *             if the operation is timed out
     * @throws NotEnoughResourceException
     *             if the is not enough resources to complete this operation
     * @throws ProviderException
     *             if an error occurs during this operation
     */
    P create() throws IllegalStateException, OperationTimeoutException, NotEnoughResourceException, ProviderException;

    /**
     * Create a new provider instance.
     * 
     * @param value
     *            some parameter settings for the provider creation
     * 
     * @return the new provider instance
     * 
     * @throws IllegalStateException
     *             if this factory is not active
     * @throws IllegalStateException
     *             if shutdown is in progress
     * @throws OperationTimeoutException
     *             if the operation is timed out
     * @throws NotEnoughResourceException
     *             if the is not enough resources to complete this operation
     * @throws ProviderException
     *             if an error occurs during this operation
     */
    <T> P create(T value)
            throws IllegalStateException, OperationTimeoutException, NotEnoughResourceException, ProviderException;

    /**
     * Destroy the given provider instance.
     * 
     * @param provider
     *            the provider to destroy
     * 
     * @throws IllegalStateException
     *             if this factory is not active
     * @throws IllegalStateException
     *             if shutdown is in progress
     * @throws IllegalArgumentException
     *             if the given provider is invalid
     * @throws ProviderException
     *             if cannot destroy the given provider
     */
    void destroy(final P provider) throws IllegalStateException, IllegalArgumentException, ProviderException;

    /**
     * Destroy the given provider instance.
     * 
     * @param provider
     *            the provider to destroy
     * @param graceTime
     *            The period allowed for housekeeping
     * @param unit
     *            The grace time unit
     * 
     * @throws IllegalStateException
     *             if this factory is not active
     * @throws IllegalStateException
     *             if shutdown is in progress
     * @throws IllegalArgumentException
     *             if the given provider is invalid
     * @throws ProviderException
     *             if cannot destroy the given provider
     */
    void destroy(final P provider, final long graceTime, final TimeUnit unit)
            throws IllegalStateException, IllegalArgumentException, ProviderException;
}