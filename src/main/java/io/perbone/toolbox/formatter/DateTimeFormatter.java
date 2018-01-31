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

package io.perbone.toolbox.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date and time formatter class.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
// FIXME Use Java 8 time API DateTimeFormatter
public final class DateTimeFormatter
{
    // RFC2822 date format
    private static final String PATTERN_RFC2822 = "EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z";

    /**
     * Formats the given date into a string compliant with the standard described at section 3.3 of
     * RFC-2822.
     * 
     * @param date
     *            the date to format
     * 
     * @return a string containing RFC-2822 date and time in English
     * 
     * @see {@link http://tools.ietf.org/html/rfc2822#section-3.3}
     */
    public static String toRFC2822(final Date date)
    {
        return new SimpleDateFormat(PATTERN_RFC2822, Locale.ENGLISH).format(date);
    }

    /**
     * Converts a string date formated with RFC2822 to a {@link Date} object
     * 
     * @param source
     *            the string date to be converted in English
     * 
     * @return the converted {@link Date}
     * 
     * @see {@link http://tools.ietf.org/html/rfc2822#section-3.3}
     */
    public static Date fromRFC2822(final String source)
    {
        Date date = null;
        try
        {
            date = new SimpleDateFormat(PATTERN_RFC2822, Locale.ENGLISH).parse(source);
        }
        catch (ParseException e)
        {
            return null;
        }
        return date;
    }

    /**
     * 
     * @param date
     * @return
     */
    public static String toISO8601(final Date date)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

        TimeZone tz = TimeZone.getTimeZone("UTC");

        df.setTimeZone(tz);

        String output = df.format(date);

        int inset0 = 9;
        int inset1 = 6;

        String s0 = output.substring(0, output.length() - inset0);
        String s1 = output.substring(output.length() - inset1, output.length());

        String result = s0 + s1;

        result = result.replaceAll("UTC", "+00:00");

        return result;
    }

    public static Date fromISO8601(final String source)
    {
        // NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        // things a bit. Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        String dateString;

        // this is zero time so we need to add that TZ indicator for
        if (source.endsWith("Z"))
        {
            dateString = source.substring(0, source.length() - 1) + "GMT-00:00";
        }
        else
        {
            int inset = 6;

            String s0 = source.substring(0, source.length() - inset);
            String s1 = source.substring(source.length() - inset, source.length());

            dateString = s0 + "GMT" + s1;
        }

        try
        {
            return df.parse(dateString);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
}