package de.sranko_informatik.si_xml_to_pdf_restApi;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import java.lang.StringBuilder;
 
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
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		JSONPayload req;
		
		try {
			req = mapper.readValue(payload, JSONPayload.class);
		} catch (JsonProcessingException sxe ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(sxe), sxe);
		}
		
		File templateFile = new File(req.getTemplate());

		Map dataModel = new HashMap();
		ObjectMapper mapJson = new ObjectMapper();
		
		switch(req.getDatenTyp()){ 

        case "xml": 
    		try {
    			InputSource inputSource = new InputSource( new StringReader(req.getData()));
    			dataModel.put("xml", NodeModel.parse(inputSource));
    		} catch (IOException|SAXException|ParserConfigurationException ioe ) {
    			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(ioe), ioe);
    		}
    		
            break; 
        default: 
        	//dataModel.put("json", req.getData());
    		try {
    			dataModel.put("json", mapJson.readValue(req.getData(), Map.class));
    		} catch (JsonProcessingException sxe ) {
    			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(sxe), sxe);
    		}
            break; 
        } 
	   
		FreemarkerGenerator fgen;
		try {
			fgen = new FreemarkerGenerator(templateFile.getParent());
		} catch (IOException ioe ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, convertStackTraceToString(ioe), ioe);
		}
		
		File tplName = new File(req.getTemplate());
		byte[] pdf;
		try {
			pdf = fgen.generatePDF(dataModel, tplName.getName(), Locale.GERMAN);
		} catch (TemplateException|IOException te ) {
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(te), te);
		} catch  (SAXException sxe) {
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(sxe), sxe);
		} catch  (ParserConfigurationException pce) {
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(pce), pce);
		}
		return pdf; 
    }
	
	@RequestMapping(
			  value = "/getHTML",
			  method = RequestMethod.POST,
			  headers = "Accept=application/json",
			  produces = MediaType.TEXT_HTML_VALUE
			)
  public @ResponseBody String getHTML(@RequestBody String payload) throws FileNotFoundException 
  {
			
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		JSONPayload req;
		
		try {
			req = mapper.readValue(payload, JSONPayload.class);
		} catch (JsonProcessingException sxe ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(sxe), sxe);
		}
		
		File templateFile = new File(req.getTemplate());

		Map dataModel = new HashMap();
		ObjectMapper mapJson = new ObjectMapper();
		
		switch(req.getDatenTyp()){ 

      case "xml": 
  		try {
  			InputSource inputSource = new InputSource( new StringReader(req.getData()));
  			dataModel.put("xml", NodeModel.parse(inputSource));
  		} catch (IOException|SAXException|ParserConfigurationException ioe ) {
  			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(ioe), ioe);
  		}
  		
          break; 
      default: 
      	//dataModel.put("json", req.getData());
  		try {
  			dataModel.put("json", mapJson.readValue(req.getData(), Map.class));
  		} catch (JsonProcessingException sxe ) {
  			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(sxe), sxe);
  		}
          break; 
      } 
	   
		FreemarkerGenerator fgen;
		try {
			fgen = new FreemarkerGenerator(templateFile.getParent());
		} catch (IOException ioe ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, convertStackTraceToString(ioe), ioe);
		}
		
		File tplName = new File(req.getTemplate());
		String html;
		try {
			html = fgen.generateHTML(dataModel, tplName.getName(), Locale.GERMAN);
		} catch (TemplateException|IOException te ) {
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(te), te);
		} catch  (SAXException sxe) {
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(sxe), sxe);
		} catch  (ParserConfigurationException pce) {
		    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, convertStackTraceToString(pce), pce);
		}
		return html; 
  }
	
	public String convertWithIteration(Map<String, Object> map) {
	    StringBuilder mapAsString = new StringBuilder("{");
	    for (String key : map.keySet()) {
	        mapAsString.append(key + "=" + map.get(key) + ", ");
	    }
	    mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
	    return mapAsString.toString();
	}
	
	private static String convertStackTraceToString(Throwable throwable) 
    {
        try (StringWriter sw = new StringWriter(); 
               PrintWriter pw = new PrintWriter(sw)) 
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } 
        catch (IOException ioe) 
        {
            throw new IllegalStateException(ioe);
        }
    }  
}
