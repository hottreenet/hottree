/*
 * Created on 2005-07-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.hottree.template.impl;

import net.hottree.template.ITemplateKey;

/**
 * 
 */
public class BasicTemplateKeyCreator implements ITemplateKeyCreator {

	private final char[] startSequence;

	private final char[] endSequence;

	/**
	 * 
	 * @param startSequence
	 * @param endSequence
	 */
	public BasicTemplateKeyCreator(String startSequence, String endSequence) {
		this(startSequence.toCharArray(), endSequence.toCharArray());
	}

	/**
	 * 
	 * @param keyType
	 * @param startSequence
	 * @param endSequence
	 */
	public BasicTemplateKeyCreator(char[] startSequence, char[] endSequence) {
		this.startSequence = startSequence;
		this.endSequence = endSequence;
	}

	@Override
	public synchronized ITemplateKey createKey(IKeyScanner template, int offset, int length, String keyContent) {
			return new TemplateKey(template, offset, length, keyContent,0);
	}

	@Override
	public char[] getKeyStartSequence() {
		return startSequence;
	}

	@Override
	public char[] getKeyEndSequence() {
		return endSequence;
	}

}
