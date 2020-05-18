
package net.hottree.template.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import net.hottree.template.ITemplate;
import net.hottree.template.ITemplateSequence;

/**
 * Klasa <tt>TemplateManager</tt> s³u¿y do zarz¹dzania szablonami HotTree w
 * aplikacji. Umo¿liwia pobierania szablonów o okreœlonej lokalizacji lub ich
 * fragmentów. Wszystkie pobrane szablony s¹ skanowane w calu znalezienia kluczy
 * zawartych pomiêdzy okreœlonymi znacznikami. Domyœlnie pocz¹tkowym znacznikiem
 * kluczy jest ci¹g "*[" zaœ koñcowym "]*". Skanowana jest równie¿ zawartoœæ
 * kluczy w celu znalezienia ich w³aœciwoœci. Zawartoœæ kluczy ma nastêpujêcy
 * format KEY_NAME(propertyName1=property1;propertyName2=property2,...) i nie
 * mog¹ one zawieraæ znaków ' ', '\t', '\n', '\r'.
 */
public final class TemplateManager {

	private final static char FILE_SEPARATOR = '/';

	private final Map<String, FastTemplate> templates = new HashMap<String, FastTemplate>();

	/**
	 * The root directory
	 */
	private final FileObject rootDir;

	private final boolean cache;

	private ITemplateKeyCreator keyCreator = null;

	private final String defaultCharsetName;

	/**
	 * 
	 * @param rootDir
	 */
	public TemplateManager(FileObject rootDir) {
		this(rootDir, true);
	}

	/**
	 * 
	 * @param rootDir
	 * @param cache
	 */
	public TemplateManager(FileObject rootDir, boolean cache) {
		this(rootDir, cache, "*[", "]*", null);
	}

	/**
	 * 
	 * @param rootDir
	 * @param cache
	 * @param keyStartSeqnence
	 * @param keyEndSequence
	 * @param defaultCharsetName
	 */
	public TemplateManager(FileObject rootDir, boolean cache, String keyStartSeqnence, String keyEndSequence,
			String defaultCharsetName) {
		if (rootDir == null)
			ThrowUtil.nullArg("rootDir");
		if (keyStartSeqnence == null)
			ThrowUtil.nullArg("keyStartSeqnence");
		if (keyEndSequence == null)
			ThrowUtil.nullArg("keyEndSequence");
		if (keyStartSeqnence.length() == 0)
			ThrowUtil.err("keyStartSeqnence cannot be an empty string");
		if (keyEndSequence.length() == 0)
			ThrowUtil.err("keyEndSequence cannot be an empty string");

		this.rootDir = rootDir;
		this.cache = cache;
		this.keyCreator = new TemplateKeyCreator(keyStartSeqnence, keyEndSequence);
		this.defaultCharsetName = defaultCharsetName == null ? "UTF-8" : defaultCharsetName;
	}

	/**
	 * 
	 * @param location
	 * @return
	 */
	public boolean hasTemplate(String location) {
		try {
			getTemplateFileOrThrow(rootDir, location);
			return true;
		} catch (TemplateNotFoundException e) {
			// ignore
		}
		return false;
	}

	/**
	 * 
	 * @param location
	 * @return
	 * @throws TemplateNotFoundException
	 */
	public ITemplate getTemplate(String location) throws TemplateNotFoundException {
		return getTemplate(location, defaultCharsetName);
	}

	/**
	 * 
	 * @param location
	 * @param charsetName
	 * @return
	 * @throws TemplateNotFoundException
	 */
	public ITemplate getTemplate(String location, String charsetName) throws TemplateNotFoundException {
		if (cache) {
			return (ITemplate) getFastTemplate(location, charsetName).clone();
		} else {
			return getFastTemplate(location, charsetName);
		}
	}

	/**
	 * 
	 * @param location
	 * @param charsetName
	 * @return
	 * @throws TemplateNotFoundException
	 */
	private FastTemplate getFastTemplate(String location, String charsetName) throws TemplateNotFoundException {
		if (StringUtils.isEmpty(charsetName)) {
			charsetName = defaultCharsetName;
		}
		if (cache) {
			FastTemplate template = templates.get(location);
			if (template == null) {
				template = new FastTemplate(new FastTemplateSource(getTemplateSource(location, charsetName)),
						new KeyScanner(keyCreator));
				templates.put(location, template);
			}
			return template;
		}
		return new FastTemplate(new FastTemplateSource(getTemplateSource(location, charsetName)),
				new KeyScanner(keyCreator));
	}

	public ITemplate getTemplate(String location, String keyFromName, String keyToName)
			throws TemplateNotFoundException, TemplateKeyNotFoundException {
		return getTemplate(location, defaultCharsetName, keyFromName, keyToName);
	}

	/**
	 * 
	 * @param location
	 * @param charsetName
	 * @param keyFromName
	 * @param keyToName
	 * @return
	 * @throws TemplateNotFoundException
	 * @throws TemplateKeyNotFoundException
	 */
	public ITemplate getTemplate(String location, String charsetName, String keyFromName, String keyToName)
			throws TemplateNotFoundException, TemplateKeyNotFoundException {
		return getFastTemplate(location, charsetName).copy(keyFromName, keyToName);
	}

	/**
	 * 
	 * @param location
	 * @param charsetName
	 * @param keyFromName
	 * @param keyToName
	 * @param nLoops
	 * @return
	 * @throws TemplateNotFoundException
	 * @throws TemplateKeyNotFoundException
	 */
	public ITemplateSequence getTemplateSequence(String location, String charsetName, String keyFromName,
			String keyToName, int nLoops) throws TemplateNotFoundException, TemplateKeyNotFoundException {
		return getFastTemplate(location, charsetName).newSequence(keyFromName, keyToName, null, nLoops);
	}

	/**
	 * 
	 * @param location
	 * @param charsetName
	 * @return
	 * @throws TemplateNotFoundException
	 */
	private TemplateBuffer getTemplateSource(String location, String charsetName) throws TemplateNotFoundException {

		FileObject file = getTemplateFileOrThrow(rootDir, location);
		try (InputStream input = file.getContent().getInputStream()) {
			String content = IOUtils.toString(input, charsetName);
			TemplateBuffer out = new TemplateBuffer(location);
			out.append(content);
			return out;
		} catch (IOException e) {
			throw new TemplateNotFoundException(e);
		} finally {

		}
	}

	private FileObject getTemplateFileOrThrow(FileObject root, String relativePath) throws TemplateNotFoundException {
		if (StringUtils.isEmpty(relativePath)) {
			throw new TemplateRuntimeException("Empty template path.");
		}
		relativePath = relativePath.replace('\\', FILE_SEPARATOR);
		if (relativePath.charAt(0) == FILE_SEPARATOR) {
			relativePath = relativePath.substring(1);
		}
		try {
			FileObject file = root.resolveFile(relativePath);
			if (file.exists() == false) {
				throw new TemplateNotFoundException(
						"Template not found:" + relativePath + " in " + rootDir.getURL().getPath());
			}
			return file;
		} catch (FileSystemException e) {
			throw new TemplateNotFoundException(e);
		}
	}

}
