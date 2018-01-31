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

import java.nio.ByteBuffer;

/**
 * Serializes objects to and from a {@link ByteBuffer}.
 * 
 * @author Paulo Perbone <pauloperbone@yahoo.com>
 * @since 0.1.0
 */
public interface Serializer
{
    /**
     * Serializes an object instance into a byte stream representation.
     * <p>
     * The interface is schema agnostic so it is up to the implementations to define its on kind of
     * meta-info inside the byte stream.
     * <p>
     * The interface user is responsible to match the implementations for both deflate and inflate
     * operations.
     * 
     * @param object
     *            The object instance to be serialized
     * 
     * @return the byte stream of the object
     * 
     * @throws IllegalArgumentException
     *             if the object is invalid
     * @throws SerializationException
     *             if the byte stream could not be generated
     */
    byte[] deflate(Object object) throws IllegalArgumentException, SerializationException;

    /**
     * Deserializes the byte stream into the object class type.
     * <p>
     * As if deflate method this interface is schema agnostic so it is up to the implementations to
     * define its on kind of meta-info inside the byte stream.
     * <p>
     * Again as if deflate method the interface user is responsible to match the implementations for
     * both deflate and inflate operations.
     * 
     * @param type
     *            The class type for the object to be inflated with the byte stream
     * @param data
     *            The object byte stream
     * 
     * @return An object instance inflated with the byte stream
     * 
     * @throws IllegalArgumentException
     *             if the type is invalid
     * @throws IllegalArgumentException
     *             if the byte stream is invalid
     * @throws IllegalStateException
     *             if the data byte stream is inconsistent
     * @throws SerializationException
     *             if the object instance could not be constructed
     */
    <T> T inflate(Class<T> type, byte[] data)
            throws IllegalArgumentException, IllegalStateException, SerializationException;

    /**
     * Clones the given object into a new the object instance.
     * 
     * @param object
     *            The object instance to be cloned
     * 
     * @return a new object instance
     * 
     * @throws IllegalArgumentException
     *             if the object to be cloned is invalid
     * @throws SerializationException
     *             if the object instance could not be constructed
     */
    <T> T clone(Object object) throws IllegalArgumentException, SerializationException;
}