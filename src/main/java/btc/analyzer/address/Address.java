package btc.analyzer.address;

import java.time.LocalDateTime;

public class Address {
	
	private String address;
	private String hash;
	private Integer balance;
	private Integer received;
	private Integer sent;
	private Integer unconfirmed_received;
	private Integer unconfirmed_sent;
	private Integer unconfirmed_transactions;
	private Integer total_transactions_in;
	private Integer total_transactions_out;
	private Integer total_transactions;
	private String category;
	private String tag;
	
	private Double transactionsPerWeek;
	private Double transactionsPerYear;
	
	private Boolean isIllegal;
	private Boolean isCompany;
	private Boolean haveOutAddress;
	private Boolean isRoot;
	private LocalDateTime firstUse;
	
	private Transaction[] transactions;
	
	private Integer[] score;
	private Integer numberTradingPartner;
	private Double finalScore;
	private Double userRating;
	private Character grade;
	
	private Double[] factors;
	
	public Address(String adr, String hs, Integer bal, Integer rx, Integer sx, Integer urx, Integer usx, Integer utrans, Integer ttransi, Integer ttranso, 
			String cat, String tg) {
		
		address = adr;
		hash = hs;
		balance = bal;
		received = rx;
		sent = sx;
		unconfirmed_received = urx;
		unconfirmed_sent = usx;
		unconfirmed_transactions = utrans;
		total_transactions_in = ttransi;
		total_transactions_out = ttranso;
		total_transactions = total_transactions_in + total_transactions_out;
		category = cat;
		tag = tg;
		
		transactions = null;
		score = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};
		
		factors = new Double[]{1.0, 0.75, 2.0, 1.5, 1.75, 1.5, 1.0, 2.0};
		
		isIllegal = false;
		isCompany = false;
		isRoot = false;
		
		userRating = 0.0;
		
	}
	
	public void setOwnerAddress() {
		
		for(int n = 0; n <= transactions.length-1; n++)
		{	
			transactions[n].setOwnerAddress(this);	
		}
		
	}
	
	public double satoshiToBtc(Integer satoshi) {
		
		double btc = 0.00000001;
		return satoshi * btc;
		
	}
	
	public void calculateScore() {
		
		Double sc = 0.0;
		
		for(int i = 0; i <= score.length-1; i++)
		{
			
			sc = sc + (score[i] * factors[i]);
			
		}
		
		finalScore = sc;
		
	}
	
	public void checkOutAddress() {
		
		Boolean decision = false;
		Integer valueIn = 0;
		Integer valueOut = 0;
		
		for(int j = 0; j <= transactions.length-1; j++)
		{
			try{	
				for(int k = 0; k <= transactions[j].getTransactionsIn().length-1; k++)
				{
					if(transactions[j].getTransactionsIn()[k].getFromAddress().equals(this.address)) valueIn = transactions[j].getTransactionsIn()[k].getValue();
					else valueIn = 0;
				}
				for(int l = 0; l <= transactions[j].getTransactionsOut().length-1; l++)
				{
					if(transactions[j].getTransactionsOut()[l].getToAddress().equals(this.address)) valueOut = transactions[j].getTransactionsOut()[l].getValue();
					else valueOut = 0;
				}
				if(valueIn >= valueOut) {
					decision = true;
					transactions[j].setOut(true);
				}
				else transactions[j].setIn(true);
			}
			catch (NullPointerException e) {
				System.out.println("NullPointer");
			}
		}
		
		this.haveOutAddress = decision;
		
	}
	
	public void awardGrade() {
		
		Double fscore = this.finalScore;
		
		if(fscore < 0) grade = 'F';
		else if(fscore < 89) grade = 'E';
			 else if(fscore < 178) grade = 'D';
			 	  else if(fscore < 267) grade = 'C';
			 	  	   else if(fscore < 356) grade = 'B';
			 	  	   		else grade = 'A';
		
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getHash() {
		return hash;
	}
	
	public Integer getBalance() {
		return balance;
	}
	
	public Integer getReceived() {
		return received;
	}
	
	public Integer getSent() {
		return sent;
	}
	
	public Integer getUnconfirmedReceived() {
		return unconfirmed_received;
	}
	
	public Integer getUnconfirmedSent() {
		return unconfirmed_sent;
	}
	
	public Integer getUnconfirmedTransactions() {
		return unconfirmed_transactions;
	}
	
	public Integer getTotalTransactions() {
		return total_transactions;
	}

	public Integer getTotalTransactionsIn() {
		return total_transactions_in;
	}
	
	public Integer getTotalTransactionsOut() {
		return total_transactions_out;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getTag() {
		return tag;
	}
	
	public Transaction[] getTransactions() {
		return transactions;
	}
	
	public Boolean getIsIllegal() {
		return isIllegal;
	}
	
	public Boolean getIsCompany() {
		return isCompany;
	}
	
	public Integer[] getScore() {
		return score;
	}
	
	public Double getFinalScore() {
		return finalScore;
	}
	
	public Double getTransactionsPerWeek() {
		return transactionsPerWeek;
	}
	
	public Double getTransactionsPerYear() {
		return transactionsPerYear;
	}
	
	public Boolean getHaveOutAddress() {
		return haveOutAddress;
	}
	
	public Boolean getIsRoot() {
		return isRoot;
	}
	
	public Double[] getFactors() {
		return factors;
	}
	
	public Character getGrade() {
		return grade;
	}
	
	public Double getUserRating() {
		return userRating;
	}
	
	public LocalDateTime getFirstUse() {
		return firstUse;
	}
	
	public Integer getNumberTradingPartner() {
		return numberTradingPartner;
	}
	
	public void setTransactions(Transaction[] t) {
		transactions = t;
	}
	
	public void setIsIllegal(Boolean il) {
		isIllegal = il;
	}
	
	public void setIsCompany(Boolean ic) {
		isCompany = ic;
	}
	
	public void setScoreIndex(Integer[] s){
		score = s;
	}
	
	public void setFinalScor(Double fs){
		finalScore = fs;
	}
	
	public void setTransactionsPerWeek(Double tpw) {
		transactionsPerWeek = tpw;
	}
	
	public void setTransactionsPerYear(Double tpy) {
		transactionsPerYear = tpy;
	}
	
	public void setIsRoot(Boolean iR) {
		isRoot = iR;
	}
	
	public void setUserRating(Double uR) {
		userRating = uR;
	}
	
	public void setFirstUse(LocalDateTime fi) {
		firstUse = fi;
	}
	
	public void setNumberTradingPartner(Integer tP) {
		numberTradingPartner = tP;
	}
	
}
