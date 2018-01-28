package btc.analyzer.address;

import java.lang.reflect.Array;

public class Transaction {
	
	private Address ownerAddress;
	private String transaction_hash;
	private String transaction_time;
	private Integer transaction_confirmations;
	private Integer transaction_blockHeight;
	private String transaction_blockHash;
	private Boolean transaction_isCoinbase;
	private Integer transaction_estimatedValue;
	private Integer transaction_totalInputValue;
	private Integer transaction_totalOutputValue;
	private Integer transaction_totalFee;
	private Integer transaction_estimatedChange;
	private String transaction_estimatedChangeAddress;
	
	private TransactionInput[] transaction_Ins;
	private TransactionOutput[] transaction_Outs;
	
	private Boolean in;
	private Boolean out;
	
	public Transaction(String th, String tt, Integer tc, Integer tbh, String tbha, Boolean tic, Integer tev, Integer ttiv, 
			Integer ttov, Integer ttf, Integer tec, String teca ) {
		
		transaction_hash = th;
		transaction_time = tt;
		transaction_confirmations = tc;
		transaction_blockHeight = tbh;
		transaction_blockHash = tbha;
		transaction_isCoinbase = tic;
		transaction_estimatedValue = tev;
		transaction_totalInputValue = ttiv;
		transaction_totalOutputValue = ttov;
		transaction_totalFee = ttf;
		transaction_estimatedChange = tec;
		transaction_estimatedChangeAddress = teca;
		
		in = false;
		out = false;
		
	}
	
	public Address getOwnerAddress() {
		return ownerAddress;
	}
	
	public String getTransactionTime() {
		return transaction_time;
	}
	
	public Integer getTransactionConfirmations() {
		return transaction_confirmations;
	}
	
	public Boolean getTransactionIsCoinbase() {
		return transaction_isCoinbase;
	}
	
	public Integer getTransactionEstimatedValue() {
		return transaction_estimatedValue;
	}
	
	public Integer getTransactionTotalInputValue() {
		return transaction_totalInputValue;
	}
	
	public Integer getTransactionTotalOutputValue() {
		return transaction_totalOutputValue;
	}
	
	public Integer getTransactionTotalFee() {
		return transaction_totalFee;
	}
	
	public Integer getTransactionestimatedChange() {
		return transaction_estimatedChange;
	}
	
	public String getTransactionEstimatedChangeAddress() {
		return transaction_estimatedChangeAddress;
	}
	
	public TransactionInput[] getTransactionsIn() {
		return transaction_Ins;
	}
	
	public TransactionOutput[] getTransactionsOut() {
		return transaction_Outs;
	}
	
	public Boolean getIn() {
		return in;
	}
	
	public Boolean getOut() {
		return out;
	}
	
	public void setOwnerAddress(Address adr) {
		ownerAddress = adr;
	}
	
	public void setTransactionsIn(TransactionInput[] ti) {
		transaction_Ins = ti;
	}
	
	public void setTransactionsOut(TransactionOutput[] to) {
		transaction_Outs = to;
 	}
	
	public void setIn(Boolean i) {
		in = i;
	}
	
	public void setOut(Boolean o) {
		out = o;
	}
}
