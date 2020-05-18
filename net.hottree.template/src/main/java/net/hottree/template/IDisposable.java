/**
 * 
 */
package net.hottree.template;

/**
 * This is implemented by objects that need to be disposed after they are no longer
 * needed.
 */
public interface IDisposable {

	/**
	 * This is called to dispose the object.
	 */
	public void dispose();

}
