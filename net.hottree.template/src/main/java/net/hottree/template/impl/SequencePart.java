/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.ITemplateBuffer;
import net.hottree.template.IWriter;

/**
 * @author MW
 * 
 */
public class SequencePart implements ISequencePart {

	private final ITemplateBuffer buffer;

	private final int offset;

	private final int length;

	/**
	 * 
	 * @param buffer
	 * @param start
	 * @param end
	 */
	public SequencePart(ITemplateBuffer buffer, int offset, int length) {
		this.buffer = buffer;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public void write(IWriter writer) {
		buffer.write(writer, offset, length);
	}

	@Override
	public int getLength() {
		return length;
	}

}
