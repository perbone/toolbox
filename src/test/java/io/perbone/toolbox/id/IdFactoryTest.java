/*
 * This file is part of AAA
 * https://github.com/perbone/AAA/
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

package io.perbone.toolbox.id;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import io.perbone.toolbox.time.StopWatch;

/**
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public class IdFactoryTest
{
    private static final long divisor = 1000000000L;

    @Before
    public void before()
    {
        for (int i = 0; i < 10; i++)
        {
            IdFactory.id();
            IdFactory.id(divisor);
        }
    }

    @Test
    public void generateId()
    {
        StopWatch sw = new StopWatch().start();
        for (int i = 0; i < 10; i++)
        {
            IdFactory.id(divisor);
        }
        System.out.println(String.format("%.3fms", sw.elapsedTime() / 1000000F));
    }

    @Test
    public void hashCollision()
    {
        final long divisor = 999999999999999L; // 15 digitis
        final int size = 10000000; // 10 millions
        int count = 0;
        Set<Long> table = new HashSet<>(size);
        for (int i = 0; i < size; i++)
        {
            Long id = IdFactory.id(divisor);
            if (table.contains(id))
            {
                System.out.println("Hash collision for " + id);
                count++;
                continue;
            }

            table.add(id);
        }

        System.out.println("hash collision count " + count);
    }
}
