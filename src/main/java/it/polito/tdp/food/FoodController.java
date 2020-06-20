/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.VerticeAttraversato;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	int n;
    	try {
    		n = Integer.parseInt(this.txtPassi.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("INSERIRE UN VALORE INTERO NEL CAMPO PASSI");
    		return;
    	}
    	if(this.boxPorzioni.getValue() == null) {
    		txtResult.appendText("PRIMA DI ACCEDERE A QUESTA FUNZIONE ASSICURARSI DI AVER CREATO IL GARFO \nE SCELTO UN TIPO DI PORZIONE DAL MENU A TENDINA");
    		return;
    	}
    
    	txtResult.appendText("CAMMINO MASSIMO: \n\n");
    	List<VerticeAttraversato> result = this.model.trovaCammino(this.boxPorzioni.getValue(), n);
    	for(int i = 1; i< result.size(); i++) {
    		txtResult.appendText("*"+result.get(i).getNome()+" - peso: "+result.get(i).getPeso()+"\n");
    	}
    	txtResult.appendText("CON PESO TOTALE = " + this.model.calcolaPeso(result));
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	
    	String result = "";
    	String scelta = this.boxPorzioni.getValue();
    	Map<String, Double> mappa = this.model.getCorrelate(scelta);
    	
    	for(String s : mappa.keySet()) {
    		result += s+ " - " + (mappa.get(s).intValue()+"\n"); 
    	}
    	txtResult.appendText("Porzioni connesse a " + scelta + ":\n\n");
    	txtResult.appendText(result);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.boxPorzioni.getItems().clear();
    	Double c = -1.0;
    	try{
    		c = Double.parseDouble(this.txtCalorie.getText());
    	} catch(NumberFormatException e) {
    		txtResult.appendText("Inserire un valore numerico nel campo calorie!");
    		return;
    	}
    	this.model.creaGrafo(c);
		this.boxPorzioni.getItems().addAll(this.model.getVertici());	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
