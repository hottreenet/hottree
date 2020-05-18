
package net.hottree.template.impl;


/**
 * 
 * @author MW
 *
 */
public class TemplateNotFoundException extends TemplateRuntimeException {

	private static final long serialVersionUID = -3461843701524656167L;

	/**
	 * 
	 * @param msg
	 */
	public TemplateNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param cause
	 */
	public TemplateNotFoundException(Throwable cause) {
		super(cause);
	}

}
