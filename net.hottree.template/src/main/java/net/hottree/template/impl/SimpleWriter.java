/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.IWriter;

/**
 * @author MW
 * 
 */
public class SimpleWriter implements IWriter, Cloneable {

	private StringBuilder buffer;

	/**
	 * 
	 * 
	 */
	public SimpleWriter() {
		this(50);
	}

	/**
	 * 
	 * @param initialCapacity
	 */
	public SimpleWriter(int initialCapacity) {
		buffer = new StringBuilder(initialCapacity);
	}

	@Override
	public void write(String str) {
		buffer.append(str);

	}

	@Override
	public String toString() {
		return buffer.toString();
	}

	/**
	 * 
	 * @return
	 */
	public int length() {
		return buffer.length();
	}

	/**
	 * 
	 * @return
	 */
	public StringBuilder getBuffer() {
		return buffer;
	}

	@Override
	public void write(char[] str, int offset, int len) {
		buffer.append(str, offset, len);
	}

	@Override
	public Object clone() {
		try {
			SimpleWriter clone = (SimpleWriter) super.clone();
			clone.buffer = new StringBuilder(this.buffer.length());
			clone.buffer.append(this.buffer);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
