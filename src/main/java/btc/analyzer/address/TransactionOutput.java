package btc.analyzer.address;

import java.lang.reflect.Array;

public class TransactionOutput {
	
	private Integer index;
	private Integer value;
	private String toAddress;
	private String type;
	private String multisig;
	private Array multisig_address;
	private String script;
	private String script_hex;
	private String spent_hash;
	private Integer spent_index;
	
	public TransactionOutput(Integer i, Integer v, String adr, String ty, String ms, String sc,String sch, String sh, Integer si) {
		
		index = i;
		value = v;
		toAddress = adr;
		type = ty;
		multisig = ms;
		script = sc;
		script_hex = sch;
		spent_hash = sh;
		spent_index = si;
		
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public String getToAddress() {
		return toAddress;
	}
	
	public String getSpentHash() {
		return spent_hash;
	}
	
	public Integer getSpentIndex() {
		return spent_index;
	}

}
