/**
 * 
 */
package net.hottree.template.impl;

import java.util.LinkedList;
import java.util.List;

import net.hottree.template.ITemplate;
import net.hottree.template.ITemplateBuffer;
import net.hottree.template.ITemplateKey;
import net.hottree.template.ITemplatePart;
import net.hottree.template.ITemplateSequence;
import net.hottree.template.ITemplateSource;
import net.hottree.template.IWriter;

/**
 * 
 * 
 */
public class FastTemplate implements ITemplate, Cloneable {

	private ITemplateSource source;

	private int start;

	private int end;

	private PartList partList;

	private boolean inner = false;

	/**
	 * 
	 * @param source
	 */
	public FastTemplate(ITemplateSource source) {
		this(source, new KeyScanner());
	}

	/**
	 * 
	 * @param source
	 * @param keyScanner
	 */
	public FastTemplate(ITemplateSource source, KeyScanner keyScanner) {
		this(source, 0, source.getBuffer().length(), createPartList(keyScanner.scann(source)));
	}

	private static PartList createPartList(ITemplateKey[] keys) {
		FastTemplateKey[] fastKeys = new FastTemplateKey[keys.length];
		for (int i = 0; i < keys.length; i++) {
			fastKeys[i] = new FastTemplateKey(keys[i]);
		}
		return new PartList(fastKeys, fastKeys.length);
	}

	/**
	 * 
	 * @param source
	 * @param start
	 * @param end
	 * @param partList
	 */
	public FastTemplate(ITemplateSource source, int start, int end, PartList partList) {
		this.source = source;
		this.start = start;
		this.end = end;
		this.partList = partList;
	}

	@Override
	public String getLocation() {
		return source.getLocation();
	}

	@Override
	public String getName() {
		if (start == 0 && end == source.getBuffer().length()) {
			return source.getLocation();
		}
		return getSubTemplateName(start, end);
	}

	/**
	 * Ustawiany przez klucz, informuje o tym ¿e templejt znajduje siê w innym templejcie
	 */
	public void setInner() {
		this.inner = true;
	}

	@Override
	public void append(Object object) {
		checkNotInnerAccess();
		if (object == null)
			ThrowUtil.nullArg("object"); //$NON-NLS-1$
		partList.add(new FastTemplateData(end, object));
	}

	@Override
	public void replace(String keyName, Object object) throws TemplateKeyNotFoundException {
		checkNotInnerAccess();
		if (object == null)
			ThrowUtil.nullArg("object"); //$NON-NLS-1$
		int index = -1;
		boolean found = false;
		do {
			index = partList.getKeyIndex(keyName, index);
			if (index >= 0) {
				((FastTemplateKey) partList.getPart(index)).setData(object);
				found = true;
			} else {
				break;
			}
		} while (true);
		if (found == false) {
			throwKeyNotFoundException(keyName);
		}
	}

	@Override
	public void replace(int keyIndex, Object object) throws TemplateKeyNotFoundException {
		checkNotInnerAccess();
		if (object == null)
			ThrowUtil.nullArg("object"); //$NON-NLS-1$
		FastTemplateKey key = getKeyByIndex(keyIndex);
		key.setData(object);
	}

	@Override
	public void replace(String keyFromName, String keyToName, Object object) throws TemplateKeyNotFoundException {
		checkNotInnerAccess();
		if (object == null)
			ThrowUtil.nullArg("object"); //$NON-NLS-1$

		if (keyFromName == null || keyToName == null) {
			int indexFrom = partList.getKeyIndex(keyFromName);
			if (indexFrom == -1 && keyFromName != null) {
				throwKeyNotFoundException(keyFromName);
			}
			int indexTo = partList.getKeyIndex(keyToName, indexFrom);
			if (indexTo == -1 && keyToName != null) {
				throwKeyToNotFoundException(keyFromName, keyToName);
			}
			internalReplace(indexFrom, indexTo, object);
			return;
		}

		int lastIndexFrom = -1;
		int lastIndexTo = -1;
		do {
			try {
				lastIndexFrom = partList.getKeyIndex(keyFromName, lastIndexTo);
				FastTemplateKey keyFrom = getKeyByIndex(lastIndexFrom, keyFromName);
				int index = partList.getKeyIndex(keyToName, lastIndexFrom);
				FastTemplateKey keyTo = getKeyToByIndex(index, keyFromName, keyToName);
				keyFrom.setData(object);
				keyFrom.setPairKey(keyTo);
				keyTo.setPairKey(keyFrom);
				lastIndexTo = index;
				// ustawienie wszystkiego co jest pomiêdzy kluczami do usuniêcia
				partList.setRemoved(lastIndexFrom + 1, lastIndexTo - 1);
			} catch (TemplateKeyNotFoundException e) {
				if (lastIndexTo == 0) {
					throw e;
				}
				break;
			}
		} while (true);
	}

