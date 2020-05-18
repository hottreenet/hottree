/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.ITemplatePart;

/**
 * @author MW
 * 
 */
public class PartList implements Cloneable {

	private ITemplatePart[] parts;

	private int size = 0;

	/**
	 * 
	 * 
	 */
	public PartList() {
		this(3);
	}

	/**
	 * 
	 * @param initialCapacity
	 */
	public PartList(int initialCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		this.parts = new ITemplatePart[initialCapacity];
	}

	/**
	 * 
	 * @param parts
	 */
	public PartList(ITemplatePart[] parts, int size) {
		this.parts = parts;
		this.size = size;
	}

	/**
	 * Increases the capacity of this <tt>ArrayList</tt> instance, if necessary,
	 * to ensure that it can hold at least the number of elements specified by
	 * the minimum capacity argument.
	 * 
	 * @param minCapacity
	 *            the desired minimum capacity.
	 */
	public void ensureCapacity(int minCapacity) {
		int oldCapacity = parts.length;
		if (minCapacity > oldCapacity) {
			ITemplatePart oldData[] = parts;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			parts = new ITemplatePart[newCapacity];
			System.arraycopy(oldData, 0, parts, 0, size);
		}
	}

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list.
	 */
	public int size() {
		return size;
	}

	/**
	 * Tests if this list has no elements.
	 * 
	 * @return <tt>true</tt> if this list has no elements; <tt>false</tt>
	 *         otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns <tt>true</tt> if this list contains the specified element.
	 * 
	 * @param elem
	 *            element whose presence in this List is to be tested.
	 * @return <tt>true</tt> if the specified element is present; <tt>false</tt>
	 *         otherwise.
	 */
	public boolean contains(ITemplatePart elem) {
		return indexOf(elem) >= 0;
	}

	/**
	 * Searches for the first occurence of the given argument, testing for
	 * equality using the <tt>equals</tt> method.
	 * 
	 * @param elem
	 *            an object.
	 * @return the index of the first occurrence of the argument in this list;
	 *         returns <tt>-1</tt> if the object is not found.
	 * @see Object#equals(Object)
	 */
	public int indexOf(ITemplatePart elem) {
		if (elem == null) {
			for (int i = 0; i < size; i++) {
				if (parts[i] == null)
					return i;
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (elem.equals(parts[i]))
					return i;
			}
		}
		return -1;
	}

	/**
	 * Appends the specified element to the end of this list.
	 * 
	 * @param o
	 *            element to be appended to this list.
	 * @return <tt>true</tt> (as per the general contract of Collection.add).
	 */
	public void add(ITemplatePart part) {
		ensureCapacity(size + 1);
		parts[size++] = part;
	}

