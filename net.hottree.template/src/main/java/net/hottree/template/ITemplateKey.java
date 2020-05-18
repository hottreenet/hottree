/*
 * Created on 2005-07-09
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.hottree.template;

/**
 * 
 * @author MW
 *
 */
public interface ITemplateKey {

	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public boolean hasProperty(String propertyName);

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public String getProperty(String propertyName);

	/**
	 * 
	 * @return
	 */
	public String[] getPropertyNames();

	/**
	 * 
	 * @return
	 */
	public String getText();

	/**
	 * 
	 * @return
	 */
	public int getOffset();

	/**
	 * 
	 * @return
	 */
	public int getLength();
	
	/**
	 * 
	 * @return
	 */
	public ITemplateSource getSource();
	
	/**
	 * 
	 * @param flag
	 * @return
	 */
	public boolean hasFlag(int flag);
}
