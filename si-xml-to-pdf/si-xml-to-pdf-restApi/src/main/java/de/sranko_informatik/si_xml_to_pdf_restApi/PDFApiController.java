package de.sranko_informatik.si_xml_to_pdf_restApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.File;
import java.io.StringReader;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import de.sranko_informatik.si_xml_to_pdf_core.FreemarkerGenerator;
import freemarker.template.TemplateException;
import freemarker.ext.dom.NodeModel;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
 
@RestController
public class PDFApiController
{
	@RequestMapping(
			  value = "/getPDF",
			  method = RequestMethod.POST,
			  headers = "Accept=application/json",
			  produces = MediaType.APPLICATION_PDF_VALUE
			)
    public @ResponseBody byte[] getPDF(@RequestBody String payload) throws FileNotFoundException 
    {
		
		File file = new File("/tmp/si-xml-to-pdf.log");
		PrintStream ps = new PrintStream(file);
		ps.println(payload);ps.flush();
		System.setOut(ps);
		System.setErr(ps);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		JSONPayload req;
		
		try {
			req = mapper.readValue(payload, JSONPayload.class);
			ps.println(req.toString());ps.flush();
		} catch (JsonProcessingException sxe ) {
			sxe.printStackTrace(ps);ps.flush();
			//throw new ResponseStatusException(HttpStatus.NOT_FOUND, "XML parsing problem.", sxe);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "XML parsing problem", sxe);
		}
		
		File templateFile = new File(req.getTemplate());

		Map dataModel = new HashMap();
		
		switch(req.getDatenTyp()){ 
        case "json": 
        	dataModel.put("json", req.getData());
            break; 
        case "xml": 
    		try {
    			InputSource inputSource = new InputSource( new StringReader(req.getData()));
    			dataModel.put("xml", NodeModel.parse(inputSource));
    		} catch (IOException|SAXException|ParserConfigurationException ioe ) {
    			ioe.printStackTrace(ps);ps.flush();
    			//throw new ResponseStatusException(HttpStatus.NOT_FOUND, "XML parsing problem.", ioe);
    			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "XML parsing problem", ioe);
    		}
    		
            break; 
        default: 
        	dataModel.put("json", req.getData());
            break; 
        } 
	   
		FreemarkerGenerator fgen;
		try {
			fgen = new FreemarkerGenerator(templateFile.getParent());
		} catch (IOException ioe ) {
			ioe.printStackTrace(ps);ps.flush();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Generator konnte nicht gestartet werden", ioe);
		}
		
		File tplName = new File(req.getTemplate());
		byte[] pdf;
		try {
			pdf = fgen.generatePDF(dataModel, tplName.getName(), Locale.GERMAN);
		} catch (TemplateException|IOException te ) {
		    te.printStackTrace(ps);ps.flush();
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Template systax problem. More details see in ".concat(file.getAbsolutePath()), te);
		} catch  (SAXException sxe) {
		    sxe.printStackTrace(ps);ps.flush();
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Parsing konnte nicht ausgefuhrt werden. More details see in ".concat(file.getAbsolutePath()), sxe);
		} catch  (ParserConfigurationException pce) {
		    pce.printStackTrace(ps);ps.flush();
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Parser Configuration Problem. More details see in ".concat(file.getAbsolutePath()), pce);
		}
		return pdf; 
    }
}
