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

package io.perbone.toolbox.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread Info helper class.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class ThreadInfo
{
    private static final Map<Long, ThreadInfo> requests = new ConcurrentHashMap<Long, ThreadInfo>();

    /**
     * Returns the current {@link ThreadInfo} object bound to the caller thread. A new object will
     * be create if there is none.
     * 
     * @return the current object
     */
    public static ThreadInfo current()
    {
        long id = Thread.currentThread().getId();
        ThreadInfo tinfo = requests.get(id);
        if (tinfo == null)
        {
            tinfo = new ThreadInfo();
            requests.put(id, tinfo);
        }
        return tinfo;
    }

    /**
     * Resets the current {@link ThreadInfo} object bound to the caller thread with a new one.
     * 
     * @return the new created object
     */
    public static ThreadInfo reset()
    {
        ThreadInfo tinfo = new ThreadInfo();
        requests.put(Thread.currentThread().getId(), tinfo);
        return tinfo;
    }

    /**
     * Unbinds (deletes) the current {@link ThreadInfo} object bound to the caller thread.
     */
    public static void unbind()
    {
        requests.remove(Thread.currentThread().getId());
    }

    private final Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();

    private ThreadInfo()
    {
    }

    /**
     * Adds a new property to this {@link ThreadInfo} object.
     * 
     * @param key
     *            the property key
     * @param value
     *            the property value
     * 
     * @return this object
     */
    public ThreadInfo put(final Object key, final Object value)
    {
        map.put(key, value);
        return this;
    }

    /**
     * Returns the property value associated with the given key from this {@link ThreadInfo} object.
     * 
     * @param key
     *            the property key
     * 
     * @return the property value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final Object key)
    {
        return (T) map.get(key);
    }

    /**
     * Removes the property value associated with the given key from this {@link ThreadInfo} object.
     * 
     * @param key
     *            the property key
     * 
     * @return the property value; null with there is none
     */
    public Object remove(final Object key)
    {
        return map.remove(key);
    }

    /**
     * Removes all of properties from this {@link ThreadInfo} object.
     * 
     * @return this object
     */
    public ThreadInfo clear()
    {
        map.clear();
        return this;
    }
}