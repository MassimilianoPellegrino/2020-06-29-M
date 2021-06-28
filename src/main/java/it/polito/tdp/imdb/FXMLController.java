/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.db.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.RegistaPeso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.boxRegista.getItems().clear();
    	int anno;
    	try {
    		anno = this.boxAnno.getValue();
    		model.creaGrafo(anno);
    		this.txtResult.appendText("grafo creato con "+model.getGrafo().vertexSet().size()+" vertici e "+model.getGrafo().edgeSet().size()+" archi");
    		this.boxRegista.getItems().addAll(model.getGrafo().vertexSet());
    	}catch(NullPointerException e) {
    		this.txtResult.setText(e.getMessage());
    	}
    	
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	this.txtResult.clear();
    	Director dir;
    	try {
    		dir = this.boxRegista.getValue();
    		List<RegistaPeso> res = model.getAdiacenti(dir);
    		for(RegistaPeso rp: res) {
    			if(rp.getD1().equals(dir))
    				this.txtResult.appendText(rp.getD2()+" "+rp.getW()+"\n");
    			else if(rp.getD2().equals(dir))
    				this.txtResult.appendText(rp.getD1()+" "+rp.getW()+"\n");
    		}

    		
    	}catch(NullPointerException e){
    		e.printStackTrace();
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	this.txtResult.clear();
    	int c;
    	Director d;
    	try {
    		c = Integer.parseInt(this.txtAttoriCondivisi.getText());
    		d = boxRegista.getValue();
    		List<Director> res = model.getCammino(d, c);
    		int sum = model.getBestSum();
    		for(Director dir: res)
    			this.txtResult.appendText(dir+"\n");
    		
    		this.txtResult.appendText("somma: "+sum);
    		
    	}catch(NullPointerException e) {
    		this.txtResult.setText(e.getMessage());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero intero");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	this.boxAnno.getItems().add(2004);
    	this.boxAnno.getItems().add(2005);
    	this.boxAnno.getItems().add(2006);

    	
    }
    
}
