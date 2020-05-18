/*******************************************************************************
 * Copyright (c) 2008, 2010 HotTree Sp. z o.o.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Magik Development Tools User Agreement
 * which accompanies this distribution, and is available at
 * http://www.mdt-project.com/license
 *******************************************************************************/
package net.hottree.template.impl;

/**
 * 
 */
public class ThrowUtil {

	/**
	 * 
	 * @param name
	 */
	public static void nullArg(String name) {
		throw new IllegalArgumentException(name);
	}

	/**
	 * 
	 * @param name
	 */
	public static void err(String name) {
		throw new RuntimeException(name);
	}

}
