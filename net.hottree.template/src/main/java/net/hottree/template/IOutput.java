/**
 * 
 */
package net.hottree.template;

/**
 * 
 * @author MW
 *
 */
public interface IOutput {

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
