package btc.analyzer.database.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class XMLIllegalAddressLoader {
	
	private String[] illegalAddresses;
	private Integer counter;
	
	private XMLStreamReader xtr = null;
	
	public XMLIllegalAddressLoader() throws FileNotFoundException, XMLStreamException {
		
		counter = 0;
		this.load("IllegalAddresses.xml");
		
	}
	
	public void load(String xml) {
		
		if(this.loadIllegalAddresses(xml) == false) System.out.println("XML illegal nicht vorhanden!");
		counter = 0;
	}
	
	public boolean loadIllegalAddresses(String filename) {
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
		
		if(elemName.equals("IllegalAddresses")) illegalAddresses = new String[Integer.parseInt(xtr.getAttributeValue(0))];
		
		if(elemName.equals("adr")) {
			illegalAddresses[counter] = xtr.getAttributeValue(0);
			counter++;
		}
		
	}
	
	public void testOutput() {
		
		for(int i = 0; i <= illegalAddresses.length-1; i++) 
		{
			
			System.out.println(illegalAddresses[i]);
			
		}
		
	}
	
	private String addXMLIfMissing(String filename) {
		// ensures, that the file ends with .xml
		if (!filename.endsWith(".xml"))
			return filename + ".xml";
		else
			return filename;
	}
	
	public String[] getIllegalAddresses() {
		return illegalAddresses;
	}

}
