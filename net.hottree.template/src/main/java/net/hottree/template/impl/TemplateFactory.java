/**
 * 
 */
package net.hottree.template.impl;

import net.hottree.template.ITemplate;
import net.hottree.template.ITemplateFactory;

/**
 * @author MW
 * 
 */
public class TemplateFactory implements ITemplateFactory {

	private final ITemplateKeyCreator keyCreator;
	private final boolean breakOnWhitespace;

	/**
	 * 
	 * @param keyStartSeqnence
	 * @param keyEndSequence
	 * @param breakOnWhitespace
	 * @param parseProperties
	 */
	public TemplateFactory(String keyStartSeqnence, String keyEndSequence, boolean breakOnWhitespace, boolean parseProperties) {
		this.breakOnWhitespace = breakOnWhitespace;
		this.keyCreator = parseProperties ? new TemplateKeyCreator(keyStartSeqnence, keyEndSequence) : new BasicTemplateKeyCreator(keyStartSeqnence, keyEndSequence);
	}

	@Override
	public ITemplate createTemplate(String content) {
		if (content == null) {
			content = "";
		}
		return new FastTemplate(new FastTemplateSource(new TemplateBuffer(content, "")), new KeyScanner(keyCreator, breakOnWhitespace));
	}

}
