
package net.hottree.template.impl;

import java.util.ArrayList;
import java.util.List;

import net.hottree.template.ITemplateKey;

/**
 * 
 */
public class TemplateKeyCreator implements ITemplateKeyCreator {

	private final char[] startSequence;

	private final char[] endSequence;

	private final List<String> propertyNames = new ArrayList<String>(10);

	private final List<String> propertyValues = new ArrayList<String>(10);

	/**
	 * 
	 * @param startSequence
	 * @param endSequence
	 */
	public TemplateKeyCreator(String startSequence, String endSequence) {
		this(startSequence.toCharArray(), endSequence.toCharArray());
	}

	/**
	 * 
	 * @param keyType
	 * @param startSequence
	 * @param endSequence
	 */
	public TemplateKeyCreator(char[] startSequence, char[] endSequence) {
		this.startSequence = startSequence;
		this.endSequence = endSequence;
	}

	@Override
	public synchronized ITemplateKey createKey(IKeyScanner template, int offset, int length, String keyContent) {
		if (keyContent.length() >= 2 && keyContent.indexOf('(') > 0) {
			propertyNames.clear();
			propertyValues.clear();
			String name = cutProperties(keyContent, propertyNames, propertyValues);
			return new TemplateKey(template, offset, length, name, propertyNames.toArray(new String[propertyNames
				.size()]), propertyValues.toArray(new String[propertyValues.size()]), 0);
		} else {
			return new TemplateKey(template, offset, length, keyContent, null, null, 0);
		}
	}

	/**
	 * 
	 * @param keyContent
	 * @param propertyNames
	 * @param propertyValues
	 * @return
	 */
	private String cutProperties(String keyContent, List<String> propertyNames, List<String> propertyValues) {
		int lastIndex = -1;
		String propertName = null;

		int startIndex = keyContent.indexOf('(');
		int endIndex = keyContent.lastIndexOf(')');
		if (startIndex == -1) {
			return keyContent;
		}
		if (endIndex > 0) {
			if (endIndex < startIndex) {
				return keyContent;
			}
		} else {
			endIndex = keyContent.length();// mimo, ze nie znalaz³ ')' idz dalej
		}

		lastIndex = startIndex;
		for (int i = startIndex; i < endIndex; i++) {
			char ch = keyContent.charAt(i);
			if (ch == '=') {
				propertName = keyContent.substring(lastIndex + 1, i);
				lastIndex = i;
			}
			if (ch == ';' && propertName != null) {
				propertyNames.add(propertName);
				propertyValues.add(keyContent.substring(lastIndex + 1, i));
				propertName = null;
				lastIndex = i;
			}
		}

		// pobranie ostatniego propsa
		if (propertName != null) {
			propertyNames.add(propertName);
			propertyValues.add(keyContent.substring(lastIndex + 1, endIndex));
		}

		return keyContent.substring(0, startIndex);
	}

	@Override
	public char[] getKeyStartSequence() {
		return startSequence;
	}

	@Override
	public char[] getKeyEndSequence() {
		return endSequence;
	}

}
