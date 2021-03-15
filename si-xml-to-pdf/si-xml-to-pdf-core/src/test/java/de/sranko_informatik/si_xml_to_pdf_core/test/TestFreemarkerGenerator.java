package de.sranko_informatik.si_xml_to_pdf_core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import de.sranko_informatik.si_xml_to_pdf_core.FreemarkerGenerator;
import freemarker.template.TemplateException;

class TestFreemarkerGenerator {
	static String src = "src/test/resources/";

	public static void main(String[] args) throws IOException, TemplateException, SAXException, ParserConfigurationException {
		generateHTML();
		generatePDF();
	}
	
	@Test
	static
	void generateHTML() throws IOException, TemplateException, SAXException, ParserConfigurationException {

		FreemarkerGenerator fgen = new FreemarkerGenerator(src);

		String html = fgen.generateHTML(src.concat("test.xml"), "test.ftl", Locale.GERMAN);

		assertEquals("<html>Datan aus Element 1</html>", html, "HTML generate");
	}
	
	@Test
	static
	void generatePDF() throws IOException, TemplateException, SAXException, ParserConfigurationException {

		FreemarkerGenerator fgen = new FreemarkerGenerator(src);

		byte[] pdf = fgen.generatePDF(src.concat("test.xml"), "test.ftl", Locale.GERMAN);

		//assertEquals("<html>Datan aus Element 1</html>", html, "HTML generate");
	}

}
