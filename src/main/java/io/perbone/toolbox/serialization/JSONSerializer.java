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

package io.perbone.toolbox.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.rits.cloning.Cloner;

/**
 * Serializer implementation for JSON format.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public class JSONSerializer implements Serializer
{
    private static final Gson gson = new Gson();

    private static final Cloner cloner = new Cloner();

    @Override
    public byte[] deflate(final Object object) throws IllegalArgumentException, SerializationException
    {
        byte[] bytes = gson.toJson(object).getBytes();

        return bytes;
    }

    @Override
    public <T> T inflate(final Class<T> type, final byte[] data)
            throws IllegalArgumentException, IllegalStateException, SerializationException
    {
        T result = null;

        try
        {
            final String raw = new String(data);
            result = gson.fromJson(raw, type);
        }
        catch (JsonParseException e)
        {
            // do nothing
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T clone(final Object object) throws IllegalArgumentException, SerializationException
    {
        return (T) cloner.deepClone(object);
    }
}
