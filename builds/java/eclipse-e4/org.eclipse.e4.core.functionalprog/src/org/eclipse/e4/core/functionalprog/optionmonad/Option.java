/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.core.functionalprog.optionmonad;

/**
 * A Java implementation of the "Option Monad" design pattern.  (Note that this
 * particular implementation isn't actually a full monad, but for our purposes
 * it doesn't matter.)  This is inspired by the following blog:
 * http://www.codecommit.com/blog/scala/the-option-pattern and also 
 * Functional Java (http://www.functionaljava.org)'s Option implementation.
 * <p>
 * The Option pattern provides a unified method for coding functions that could
 * return Some<T>(value) or also might fail to produce a value, and thus return
 * None<T>.
 * <p>
 * This pattern makes explicit that a method might not return a value, eliminating
 * guesswork about if the method might return null when reading/learning APIs.
 *
 * @param <T> The type the Option encapsulates
 */
public interface Option<T> {
    /**
     * Return the value inside the Option, or throw an UnsupportedOperationException
     * if there is no value.
     * 
     * @return the encapsulated T or throw UnsupportedOperationException if empty
     */
    public T get();
    
    /**
     * Return the encapsulated instance of T, if there is one, or alternatively
     * return the defaultValue if there is no encapsulated T.
     * 
     * @param defaultValue The default value to return if there is no T in the 
     * container.
     * 
     * @return the encapsulated instance of T, if there is one, or alternatively
     * return the defaultValue if there is no encapsulated T.
     */
    public T getOrSubstitute(T defaultValue);
    
    /**
     * Return the encapsulated instance of T, if there is one.  If the 
     * container is empty, throw the passed exception.
     * 
     * @param <E> The type of exception to throw.
     * @param exception The exception to throw.
     * @return the encapsulated instance of T, if there is one.  If the 
     * container is empty, throw the passed exception.
     * @throws E The exception type that could be thrown.
     */
    public <E extends Throwable> T getOrThrow(E exception) throws E;
    
    /**
     * Return true if this Option contains a value or false if it is empty.
     * 
     * @return true if this Option contains a value or false if it is empty.
     */
    public boolean hasValue();
}
 
