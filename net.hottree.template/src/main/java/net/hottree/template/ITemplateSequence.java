package net.hottree.template;

import net.hottree.template.impl.TemplateKeyNotFoundException;

/**
 * 
 * @author MW
 *
 */
public interface ITemplateSequence extends IOutput, Cloneable {

	/**
	 * 
	 * @param object
	 * @throws TemplateKeyNotFoundException
	 */
	public void replace(Object object) throws TemplateKeyNotFoundException;

	/**
	 * 
	 */
	public void newLoop();

	/**
	 * 
	 * @param nLoops
	 */
	public void ensureLoops(int nLoops);

	/**
	 * 
	 * @return
	 */
	public int getKeyCount();

	/**
	 * 
	 * @return
	 */
	public Object clone();

}
