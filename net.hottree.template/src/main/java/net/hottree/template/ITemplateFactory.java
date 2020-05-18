/**
 * 
 */
package net.hottree.template;

/**
 * @author MW
 * 
 */
public interface ITemplateFactory {

	/**
	 * 
	 * @param content
	 * @return
	 */
	public ITemplate createTemplate(String content);

}
