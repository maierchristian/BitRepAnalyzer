package btc.analyzer.system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

import javax.xml.stream.XMLStreamException;

import btc.analyzer.address.Address;
import btc.analyzer.address.Transaction;
import btc.analyzer.api.blocktrail.ScriptLoader;
import btc.analyzer.database.Database;
import btc.analyzer.view.stage.ApplicationStage;

public class AnalyzerSystem {
	
	private ApplicationStage applicationStage;
	private ScriptLoader scriptLoader;
	private ArrayList<Address> address;
	private Database database;
	private LocalDateTime foundingDate;
	private LocalDateTime recentDate;
	
	private Double[] courseBTC_USD;
	private Integer[] satoshiPerByte;
	
	private String blocktrailPath;
	private String blocktrailKey;
	private String blocktrailSecret;
	
	public AnalyzerSystem() throws FileNotFoundException, XMLStreamException {
		
		scriptLoader = null;
		address = null;
		database = new Database();
		foundingDate = LocalDateTime.parse("2009-01-04T00:00:00");
		recentDate = LocalDateTime.now();
		courseBTC_USD = new Double[]{0.12, 8.0, 9.0, 150.0, 500.0, 250.0, 550.0, 5000.0, 14000.0}; 
		satoshiPerByte = new Integer[]{2, 5, 7, 15, 30, 40, 100, 300, 500};
		
		address = new ArrayList<Address>();
		
	}
	
	public void getAddressInformation(String adr) throws IOException {
		
		scriptLoader = new ScriptLoader(adr, blocktrailPath, blocktrailKey, blocktrailSecret, this);
		scriptLoader.exportAnalyzerAddress();

	}
	
	public void getAddressTransactionInformation() throws IOException {
		
		scriptLoader.exportAnalyzerTransactions();	
		
	}
	
	public void checkAddressReputation(Integer i) throws IOException {
		
		this.checkAddressDatabase(i);
		this.checkAddressFirstUse(i);
		this.checkTransactionAmount(i);
		this.checkAddressTransactionValues(i);
		this.checkTransactionFees(i);
		this.checkAddressValue(i);
		this.checkAddressConfirmations(i);
		if(address.get(i).getIsRoot() == true) {
			address.get(i).setUserRating(database.getAddressUserRating(address.get(i).getAddress()));
			this.checkRecursively();
		}
	
		
	}
	
	public void checkAddressDatabase(Integer i) {
		
		 if(database.isIllegalAddress(address.get(i).getAddress()) == true) {
			 
			 address.get(i).getScore()[0] = address.get(i).getScore()[0] + (-999);
			 address.get(i).setIsIllegal(true);
		 }
		 
		 if(database.isCompanyAddress(address.get(i).getAddress()) == true) {
			 
			 address.get(i).getScore()[0] = address.get(i).getScore()[0] + (database.getCompanyBonus(address.get(i).getAddress()));
			 address.get(i).setIsCompany(true);
		 }
		 
	}
	
	public void checkAddressFirstUse(Integer i) {
		
		Transaction[] transactions = null;
		LocalDateTime firstUse = null;
		Duration duration1 = null;
		Duration duration2 = null;
		long diff1 = 0;
		long diff2 = 0;
			
		transactions = address.get(i).getTransactions();
		firstUse = LocalDateTime.parse(transactions[0].getTransactionTime().substring(0,19));
		
		address.get(i).setFirstUse(firstUse);
		
		duration1 = Duration.between(foundingDate, firstUse);
		diff1 = Math.abs((duration1.toDays() / 30 - 36));
		
		duration2 = Duration.between(foundingDate, recentDate);
		diff2 = Math.abs((duration2.toDays() / 30 - 36));
		
		if(Math.toIntExact(diff2 - diff1) < 48) address.get(i).getScore()[1] = Math.toIntExact(diff2 - diff1);
		else address.get(i).getScore()[1] = 48;
		
	}
	
