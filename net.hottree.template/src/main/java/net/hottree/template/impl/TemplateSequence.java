/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.IOutput;
import net.hottree.template.ITemplateSequence;
import net.hottree.template.IWriter;

/**
 * @author MW
 * 
 */
public class TemplateSequence implements ITemplateSequence {

	private Object[] values;

	private ISequencePart[] parts;

	private int cursor;

	private int max;

	private int size;

	/**
	 * 
	 * @param baseSequence
	 */
	public TemplateSequence(ISequencePart[] parts) {
		this(parts, 5);
	}

	/**
	 * 
	 * @param parts
	 * @param nLoops
	 */
	public TemplateSequence(ISequencePart[] parts, int nLoops) {
		this.parts = parts;
		values = new Object[(parts.length * nLoops) + 1];
		max = parts.length - 2; // dla 3 czesci jest 0, 1
		if (max < -1) {
			max = -1;
		}
		size = 0;
	}

	@Override
	public void replace(Object object) throws TemplateKeyNotFoundException {
		if (cursor > max) {
			throw new TemplateKeyNotFoundException("No more keys; index:" + cursor + ", max:" + max);
		}
		ensureCapacity(size + 1);
		if (object instanceof IOutput) {
			values[size++] = object;
		} else {
			values[size++] = object.toString();
		}
		cursor++;
	}

	/**
	 * 
	 * @param minCapacity
	 */
	private void ensureCapacity(int minCapacity) {
		int oldCapacity = values.length;
		if (minCapacity > oldCapacity) {
			Object oldData[] = values;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			values = new Object[newCapacity];
			System.arraycopy(oldData, 0, values, 0, size);
		}
	}

	@Override
	public void newLoop() {
		cursor = 0;
	}

	@Override
	public void ensureLoops(int minLoops) {
		int newCapacity = (parts.length * minLoops) + 1;
		if (newCapacity > values.length) {
			Object oldData[] = values;
			values = new Object[newCapacity];
			System.arraycopy(oldData, 0, values, 0, size);
		}
	}

	@Override
	public void write(IWriter writer) {
		int index = 0;
		for (int i = 0; i < size; i++) {
			Object value = values[i];
			parts[index].write(writer);
			if (value instanceof IOutput) {
				((IOutput) value).write(writer);
			} else {
				writer.write(value.toString());
			}
			index++;
			if (index > max) {
				parts[index].write(writer);
				index = 0;
			}
		}
	}

	@Override
	public int getLength() {
		int length = 0;
		int index = 0;
		for (int i = 0; i < size; i++) {
			Object value = values[i];
			length += parts[index].getLength();
			if (value instanceof IOutput) {
				length += ((IOutput) value).getLength();
			} else {
				// value = value.toString();
				length += value.toString().length();
				// values[i] = value;
			}
			index++;
			if (index > max) {
				length += parts[index].getLength();
				index = 0;
			}
		}
		return length;
	}

	@Override
	public Object clone() {
		try {
			TemplateSequence clone = (TemplateSequence) super.clone();
			clone.values = (Object[]) values.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new TemplateRuntimeException(e);
		}
	}

	@Override
	public String toString() {
		SimpleWriter writer = new SimpleWriter();
		write(writer);
		return writer.toString();
	}

	@Override
	public int getKeyCount() {
		return max + 1;
	}

	/**
	 * 
	 * @return
	 */
	public String debug() {
		return "" + TemplateSequence.class.getName();
	}

}
