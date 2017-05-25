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

package io.perbone.toolbox.time;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The <code>TimerQueue</code> class contains an ordered list of the timers instances.
 * <p>
 * Timer instances are ordered by the the expiryTime with lowest expiry time in the front.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public class TimerQueue
{
    private class Timer
    {
        public final long delay;
        public final Object context;
        public final long expiryTime;

        public Timer(long delay, Object context)
        {
            this.delay = delay;
            this.context = context;
            this.expiryTime = System.currentTimeMillis() + delay;
        }
    }

    private class TimerComparator implements Comparator<Timer>
    {
        @Override
        public int compare(Timer t1, Timer t2)
        {
            if (t1.delay < t2.delay)
                return -1;
            if (t1.delay > t2.delay)
                return 1;
            else
                return 0;
        }
    }

    private Queue<Timer> timers = new PriorityBlockingQueue<Timer>(10, new TimerComparator());

    /**
     * Adds a timer to the queue.
     * 
     * @param delay
     *            The time delay from now when to expire the timer
     * @param unit
     *            The time unit for the delay value
     * @param context
     *            The context object to be passed back at expire time; may be <tt>null</tt>
     */
    public void add(final long delay, final TimeUnit unit, final Object context)
    {
        if (delay < 0)
            throw new IllegalArgumentException("Cannot add an expired time to the queue");
        else if (unit == null)
            throw new IllegalArgumentException("TimeUnit cannot be null");

        Timer t = new Timer(TimeUnit.MILLISECONDS.convert(delay, unit), context);
        timers.add(t);
    }

    /**
     * Return the delay, in milliseconds, from now to the time of the first (head) timer in the
     * queue.
     * 
     * @return
     *         <li>Positive value - delay from now to the expire time of the first timer in the
     *         queue
     *         <li>Value of 0 denotes that queue is empty
     *         <li>Negative value denotes that elements expire time has passed.
     */
    public long getFirstDelay()
    {
        if (timers.isEmpty())
            return 0;
        long firstDelay = timers.peek().expiryTime - System.currentTimeMillis();
        return (0 != firstDelay) ? firstDelay : -1;
    }

    /**
     * Returns expired timers
     * <p>
     * If a timer operation was cancelled by that time it will be discarded.
     * 
     * @return collection of timers that expired by now.
     */
    public List<Object> getExpiredTimers()
    {
        List<Object> et = new ArrayList<Object>();
        long now = System.currentTimeMillis();

        for (Iterator<Timer> i = timers.iterator(); i.hasNext();)
        {
            Timer t = i.next();

            if (t.expiryTime <= now)
            {
                i.remove();
                if (t.context != null)
                    et.add(t.context);
            }
        }

        return et;
    }
}