	public void checkTransactionAmount(Integer i) {
		
		Transaction[] transactions = null;
		LocalDateTime firstUse = null;
		LocalDateTime secondUse = null;
		Double transactionsPerWeek = 0.0;
		Double transactionsPerYear = 0.0;
		Duration duration = null;
		Long diff1 = null;
		Long diff2 = null;
		Long diff3 = null;
		
		transactions = address.get(i).getTransactions();
		firstUse = LocalDateTime.parse(transactions[0].getTransactionTime().substring(0,19));
		if(transactions.length > 1) secondUse = LocalDateTime.parse(transactions[1].getTransactionTime().substring(0,19));
		
		duration = Duration.between(firstUse, recentDate);
		diff1 = Math.abs(duration.toDays() / 7);
		transactionsPerWeek = (transactions.length / diff1.doubleValue());
		
		diff2 = Math.abs(duration.toDays() / 365);
		transactionsPerYear = (transactions.length / diff2.doubleValue());
		
		diff3 = 0L;
		
		if(transactions.length > 1) {
			duration = Duration.between(firstUse, secondUse);
			diff3 = Math.abs(duration.toDays() / 30);
		}
		
		
		address.get(i).setTransactionsPerWeek(transactionsPerWeek);
		address.get(i).setTransactionsPerYear(transactionsPerYear);
		
			if(transactionsPerWeek <= 2.0) {
				if(transactionsPerWeek < 1.0) {
					if(transactionsPerWeek <= 0.75) {
						if(transactionsPerWeek <= 0.5) {
							if(transactionsPerWeek <= 0.25) {
								address.get(i).getScore()[2] = -15;
							}
							else address.get(i).getScore()[2] = -10;
						}
						else address.get(i).getScore()[2] = -5;
					}
					else address.get(i).getScore()[2] = 0;
				}
				else address.get(i).getScore()[2] = address.get(i).getScore()[2] + 15;
			}
			else {
				
				if(address.get(i).getIsCompany() == true) address.get(i).getScore()[2] = 15 + 15;
				else {
					if(address.get(i).getIsIllegal() == true) address.get(i).getScore()[2] = -25;
					else address.get(i).getScore()[2] = -15;
				}
				
			}
			
			if(diff3 == 0) diff3 = 1L;
			
			address.get(i).getScore()[2] = address.get(i).getScore()[2] +  Math.toIntExact(((diff3 - 1) * -2));
		
	}
	
	public void checkAddressTransactionValues(Integer i) {
		
		Boolean highValues = false;
		Integer changeValue = 0;
		Integer lowChangeCounter = 0;
		Integer highChangeCounter = 0;
		Integer sameChangeCounter = 0;
		Integer transactionYear = 0;
		Integer transactionYear1 = 0;
		Address addressThis = address.get(i);
		Integer addressThisScore = addressThis.getScore()[3];
		Transaction transactions[] = addressThis.getTransactions();
		
		for(int j = 0; j <= transactions.length-1; j++)
		{
			try{
				changeValue = transactions[j].getTransactionestimatedChange();
				transactionYear = Integer.parseInt("" + transactions[j].getTransactionTime().charAt(3));
				if((courseBTC_USD[transactionYear] * addressThis.satoshiToBtc(changeValue)) > 400.0) {
					highValues = true;
					highChangeCounter++;
				}
				else lowChangeCounter++;
			}
			catch (NullPointerException e) {
				changeValue = 0;
			}
		}
		
		if(addressThis.getIsCompany() == true && highValues == true) addressThisScore = 10;
		else if(addressThis.getIsCompany() == true) addressThisScore = 10; 
		
		if(addressThis.getHaveOutAddress() == true && highValues == true && addressThis.getIsCompany() == false) addressThisScore = addressThisScore + -3;
		
		if(addressThis.getIsCompany() == false) {
			addressThisScore = addressThisScore + ((lowChangeCounter * 1));
			addressThisScore = addressThisScore + ((Double)(highChangeCounter * -0.5)).intValue();
		}
		
		if(addressThis.getHaveOutAddress() == false) addressThisScore = 0;
		
		for(int k = 0; k <= transactions.length-1; k++)
		{
			
				transactionYear = Integer.parseInt("" + transactions[k].getTransactionTime().charAt(3));
				for(int l = 0; l <= transactions.length-1; l++)
				{
					try{
						transactionYear1 = Integer.parseInt("" + transactions[l].getTransactionTime().charAt(3));
						if((courseBTC_USD[transactionYear] * addressThis.satoshiToBtc(transactions[k].getTransactionestimatedChange())) == (courseBTC_USD[transactionYear1] * addressThis.satoshiToBtc(transactions[l].getTransactionestimatedChange()))) {
							sameChangeCounter++;
						}
					}
					catch (NullPointerException e) {
							
					}
					
				}
				sameChangeCounter = sameChangeCounter -1;
		}
		
		if(addressThis.getIsCompany() == false) addressThisScore = addressThisScore + (sameChangeCounter * -2);
		else addressThisScore = addressThisScore + ((Double)(sameChangeCounter * 0.1)).intValue();
		
		if(addressThisScore > 50) addressThisScore = 50;
		else if(addressThisScore < -50) addressThisScore = -50;
		
		addressThis.getScore()[3] = addressThisScore;		
	}
	
