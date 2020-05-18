/**
 * 
 */
package net.hottree.template;

/**
 * @author MW
 * 
 */
public interface ITemplateSource extends Cloneable, IDisposable {

	/**
	 * 
	 * @return
	 */
	public String getLocation();

	/**
	 * 
	 * @return
	 */
	public ITemplateBuffer getBuffer();

}
