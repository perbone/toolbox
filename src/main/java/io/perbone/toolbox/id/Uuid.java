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

import java.util.Arrays;

/**
 * UUID standard implementation.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class Uuid
{
    /** Raw value */
    private final byte id[];

    /**
     * Creates the object based on the given raw value.
     * 
     * @param id
     *            the raw value
     */
    public Uuid(byte[] id)
    {
        this.id = id;
    }

    /**
     * Returns the raw value.
     * 
     * @return the raw value
     */
    public byte[] value()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(id);
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Uuid other = (Uuid) obj;
        if (!Arrays.equals(id, other.id))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Uuid [id=" + Arrays.toString(id) + "]";
    }
}