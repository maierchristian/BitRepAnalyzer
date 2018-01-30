package btc.analyzer.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import btc.analyzer.address.Address;
import btc.analyzer.system.AnalyzerSystem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


public class MainApplicationController extends Controller {
	
	private AnalyzerSystem analyzerSystem;
	
	@FXML Button checkAddress;
	@FXML Button evaluateAddress;
	@FXML Button settings;
	
	@FXML TextField inputAddress;
	
	@FXML Text error_Out;
	@FXML Text address_Out;
	@FXML Text hash_Out;
	@FXML Text balance_Out;
	@FXML Text received_Out;
	@FXML Text sent_Out;
	@FXML Text unconfirmed_rx_Out;
	@FXML Text unconfirmed_sx_Out;
	@FXML Text unconfirmed_trans_Out;
	@FXML Text tot_trans_Out;
	@FXML Text tot_trans_in_Out;
	@FXML Text tot_trans_out_Out;
	@FXML Text cat_Out;
	@FXML Text tg_Out;
	
	@FXML Text scoreDatabase;
	@FXML Text addressType;
	@FXML Text scoreFirstUsage;
	@FXML Text firstDate;
	@FXML Text scoreTransactionAmount;
	@FXML Text tpY;
	@FXML Text scoreTransactionValue;
	@FXML Text scoreTransactionFee;
	@FXML Text scoreBalance;
	@FXML Text scoreConfirmations;
	@FXML Text scoreTradingPartner;
	@FXML Text numberTradingPartner;
	@FXML Text scoreResult;
	@FXML Text grade;
	@FXML Text userRating;
	
	@FXML Text factorDatabase;
	@FXML Text factorFirstUsage;
	@FXML Text factorTransactionAmount;
	@FXML Text factorTransactionValue;
	@FXML Text factorTransactionFee;
	@FXML Text factorBalance;
	@FXML Text factorConfirmations;
	@FXML Text factorTradingPartner;
	
		
	@FXML
	public void displayInformation() throws IOException {
		
		if(inputAddress.getText().length() == 0 ||inputAddress.getText().length() < 27 || inputAddress.getText().length() > 34) {
			error_Out.setText("Invalid address or settings");
			error_Out.setStyle("-fx-text-fill: red");
		}
		else {
			try{
				
				analyzerSystem.getAddressInformation(inputAddress.getText());
				Address adr = analyzerSystem.getScriptLaoder().getAddress();
				inputAddress.setDisable(true);
				
				error_Out.setText("                                ");
				address_Out.setText(adr.getAddress());
				hash_Out.setText(adr.getHash());
				balance_Out.setText(String.format("%.8f", adr.satoshiToBtc(adr.getBalance())) + " BTC");
				received_Out.setText(String.format("%.8f", adr.satoshiToBtc(adr.getReceived())) + " BTC");
				sent_Out.setText(String.format("%.8f", adr.satoshiToBtc(adr.getSent())) + " BTC");
				unconfirmed_rx_Out.setText(String.format("%.8f", adr.satoshiToBtc(adr.getUnconfirmedReceived())) + " BTC");
				unconfirmed_sx_Out.setText(String.format("%.8f", adr.satoshiToBtc(adr.getUnconfirmedSent())) + " BTC");
				unconfirmed_trans_Out.setText(adr.getUnconfirmedTransactions().toString());
				tot_trans_Out.setText(adr.getTotalTransactions().toString());
				tot_trans_in_Out.setText(adr.getTotalTransactionsIn().toString());
				tot_trans_out_Out.setText(adr.getTotalTransactionsOut().toString());
				cat_Out.setText(adr.getCategory());
				tg_Out.setText(adr.getTag());
				
				evaluateAddress.setDisable(false);}
				catch (UnsupportedOperationException e) {
					error_Out.setText("Invalid address or Network Error");
					error_Out.setStyle("-fx-text-fill: red");
					
					inputAddress.setDisable(true);
					evaluateAddress.setDisable(true);
				}
		
		}
		
	}
	
