/*
 * Created on 2005-07-09
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.hottree.template.impl;

import net.hottree.template.ITemplateKey;

/**
 * 
 */
public interface ITemplateKeyCreator extends Cloneable {

	/**
	 * 
	 * @param scanner
	 * @param offset
	 * @param length
	 * @param keyContent
	 * @return
	 */
	public ITemplateKey createKey(IKeyScanner scanner, int offset, int length, String keyContent);

	/**
	 * 
	 * @return
	 */
	public char[] getKeyStartSequence();

	/**
	 * 
	 * @return
	 */
	public char[] getKeyEndSequence();

}
