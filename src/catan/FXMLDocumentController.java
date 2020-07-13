/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author José Sampaio
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private Rectangle rchat;
    @FXML
    private Rectangle rtrades;
    @FXML
    private Text chatTitle;
    @FXML
    private TextArea outputChatText;
    @FXML
    public static TextField inputChatText;
    @FXML
    private Text tradesTitle;
    @FXML
    private Text bankText;
    @FXML
    private Text harborText1;
    @FXML
    private Text harborText;
    @FXML
    private MenuButton playerMenu1;
    @FXML
    private MenuItem PlayerOption1;
    @FXML
    private MenuItem playerOption2;
    @FXML
    private MenuItem playerOption3;
    @FXML
    private MenuButton harborMenu;
    @FXML
    private MenuItem harborOption1;
    @FXML
    private MenuItem harborOption2;
    @FXML
    private Button bankTradeButton;
    @FXML
    private Button harborTradeButton1;
    @FXML
    private Button harborTradeButton;
    @FXML
    private Tab InformationTab;
    @FXML
    private Button roadButton;
    @FXML
    private Button houseButton;
    @FXML
    private Button cityButton;
    @FXML
    private Button developmentCardButton;
    @FXML
    private Tab Player1Tab;
    @FXML
    private Text numberBrickPlayer1;
    @FXML
    private Text numberMetalPlayer1;
    @FXML
    private Text numberWheatPlayer1;
    @FXML
    private Text numberWoodPlayer1;
    @FXML
    private Text numberCottonPlayer1;
    @FXML
    private Text numberArmyPlayer1;
    @FXML
    private Text numberCardsPlayer1;
    @FXML
    private Text numberVillagePlayer1;
    @FXML
    private Text numberStreetPlayer1;
    @FXML
    private Text numberCityPlayer1;
    @FXML
    private Tab Player2Tab;
    @FXML
    private Text numberBrickPlayer2;
    @FXML
    private Text numberMetalPlayer2;
    @FXML
    private Text numberWheatPlayer2;
    @FXML
    private Text numberWoodPlayer2;
    @FXML
    private Text numberCottonPlayer2;
    @FXML
    private Text numberArmyPlayer2;
    @FXML
    private Text numberCardsPlayer2;
    @FXML
    private Text numberVillagePlayer2;
    @FXML
    private Text numberStreetPlayer2;
    @FXML
    private Text numberCityPlayer2;
    @FXML
    private Tab player3Tab;
    @FXML
    private Text numberBrickPlayer3;
    @FXML
    private Text numberMetalPlayer3;
    @FXML
    private Text numberWheatPlayer3;
    @FXML
    private Text numberWoodPlayer3;
    @FXML
    private Text numberCottonPlayer3;
    @FXML
    private Text numberArmyPlayer3;
    @FXML
    private Text numberCardsPlayer3;
    @FXML
    private Text numberVillagePlayer3;
    @FXML
    private Text numberStreetPlayer3;
    @FXML
    private Text numberCityPlayer3;
    @FXML
    private Tab player4Tab;
    @FXML
    private Text numberBrickPlayer4;
    @FXML
    private Text numberMetalPlayer4;
    @FXML
    private Text numberWheatPlayer4;
    @FXML
    private Text numberWoodPlayer4;
    @FXML
    private Text numberCottonPlayer4;
    @FXML
    private Text numberArmyPlayer4;
    @FXML
    private Text numberCardsPlayer4;
    @FXML
    private Text numberVillagePlayer4;
    @FXML
    private Text numberStreetPlayer4;
    @FXML
    private Text numberCityPlayer4;
    @FXML
    private Button endTurnButton;
    @FXML
    private Text player1Text;
    @FXML
    private Label player1Label;
    @FXML
    private Text pvText_player1;
    @FXML
    private Label pvLabel_player1;
    @FXML
    private Text player2Text;
    @FXML
    private Label player2Label;
    @FXML
    private Text pvText_player2;
    @FXML
    private Label pvLabel_player2;
    @FXML
    private Text pvText_player4;
    @FXML
    private Label pvLabel_player4;
    @FXML
    private Text player4Text;
    @FXML
    private Label player4Label;
    @FXML
    private Text pvText_player3;
    @FXML
    private Label pvLabel_player3;
    @FXML
    private Text player3Text;
    @FXML
    private Label player3Label;
    
    public static TextArea chat;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        chat=outputChatText;
        inputChatText= new TextField();
        // all
        
    }    

    @FXML
    private void onClick(MouseEvent event) {
        outputChatText.appendText("fdc");
        
    }

    public void appendChat(String msg){
    chat.appendText(msg);
    
    }
    
    public String getText(){
        
        
    return inputChatText.getText();
    
    
    }
    
   
    public void submitText(KeyEvent event){
    if(event.getCode() == KeyCode.ENTER)
        outputChatText.appendText(inputChatText.getText());
    //return inputChatText.getText();
    
    
    
    }
   
    
    
    
}
