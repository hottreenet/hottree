/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.ITemplateBuffer;
import net.hottree.template.ITemplateSource;

/**
 * @author MW
 * 
 */
public  class FastTemplateSource implements ITemplateSource {

	private TemplateBuffer buffer;

	/**
	 * 
	 * @param buffer
	 */
	public FastTemplateSource(TemplateBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public String getLocation() {
		return buffer.getLocation();
	}

	@Override
	public ITemplateBuffer getBuffer() {
		return buffer;
	}

	@Override
	public void dispose() {
		buffer = null;
	}

}
