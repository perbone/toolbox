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

import java.util.concurrent.TimeUnit;

/**
 * Designed to measure the amount of time elapsed from a particular time when activated to when it
 * is deactivated. The precision is nanoseconds but the real observed precision is platform
 * dependent and almost all of them can give good precision on milliseconds based time. Below
 * milliseconds, it has a observed overhead (precision error due function call) that again, is
 * platform dependent.
 * <p>
 * So it's recommend to use this class only on situations when precision is above 1 ms, when the
 * overhead will be insignificant to the hole amount of elapsed time.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public class StopWatch
{
    private static final String MSG_ILLEGAL_STATE_NOT_RUNNING = "StopWatch is not running";

    private static final String MSG_ILLEGAL_STATE_ALREADY_RUNNING = "StopWatch already running";

    private static final String MSG_ILLEGAL_ARGUMENT_FUTURE = "StopWatch cannot count on a future time";

    private static final long INVALIT_TIME = -1;

    private long startTime;

    private long stopTime;

    private boolean running;

    /**
     * 
     */
    public StopWatch()
    {
        reset();
    }

    /**
     * 
     * @param startTime
     */
    public StopWatch(final long startTime, final TimeUnit unit)
    {
        start(startTime, unit);
    }

    /**
     * 
     */
    public StopWatch reset()
    {
        startTime = INVALIT_TIME;
        stopTime = INVALIT_TIME;
        running = false;

        return this;
    }

    /**
     * 
     */
    public StopWatch start()
    {
        return start(System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    /**
     * 
     * @param startTime
     */
    public StopWatch start(final long startTime, final TimeUnit unit)
    {
        if (running)
            throw new IllegalStateException(MSG_ILLEGAL_STATE_ALREADY_RUNNING);
        else if (startTime > System.nanoTime())
            throw new IllegalArgumentException(MSG_ILLEGAL_ARGUMENT_FUTURE);
        this.startTime = TimeUnit.NANOSECONDS.convert(startTime, unit);
        stopTime = INVALIT_TIME;
        running = true;
        return this;
    }

    public long startTime()
    {
        return startTime;
    }

    /**
     * 
     */
    public StopWatch stop()
    {
        long now = System.nanoTime(); // have to be the first operation just to be precise

        if (running)
        {
            stopTime = now;
            running = false;
        }
        else
        {
            throw new IllegalStateException(MSG_ILLEGAL_STATE_NOT_RUNNING);
        }

        return this;
    }

    /**
     * Elapsed time in nanoseconds (raw time unit)
     * 
     * @return Current value in nanoseconds representing the elapsed time between start and stop
     */
    public long elapsedTime()
    {
        long now = System.nanoTime(); // have to be the first operation just to be precise

        if (running)
        {
            return now - startTime;
        }
        else if (startTime != INVALIT_TIME && stopTime != INVALIT_TIME)
        {
            return stopTime - startTime;
        }
        else
            throw new IllegalStateException(MSG_ILLEGAL_STATE_NOT_RUNNING);
    }

    /**
     * Elapsed time in milliseconds (default time unit)
     * 
     * @return Current value in milliseconds representing the elapsed time between start and stop
     */
    public long elapsedMillisTime()
    {
        long now = System.nanoTime(); // have to be the first operation just to be precise

        if (running)
        {
            return TimeUnit.MILLISECONDS.convert(now - startTime, TimeUnit.NANOSECONDS);
        }
        else if (startTime != INVALIT_TIME && stopTime != INVALIT_TIME)
        {
            return TimeUnit.MILLISECONDS.convert(stopTime - startTime, TimeUnit.NANOSECONDS);
        }
        else
            throw new IllegalStateException(MSG_ILLEGAL_STATE_NOT_RUNNING);
    }

    /**
     * Elapsed time in milliseconds
     * 
     * @param unit
     *            TimeUnit to convert before returning the current value
     * @return Current value in <code>unit</code> units representing the elapsed time between start
     *         and stop
     */
    public long elapsedTime(final TimeUnit unit)
    {
        long now = System.nanoTime(); // have to be the first operation just to be precise

        if (running)
        {
            return unit.convert(now - startTime, TimeUnit.NANOSECONDS);
        }
        else if (startTime != INVALIT_TIME && stopTime != INVALIT_TIME)
        {
            return unit.convert(stopTime - startTime, TimeUnit.NANOSECONDS);
        }
        else
            throw new IllegalStateException(MSG_ILLEGAL_STATE_NOT_RUNNING);
    }

    @Override
    public String toString()
    {
        return "" + elapsedMillisTime();
    }
}