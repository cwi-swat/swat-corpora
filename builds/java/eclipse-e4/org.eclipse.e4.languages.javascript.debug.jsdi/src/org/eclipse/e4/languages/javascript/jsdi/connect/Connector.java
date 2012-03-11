/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.jsdi.connect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Describes an extensible connector for JSDI
 * 
 * @since 0.9
 */
public interface Connector {
	/**
	 * Default description of an argument that this connector can support
	 */
	public interface Argument extends Serializable {
		/**
		 * A human readable description of the argument
		 * 
		 * @return the description of the argument
		 */
		public String description();

		/**
		 * The simple label for this argument
		 * 
		 * @return the label for this argument
		 */
		public String label();

		/**
		 * Returns the if this argument must be specified or not
		 * 
		 * @return true if this argument is required, false otherwise
		 */
		public boolean mustSpecify();

		/**
		 * The name of the argument
		 * 
		 * @return the name or the argument
		 */
		public String name();

		/**
		 * Returns if the given value is valid with-respect-to this {@link Argument}
		 * 
		 * @param value
		 * @return true if the argument is valid false otherwise
		 */
		public boolean isValid(String value);

		/**
		 * Sets the value of the argument to be the given value
		 * 
		 * @param value
		 */
		public void setValue(String value);

		/**
		 * Returns the value of the argument
		 * 
		 * @return the string value of the argument
		 */
		public String value();
	}

	/**
	 * Specialization of an {@link Argument} for handling integers
	 * 
	 * @see Argument
	 */
	public interface IntegerArgument extends Connector.Argument {
		/**
		 * Returns the integer value of the argument
		 * 
		 * @return the integer value of the argument
		 */
		public int intValue();

		/**
		 * If the integer value if valid with-respect-to this argument
		 * 
		 * @param intValue
		 *            the value to test
		 * @return true if the given value if valid, false otherwise
		 */
		public boolean isValid(int intValue);

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.e4.languages.javascript.jsdi.connect.Connector.Argument#isValid(java.lang.String)
		 */
		public boolean isValid(String value);

		/**
		 * Returns the maximum value that this argument will accept as being valid
		 * 
		 * @return the maximum value for this argument
		 */
		public int max();

		/**
		 * Returns the minimum value that this argument will accept as being valid
		 * 
		 * @return the minimum value for this argument
		 */
		public int min();

		/**
		 * Sets the integer value of this argument
		 * 
		 * @param intValue
		 *            the new value to set
		 */
		public void setValue(int intValue);
	}

	/**
	 * Boolean specialization of an {@link Argument}
	 * 
	 * @see Argument
	 */
	public interface BooleanArgument extends Connector.Argument {
		/**
		 * Returns the boolean value for this argument
		 * 
		 * @return the boolean value
		 */
		public boolean booleanValue();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.e4.languages.javascript.jsdi.connect.Connector.Argument#isValid(java.lang.String)
		 */
		public boolean isValid(String value);

		/**
		 * Sets the new boolean value for this argument
		 * 
		 * @param booleanValue
		 *            the new value
		 */
		public void setValue(boolean booleanValue);
	}

	/**
	 * String specialization of an {@link Argument}
	 * 
	 * @see Argument
	 */
	public interface StringArgument extends Connector.Argument {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.e4.languages.javascript.jsdi.connect.Connector.Argument#isValid(java.lang.String)
		 */
		public boolean isValid(String arg1);
	}

	/**
	 * Specialization of {@link Argument} whose value is a String selected from a list of choices.
	 */
	public interface SelectedArgument extends Connector.Argument {
		/**
		 * The list of choices for this connector
		 * 
		 * @return the complete list of choices for ths connector
		 */
		public List choices();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.e4.languages.javascript.jsdi.connect.Connector.Argument#isValid(java.lang.String)
		 */
		public boolean isValid(String value);
	}

	/**
	 * Returns the map of default arguments for this connector
	 * 
	 * @return the map of default arguments
	 */
	public Map defaultArguments();

	/**
	 * A human readable description of this connector
	 * 
	 * @return the description of this connector
	 */
	public String description();

	/**
	 * The name of this connector
	 * 
	 * @return the name of the connector
	 */
	public String name();

	/**
	 * The unique identifier for this connector
	 * 
	 * @return the identifier of this connector
	 */
	public String id();
}
