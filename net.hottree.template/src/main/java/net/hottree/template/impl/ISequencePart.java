/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.IWriter;

/**
 * 
 * 
 *
 */
public interface ISequencePart {

	/**
	 * 
	 * @param writer
	 */
	public void write(IWriter writer);

	/**
	 * 
	 * @return
	 */
	public int getLength();

}
