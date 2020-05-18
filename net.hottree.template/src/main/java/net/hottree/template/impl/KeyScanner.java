/**
 * 
 */
package net.hottree.template.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import net.hottree.template.ITemplateBuffer;
import net.hottree.template.ITemplateKey;
import net.hottree.template.ITemplateSource;

/**
 * @author MW
 * 
 */
public class KeyScanner implements IKeyScanner {

	public final static int EOF = -1;

	/**
	 * 
	 */
	private static char[] BREAK_CHARS = {' ', '\t', '\n', '\r'};

	private static char[] BREAK_CHARS_WITHOUT_WHITESPACE = {'\n', '\r'};

	private static ITemplateKeyCreator defaultKeyCreator = new TemplateKeyCreator("*[", "]*");

	/**
	 * 
	 */
	private char firstChar = '*';
	
	private char[] breakChars;

	// ------------------------------------------------------

	private ITemplateBuffer text;

	private List<ITemplateKey> keys = null;

	private ITemplateSource source;

	private final ITemplateKeyCreator keyCreator;

	/**
	 * 
	 */
	public KeyScanner() {
		this(defaultKeyCreator);
	}

	/**
	 * 
	 * @param keyCreator
	 */
	public KeyScanner(ITemplateKeyCreator keyCreator) {
		this(keyCreator, true);
	}

	/**
	 * 
	 * @param keyCreator
	 */
	public KeyScanner(ITemplateKeyCreator keyCreator, boolean breakOnWhitespace) {
		this.keyCreator = keyCreator;
		this.firstChar = keyCreator.getKeyStartSequence()[0];
		this.breakChars = breakOnWhitespace ? BREAK_CHARS : BREAK_CHARS_WITHOUT_WHITESPACE;
	}

	@Override
	public ITemplateSource getSource() {
		return source;
	}

	@Override
	public ITemplateKey[] scann(ITemplateSource source) {
		this.source = source;
		this.text = source.getBuffer();

		if (keys == null) {
			keys = new ArrayList<ITemplateKey>(10);
		} else {
			keys.clear();
		}

		scannAll();
		
		ITemplateKey[] out = keys.toArray(new ITemplateKey[keys.size()]);
		keys.clear();
		keys = null;
		return out;
	}

	@Override
	public ITemplateKeyCreator getKeyCreator() {
		return keyCreator;
	}

	/**
	 * 
	 * @param bufferOffset
	 * @param length
	 */
	private void scannAll() {
		// ogs.wrln(114, "scannAll() text.length():"+text.length());
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == firstChar) {
				i = detectKey(i, true);
			}
		}
	}

	/**
	 * Ta metoda jest wywo³ywana kiedy natrafiono na pierwszy znak key'a. Tutaj
	 * sprawdzane s¹ key'e, je¿eli jakiœ zosta³ zdetektowany to zostaje zwracany
	 * 
	 * @param text
	 * @param offset
	 * @return
	 */
	private int detectKey(int offset, boolean appendKey) {
		int nextOffset = detectSequenece(text, offset, keyCreator.getKeyStartSequence());
		// to znaczy ¿e nie natrafiono na sekwencje startow¹ klucza
		if (nextOffset == offset) {
			return offset;
		}
		// ogs.wrln(114,
		// "detected startSequence, offset:"+offset+" nextOffset:"+nextOffset);
		int endOffset = detectEndSequence(text, nextOffset, keyCreator.getKeyEndSequence());
		if (endOffset > nextOffset) {
			// ogs.wrln(114, "detected endSequence, endOffset:"+endOffset);
			int start = offset + keyCreator.getKeyStartSequence().length;
			int end = endOffset - keyCreator.getKeyEndSequence().length;

			ITemplateKey keyDetected = keyCreator
				.createKey(this, offset, endOffset - offset, text.substring(start, end));
			if (appendKey) {
				keys.add(keyDetected);
			} else {
				// umieœæ klucz w odpowiednim miejscu
				int index = getKeyIndexAfterOffset(keyDetected.getOffset() + keyDetected.getLength());

				if (index == -1) {
					keys.add(keyDetected);
				} else if (index >= 0) {
					keys.add(index, keyDetected);
				} else if (index == -2) {
					// ..nie dodawaj klucza bo istnieje ju¿
				}

			}

			// ogs.wrln(38, "DETECT " + keyDetected + " CONTENT:" +
			// keyDetected.getText());

			// zwrócenie indeksu pomniejszonego o jeden, tak aby znak
			// wskazywa³ na ostatni znak wykrytego klucza, potem i tak
			// zostanie zwiêkszony o jeden
			return --endOffset;
		}

		return offset;
	}

	/**
	 * Zwraca indeks pierwszego klucza w liœcie który rozpoczony siê za
	 * offsetem, lub -1 je¿eli nie ma ju¿ ¿adnego klucza, lub -2 gdy klucz z
	 * takim offsetem istnieje
	 * 
	 * @param offset
	 * @return
	 */
	private int getKeyIndexAfterOffset(int offset) {
		// Log.wrln(38,"GET INDEX FOR "+offset);
		for (int i = 0; i < keys.size(); i++) {
			ITemplateKey key = keys.get(i);
			if (key.getOffset() == offset) {
				return -2;
			}
			if (key.getOffset() > offset) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param text
	 * @param offset
	 * @param sequence
	 * @return
	 */
	protected int detectSequenece(ITemplateBuffer text, int offset, char[] sequence) {
		// Log.wrln(38, "detectSequence");
		if (offset + sequence.length > text.length()) {
			return offset;
		}
		int i;
		for (i = 1; i < sequence.length; i++) {
			int ch = text.charAt(i + offset);
			// Log.wrln(38, "ch:" + (char) ch + " ?= " + sequence[i]);
			if (ch == KeyScanner.EOF) {
				return offset;
			} else if (ch != sequence[i]) {
				return offset;
			}
		}
		// je¿eli wszystko okey zwraca przesuniêty offset
		return offset + i;
	}

	/**
	 * Robi detekcje koñcowego wzorca w tym ¿e wy³apuje te¿ klucze wewnêtrzne,
	 * je¿eli na taki trafi to koñczy sprawdzanie i zwraca offset wejœciowy
	 * czyli false. Klucze nie mogê zawieraæ sekwencji innych kluczy w sobie
	 * 
	 * @param text
	 * @param offset
	 * @param endSequence
	 * @return
	 */
	protected int detectEndSequence(ITemplateBuffer text, int offset, char[] endSequence) {
		char ch;
		int pos = offset;
		while (pos < text.length() && !ArrayUtils.contains(breakChars, (ch = text.charAt(pos)))) {
			if (ch == endSequence[0]) {
				if (endSequence.length == 1) {
					// przeskocz o jeden bo inaczej by by³o offset + 0
					return pos + 1;
				} else {
					int nextOffset = detectSequenece(text, pos, endSequence);
					if (nextOffset > pos) {
						return nextOffset;
					}
				}
			}
			pos++;
		}
		return offset;
	}

}