	@FXML
	public void startEvaluate() throws IOException {
		
		Integer[] score = null;
		Double[] factors = null;
		
		checkAddress.setDisable(true);
		evaluateAddress.setDisable(true);
		analyzerSystem.getAddressTransactionInformation();
		
		analyzerSystem.checkAddressReputation(0);
		
		Address firstAddress = analyzerSystem.getAddress().get(0);
		
		score = firstAddress.getScore();
		factors = firstAddress.getFactors();
		
		scoreDatabase.setText(score[0].toString());
		
		if(firstAddress.getIsCompany().equals(true)) addressType.setText("Company address");
		if(firstAddress.getIsIllegal().equals(true)) addressType.setText("Illegal address");
		
		scoreFirstUsage.setText(score[1].toString());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		firstDate.setText("Date: " + firstAddress.getFirstUse().format(formatter));
		
		scoreTransactionAmount.setText(score[2].toString());
		tpY.setText("Transactions/year: " + String.format("%.1f", firstAddress.getTransactionsPerYear()));
		
		scoreTransactionValue.setText(score[3].toString());
		scoreTransactionFee.setText(score[4].toString());
		scoreBalance.setText(score[5].toString());
		scoreConfirmations.setText(score[6].toString());
		scoreTradingPartner.setText(score[7].toString());
		numberTradingPartner.setText("Number: " + firstAddress.getNumberTradingPartner().toString());
		scoreResult.setText(firstAddress.getFinalScore().toString());
		
		if(firstAddress.getGrade().equals('A')) grade.setFill(Color.GREEN);
		else if(firstAddress.getGrade().equals('B')) grade.setFill(Color.CHARTREUSE);
			else if(firstAddress.getGrade().equals('C')) grade.setFill(Color.YELLOW);
				else if(firstAddress.getGrade().equals('D')) grade.setFill(Color.ORANGE);
					else if(firstAddress.getGrade().equals('E')) grade.setFill(Color.ORANGERED);
						else if(firstAddress.getGrade().equals('F')) grade.setFill(Color.RED);
		
		grade.setText(firstAddress.getGrade()+"");
		
		userRating.setText(firstAddress.getUserRating()+"");
		
		factorDatabase.setText(factors[0].toString());
		factorFirstUsage.setText(factors[1].toString());
		factorTransactionAmount.setText(factors[2].toString());
		factorTransactionValue.setText(factors[3].toString());
		factorTransactionFee.setText(factors[4].toString());
		factorBalance.setText(factors[5].toString());
		factorConfirmations.setText(factors[6].toString());
		factorTradingPartner.setText(factors[7].toString());

	}
	
	@FXML
	public void reset() {
		analyzerSystem.setAddress(null);
		if(analyzerSystem.getScriptLaoder() != null) {
			analyzerSystem.getScriptLaoder().setAddress(null);
		}
		
		analyzerSystem.setScriptLoader(null);
		
		inputAddress.setDisable(false);
		inputAddress.setText("");
		error_Out.setText("                    ");
		address_Out.setText("no address");
		hash_Out.setText("no hash");
		balance_Out.setText("0.00000000 BTC");
		received_Out.setText("0.00000000 BTC");
		sent_Out.setText("0.00000000 BTC");
		unconfirmed_rx_Out.setText("0.00000000 BTC");
		unconfirmed_sx_Out.setText("0.00000000 BTC");
		unconfirmed_trans_Out.setText("0");
		tot_trans_Out.setText("0");
		tot_trans_in_Out.setText("0");
		tot_trans_out_Out.setText("0");
		cat_Out.setText("no category");
		tg_Out.setText("no tag");
		
		scoreDatabase.setText("0");
		addressType.setText("");
		scoreFirstUsage.setText("0");
		firstDate.setText("");
		scoreTransactionAmount.setText("0");
		tpY.setText("");
		scoreTransactionValue.setText("0");
		scoreTransactionFee.setText("0");
		scoreBalance.setText("0");
		scoreConfirmations.setText("0");
		scoreTradingPartner.setText("0");
		numberTradingPartner.setText("");
		scoreResult.setText("0");
		grade.setText("No Grade");
		userRating.setText("0.0");
		
		factorDatabase.setText("0.0");
		factorFirstUsage.setText("0.0");
		factorTransactionAmount.setText("0.0");
		factorTransactionValue.setText("0.0");
		factorTransactionFee.setText("0.0");
		factorBalance.setText("0.0");
		factorConfirmations.setText("0.0");
		factorTradingPartner.setText("0.0");
		
		checkAddress.setDisable(false);
		evaluateAddress.setDisable(true);
		
		System.gc();
		
	}
	
	@FXML
	public void openSettings() {
		analyzerSystem.getApplicationStage().setSceneAndShow("settings");
	}
	
	@Override
	public void setAnalyzerSystem(AnalyzerSystem aS) {
		analyzerSystem = aS;
	}
	
	public Button getCheckAddressButton() {
		return checkAddress;
	}

}
