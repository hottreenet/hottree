/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.ITemplateKey;
import net.hottree.template.ITemplateSource;

/**
 * 
 */
public interface IKeyScanner {

	/**
	 * 
	 * @return
	 */
	public ITemplateSource getSource();

	/**
	 * 
	 * @return
	 */
	public ITemplateKeyCreator getKeyCreator();

	/**
	 * 
	 * @param source
	 * @return
	 */
	public ITemplateKey[] scann(ITemplateSource source);

}
