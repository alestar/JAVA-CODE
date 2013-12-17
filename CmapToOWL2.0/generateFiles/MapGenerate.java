/**
 * 
 */
package generateFiles;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * @author UserAdmin
 *
 *Clase que contiene los metodo para mostrar el contenido del MC cargado
 */
public class MapGenerate {

	public static String showMap(String pathFile){

		String strXml ="";
		try {

			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			org.w3c.dom.Document docMap =  domBuilder.parse(pathFile);

			StringWriter strWriter = null;
			XMLSerializer xmlSerializer = null;
			OutputFormat outFormat = null;


			xmlSerializer = new XMLSerializer();
			strWriter = new StringWriter();
			outFormat = new OutputFormat();

			outFormat.setEncoding("iso-8859-1");
			outFormat.setVersion("1.0");
			outFormat.setIndenting(true);
			outFormat.setIndent(4);

			xmlSerializer.setOutputCharStream(strWriter);
			xmlSerializer.setOutputFormat(outFormat);
			xmlSerializer.serialize(docMap);
			strWriter.close();

			strXml = strWriter.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return strXml;

	}

	public static String showMapFromText(String text){

		String strXml ="";
		try {

			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			ByteArrayInputStream byteArray= new ByteArrayInputStream(text.getBytes());
			InputStream dis = new DataInputStream(byteArray);
			org.w3c.dom.Document doc = domBuilder.parse(dis);
			
			StringWriter strWriter = null;
			XMLSerializer xmlSerializer = null;
			OutputFormat outFormat = null;


			xmlSerializer = new XMLSerializer();
			strWriter = new StringWriter();
			outFormat = new OutputFormat();

			outFormat.setEncoding("iso-8859-1");
			outFormat.setVersion("1.0");
			outFormat.setIndenting(true);
			outFormat.setIndent(4);

			xmlSerializer.setOutputCharStream(strWriter);
			xmlSerializer.setOutputFormat(outFormat);
			xmlSerializer.serialize(doc);
			strWriter.close();

			strXml = strWriter.toString();

		} catch (Exception e) {
			// TODO: handle exception
		}

		return strXml;

	}
}