	public void checkTransactionFees(Integer i) {
		
		Address addressThis = address.get(i);
		Transaction transactions[] = addressThis.getTransactions();
		ArrayList<Transaction> transactionsOut = new ArrayList<Transaction>();
		Integer inputSize = 0;
		Integer transactionSize = 0;
		Integer transactionYear = 0;
		Integer averageFee = 0;
		Integer averageFeeBuffer = 0;
		Integer averageFeeLower = 0;
		Integer averageFeeHigher = 0;
		Integer averageFeeSame = 0;
		Double percentHigher = 0.0;
		Double percentLower = 0.0;
		Double highestValue = 25.0;
		Double highestValueOrigin = 0.0;
		Integer inputValue = 0;
		Integer outputValue = 0;
		Random randomNumber = new Random();
		
		for(int n = 0; n <= transactions.length-1; n++)
		{
			for(int x = 0; x <= transactions[n].getTransactionsIn().length-1; x++)	
			{
				try{
					if(addressThis.getAddress().equals(transactions[n].getTransactionsIn()[x].getFromAddress())) {
						inputValue = transactions[n].getTransactionsIn()[x].getValue();
					}
				}
				catch (NullPointerException e){}
			}
			for(int l = 0; l <= transactions[n].getTransactionsOut().length-1; l++)	
			{
				try{
					if(addressThis.getAddress().equals(transactions[n].getTransactionsOut()[l].getToAddress())) {
						outputValue = transactions[n].getTransactionsOut()[l].getValue();
					}
				}
				catch (NullPointerException e){}
			}
			if (inputValue >= outputValue) transactionsOut.add(transactions[n]);
			inputValue = 0; 
			outputValue = 0;
		}
		
		for(int m = 0; m <= transactionsOut.size()-1; m++)
		{
			try{
				//in*148 + out*34 + 10 plus or minus 'in'
				inputSize = transactionsOut.get(m).getTransactionsIn().length;
				transactionSize = (inputSize * 148) + (transactionsOut.get(m).getTransactionsOut().length * 34) + 10 + (randomNumber.nextInt(inputSize - 0 + 1) + 0) - (randomNumber.nextInt(transactionsOut.get(m).getTransactionsIn().length) + 0);
				transactionYear = Integer.parseInt("" + transactionsOut.get(m).getTransactionTime().charAt(3));
				averageFee = (transactionSize * satoshiPerByte[transactionYear]);
				averageFeeBuffer = (averageFee / 100) * 5;
				if(transactionsOut.get(m).getTransactionTotalFee() < (averageFee - averageFeeBuffer)) averageFeeLower++;
				else {
					if(transactionsOut.get(m).getTransactionTotalFee() > (averageFee + averageFeeBuffer)) averageFeeHigher++;
					else {
						if(transactionsOut.get(m).getTransactionTotalFee() > (averageFee - averageFeeBuffer) && transactionsOut.get(m).getTransactionTotalFee() < (averageFee + averageFeeBuffer)) averageFeeSame++;
					}
				}	
			}
			catch (NullPointerException e) {}
		}
		
		if(averageFeeLower == transactionsOut.size()) addressThis.getScore()[4] = -25;
		else {
			if(averageFeeHigher == transactionsOut.size()) addressThis.getScore()[4] = 25 + 25;
			else {
				if(averageFeeSame == transactionsOut.size()) addressThis.getScore()[4] = 25;
				else {
					if(averageFeeLower != 0) percentLower = ((averageFeeLower.doubleValue() * 100) / transactionsOut.size());
					if(averageFeeHigher != 0) percentHigher = ((averageFeeHigher.doubleValue() * 100) / transactionsOut.size());
					
					highestValueOrigin = (highestValue / 100);
					
					highestValue = (highestValue - (highestValueOrigin * (percentLower.doubleValue())));
					
					if((highestValueOrigin * 100) > highestValue) {
						addressThis.getScore()[4] = highestValue.intValue();
					}
					else {
						addressThis.getScore()[4] = ((Double)(highestValueOrigin * 100)).intValue();
					}
				}
			}
		}
	}
	
	public void checkAddressValue(Integer i) {
		
		Address addressThis = address.get(i);
		Integer balanceSatoshi = addressThis.getBalance();
		
		if(balanceSatoshi == 0) addressThis.getScore()[5] = -15;
		
		if((addressThis.satoshiToBtc(balanceSatoshi) * courseBTC_USD[Integer.parseInt(""+recentDate.toString().charAt(3))]) <= 100.0) addressThis.getScore()[5] = 5;
		else {
			if((addressThis.satoshiToBtc(balanceSatoshi) * courseBTC_USD[Integer.parseInt(""+recentDate.toString().charAt(3))]) <= 500.0) addressThis.getScore()[5] = 10;
			else {
				if((addressThis.satoshiToBtc(balanceSatoshi) * courseBTC_USD[Integer.parseInt(""+recentDate.toString().charAt(3))]) <= 1000.0) addressThis.getScore()[5] = 15;
				else {
					if((addressThis.satoshiToBtc(balanceSatoshi) * courseBTC_USD[Integer.parseInt(""+recentDate.toString().charAt(3))]) <= 5000.0) addressThis.getScore()[5] = 20;
					else {
						if((addressThis.satoshiToBtc(balanceSatoshi) * courseBTC_USD[Integer.parseInt(""+recentDate.toString().charAt(3))]) > 5000.0) addressThis.getScore()[5] = -5;
					}
				}
			}
		}
 		
	}
	
