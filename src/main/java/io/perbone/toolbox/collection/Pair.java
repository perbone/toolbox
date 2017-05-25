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

package io.perbone.toolbox.collection;

/**
 * This class couples together a pair of values, which may be of different types (F and S). The
 * individual values can be accessed through the public mutator methods {@code first} and
 * {@code second}.
 * <p>
 * An {@code Pair} object doesn't convey any semantics about the relationship between the two
 * values. It just expresses a minor property of the data, that it splits into two parts. It is best
 * suited as a wrapper for a pair of values with no easily-describable relationship other than being
 * paired.
 * <p>
 * For a more meaningful semantics it is suggested to extend this {@code Pair} class with a more
 * powerful name and field types.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public final class Pair<F extends Object, S extends Object>
{
    private F first;
    private S second;

    /**
     * Creates an {@code Pair} object initialized with {@code null} for both first and second field
     * members.
     */
    public Pair()
    {
        this.first = null;
        this.second = null;
    }

    /**
     * Creates an {@code Pair} object initialized with informed values for both first and second
     * field members.
     * 
     * @param first
     *            The first field member payload value
     * @param second
     *            The second field member payload value
     */
    public Pair(final F first, final S second)
    {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first field member payload value of this {@code Pair}.
     * 
     * @return The first value
     */
    public F first()
    {
        return first;
    }

    /**
     * Sets the first field member payload value of this {@code Pair}.
     * 
     * @param first
     *            The first value
     */
    public void first(final F first)
    {
        this.first = first;
    }

    /**
     * Returns the second field member payload value of this {@code Pair}.
     * 
     * @return The second value
     */
    public S second()
    {
        return second;
    }

    /**
     * Sets the second field member payload value of this {@code Pair}.
     * 
     * @param second
     *            The second value
     */
    public void second(final S second)
    {
        this.second = second;
    }

    @Override
    public int hashCode()
    {
        int hash = (first == null ? 0 : first.hashCode() * 31) + (second == null ? 0 : second.hashCode());
        return hash;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (o == null)
            return false;
        else if (o == this)
            return true;
        else if (!(o instanceof Pair<?, ?>))
            return false;

        Pair<?, ?> p = (Pair<?, ?>) o;

        return (first == null ? p.first == null : first.equals(p.first))
                && (second == null ? p.second == null : second.equals(p.second));
    }

    @Override
    public String toString()
    {
        return String.format("first=%s; second=%s", first.toString(), second.toString());
    }
}