package de.sranko_informatik.si_xml_to_pdf_restApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import de.sranko_informatik.si_xml_to_pdf_core.FreemarkerGenerator;
import freemarker.template.TemplateException;
 
@RestController
public class TestController 
{
   @RequestMapping("/getPDF")
    public  @ResponseBody byte[] getPDF() throws SAXException, IOException, ParserConfigurationException, TemplateException 
    {
	   String src = "src/test/resources/";
	   
	   FreemarkerGenerator fgen = new FreemarkerGenerator(src, src.concat("test.xml"), Locale.GERMAN);
	   return fgen.generatePDF("test.ftl");
    }
}