	@Override
	public void replace(int keyFromIndex, int keyToIndex, Object object) throws TemplateKeyNotFoundException {
		checkNotInnerAccess();
		if (object == null)
			ThrowUtil.nullArg("object"); //$NON-NLS-1$
		checkIndexOrder(keyFromIndex, keyToIndex);
		internalReplace(keyFromIndex, keyToIndex, object);
	}

	/**
	 * 
	 * @param keyIndexFrom
	 * @param keyIndexTo
	 * @param object
	 * @throws TemplateKeyNotFoundException
	 */
	protected void internalReplace(int keyIndexFrom, int keyIndexTo, Object object) throws TemplateKeyNotFoundException {

		// ogs.wrln(110, "internalReplace from " + keyIndexFrom + " to " +
		// keyIndexTo);

		if (keyIndexFrom == -1 && keyIndexTo == -1) {
			// ogs.wrln(110, "replace 1");
			partList.clear();
			partList.add(new FastTemplateData(start, object));
			end = start;
		} else if (keyIndexFrom == -1) {
			// ogs.wrln(110, "replace 2");
			FastTemplateKey keyTo = getKeyByIndex(keyIndexTo);
			// pocz¹tek przechodzi na koniec klucza, który zostanie usuniêty
			start = keyTo.getOffset() + keyTo.getReplacedLength();
			partList.setPart(keyIndexTo, new FastTemplateData(start, object));
			if (keyIndexTo > 0) {
				partList.setRemoved(0, keyIndexTo - 1); // usuniêcie wszystkich
				// przed
			}
		} else if (keyIndexTo == -1) {
			// ogs.wrln(110, "replace 3");
			FastTemplateKey keyFrom = getKeyByIndex(keyIndexFrom);
			end = keyFrom.getOffset();
			partList.setPart(keyIndexFrom, new FastTemplateData(end, object));
			if (keyIndexFrom < partList.size() - 1) {
				partList.setRemoved(keyIndexFrom + 1, partList.size() - 1);
			}
		} else {
			// ogs.wrln(110, "replace 4");
			FastTemplateKey keyTo = getKeyByIndex(keyIndexTo);
			FastTemplateKey keyFrom = getKeyByIndex(keyIndexFrom);

			keyFrom.setData(object);
			keyFrom.setPairKey(keyTo);
			keyTo.setPairKey(keyFrom);

			// ustawienie wszystkiego co jest pomiêdzy kluczami do usuniêcia
			partList.setRemoved(keyIndexFrom + 1, keyIndexTo - 1);
		}
	}

	@Override
	public ITemplate copy(String keyFromName, String keyToName) throws TemplateKeyNotFoundException {
		checkAccess();
		int indexFrom = partList.getKeyIndex(keyFromName);
		if (indexFrom == -1 && keyFromName != null) {
			throwKeyNotFoundException(keyFromName);
		}
		int indexTo = partList.getKeyIndex(keyToName, indexFrom);
		if (indexTo == -1 && keyToName != null) {
			throwKeyToNotFoundException(keyFromName, keyToName);
		}
		return internalCopy(indexFrom, indexTo);
	}