	public void checkAddressConfirmations(Integer i) {
		
		Address addressThis = address.get(i);
		Integer combinedValue = (addressThis.getUnconfirmedReceived() + addressThis.getBalance());
		
		if(addressThis.getUnconfirmedTransactions() == 0) addressThis.getScore()[6] = 10;
		else {
			if(addressThis.getUnconfirmedTransactions() > 0) {
				if(((combinedValue / 100) * 75) < addressThis.getUnconfirmedReceived()) addressThis.getScore()[6] = -5;
				else {
					addressThis.getScore()[6] = 0;
				}
			}
		}
	}
	
	public void checkRecursively() throws IOException {
		
		ArrayList<String> tradingPartner = new ArrayList<String>();
		Address addressThis = address.get(0);
		Transaction[] transactions = addressThis.getTransactions();
		ScriptLoader sL = null;
		Boolean same = false;
		Integer posTrader = 0;
		
		for(int o = 0; o <= transactions.length-1; o++)
		{
			try{
				if(transactions[o].getIn() == true) {
					for(int q = 0; q <= transactions[o].getTransactionsIn().length-1; q++)
					{
						if(!(transactions[o].getTransactionsIn()[q].getFromAddress().equals(addressThis.getAddress()))) {
							for(int t = 0; t <= tradingPartner.size()-1; t++) {
								if(transactions[o].getTransactionsIn()[q].getFromAddress().equals(tradingPartner.get(t))) same = true;
							}
							if(same.equals(false)) tradingPartner.add(transactions[o].getTransactionsIn()[q].getFromAddress());
							same = false;
						}
					}
				}
				
				if(transactions[o].getOut() == true) {
					for(int s = 0; s <= transactions[o].getTransactionsOut().length-1; s++)
					{
						if(!(transactions[o].getTransactionsOut()[s].getToAddress().equals(addressThis.getAddress()))) {
							for(int u = 0; u <= tradingPartner.size()-1; u++) {
								if(transactions[o].getTransactionsOut()[s].getToAddress().equals(tradingPartner.get(u))) same = true;
							}
							if(same.equals(false)) tradingPartner.add(transactions[o].getTransactionsOut()[s].getToAddress());
							same = false;
						}
					}
				}
			}
			catch (NullPointerException e) {
				
			}
			
		}
		
		if(tradingPartner.size() > 0) {
			addressThis.setNumberTradingPartner(tradingPartner.size());
			for(int p = 0; p <= tradingPartner.size()-1; p++)
			{
				sL = new ScriptLoader(tradingPartner.get(p), blocktrailPath, blocktrailKey, blocktrailSecret, this);
				sL.exportAnalyzerAddress();
				sL.exportAnalyzerTransactions();
				this.checkAddressReputation(p+1);
				address.get(p+1).getScore()[7] = 0;
			}
			
			for(int r = 1; r <= address.size()-1; r++)
			{
				address.get(r).calculateScore();
				address.get(r).getFinalScore();
				address.get(r).awardGrade();
			}
			
			for(int u = 1; u <= address.size()-1; u++)
			{
				if(address.get(u).getGrade().equals('A') || address.get(u).getGrade().equals('B') || address.get(u).getGrade().equals('C')) {
					posTrader++;
				}
			}
			
			addressThis.getScore()[7] = ((50 / 100) * posTrader);
			
			addressThis.calculateScore();
			addressThis.getFinalScore();
			addressThis.awardGrade();
			
		}
		else addressThis.getScore()[7] = 0;
		
	}
	
	public ApplicationStage getApplicationStage() {
		return applicationStage;
	}
	
	public ArrayList<Address> getAddress() {
		return address;
	}
	
	public ScriptLoader getScriptLaoder() {
		return scriptLoader;
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public String getBlocktrailPath() {
		return blocktrailPath;
	}
	
	public String getBlocktrailKey() {
		return blocktrailKey;
	}
	
	public String getBlocktrailSecret() {
		return blocktrailSecret;
	}
	
	public void setAddress(ArrayList<Address> adr) {
		address = adr;
	}
	
	public void setScriptLoader(ScriptLoader sl) {
		scriptLoader = sl;
	}
	
	public void setApplicationStage(ApplicationStage aS) {
		applicationStage = aS;
	}
	
	public void setBlocktrailPath(String bP) {
		blocktrailPath = bP;
	}
	
	public void setBlocktrailKey(String bK) {
		blocktrailKey = bK;
	}
	
	public void setBlocktrailSecret(String bS) {
		blocktrailSecret = bS;
	}

}
