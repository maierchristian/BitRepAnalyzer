package btc.analyzer.database.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLCompanyAddressLoader {
	
	private String[][] companyAddresses;
	private Integer counter;
	
	private XMLStreamReader xtr = null;
	
	public XMLCompanyAddressLoader() {
		
		counter = 0;
		this.load("CompanyAddresses.xml");
		
	}
	
	private void load(String xml) {
			
			if(this.loadCompanyAddresses(xml) == false) System.out.println("XML company nicht vorhanden!");
			counter = 0;
		}
	
	private boolean loadCompanyAddresses(String filename) {
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
	
	private void parseFile(String fileName) throws FileNotFoundException, XMLStreamException {
		
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
		
		if(elemName.equals("CompanyAddresses")) companyAddresses = new String[Integer.parseInt(xtr.getAttributeValue(0))][3];
		
		if(elemName.equals("adr")) {
			companyAddresses[counter][0] = xtr.getAttributeValue(0);
			companyAddresses[counter][1] = xtr.getAttributeValue(1);
			companyAddresses[counter][2] = xtr.getAttributeValue(2);
			counter++;
		}
		
	}
	
	private void testOutput() {
		
		for(int i = 0; i <= companyAddresses.length-1; i++) 
		{
			
			System.out.println(companyAddresses[i][0] + " " + companyAddresses[i][1] + " " + companyAddresses[i][2]);
			
		}
		
	}
	
	private String addXMLIfMissing(String filename) {
		// ensures, that the file ends with .xml
		if (!filename.endsWith(".xml"))
			return filename + ".xml";
		else
			return filename;
	}
	
	public String[][] getCompanyAddresses() {
		return companyAddresses;
	}

}