	/**
	 * 
	 * @param index
	 * @param part
	 */
	public void add(int index, ITemplatePart part) {
		if (index > size || index < 0) {
			throw new TemplateRuntimeException("<?> (internal error) index: " + index + ", size: " + size);
		}
		ensureCapacity(size + 1); // Increments modCount!!
		System.arraycopy(parts, index, parts, index + 1, size - index);
		parts[index] = part;
		size++;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public ITemplatePart getPart(int index) {
		RangeCheck(index);
		return parts[index];
	}

	/**
	 * 
	 * @param index
	 * @param part
	 */
	public void setPart(int index, ITemplatePart part) {
		RangeCheck(index);
		parts[index] = part;
	}

	/**
	 * Zastêpuje klucze o podanych indeksach (³¹cznie) przez podan¹ czêœæ
	 * 
	 * @param indexFrom
	 * @param indexTo
	 * @param part
	 */
	public void replace(int indexFrom, int indexTo, ITemplatePart part) {
		RangeCheck(indexFrom);
		RangeCheck(indexTo);
		parts[indexFrom] = part;
		setRemoved(indexFrom + 1, indexTo);
	}

	/**
	 * 
	 * @param index
	 */
	private void RangeCheck(int index) {
		if (index >= size || index < 0)
			throw new TemplateRuntimeException("<?> (internal error) index: " + index + ", size: " + size);
	}

	/**
	 * 
	 * 
	 * @param indexFrom
	 * @param indexTo
	 * @return
	 */
	public boolean containDataOrRemovedParts(int indexFrom, int indexTo) {
		indexTo = (indexTo == -1 ? size : indexTo);
		for (int i = indexFrom + 1; i < indexTo; i++) {
			ITemplatePart part = parts[i];
			if (part.getPartType() != ITemplatePart.KEY_PART || part.isRemoved() || part.hasData()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param keyType
	 * @param keyName
	 * @return
	 */
	public int getKeyIndex(String keyName) {
		if (keyName == null) {
			return -1;
		}
		for (int i = 0; i < size; i++) {
			ITemplatePart part = parts[i];
			if (part.getPartType() == ITemplatePart.KEY_PART && !part.isRemoved() && !part.hasData()
				&& part.getName().equals(keyName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param keyName
	 * @return
	 */
	public ITemplatePart getKey(String keyName) {
		if (keyName == null) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			ITemplatePart part = parts[i];
			if (part.getPartType() == ITemplatePart.KEY_PART && !part.isRemoved() && !part.hasData()
				&& part.getName().equals(keyName)) {
				return part;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param keyType
	 * @param keyName
	 * @param indexFrom
	 * @return
	 */
	public int getKeyIndex(String keyName, int indexFrom) {
		if (indexFrom >= size || keyName == null) {
			return -1;
		}
		for (int i = (indexFrom < 0 ? 0 : indexFrom); i < size; i++) {
			ITemplatePart part = parts[i];
			if (part.getPartType() == ITemplatePart.KEY_PART && !part.isRemoved() && !part.hasData()
				&& part.getName().equals(keyName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param keyIndex
	 * @return
	 */
	public FastTemplateKey getKey(int keyIndex) {
		if (keyIndex < 0 || keyIndex >= size) {
			return null;
		}
		ITemplatePart part = parts[keyIndex];
		if (part.getPartType() == ITemplatePart.KEY_PART && !part.isRemoved() && !part.hasData()) {
			return (FastTemplateKey) part;
		}
		return null;
	}

	/**
	 * Zwraca indeks ostatniego klucza, który by³ na pocz¹tku, móg³ on zostaæ
	 * usuniêty, zast¹piony itd
	 * 
	 * @return
	 */
	public int getLastKeyIndex() {
		int lastIndex = -1;
		for (int i = 0; i < parts.length; i++) {
			ITemplatePart part = parts[i];
			if (part.getPartType() == ITemplatePart.KEY_PART) {
				lastIndex = i;
			}
		}
		return lastIndex;
	}

	/**
	 * 
	 * @param indexFrom
	 * @param indexTo
	 */
	public void setRemoved(int indexFrom, int indexTo) {
		for (int i = indexFrom; i <= indexTo; i++) {
			parts[i].setRemoved();
		}
	}

	/**
	 * 
	 * @param indexFrom
	 * @param indexTo
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public PartList copyParts(int indexFrom, int indexTo, boolean onlyKeys) {
		try {
			// //ogs.wrln(110,
			// "PartList.copyParts(including): from "+indexFrom+"
			// to "+indexTo);

			if (indexTo < indexFrom) {
				return new PartList();
			}
			RangeCheck(indexFrom);
			RangeCheck(indexTo);

			ITemplatePart[] copyParts = new ITemplatePart[indexTo - indexFrom + 1]; // kopiuje
			// ³¹cznie
			int n = 0;
			FastTemplateKey lastKeyFrom = null;
			boolean switchPairsOkey = true;
			for (int i = indexFrom; i <= indexTo; i++) {
				ITemplatePart part = parts[i];
				// usuniêtych, czyli po³kniêtych kluczy nie licz
				if (!part.isRemoved()) {
					// //ogs.wrln(110, "COPY part: "+part);

					if (part instanceof FastTemplateKey) {
						FastTemplateKey key = (FastTemplateKey) part;
						FastTemplateKey newKey = (FastTemplateKey) key.clone();
						copyParts[n++] = newKey;
						// //ogs.wrln(110, "CLONE AS: "+newKey);
						if (newKey.getPairKey() != null) {
							// //ogs.wrln(110, "HASE PAIR KAY:
							// "+newKey.getPairTemplateKey());
							if (lastKeyFrom == null) {
								lastKeyFrom = newKey;
								switchPairsOkey = false;
								// //ogs.wrln(110, "SET AS LAST KEY WITH PAIR");
							} else {
								// //ogs.wrln(110, "CHECK LAST");
								if (lastKeyFrom.getPairKey() == key) {
									newKey.setPairKey(lastKeyFrom);
									lastKeyFrom.setPairKey(newKey);
									switchPairsOkey = true;
								} else {
									throw new TemplateRuntimeException("Internal error. Invalid key pairs.");
								}
							}
						}
					} else if (!onlyKeys) {

						copyParts[n++] = part; // FastTemplateData nie trzeba
						// klonowaæ
						// itd !!!
					}

				}
			}
			if (switchPairsOkey == false) {
				throw new TemplateRuntimeException("Internal error. Switch key pairs failed.");
			}
			return new PartList(copyParts, n);
		} catch (CloneNotSupportedException e) {
			throw new TemplateRuntimeException(e);
		}
	}

	/**
	 * Wycina czêœci od danej pozycji do danej pozycji ³¹cznie
	 * 
	 * @param indexFrom
	 * @param indexTo
	 * @return
	 */
	public PartList cutParts(int indexFrom, int indexTo) {
		// //ogs.wrln(110,"PartList.cutKeys(including): from "+indexFrom+" to
		// "+indexTo);
		if (indexTo < indexFrom) {
			return new PartList();
		}

		RangeCheck(indexFrom);
		RangeCheck(indexTo);

		int cutSize = indexTo - indexFrom + 1;

		ITemplatePart[] out = new ITemplatePart[cutSize];
		System.arraycopy(parts, indexFrom, out, 0, cutSize);

		if (indexFrom < indexTo) {
			remove(indexFrom, indexTo); // usuwa ³¹cznie z tym po prawej
		} else {
			remove(indexFrom);
		}

		return new PartList(out, cutSize);
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public ITemplatePart remove(int index) {
		RangeCheck(index);

		ITemplatePart oldValue = parts[index];

		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(parts, index + 1, parts, index, numMoved);
		parts[--size] = null;

		return oldValue;
	}

	/**
	 * Usuwa ³¹cznie z indeksami
	 * 
	 * @param indexFrom
	 * @param indexTo
	 */
	protected void remove(int indexFrom, int indexTo) {
		int numMoved = size - (indexTo + 1);
		System.arraycopy(parts, indexTo + 1, parts, indexFrom, numMoved);

		int newSize = size - (indexTo + 1 - indexFrom);
		while (size != newSize) {
			parts[--size] = null;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return copyParts(0, size - 1, false);
	}

	/**
	 * 
	 * @param nr
	 * @param msg
	 */
	public void printParts(int nr, String msg) {
	// ogs.wrln(nr, "=====================================\n Parts:  " + msg
	// + "\n--------------------------------------------------");
	// for (int i = 0; i < size; i++) {
	// ITemplatePart part = parts[i];
	// if (!part.isRemoved() && !part.isDataKey()) {
	// ogs.wrln(nr, part.toString());
	// }
	// }
	// ogs.wrln(nr,
	// "------------------------------------------------------\n");
	}

	/**
	 * @return Returns the parts.
	 */
	ITemplatePart[] getParts() {
		return parts;
	}

	/**
	 * 
	 * 
	 */
	public void clear() {
		for (int i = 0; i < parts.length; i++) {
			parts[i] = null;
		}
		size = 0;
	}

	/**
	 * Tylko z FastTemplatre
	 * 
	 */
	public void dispose() {
		clear();
		parts = null;
	}

}
