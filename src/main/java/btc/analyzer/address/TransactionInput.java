package btc.analyzer.address;

public class TransactionInput {
	
	private Integer index;
	private String output_hash;
	private Integer output_index;
	private Integer value;
	private String fromAddress;
	private String type;
	private String multisig;
	private String script_signature;
	
	public TransactionInput(Integer i, String oh, Integer oi, Integer v, String adr, String ty, String ms, String ss) {
		
		index = i;
		output_hash = oh;
		output_index = oi;
		value = v;
		fromAddress = adr;
		type = ty;
		multisig = ms;
		script_signature = ss;
		
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public Integer getOutputIndex() {
		return output_index;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public String getFromAddress() {
		return fromAddress;
	}
 
}
