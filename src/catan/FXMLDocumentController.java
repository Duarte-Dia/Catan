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
import javafx.scene.Group;
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
import javafx.scene.shape.Line;
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
    private TextField inputChatText;
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
    private Text pvText_player1;
    @FXML
    private Label pvLabel_player1;
    @FXML
    private Text player2Text;
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
    private Text pvText_player3;
    @FXML
    private Label pvLabel_player3;
    @FXML
    private Text player3Text;
    @FXML
    private Group rc;
    @FXML
    private Line r2l2;
    @FXML
    private Line r2l8;
    @FXML
    private Line r2l4;
    @FXML
    private Line r2l6;
    @FXML
    private Line c3l2;
    @FXML
    private Line c3l3;
    @FXML
    private Line c3l6;
    @FXML
    private Line c3l1;
    @FXML
    private Line c3l4;
    @FXML
    private Line c3l5;
    @FXML
    private Line r6l1;
    @FXML
    private Line r6l3;
    @FXML
    private Line r6l5;
    @FXML
    private Line c1l1;
    @FXML
    private Line c1l3;
    @FXML
    private Line c1l2;
    @FXML
    private Line c1l4;
    @FXML
    private Line c2l1;
    @FXML
    private Line c2l3;
    @FXML
    private Line c2l2;
    @FXML
    private Line c2l5;
    @FXML
    private Line c2l4;
    @FXML
    private Line r5l2;
    @FXML
    private Line r5l8;
    @FXML
    private Line r5l6;
    @FXML
    private Line r5l4;
    @FXML
    private Line r6l2;
    @FXML
    private Line r6l4;
    @FXML
    private Line r6l6;
    @FXML
    private Line r3l9;
    @FXML
    private Line r3l3;
    @FXML
    private Line r3l1;
    @FXML
    private Line r3l5;
    @FXML
    private Line r3l7;
    @FXML
    private Line c4l1;
    @FXML
    private Line c4l3;
    @FXML
    private Line c4l2;
    @FXML
    private Line c4l5;
    @FXML
    private Line c4l4;
    @FXML
    private Line r2l1;
    @FXML
    private Line r2l7;
    @FXML
    private Line r2l5;
    @FXML
    private Line r2l3;
    @FXML
    private Line c5l1;
    @FXML
    private Line c5l3;
    @FXML
    private Line c5l2;
    @FXML
    private Line c5l4;
    @FXML
    private Line r4l3;
    @FXML
    private Line r4l5;
    @FXML
    private Line r4l9;
    @FXML
    private Line r4l1;
    @FXML
    private Line r4l7;
    @FXML
    private Line r1l2;
    @FXML
    private Line r1l4;
    @FXML
    private Line r1l6;
    @FXML
    private Line r5l1;
    @FXML
    private Line r5l7;
    @FXML
    private Line r5l3;
    @FXML
    private Line r5l5;
    @FXML
    private Line r4l10;
    @FXML
    private Line r4l4;
    @FXML
    private Line r4l2;
    @FXML
    private Line r4l6;
    @FXML
    private Line r4l8;
    @FXML
    private Line r1l1;
    @FXML
    private Line r1l3;
    @FXML
    private Line r1l5;
    @FXML
    private Line r3l4;
    @FXML
    private Line r3l6;
    @FXML
    private Line r3l10;
    @FXML
    private Line r3l2;
    @FXML
    private Line r3l8;

    public static TextArea chat;
    public static TextField inputChat;
    public static Tab tp1, tp2, tp3, tp4;
    public static Button endTurn, roadBtn, bankTradeBtn ;
    public static Group linesGroup;
    public static MenuItem exitBtn, contributorsBtn, playerOpt1, playerOpt2, playerOpt3, harborOpt1, harborOpt2;
    @FXML
    private MenuItem exitButton;
    @FXML
    private MenuItem ruleButton;
    @FXML
    private MenuItem contributorsButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        chat = outputChatText;
        inputChat = inputChatText;
        tp1 = Player1Tab;
        tp2 = Player2Tab;
        tp3 = player3Tab;
        tp4 = player4Tab;
        endTurn = endTurnButton;
        roadBtn = roadButton;
        linesGroup = rc;
        exitBtn =exitButton;
        contributorsBtn = contributorsButton;
        playerOpt1 = PlayerOption1;
        playerOpt2 = playerOption2;
        playerOpt3 = playerOption3;
        harborOpt1 = harborOption1;
        harborOpt2 = harborOption2;
        bankTradeBtn = bankTradeButton;        
        // all
    }

    @FXML
    public void endTurnClick(ActionEvent event) {

    }

    public String getText() {

        return inputChatText.getText();

    }

    @FXML
    public void submitText(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            outputChatText.appendText(inputChatText.getText() + "\n");
            inputChatText.clear();

        }
        //return inputChatText.getText();

    }

    @FXML
    private void displayContributors(ActionEvent event) {
    }

}
