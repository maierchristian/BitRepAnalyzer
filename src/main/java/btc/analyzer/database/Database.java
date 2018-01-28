package btc.analyzer.database;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import btc.analyzer.database.loader.XMLCompanyAddressLoader;
import btc.analyzer.database.loader.XMLIllegalAddressLoader;
import btc.analyzer.database.loader.XMLUserRatingLoader;

public class Database {
	
	private String[] illegalAddresses;
	private String[][] companyAddresses;
	private String[][] userRating;
	
	private XMLIllegalAddressLoader illegalAddressLoader;
	private XMLCompanyAddressLoader companyAddressLoader;
	private XMLUserRatingLoader userRatingsLoader;
	
	public Database() throws FileNotFoundException, XMLStreamException {
		
		illegalAddressLoader = new XMLIllegalAddressLoader();
		companyAddressLoader = new XMLCompanyAddressLoader();
		userRatingsLoader = new XMLUserRatingLoader();
		illegalAddresses = illegalAddressLoader.getIllegalAddresses();
		companyAddresses = companyAddressLoader.getCompanyAddresses();
		userRating = userRatingsLoader.getUserRating();
		
	}
	
	public Boolean isIllegalAddress(String adr) {
		
		Boolean decision = false;
		
		for(int i = 0; i <= illegalAddresses.length-1; i++)
		{
			
			if(illegalAddresses[i].equalsIgnoreCase(adr)) decision = true;
			
		}
		
		return decision;
		
	}
	
	public Boolean isCompanyAddress(String adr) {
		
		Boolean decision = false;
		
		for(int i = 0; i <= companyAddresses.length-1; i++)
		{
			
			if(companyAddresses[i][1].equalsIgnoreCase(adr)) decision = true;
			
		}
		
		return decision;
		
	}
	public Integer getCompanyBonus(String adr) {
		
		Integer bonus = 0;
		
		for(int i = 0; i <= companyAddresses.length-1; i++)
		{
			
			if(companyAddresses[i][1].equalsIgnoreCase(adr)) bonus = Integer.parseInt(companyAddresses[i][2]);
			
		}
		return bonus;
		
	}
	
	public Double getAddressUserRating(String adr) {
		Double rating = 0.0;
		Integer count = 0;
		
		for(int i = 0; i <= userRating.length-1; i++)
		{
			
			if(userRating[i][1].equals(adr)) {
				
				rating = rating + Double.parseDouble(userRating[i][2]);
				count++;
				
			}
			
		}
		
		rating = rating / count;
		
		count = 0;
		return rating;
		
	}
	
	public String[] getIllegalAddresses() {
		return illegalAddresses;		
	}
	
	public String[][] getCompanyAddresses() {
		return companyAddresses;
	}

}
