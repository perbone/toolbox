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
 * Provider interface.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public interface Provider
{
    /**
     * The provider id.
     * <p>
     * This id should to be unique application wise.
     * 
     * @return the provider id
     */
    String id();

    /**
     * Activates this provider.
     * 
     * @return this concrete {@link Provider} implementation
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
    <P extends Provider> P activate() throws IllegalStateException, NotEnoughResourceException, ProviderException;

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
     * @return this concrete {@link Provider} implementation
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
    <P extends Provider> P shutdown(final long graceTime, final TimeUnit unit)
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
     * Opens a new resource instance bound to this provider.
     * 
     * @return a new opened resource object
     * 
     * @throws IllegalStateException
     *             if shutdown is in progress or this provider is inactive
     * @throws IllegalArgumentException
     *             if value is invalid
     * @throws OperationTimeoutException
     *             if the operation is timed out
     * @throws NotEnoughResourceException
     *             if the is not enough resources to complete this operation
     * @throws ProviderException
     *             if an error occurs during this operation
     * 
     */
    Resource openResource() throws IllegalStateException, IllegalArgumentException, OperationTimeoutException,
            NotEnoughResourceException, ProviderException;

    /**
     * Opens a new resource instance bound to this provider.
     * 
     * @param value
     *            some parameter settings to open the resource
     * 
     * @return a new opened resource object
     * 
     * @throws IllegalStateException
     *             if shutdown is in progress or this provider is inactive
     * @throws IllegalArgumentException
     *             if value is invalid
     * @throws OperationTimeoutException
     *             if the operation is timed out
     * @throws NotEnoughResourceException
     *             if the is not enough resources to complete this operation
     * @throws ProviderException
     *             if an error occurs during this operation
     * 
     */
    <T> Resource openResource(T value) throws IllegalStateException, IllegalArgumentException, OperationTimeoutException,
            NotEnoughResourceException, ProviderException;
}