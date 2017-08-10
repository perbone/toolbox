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

package io.perbone.toolbox.id;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import io.perbone.toolbox.hash.CRC64;

/**
 * UUID helper class.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public abstract class IdFactory
{
    private static final Random RNG = new SecureRandom();

    private static final Pattern UUID_VALIDATION_PATTERN_NO_HYPHENS = Pattern.compile("[0-9a-fA-F]{32}");
    private static final Pattern UUID_VALIDATION_PATTERN_WITH_HYPHENS = Pattern
            .compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    public static boolean isValidUuid(final String uuid, final boolean withHyphens)
    {
        return uuid == null ? false
                : withHyphens ? UUID_VALIDATION_PATTERN_WITH_HYPHENS.matcher(uuid).matches()
                        : UUID_VALIDATION_PATTERN_NO_HYPHENS.matcher(uuid).matches();
    }

    public static boolean isValidUuid(final String uuid)
    {
        return uuid == null ? false : UUID_VALIDATION_PATTERN_WITH_HYPHENS.matcher(uuid).matches();
    }

    public static String uuid()
    {
        return UUID.randomUUID().toString();
    }

    public static String uuid(final boolean withHyphens)
    {
        return withHyphens ? UUID.randomUUID().toString() : UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static long id()
    {
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder().append(uuid.getLeastSignificantBits()).append(
                uuid.getMostSignificantBits());

        return Math.abs(CRC64.checksum(sb.toString().getBytes()));
    }

    public static long id(final long divisor)
    {
        byte[] bytes = new byte[16];
        RNG.nextBytes(bytes);
        return Math.abs(CRC64.checksum(bytes)) % divisor;
    }
}