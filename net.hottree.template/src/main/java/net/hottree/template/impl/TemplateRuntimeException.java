package net.hottree.template.impl;

/**
 * 
 * @author MW
 * 
 */
public class TemplateRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3012880971909606363L;

	/**
	 * 
	 * @param msg
	 */
	public TemplateRuntimeException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param cause
	 */
	public TemplateRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public TemplateRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
