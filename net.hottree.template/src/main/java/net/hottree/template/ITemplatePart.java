/**
 * 
 */
package net.hottree.template;

/**
 * @author MW
 * 
 */
public interface ITemplatePart extends Cloneable {

	public static final int KEY_PART = -9;

	public static final int DATA_PART = -10;

	public static final String REMOVED = "@&#_@$!";

	/**
	 * 
	 * @return
	 */
	public int getPartType();

	/**
	 * 
	 * @return
	 */
	public boolean hasData();

	/**
	 * 
	 * @return
	 */
	public boolean isRemoved();

	/**
	 * 
	 *
	 */
	public void setRemoved();

	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @return
	 */
	public int getOffset();

	/**
	 * D³ugoœæ tekstu który zostanie wstawiony
	 * 
	 * @return
	 */
	public int getInsertedLength();

	/**
	 * D³ugoœæ tekstu który zostanie zast¹piony (liczony od offsetu)
	 * 
	 * @return
	 */
	public int getReplacedLength();

	/**
	 * 
	 * @param writer
	 * @param lavel
	 */
	public void write(IWriter writer);

	/**
	 * 
	 * @param level
	 * @param writer
	 */
	public void debug(int level, IWriter writer);

}
