
package net.hottree.template.impl;

import net.hottree.template.ITemplateKey;
import net.hottree.template.ITemplateSource;

/**
 * 
 */
public class TemplateKey implements ITemplateKey {

	private static String[] NO_PROPERTY = new String[0];

	private IKeyScanner keyScanner;

	private int offset;

	private int length;

	private int flags;

	private String[] propertyNames;

	private String[] propertyValues;

	private String name;

	private int hashCode;

	/**
	 * 
	 */
	private TemplateKey() {

	}

	public TemplateKey(IKeyScanner keyScanner, int offset, int length, String name, int flags) {
		this(keyScanner, offset, length, name, NO_PROPERTY, NO_PROPERTY, flags);
	}

	public TemplateKey(IKeyScanner keyScanner, int offset, int length, String name, String[] propertyNames,
			String[] propertyValues, int flags) {
		if (keyScanner == null)
			ThrowUtil.nullArg("keyScanner");
		if (name == null)
			ThrowUtil.nullArg("name");
		this.keyScanner = keyScanner;
		this.offset = offset;
		this.length = length;
		this.name = name;
		this.hashCode = name.hashCode();
		this.propertyNames = propertyNames == null ? NO_PROPERTY : propertyNames;
		this.propertyValues = propertyValues == null ? NO_PROPERTY : propertyValues;
		this.flags = flags;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String getProperty(String propertyName) {
		for (int i = 0; i < propertyNames.length; i++) {
			if (propertyName.equals(propertyNames[i])) {
				return propertyValues[i];
			}
		}
		return null;
	}

	@Override
	public String[] getPropertyNames() {
		return propertyNames;
	}

	@Override
	public boolean hasProperty(String propertyName) {
		return getProperty(propertyName) != null;
	}

	@Override
	public String toString() {
		StringBuilder props = new StringBuilder(50);
		String[] propertyNames = getPropertyNames();
		for (int i = 0; i < propertyNames.length; i++) {
			String value = getProperty(propertyNames[i]);
			props.append(propertyNames[i] + "=");
			props.append(value + ";");
		}
		return "Key:" + getName() + " [OFFSET:" + offset + " LENGTH:" + length + "] '"
				+ keyScanner.getSource().getBuffer().substring(getOffset(), getOffset() + getLength()) + "'";
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public String getText() {
		return getSource().getBuffer().substring(getOffset(), getOffset() + getLength());
	}

	@Override
	public Object clone() {
		TemplateKey cloneKey = new TemplateKey();
		cloneKey.keyScanner = keyScanner;
		cloneKey.offset = offset;
		cloneKey.length = length;
		cloneKey.name = name;
		cloneKey.hashCode = hashCode;
		cloneKey.flags = flags;
		if (propertyNames != null && propertyValues != null) {
			cloneKey.propertyNames = new String[propertyNames.length];
			cloneKey.propertyValues = new String[propertyValues.length];
			System.arraycopy(propertyNames, 0, cloneKey.propertyNames, 0, propertyNames.length);
			System.arraycopy(propertyValues, 0, cloneKey.propertyValues, 0, propertyValues.length);
		}

		return cloneKey;
	}

	@Override
	public ITemplateSource getSource() {
		return keyScanner.getSource();
	}

	@Override
	public boolean hasFlag(int flag) {
		return (flags & flag) > 0;
	}

}
