/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.IOutput;
import net.hottree.template.ITemplatePart;
import net.hottree.template.IWriter;

/**
 * @author MW
 * 
 */
public class FastTemplateData implements ITemplatePart {

	public final static String EMPTY_NAME = "";

	public final static int HASH_CODE = EMPTY_NAME.hashCode();

	private final int offset;

	private Object data;

	/**
	 * 
	 * @param offset
	 * @param data
	 */
	public FastTemplateData(int offset, Object data) {
		this.offset = offset;
		this.data = (data instanceof IOutput ? data : String.valueOf(data));
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getInsertedLength() {
		if (data instanceof IOutput) {
			return ((IOutput) data).getLength();
		} else {
			data = data.toString();
			return data.toString().length();
		}
	}

	public int getReplacedLength() {
		return 0;
	}

	@Override
	public void write(IWriter writer) {
		if (data instanceof IOutput) {
			((IOutput) data).write(writer);
		} else {
			writer.write(data.toString());
		}
	}

	@Override
	public int getPartType() {
		return DATA_PART;
	}

	@Override
	public boolean hasData() {
		return false;
	}

	@Override
	public boolean isRemoved() {
		return (data == REMOVED);
	}

	@Override
	public void setRemoved() {
		data = REMOVED;
	}

	@Override
	public String getName() {
		return EMPTY_NAME;
	}

	@Override
	public String toString() {
		return "Data OFFSET:" + offset;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * 
	 */
	public void debug(int level, IWriter writer) {
		if (data instanceof FastTemplate) {
			((FastTemplate) data).debug(level, writer);
		} else if (data instanceof IOutput) {
			writer.write("\n-------------------------------------------------------\nLEVEL:" + level + "  IOutput:\n");
			((IOutput) data).write(writer);
		} else {
			writer.write("\n-------------------------------------------------------\nLEVEL:" + level + "  String:\n");
			writer.write(data.toString());
		}

	}

	@Override
	public int hashCode() {
		return HASH_CODE;
	}

}
