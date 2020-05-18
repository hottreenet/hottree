/**
 * 
 */
package net.hottree.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import net.hottree.template.ITemplate;
import net.hottree.template.ITemplateFactory;
import net.hottree.template.ITemplateSequence;
import net.hottree.template.impl.TemplateFactory;

/**
 * @author MW
 *
 */

public class TemplateTest {
	
	private static ITemplate PAGE1;

	private static ITemplate PAGE2;

	private static ITemplate TABLE1;

	@BeforeClass
	public static void before() throws IOException {
		ITemplateFactory factory = new TemplateFactory("*[", "]*", true, false);
		PAGE1 = factory.createTemplate(getResourceContent("./input/Page1.html"));
		PAGE2 = factory.createTemplate(getResourceContent("./input/Page2.html"));
		TABLE1 = factory.createTemplate(getResourceContent("./input/Table1.html"));
	}

	private static String getResourceContent(String relativePath) throws IOException {
		return IOUtils.toString(TemplateTest.class.getClassLoader().getResourceAsStream(relativePath), "UTF-8");
	}

	@Test
	public void testReplaceKeys() throws IOException {
		ITemplate page = PAGE1.clone();
		page.replace("TITLE", "Page title");
		page.replace("MESSAGE", "Same message");
		String text = page.toString();
		assertThat(text, equalTo(getResourceContent("./output/TestReplaceKeysResult.html")));
	}

	@Test
	public void testReplaceContentBetweenKeys() throws IOException {
		ITemplate page = PAGE2.clone();
		page.replace("TITLE", "Page title");
		page.replace("BODY_S", "BODY_E", "Same body");
		String text = page.toString();
		assertThat(text, equalTo(getResourceContent("./output/TestReplaceContentBetweenKeysResult.html")));
	}

	@Test
	public void testBuildLettersTable() throws IOException {
		ITemplate table = TABLE1.copy("TABLE_S", "TABLE_E");
		ITemplateSequence rowSeq = TABLE1.newSequence("ROW_S", "ROW_E", new String[] { "DEC", "LETTER" }, 1);
		for (int i = 65; i <= 90; i++) {
			rowSeq.newLoop();
			rowSeq.replace(i);
			rowSeq.replace(Character.toString((char) i));
		}
		table.replace("ROWS", rowSeq);
		
		ITemplate page = PAGE2.clone();
		page.replace("TITLE", "Letters");
		page.replace("BODY_S", "BODY_E", table);
		String text = page.toString();
		System.out.println("text:\n"+text);
		String result = getResourceContent("./output/TestBuildLettersTableResult.html");
		assertThat(text, equalTo(result));
	}

}
