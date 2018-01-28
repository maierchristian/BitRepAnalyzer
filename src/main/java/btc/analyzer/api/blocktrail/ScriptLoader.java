package btc.analyzer.api.blocktrail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8ResultUndefined;

import btc.analyzer.address.Address;
import btc.analyzer.address.Transaction;
import btc.analyzer.address.TransactionInput;
import btc.analyzer.address.TransactionOutput;
import btc.analyzer.system.AnalyzerSystem;

public class ScriptLoader {
	
	private AnalyzerSystem analyzerSystem;
	
	private Address address;
	private static String blocktrailPath;
	private static String apiKey;
	private static String apiSecret;
	private String addressName;
	private static String addressScript;
	private static String addressTransactionScript;
	
	public ScriptLoader(String adr, String apiP, String apiK, String apiS, AnalyzerSystem aS) {
		
		addressName = adr;
		blocktrailPath = apiP;
		apiKey = apiK;
		apiSecret = apiS;
		analyzerSystem = aS;
	    address = null;
	    
		addressScript = ""
				+"var blocktrail = require('"+blocktrailPath+"');\n"
				+"var addressInformation;\n"
				+"var client = blocktrail.BlocktrailSDK({apiKey : \""+apiKey+"\", apiSecret : \""+apiSecret+"\"});\n"
				+"client.address(\""+addressName+"\", function(err, address) {\n"
				+" if (err) {\n"
				+" console.log('address ERR', err);\n"
				+" return;\n"
				+"  }\n"
				+"exports.addressInformation = address;"
				+"});";
		
		addressTransactionScript = ""
				+"var blocktrail = require('"+blocktrailPath+"');\n"
				+"var addressTransactionInformationTotal = {};\n"
				+"var addressTransactionInformationData = {};\n"
				+"var client = blocktrail.BlocktrailSDK({apiKey : \""+apiKey+"\", apiSecret : \""+apiSecret+"\"});\n"
				+"client.addressTransactions(\""+addressName+"\", {limit: 300}, function(err, address_txs) {\n"
				+" addressTransactionInformationTotal.total = address_txs['total'];"
				+" exports.addressTransactionInformationTotal = addressTransactionInformationTotal;\n"
				+" addressTransactionInformationData.data = address_txs['data'];"
				+" exports.addressTransactionInformationData = addressTransactionInformationData;\n"
				+"});\n";
	
	}
	
	public void exportAnalyzerAddress() throws IOException {
		
		NodeJS nodeJS = null;
		
        File testScript = createTemporaryScriptFile(addressScript, "AddressScript");

        nodeJS = NodeJS.createNodeJS();
        V8Object exports = nodeJS.require(testScript);
        
        while(nodeJS.isRunning()) {
		    nodeJS.handleMessage();
		  }
        
        address = new Address(exports.getObject("addressInformation").getString("address"), exports.getObject("addressInformation").getString("hash160"), exports.getObject("addressInformation").getInteger("balance"), 
        		exports.getObject("addressInformation").getInteger("received"), exports.getObject("addressInformation").getInteger("sent"), exports.getObject("addressInformation").getInteger("unconfirmed_received"),
        		exports.getObject("addressInformation").getInteger("unconfirmed_received"),  exports.getObject("addressInformation").getInteger("unconfirmed_transactions"),  
        		exports.getObject("addressInformation").getInteger("total_transactions_in"),  exports.getObject("addressInformation").getInteger("total_transactions_out"),  
        		exports.getObject("addressInformation").getString("category"), exports.getObject("addressInformation").getString("tag"));
        
        exports.release();
		
	}
	
	public void exportAnalyzerTransactions() throws IOException {
		
		NodeJS nodeJS = null;
		Transaction[] transactions = null;
		TransactionInput[] transaction_ins = null;
		TransactionOutput[] transaction_outs = null;
		
		File testScript = createTemporaryScriptFile(addressTransactionScript, "AddressTransactionScript");
		
		nodeJS = NodeJS.createNodeJS();
        V8Object exports = nodeJS.require(testScript);
        
        while(nodeJS.isRunning()) {
		    nodeJS.handleMessage();
		  }
        
        transactions = new Transaction[exports.getObject("addressTransactionInformationData").getArray("data").length()];
        
        for(int n = 0; n <= (exports.getObject("addressTransactionInformationData").getArray("data").length())-1; n++)
        {
        	
        	Integer estimatedChange; 									//estimated_change can be null and j2v8 can't read null so we catch the exception
           	   	
        	try{estimatedChange = exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getInteger("estimated_change");}
        	catch (V8ResultUndefined e) {estimatedChange = null;}
        	        	
        	transactions[n] = new Transaction(exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getString("hash"), 
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getString("time"),
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getInteger("confirmations"),
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getInteger("block_height"),
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getString("block_hash"),
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getBoolean("is_coinbase"),
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getInteger("estimated_value"),
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getInteger("total_input_value"),
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getInteger("total_output_value"),
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getInteger("total_fee"),
        									  estimatedChange,
        									  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getString("estimated_change_address"));
        	
        	transaction_ins = new TransactionInput[exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").length()];
        	
        	for(int m = 0; m <= (exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").length())-1; m++)
        	{
        		
        		transaction_ins[m] = new TransactionInput(exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").getObject(m).getInteger("index"),
        												  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").getObject(m).getString("output_hash"),
        												  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").getObject(m).getInteger("output_index"),
        												  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").getObject(m).getInteger("value"),
        												  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").getObject(m).getString("address"),
        												  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").getObject(m).getString("type"),
        												  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").getObject(m).getString("multisig"),
        												  exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("inputs").getObject(m).getString("script_signature"));
        		
        	}
        	
        	transaction_outs = new TransactionOutput[exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").length()];
        	
        	for(int o = 0; o <= (exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").length())-1; o++)
        	{
        		
        		transaction_outs[o] = new TransactionOutput(exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getInteger("index"),
															exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getInteger("value"),
														    exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getString("address"),
															exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getString("type"),
															exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getString("multisig"),
															exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getString("script"),
															exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getString("script_hex"),
															exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getString("spent_hash"),
															exports.getObject("addressTransactionInformationData").getArray("data").getObject(n).getArray("outputs").getObject(o).getInteger("spent_index"));
        	
        	}
        
        	
        	transactions[n].setTransactionsIn(transaction_ins);
        	transactions[n].setTransactionsOut(transaction_outs);
        	transaction_ins = null;
        	transaction_outs = null;
        	
        }
         
        address.setTransactions(transactions);
        address.setOwnerAddress();
        address.checkOutAddress();
        
        analyzerSystem.getAddress().add(address);
        if(analyzerSystem.getAddress().get(0).getIsRoot() != true) analyzerSystem.getAddress().get(0).setIsRoot(true);
        
        transactions = null;
        
        exports.release();
		
	}
	
	private static File createTemporaryScriptFile(final String script, final String name) throws IOException {
        File tempFile = File.createTempFile(name, ".js.tmp");
        PrintWriter writer = new PrintWriter(tempFile, "UTF-8");
        try {
            writer.print(script);
        } finally {
            writer.close();
        }
        return tempFile;
    }
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address adr) {
		address = adr;
	}
	
	public void setAddressName(String input) {
		addressName = input;
	}
	
}
