/**
 * 
 */
package net.hottree.template;

/**
 * 
 */
public interface ITemplateBuffer {

	/**
	 * 
	 * @param index
	 * @return
	 */
	char charAt(int index);

	/**
	 * 
	 * @return
	 */
	int length();
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	String substring(int start, int end);

	/**
	 * 
	 * @param start
	 * @return
	 */
	String substring(int start);

	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	ITemplateBuffer subbuffer(int start, int end);

	/**
	 * 
	 * @param writer
	 * @param offset
	 * @param length
	 */
	void write(IWriter writer, int offset, int length);

}
