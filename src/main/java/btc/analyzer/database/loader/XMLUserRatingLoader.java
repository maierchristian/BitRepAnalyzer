package btc.analyzer.database.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLUserRatingLoader {
	
	private String[][] userRating;
	private Integer counter;
	
	private XMLStreamReader xtr = null;
	
	public XMLUserRatingLoader() throws FileNotFoundException, XMLStreamException {
		
		counter = 0;
		this.load("UserRatings.xml");
		
	}
	
	public void load(String xml) {
		
		if(this.loadUserRatings(xml) == false) System.out.println("XML illegal nicht vorhanden!");
		counter = 0;
	}
	
	public boolean loadUserRatings(String filename) {
		String xmlFilePath = addXMLIfMissing(filename);
		//String xmlFileName = xmlFilePath;

		try {
			xmlFilePath = "xml/" + xmlFilePath;
			/*if (!new File(xmlFilePath).exists()) {
				throw new IOException("fields: /xml/" + xmlFileName + " does not exists.");
			}*/
			this.parseFile(xmlFilePath);
		} catch (XMLStreamException | IOException e) {
			return false;
		}
		return true;
	}
	

	public void parseFile(String fileName) throws FileNotFoundException, XMLStreamException {
		
			XMLInputFactory factory = XMLInputFactory.newInstance();
			xtr = factory.createXMLStreamReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(fileName)));
	
			try {
				while (xtr.hasNext()) {
	
					switch (xtr.getEventType()) {
					case XMLStreamConstants.START_DOCUMENT:
						break;
	
					case XMLStreamConstants.END_DOCUMENT:
						xtr.close();
						break;
	
					case XMLStreamConstants.START_ELEMENT:
						handleElement();
						break;
	
					case XMLStreamConstants.CHARACTERS:
						break;
	
					case XMLStreamConstants.END_ELEMENT:
						break;
	
					default:
						break;
					}
					xtr.next();
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	
	private void handleElement() {
		
		String elemName = xtr.getLocalName();
		
		if(elemName.equals("UserRatings")) userRating = new String[Integer.parseInt(xtr.getAttributeValue(0))][4];
		
		if(elemName.equals("adr")) {
			userRating[counter][0] = xtr.getAttributeValue(0);
			userRating[counter][1] = xtr.getAttributeValue(1);
			userRating[counter][2] = xtr.getAttributeValue(2);
			userRating[counter][3] = xtr.getAttributeValue(3);
			counter++;
		}
		
	}
	
	public void testOutput() {
		
		for(int i = 0; i <= userRating.length-1; i++) 
		{
			
			System.out.println(userRating[i][0]);
			System.out.println(userRating[i][1]);
			System.out.println(userRating[i][2]);
			System.out.println(userRating[i][3]);
			
		}
		
	}
	
	private String addXMLIfMissing(String filename) {
		// ensures, that the file ends with .xml
		if (!filename.endsWith(".xml"))
			return filename + ".xml";
		else
			return filename;
	}
	
	public String[][] getUserRating() {
		return userRating;
	}

}
