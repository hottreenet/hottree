/**
 * 
 */
package net.hottree.template.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author MW
 *
 */
public class FileReader extends InputStreamReader {

	/**
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public FileReader(String fileName) throws FileNotFoundException {
		super(new FileInputStream(fileName));
	}

	/**
	 * 
	 * @param fileName
	 * @param charsetName
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public FileReader(String fileName, String charsetName) throws FileNotFoundException, UnsupportedEncodingException {
		super(new FileInputStream(fileName), charsetName);
	}

	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public FileReader(File file) throws FileNotFoundException {
		super(new FileInputStream(file));
	}

	/**
	 * 
	 * @param file
	 * @param charsetName
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public FileReader(File file, String charsetName) throws FileNotFoundException, UnsupportedEncodingException {
		super(new FileInputStream(file), charsetName);
	}

}
