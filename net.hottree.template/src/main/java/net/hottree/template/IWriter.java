/**
 * 
 */
package net.hottree.template;

/**
 * 
 * @author MW
 *
 */
public interface IWriter {

	/**
	 * 
	 * @param s
	 */
	public void write(String s);

	/**
	 * 
	 * @param s
	 * @param off
	 * @param len
	 */
	public void write(char s[], int off, int len);

}
