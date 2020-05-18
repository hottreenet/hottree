/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.IOutput;
import net.hottree.template.ITemplateKey;
import net.hottree.template.ITemplatePart;
import net.hottree.template.ITemplateSource;
import net.hottree.template.IWriter;

/**
 * 
 *
 */
public class FastTemplateKey implements ITemplateKey, ITemplatePart {

	private final ITemplateKey original;

	// jest to obiekt, który zostanie wstawiony w klucz etc
	private Object data = null;

	// je¿eli jest pairTemplateKey != null to dany obiekt zast¹pi te dwa klucze
	private FastTemplateKey pairKey = null;

	/**
	 * 
	 * @param original
	 */
	public FastTemplateKey(ITemplateKey original) {
		this.original = original;
	}

	/**
	 * 
	 * @param original
	 * @param data
	 * @param pairKey
	 */
	public FastTemplateKey(ITemplateKey original, Object data, FastTemplateKey pairKey) {
		this.original = original;
		this.data = data;
		this.pairKey = pairKey;
	}

	@Override
	public String getName() {
		return original.getName();
	}

	@Override
	public int hashCode() {
		return original.hashCode();
	}

	@Override
	public int getPartType() {
		return KEY_PART;
	}

	@Override
	public boolean hasProperty(String propertyName) {
		return original.hasProperty(propertyName);
	}

	@Override
	public String getProperty(String propertyName) {
		return original.getProperty(propertyName);
	}

	@Override
	public String[] getPropertyNames() {
		return original.getPropertyNames();
	}
	
	@Override
	public boolean hasFlag(int flag) {
		return original.hasFlag(flag);
	}

	/**
	 * @return Returns the object.
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            The object to set.
	 */
	public void setData(Object data) {
		if (data instanceof FastTemplate) {
			((FastTemplate) data).setInner();
		}
		this.data = data;
	}

	@Override
	public boolean hasData() {
		if (data == null) {
			return pairKey != null;
		}
		return true;
	}

	/**
	 * 
	 * @return <tt>true</tt> if this key was removed
	 */
	public boolean isRemoved() {
		return (data == REMOVED);
	}

	/**
	 *
	 */
	public void setRemoved() {
		data = REMOVED;
	}

	/**
	 * @return returns the pair key
	 */
	public FastTemplateKey getPairKey() {
		return pairKey;
	}

	/**
	 * @param pairKey
	 */
	public void setPairKey(FastTemplateKey pairKey) {
		this.pairKey = pairKey;
	}

	@Override
	public int getOffset() {
		return original.getOffset();
	}

	@Override
	public int getLength() {
		return original.getLength();
	}

	@Override
	public int getInsertedLength() {
		if (data instanceof IOutput) {
			return ((IOutput) data).getLength();
		} else if (data != null) {
			data = data.toString();
			return data.toString().length();
		}
		return original.getLength();
	}

	@Override
	public int getReplacedLength() {
		if (pairKey == null) {
			return original.getLength();
		}
		ITemplateKey pairBaseKey = this.pairKey.original;
		return pairBaseKey.getOffset() + pairBaseKey.getLength() - original.getOffset();
	}

	@Override
	public void write(IWriter writer) {
		if (data == null) {
			original.getSource().getBuffer().write(writer, original.getOffset(), original.getLength());
		} else if (data instanceof IOutput) {
			((IOutput) data).write(writer);
		} else {
			writer.write(data.toString());
		}
	}

	/**
	 * @return Returns the baseTemplateKey.
	 */
	ITemplateKey getOriginalKey() {
		return original;
	}

	@Override
	public String toString() {
		return "Fast" + original.toString() + " @" + Integer.toHexString(hashCode()) + "  REMOVED:" + this.isRemoved()
			+ "  DATA:" + hasData();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public ITemplateSource getSource() {
		return original.getSource();
	}

	@Override
	public String getText() {
		return original.getText();
	}

	@Override
	public void debug(int level, IWriter writer) {
		if (data == null) {
			original.getSource().getBuffer().write(writer, original.getOffset(), original.getLength());
		} else if (data instanceof FastTemplate) {
			((FastTemplate) data).debug(level, writer);
		} else if (data instanceof IOutput) {
			writer.write("\n-------------------------------------------------------\nLEVEL:" + level + "  IOutput:\n");
			((IOutput) data).write(writer);
		} else {
			writer.write("\n-------------------------------------------------------\nLEVEL:" + level + "  String:\n");
			writer.write(data.toString());
		}

	}

	

}