	@Override
	public ITemplate copy(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException {
		checkAccess();
		checkIndexOrder(keyFromIndex, keyToIndex);
		return internalCopy(keyFromIndex, keyToIndex);
	}

	/**
	 * 
	 * @param keyIndexFrom
	 * @param keyIndexTo
	 * @return
	 * @throws TemplateKeyNotFoundException
	 */
	protected ITemplate internalCopy(int keyIndexFrom, int keyIndexTo) throws TemplateKeyNotFoundException {

		// ogs.wrln(110, "ITemplate.copy: from " + keyIndexFrom + " to " +
		// keyIndexTo);

		if (keyIndexFrom == -1 && keyIndexTo == -1) {
			return (ITemplate) clone();
		} else if (keyIndexFrom == -1) {
			FastTemplateKey keyTo = getKeyByIndex(keyIndexTo);
			PartList parts = partList.copyParts(0, keyIndexTo - 1, false);
			return new FastTemplate(source, start, keyTo.getOffset(), parts);
		} else if (keyIndexTo == -1) {
			FastTemplateKey keyFrom = getKeyByIndex(keyIndexFrom);
			PartList parts = partList.copyParts(keyIndexFrom + 1, partList.size() - 1, false);
			return new FastTemplate(source, keyFrom.getOffset() + keyFrom.getReplacedLength(), end, parts);
		} else {
			checkIndexOrder(keyIndexFrom, keyIndexTo);
			FastTemplateKey keyFrom = getKeyByIndex(keyIndexFrom);
			FastTemplateKey keyTo = getKeyByIndex(keyIndexTo);
			PartList keys = partList.copyParts(keyIndexFrom + 1, keyIndexTo - 1, false);
			return new FastTemplate(source, keyFrom.getOffset() + keyFrom.getReplacedLength(), keyTo.getOffset(), keys);
		}
	}

	private FastTemplateKey getKeyByIndex(int index) throws TemplateKeyNotFoundException {
		FastTemplateKey key = partList.getKey(index);
		if (key == null) {
			throwKeyNotFoundException(index);
		}
		return key;
	}

	private FastTemplateKey findKeyByIndex(int index) {
		return partList.getKey(index);
	}

	@Override
	public String copyText(String keyFromName, String keyToName) throws TemplateKeyNotFoundException {
		checkAccess();
		int indexFrom = partList.getKeyIndex(keyFromName);
		if (indexFrom == -1 && keyFromName != null) {
			throwKeyNotFoundException(keyFromName);
		}
		int indexTo = partList.getKeyIndex(keyToName, indexFrom);
		if (indexTo == -1 && keyToName != null) {
			throwKeyToNotFoundException(keyFromName, keyToName);
		}
		return internalCopyText(indexFrom, indexTo);
	}

	@Override
	public String copyText(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException {
		checkAccess();
		checkIndexOrder(keyFromIndex, keyToIndex);
		return internalCopyText(keyFromIndex, keyToIndex);
	}

	/**
	 * 
	 * @param keyIndexFrom
	 * @param keyIndexTo
	 * @return
	 * @throws TemplateKeyNotFoundException
	 */
	protected String internalCopyText(int keyIndexFrom, int keyIndexTo) throws TemplateKeyNotFoundException {

		// ogs.wrln(110, "copyText() from " + keyIndexFrom + " to " +
		// keyIndexTo);

		int startOffset = start;
		int endOffset = end;

		if (keyIndexFrom == -1 && keyIndexTo == -1) {
			// //ogs.wrln(91,"double minus");
			return toString();
		} else if (keyIndexFrom == -1) {
			// //ogs.wrln(91,"left minus");
			FastTemplateKey keyTo = getKeyByIndex(keyIndexTo);
			endOffset = keyTo.getOffset();
		} else if (keyIndexTo == -1) {
			// //ogs.wrln(91,"rigth minus");
			FastTemplateKey keyFrom = getKeyByIndex(keyIndexFrom);
			startOffset = keyFrom.getOffset() + keyFrom.getReplacedLength();
			keyIndexTo = partList.size(); //
		} else {
			checkIndexOrder(keyIndexFrom, keyIndexTo);
			// //ogs.wrln(91,"no minus");
			FastTemplateKey keyFrom = getKeyByIndex(keyIndexFrom);
			FastTemplateKey keyTo = getKeyByIndex(keyIndexTo);
			startOffset = keyFrom.getOffset() + keyFrom.getReplacedLength();
			endOffset = keyTo.getOffset();
		}

		// //ogs.wrln(91,"startOffset:"+startOffset+" endOffset:"+endOffset+"
		// indexTo - indexFrom:"+(indexTo - indexFrom));

		if (keyIndexTo - keyIndexFrom > 1) {
			// to znaczy ¿e coœ pomiêdzy jest, wiêc mo¿e zawieraæ jakieœ rzeczy
			SimpleWriter writer = new SimpleWriter(30);
			ITemplateBuffer buffer = this.source.getBuffer();

			ITemplatePart[] parts = partList.getParts();

			for (int i = keyIndexFrom + 1; i < keyIndexTo; i++) {
				ITemplatePart part = parts[i];
				if (!part.isRemoved() && part.getOffset() >= startOffset) {

					int delta = (part.getOffset() - startOffset);
					if (delta > 0) {
						buffer.write(writer, startOffset, delta); // wrzucenie
					}
					part.write(writer);
					startOffset += (delta + part.getReplacedLength()); // przeskok
				}
			}

			if (startOffset < endOffset) {
				buffer.write(writer, startOffset, endOffset - startOffset);
			}

			return writer.toString();

		} else {
			ITemplateBuffer buffer = this.source.getBuffer();
			return buffer.substring(startOffset, endOffset);
		}

	}

	@Override
	public ITemplate cut(String keyFromName, String keyToName) throws TemplateKeyNotFoundException {
		checkNotInnerAccess();
		int indexFrom = partList.getKeyIndex(keyFromName);
		if (indexFrom == -1 && keyFromName != null) {
			throwKeyNotFoundException(keyFromName);
		}
		int indexTo = partList.getKeyIndex(keyToName, indexFrom);
		if (indexTo == -1 && keyToName != null) {
			throwKeyToNotFoundException(keyFromName, keyToName);
		}
		return internalCut(indexFrom, indexTo);
	}

	@Override
	public ITemplate cut(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException {
		checkNotInnerAccess();
		checkIndexOrder(keyFromIndex, keyToIndex);
		return internalCut(keyFromIndex, keyToIndex);
	}

	/**
	 * 
	 * @param keyIndexFrom
	 * @param keyIndexTo
	 * @return
	 * @throws TemplateKeyNotFoundException
	 */
	protected ITemplate internalCut(int keyIndexFrom, int keyIndexTo) throws TemplateKeyNotFoundException {

		// ogs.wrln(110, "ITemplate.cut: from "+keyIndexFrom+" to "+keyIndexTo);
		if (keyIndexFrom == -1 && keyIndexTo == -1) {
			ITemplate clone = (ITemplate) clone();
			partList.clear();
			end = start;
			return clone;
		} else if (keyIndexFrom == -1) {
			FastTemplateKey keyTo = getKeyByIndex(keyIndexTo);
			// wycina od indeksa do indeksa
			PartList parts = partList.cutParts(0, keyIndexTo - 1);
			keyTo.setRemoved(); // ustawia klucz na usuniêty
			ITemplate cut = new FastTemplate(source, start, keyTo.getOffset(), parts);
			start = keyTo.getOffset() + keyTo.getReplacedLength(); // przesuwa
			// offset po
			// wyciêciu
			return cut;
		} else if (keyIndexTo == -1) {
			FastTemplateKey keyFrom = getKeyByIndex(keyIndexFrom);
			PartList parts = partList.cutParts(keyIndexFrom + 1, partList.size() - 1);
			keyFrom.setRemoved(); // ustawia klucz na usuniêty
			ITemplate cut = new FastTemplate(source, keyFrom.getOffset() + keyFrom.getReplacedLength(), end, parts);
			end = keyFrom.getOffset(); // przesuwa koniec po wycieciu
			return cut;
		} else {
			checkIndexOrder(keyIndexFrom, keyIndexTo);
			FastTemplateKey keyFrom = getKeyByIndex(keyIndexFrom);
			FastTemplateKey keyTo = getKeyByIndex(keyIndexTo);
			PartList keys = partList.cutParts(keyIndexFrom + 1, keyIndexTo - 1);

			ITemplate cut = new FastTemplate(source, keyFrom.getOffset() + keyFrom.getReplacedLength(), keyTo
					.getOffset(), keys);

			keyFrom.setData("");
			keyFrom.setPairKey(keyTo);
			keyTo.setPairKey(keyFrom);

			return cut;
		}
	}

	/**
	 * 
	 * @param indexFrom
	 * @param indexTo
	 * @throws TemplateRuntimeException
	 */
	private void checkIndexOrder(int indexFrom, int indexTo) throws TemplateRuntimeException {
		if (indexTo >= 0 && indexTo <= indexFrom) {
			throw new TemplateRuntimeException("Invalid key index order; from " + indexFrom + " to " + indexTo);
		}
	}

	@Override
	public ITemplateKey getKey(String keyName) {
		checkAccess();
		ITemplatePart key = partList.getKey(keyName);
		if (key instanceof ITemplateKey) {
			return (ITemplateKey) key;
		}
		return null;
	}

	@Override
	public int getKeyIndex(String keyName) {
		checkAccess();
		return partList.getKeyIndex(keyName);
	}

	@Override
	public ITemplateKey getKey(int keyIndex) {
		checkAccess();
		return findKeyByIndex(keyIndex);
	}

	@Override
	public boolean hasKey(String keyName) {
		return getKeyIndex(keyName) >= 0;
	}

	/**
	 * 
	 * @return
	 */
	public PartList getPartList() {
		return this.partList;
	}

	@Override
	public List<ITemplateKey> getKeys() {
		checkAccess();
		List<ITemplateKey> out = new LinkedList<ITemplateKey>();
		for (int i = 0; i < partList.size(); i++) {
			ITemplatePart part = partList.getPart(i);
			if (part instanceof FastTemplateKey) {
				FastTemplateKey key = (FastTemplateKey) part;
				if (!key.isRemoved() && !key.hasData()) {
					out.add((FastTemplateKey) part);
				}
			}
		}
		return out;
	}

	@Override
	public int getKeyCount() {
		checkAccess();
		int count = 0;
		for (int i = 0; i < partList.size(); i++) {
			ITemplatePart part = partList.getPart(i);
			if (part instanceof FastTemplateKey) {
				FastTemplateKey key = (FastTemplateKey) part;
				if (!key.isRemoved() && !key.hasData()) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public int getLength() {
		checkAccess();
		// //ogs.wrln(91, "FastTemplate.getLength() start:" + start + " end:" +
		// end);
		int length = 0;
		int lastOffset = start;

		ITemplatePart[] parts = partList.getParts();
		for (int i = 0; i < partList.size(); i++) {
			ITemplatePart part = parts[i];
			if (!part.isRemoved() && part.getOffset() >= lastOffset) {
				// //ogs.wrln(91, "found:" + part + " lastOffset:" +
				// lastOffset);
				int delta = (part.getOffset() - lastOffset);
				length += delta;

				// //ogs.wrln(91, "length UP 1 " + length);
				if (part.hasData() || part.getPartType() == ITemplatePart.DATA_PART) {
					length += part.getInsertedLength();
					// //ogs.wrln(91, "length UP 2-1 " + length);
				} else {
					length += part.getReplacedLength();
					// //ogs.wrln(91, "length UP 2-2 " + length);
				}
				lastOffset += (delta + part.getReplacedLength());
				// //ogs.wrln(91, "lastOffset UP " + lastOffset);
			}
		}

		if (lastOffset < end) {
			length += (end - lastOffset);
		}

		return length;
	}

	@Override
	public FastTemplate clone() {
		checkAccess();
		try {
			FastTemplate clone = (FastTemplate) super.clone();
			clone.partList = (PartList) partList.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new TemplateRuntimeException(e);
		}
	}

	/**
	 * 
	 * @param nr
	 * @param msg
	 */
	public void printBuffer(int nr, String msg) {
		SimpleWriter writer = new SimpleWriter(getLength());
		write(writer);
		// String out = writer.toString();
		// ogs.wrln(nr, "-----------------------------------\n\n" + out +
		// " !len:" + out.length());
	}

	/**
	 * 
	 * @param nr
	 * @param msg
	 */
	public void printAvailableKeys(int nr, String msg) {
		partList.printParts(nr, msg);
	}

	/**
	 * 
	 * @throws TemplateRuntimeException
	 */
	private void checkAccess() throws TemplateRuntimeException {
		if (source == null) {
			throw new TemplateRuntimeException(
					"Tamplate is disposed; template:" + getName());
		}
	};

	/**
	 * 
	 * @throws TemplateRuntimeException
	 */
	private void checkNotInnerAccess() throws TemplateRuntimeException {
		if (inner == true) {
			throw new TemplateRuntimeException(
					"Tamplate has been put into another template and cannot be read or modify; template:" + getName());
		}
	};

	/**
	 * 
	 * @param keyIndex
	 * @param keyName
	 * @return
	 * @throws TemplateKeyNotFoundException
	 */
	private FastTemplateKey getKeyByIndex(int keyIndex, String keyName) throws TemplateKeyNotFoundException {
		if (keyIndex < 0) {
			throwKeyNotFoundException(keyName);
		}
		return (FastTemplateKey) partList.getPart(keyIndex);
	}

	/**
	 * 
	 * @param keyIndex
	 * @param keyFrom
	 * @param keyTo
	 * @return
	 * @throws TemplateKeyNotFoundException
	 */
	private FastTemplateKey getKeyToByIndex(int keyIndex, String keyFrom, String keyTo)
			throws TemplateKeyNotFoundException {
		if (keyIndex < 0) {
			throwKeyToNotFoundException(keyFrom, keyTo);
		}
		return (FastTemplateKey) partList.getPart(keyIndex);
	}

	/**
	 * 
	 * @param keyName
	 * @throws TemplateKeyNotFoundException
	 */
	private void throwKeyNotFoundException(String keyName) throws TemplateKeyNotFoundException {
		throw new TemplateKeyNotFoundException("Key " + keyName + " not found; template:" + getName());
	};

	/**
	 * 
	 * @param keyFrom
	 * @param keyTo
	 * @throws TemplateKeyNotFoundException
	 */
	private void throwKeyToNotFoundException(String keyFrom, String keyTo) throws TemplateKeyNotFoundException {
		throw new TemplateKeyNotFoundException("Key " + keyTo + " not found after key " + keyFrom + "; template:"
				+ this.getName());
	};

	/**
	 * 
	 * @param index
	 * @throws TemplateKeyNotFoundException
	 */
	private void throwKeyNotFoundException(int index) throws TemplateKeyNotFoundException {
		if (index < 0 || index >= partList.size()) {
			throw new TemplateKeyNotFoundException("Key of index " + index + " not found; template:" + getName());
		}
		throw new TemplateKeyNotFoundException("Key of index " + index
				+ " not found (could be replaced or removed); template:" + getName());
	}

	@Override
	public void write(IWriter writer) {
		// ogs.wrln(119, "template:\n"+source.getBuffer().toString()
		// +" \n write ...");
		int lastOffset = start;
		ITemplateBuffer buffer = source.getBuffer();

		ITemplatePart[] parts = partList.getParts();
		for (int i = 0; i < partList.size(); i++) {
			ITemplatePart part = parts[i];
			if (!part.isRemoved() && part.getOffset() >= lastOffset) {
				int delta = (part.getOffset() - lastOffset);
				if (delta > 0) {
					// ogs.wrln(119,
					// ">>>> write 1; from "+lastOffset+" to "+(lastOffset+delta)
					// );
					buffer.write(writer, lastOffset, delta); // wrzucenie
				}
				// ogs.wrln(119, ">>>> write 2; part content");
				part.write(writer); // wrzucenie tego co ma klucz/part
				lastOffset += (delta + part.getReplacedLength()); // przeskok
			}
		}

		if (lastOffset < end) {
			// ogs.wrln(119, ">>>> write 3; from "+lastOffset+" to "+(end -
			// lastOffset));
			buffer.write(writer, lastOffset, end - lastOffset);
		}

	}

	// ==========================================================================
	// ======================
	// Pakietowe i prywatne metody etc

	/**
	 * Zwraca nazwê subtemplate okreœlon¹ przez offset
	 * 
	 * @param startKey
	 * @param enKey
	 * @return
	 */
	private String getSubTemplateName(int startOffset, int endOffset) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("SUBTEMPLATE [");
		buffer.append(start + "-");
		buffer.append(end + "] of ");
		buffer.append(source.getLocation());
		return buffer.toString();
	}

	@Override
	public String toString() {
		SimpleWriter writer = new SimpleWriter(getLength());
		write(writer);
		return writer.toString();
	}

	/**
	 * 
	 * @return
	 */
	public ITemplateSource getSource() {
		return source;
	}

	@Override
	public ITemplateSequence newSequence(String keyFromName, String keyToName, String[] innerKeyNames, int nLoops)
			throws TemplateKeyNotFoundException {
		checkAccess();
		int indexFrom = partList.getKeyIndex(keyFromName);
		if (indexFrom == -1 && keyFromName != null) {
			throwKeyNotFoundException(keyFromName);
		}
		int indexTo = partList.getKeyIndex(keyToName, indexFrom);
		if (indexTo == -1 && keyToName != null) {
			throwKeyToNotFoundException(keyFromName, keyToName);
		}
		return internalNewSequence(indexFrom, indexTo, innerKeyNames, nLoops);
	}

	@Override
	public ITemplateSequence newSequence(int keyFromIndex, int keyToIndex, String[] innerKeyNames, int nLoops)
			throws TemplateKeyNotFoundException {
		checkAccess();
		checkIndexOrder(keyFromIndex, keyToIndex);
		return internalNewSequence(keyFromIndex, keyToIndex, innerKeyNames, nLoops);
	}

	/**
	 * 
	 * @param keyFromIndex
	 * @param keyToIndex
	 * @param innerKeyNames
	 * @param nLoops
	 * @return
	 * @throws TemplateKeyNotFoundException
	 */
	protected ITemplateSequence internalNewSequence(int keyFromIndex, int keyToIndex, String[] innerKeyNames, int nLoops)
			throws TemplateKeyNotFoundException {
		checkSequenceIndexies(keyFromIndex, keyToIndex);

		int innerIndexFrom = keyFromIndex + 1;
		int innerIndexTo = (keyToIndex == -1 ? partList.size() - 1 : keyToIndex - 1);
		int innerKeyCount = innerIndexTo - innerIndexFrom + 1;
		if (innerKeyCount != innerKeyNames.length) {
			throw new TemplateKeyNotFoundException("Invalid inner key count:" + innerKeyCount + " required:"
					+ innerKeyNames.length + "; tamplate:" + getName());
		}
		StringBuilder notFoundInnerKeyNames = null;
		for (int i = 0; i < innerKeyNames.length; i++) {
			FastTemplateKey key = partList.getKey(innerIndexFrom + i);
			if (key == null || key.getName().equals(innerKeyNames[i]) == false) {
				if (notFoundInnerKeyNames == null) {
					notFoundInnerKeyNames = new StringBuilder(50);
				}
				notFoundInnerKeyNames.append(innerKeyNames[i]);
				notFoundInnerKeyNames.append(", ");
			}
		}
		if (notFoundInnerKeyNames != null) {
			throw new TemplateKeyNotFoundException("Inner keys not found:"
					+ notFoundInnerKeyNames.substring(0, notFoundInnerKeyNames.length() - 2) + "; tamplate:"
					+ getName());
		}
		return new TemplateSequence(createSequenceParts(keyFromIndex, keyToIndex), nLoops);
	}

	/**
	 * 
	 * @param indexFrom
	 *            - klucz pocz¹tkowy-graniczny sekwencji
	 * @param indexTo
	 *            - klucz koñcowy-graniczny sekwencji
	 * @return
	 * @throws TemplateKeyNotFoundException
	 */
	protected ISequencePart[] createSequenceParts(int indexFrom, int indexTo) throws TemplateKeyNotFoundException {

		int size = 0;
		if (indexFrom == -1 && indexTo == -1) {
			size = partList.size() + 1; // liczba fragmentów pomiêdzy kluczami =
			// nKeys + 1
		} else if (indexFrom == -1) {
			size = indexTo + 1;
		} else if (indexTo == -1) {
			size = partList.size() - indexFrom;
		} else {
			size = indexTo - indexFrom;
		}

		ISequencePart[] parts = new ISequencePart[size];

		int lastOffset = this.start;

		if (indexFrom >= 0) {
			FastTemplateKey keyFrom = getKeyByIndex(indexFrom);
			lastOffset = keyFrom.getOffset() + keyFrom.getReplacedLength();
		}

		int from = indexFrom + 1;
		int to = (indexTo == -1 ? partList.size() - 1 : indexTo);

		int count = 0;
		for (int i = from; i <= to; i++) {
			FastTemplateKey key = findKeyByIndex(i);
			if (key == null) {
				return null;
			}
			int length = key.getOffset() - lastOffset;
			// ogs.wrln(91," partOffset:"+lastOffset+" partLength:"+length);
			parts[count++] = new SequencePart(getSource().getBuffer(), lastOffset, length);
			lastOffset = key.getOffset() + key.getReplacedLength();
		}

		if (indexTo == -1) {
			parts[count++] = new SequencePart(getSource().getBuffer(), lastOffset, this.end - lastOffset);
		}

		return parts;
	}

	/**
	 * 
	 * @param indexKeyFrom
	 * @param indexKeyTo
	 * @param more
	 */
	private void checkSequenceIndexies(int indexKeyFrom, int indexKeyTo) {
		boolean noKeys = false;
		if (indexKeyFrom == -1 && indexKeyTo == -1 && partList.size() <= 1) {
			noKeys = true;
		} else if (indexKeyFrom == -1 && indexKeyTo >= 0 && indexKeyTo - indexKeyFrom <= 1) {
			noKeys = true;
		} else if (indexKeyTo == -1 && indexKeyFrom >= 0 && partList.size() - indexKeyFrom == 1) {
			noKeys = true;
		} else if (indexKeyTo >= 0 && indexKeyFrom >= 0 && indexKeyTo - indexKeyFrom <= 1) {
			noKeys = true;
		}
		String errorMsg = null;
		if (noKeys) {
			errorMsg = "Sequence cannot be created. There is no keys between specified keys.";
		} else if (partList.containDataOrRemovedParts(indexKeyFrom, indexKeyTo)) {
			errorMsg = "Sequence cannot be created. Template has been modified beetween specified keys.";
		}
		if (errorMsg != null) {
			FastTemplateKey keyFrom = findKeyByIndex(indexKeyFrom);
			FastTemplateKey keyTo = findKeyByIndex(indexKeyTo);
			throw new TemplateRuntimeException(errorMsg + " from " + (keyFrom != null ? keyFrom.getName() : "NULL")
					+ " to " + (keyTo != null ? keyTo.getName() : "NULL") + "; tamplate:" + getName());
		}
	}

	public int getStart() {
		return start;
	}

	@Override
	public void dispose() {
		partList.dispose();
		partList = null;
		source.dispose();
		source = null;
	}

	/**
	 * 
	 * @return
	 */
	public String debug() {
		SimpleWriter writer = new SimpleWriter(getLength());
		debug(0, writer);
		return writer.toString();
	}

	/**
	 * 
	 * @param level
	 * @param writer
	 */
	public void debug(int level, IWriter writer) {
		writer.write("\n-------------------------------------------------------\nLEVEL:" + level + "  template:"
				+ this.getSource().getLocation() + "\n");

		int lastOffset = start;
		ITemplateBuffer buffer = source.getBuffer();

		ITemplatePart[] parts = partList.getParts();
		for (int i = 0; i < partList.size(); i++) {
			ITemplatePart part = parts[i];
			if (!part.isRemoved() && part.getOffset() >= lastOffset) {
				int delta = (part.getOffset() - lastOffset);
				if (delta > 0) {
					// ogs.wrln(110, ">>>> write 1; from "+lastOffset+" to
					// "+(lastOffset+delta));
					buffer.write(writer, lastOffset, delta); // wrzucenie
				}
				// ogs.wrln(110, ">>>> write 2; part content");
				writer.write("$" + part.getName() + "$");
				// part.write(writer);
				lastOffset += (delta + part.getReplacedLength()); // przeskok
			}
		}

		if (lastOffset < end) {
			// ogs.wrln(110, ">>>> write 3; from "+lastOffset+" to "+(end -
			// lastOffset));
			buffer.write(writer, lastOffset, end - lastOffset);
		}

		writer.write("\n***** NEXT LEVEL ***** ");
		for (int i = 0; i < partList.size(); i++) {
			ITemplatePart part = parts[i];
			part.debug(level + 1, writer);
		}

	}

}
