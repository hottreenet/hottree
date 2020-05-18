package net.hottree.template.impl;

import net.hottree.template.ITemplateBuffer;
import net.hottree.template.ITemplateSource;
import net.hottree.template.IWriter;

/**
 * @author MW
 * 
 * 
 */
public class TemplateBuffer implements java.io.Serializable, CharSequence, Cloneable, ITemplateSource,
		ITemplateBuffer {

	private static final char[] EMPTY_VALUE = new char[0];

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The value is used for character storage.
	 * 
	 * @serial
	 */
	private char value[];

	/**
	 * The count is the number of characters in the buffer.
	 * 
	 * @serial
	 */
	private int count = 0;

	private final String location;

	/**
     * 
     *
     */
	public TemplateBuffer(String location) {
		this(16, location);
	}

	/**
	 * 
	 * @param length
	 */
	public TemplateBuffer(int length, String location) {
		value = new char[length];
		count = 0;
		this.location = location;
	}

	/**
	 * 
	 * @param length
	 */
	public TemplateBuffer(String content, String location) {
		this(content.length() + 16, location);
		append(content);
	}

	/**
	 * 
	 * @param chars
	 * @param offset
	 * @param length
	 */
	public TemplateBuffer(char[] chars, int offset, int length, String location) {
		this(length + 16, location);
		append(chars, offset, length);
	}

	/**
     *  
     */
	public synchronized int length() {
		return count;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized int capacity() {
		return value.length;
	}

	/**
	 * 
	 * @param capacity
	 */
	public synchronized void ensureCapacity(int capacity) {
		if (capacity > value.length) {
			char newValue[] = new char[capacity];
			System.arraycopy(value, 0, newValue, 0, count);
			value = newValue;
		}
	}

	/**
	 * 
	 * @param minimumCapacity
	 */
	private void expandCapacity(int minimumCapacity) {
		int newCapacity = (value.length + 1) * 2;
		if (newCapacity < 0) {
			newCapacity = Integer.MAX_VALUE;
		} else if (minimumCapacity > newCapacity) {
			newCapacity = minimumCapacity;
		}

		char newValue[] = new char[newCapacity];
		System.arraycopy(value, 0, newValue, 0, count);
		value = newValue;
	}

	/**
	 * 
	 * @param newLength
	 */
	public synchronized void setLength(int newLength) {
		if (newLength < 0) {
			throw new StringIndexOutOfBoundsException(newLength);
		}

		if (newLength > value.length) {
			expandCapacity(newLength);
		}

		if (count < newLength) {

			for (; count < newLength; count++) {
				value[count] = '\0';
			}
		} else {
			count = newLength;
		}
	}

	/**
     * 
     */
	public synchronized char charAt(int index) {
		if ((index < 0) || (index >= count)) {
			throw new StringIndexOutOfBoundsException(index);
		}
		return value[index];
	}

	/**
	 * 
	 * @param srcBegin
	 * @param srcEnd
	 * @param dst
	 * @param dstBegin
	 */
	public synchronized void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin) {
		if (srcBegin < 0) {
			throw new StringIndexOutOfBoundsException(srcBegin);
		}
		if ((srcEnd < 0) || (srcEnd > count)) {
			throw new StringIndexOutOfBoundsException(srcEnd);
		}
		if (srcBegin > srcEnd) {
			throw new StringIndexOutOfBoundsException("srcBegin > srcEnd");
		}
		System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
	}

	/**
	 * 
	 * @param index
	 * @param ch
	 */
	public synchronized void setCharAt(int index, char ch) {
		if ((index < 0) || (index >= count)) {
			throw new StringIndexOutOfBoundsException(index);
		}
		value[index] = ch;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public synchronized TemplateBuffer append(Object obj) {
		return append(String.valueOf(obj));
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public synchronized TemplateBuffer append(String str) {
		if (str == null) {
			str = String.valueOf(str);
		}

		int len = str.length();
		int newcount = count + len;
		if (newcount > value.length)
			expandCapacity(newcount);
		str.getChars(0, len, value, count);
		count = newcount;
		return this;
	}

	/**
	 * 
	 * @param sb
	 * @return
	 */
	public synchronized TemplateBuffer append(TemplateBuffer sb) {
		if (sb == null) {
			sb = NULL;
		}

		int len = sb.length();
		int newcount = count + len;
		if (newcount > value.length)
			expandCapacity(newcount);
		sb.getChars(0, len, value, count);
		count = newcount;
		return this;
	}

	private static final TemplateBuffer NULL = new TemplateBuffer("null");

	/**
	 * 
	 * @param str
	 * @return
	 */
	public synchronized TemplateBuffer append(char str[]) {
		int len = str.length;
		int newcount = count + len;
		if (newcount > value.length)
			expandCapacity(newcount);
		System.arraycopy(str, 0, value, count, len);
		count = newcount;
		return this;
	}

	/**
	 * 
	 * @param str
	 * @param offset
	 * @param len
	 * @return
	 */
	public synchronized TemplateBuffer append(char str[], int offset, int len) {
		int newcount = count + len;
		if (newcount > value.length)
			expandCapacity(newcount);
		System.arraycopy(str, offset, value, count, len);
		count = newcount;
		return this;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public synchronized TemplateBuffer append(boolean b) {
		if (b) {
			int newcount = count + 4;
			if (newcount > value.length)
				expandCapacity(newcount);
			value[count++] = 't';
			value[count++] = 'r';
			value[count++] = 'u';
			value[count++] = 'e';
		} else {
			int newcount = count + 5;
			if (newcount > value.length)
				expandCapacity(newcount);
			value[count++] = 'f';
			value[count++] = 'a';
			value[count++] = 'l';
			value[count++] = 's';
			value[count++] = 'e';
		}
		return this;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public synchronized TemplateBuffer append(char c) {
		int newcount = count + 1;
		if (newcount > value.length)
			expandCapacity(newcount);
		value[count++] = c;
		return this;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public synchronized TemplateBuffer deleteCharAt(int index) {
		if ((index < 0) || (index >= count))
			throw new StringIndexOutOfBoundsException();
		System.arraycopy(value, index + 1, value, index, count - index - 1);
		count--;
		return this;
	}

	/**
	 * 
	 * @param start
	 * @return
	 */
	public synchronized String substring(int start) {
		return substring(start, count);
	}

	/**
   * 
   */
	public CharSequence subSequence(int start, int end) {
		return this.substring(start, end);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public synchronized String substring(int start, int end) {
		if (start < 0)
			throw new StringIndexOutOfBoundsException(start);
		if (end > count)
			throw new StringIndexOutOfBoundsException(end);
		if (start > end)
			throw new StringIndexOutOfBoundsException(end - start);
		return new String(value, start, end - start);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public synchronized ITemplateBuffer subbuffer(int start, int end) {
		String location = ((start == 0 && end == this.count) ? this.location : this.location + " [" + start
				+ ":" + end + "]");
		return new TemplateBuffer(getValue(), start, end - start, location);
	}

	/*
   * 
   */
	public synchronized TemplateBuffer insert(int index, char str[], int offset, int len) {
		if ((index < 0) || (index > count))
			throw new StringIndexOutOfBoundsException();
		if ((offset < 0) || (offset + len < 0) || (offset + len > str.length))
			throw new StringIndexOutOfBoundsException(offset);
		if (len < 0)
			throw new StringIndexOutOfBoundsException(len);
		int newCount = count + len;
		if (newCount > value.length)
			expandCapacity(newCount);

		System.arraycopy(value, index, value, index + len, count - index);
		System.arraycopy(str, offset, value, index, len);
		count = newCount;
		return this;
	}

	/**
	 * 
	 * @param offset
	 * @param obj
	 * @return
	 */
	public synchronized TemplateBuffer insert(int offset, Object obj) {
		return insert(offset, String.valueOf(obj));
	}

	/**
	 * 
	 * @param offset
	 * @param str
	 * @return
	 */
	public synchronized TemplateBuffer insert(int offset, String str) {
		if ((offset < 0) || (offset > count)) {
			throw new StringIndexOutOfBoundsException();
		}

		if (str == null) {
			str = String.valueOf(str);
		}
		int len = str.length();
		int newcount = count + len;
		if (newcount > value.length)
			expandCapacity(newcount);

		System.arraycopy(value, offset, value, offset + len, count - offset);
		str.getChars(0, len, value, offset);
		count = newcount;
		return this;
	}

	/**
	 * 
	 * @param offset
	 * @param str
	 * @return
	 */
	public synchronized TemplateBuffer insert(int offset, char str[]) {
		if ((offset < 0) || (offset > count)) {
			throw new StringIndexOutOfBoundsException();
		}
		int len = str.length;
		int newcount = count + len;
		if (newcount > value.length)
			expandCapacity(newcount);

		System.arraycopy(value, offset, value, offset + len, count - offset);
		System.arraycopy(str, 0, value, offset, len);
		count = newcount;
		return this;
	}

	/**
	 * 
	 * @param offset
	 * @param b
	 * @return
	 */
	public TemplateBuffer insert(int offset, boolean b) {
		return insert(offset, String.valueOf(b));
	}

	/**
	 * 
	 * @param offset
	 * @param c
	 * @return
	 */
	public synchronized TemplateBuffer insert(int offset, char c) {
		int newcount = count + 1;
		if (newcount > value.length)
			expandCapacity(newcount);

		System.arraycopy(value, offset, value, offset + 1, count - offset);
		value[offset] = c;
		count = newcount;
		return this;
	}

	/**
	 * 
	 * @param offset
	 * @param i
	 * @return
	 */
	public TemplateBuffer insert(int offset, int i) {
		return insert(offset, String.valueOf(i));
	}

	/**
	 * 
	 * @param offset
	 * @param l
	 * @return
	 */
	public TemplateBuffer insert(int offset, long l) {
		return insert(offset, String.valueOf(l));
	}

	/**
	 * 
	 * @param offset
	 * @param f
	 * @return
	 */
	public TemplateBuffer insert(int offset, float f) {
		return insert(offset, String.valueOf(f));
	}

	/**
	 * 
	 * @param offset
	 * @param d
	 * @return
	 */
	public TemplateBuffer insert(int offset, double d) {
		return insert(offset, String.valueOf(d));
	}

	/**
	 * 
	 * @return
	 */
	public synchronized TemplateBuffer reverse() {

		int n = count - 1;
		for (int j = (n - 1) >> 1; j >= 0; --j) {
			char temp = value[j];
			value[j] = value[n - j];
			value[n - j] = temp;
		}
		return this;
	}

	private final char[] getValue() {
		return value;
	}

	/**
	 * readObject is called to restore the state of the TemplateBuffer from a
	 * stream.
	 */
	private synchronized void readObject(java.io.ObjectInputStream s) throws java.io.IOException,
			ClassNotFoundException {
		s.defaultReadObject();
		value = value.clone();
	}

	@Override
	public String toString() {
		return new String(value, 0, count);
	}

	@Override
	public Object clone() {
		TemplateBuffer cloneBuffer = new TemplateBuffer(count + 1, location);
		cloneBuffer.append(this);
		return cloneBuffer;
	}

	/**
	 * 
	 * @param writer
	 * @param offset
	 * @param length
	 */
	public void write(IWriter writer, int offset, int length) {
		writer.write(value, offset, length);
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public ITemplateBuffer getBuffer() {
		return this;
	}

	@Override
	public void dispose() {
		value = EMPTY_VALUE;
		count = 0;
	}
}
