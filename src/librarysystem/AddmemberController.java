/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarysystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Danish
 */
public class AddmemberController implements Initializable {

    DatabaseHandler handler = DatabaseHandler.getInstance();
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField id;
    @FXML
    private Button savebutton;
    @FXML
    private Button cancel;
    @FXML
    private TextField name;
    @FXML
    private TextField contact;
    @FXML
    private TextField email;
    @FXML
    private TextField vipmember;
    @FXML
    private TextField debt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addMember(ActionEvent event) {
        String MemberID = id.getText();
        String MemberName = name.getText();
        String MemberContact = contact.getText();
        String MemberEmail = email.getText();
        String MemberVIP = vipmember.getText();
        String Memberdebt = debt.getText();
        
        if(MemberID.isEmpty() || MemberName.isEmpty() || MemberContact.isEmpty() || MemberEmail.isEmpty() || MemberVIP.isEmpty() || Memberdebt.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Please complete all fields");
            alert.showAndWait();
            return;
        }
        String qu = "Insert into MEMBERS Values"
                + "( '" + MemberID + "',"
                + "'" +MemberName + "' ,"
                + "'" +MemberContact + "' ,"
                + "'" +MemberEmail + "' ,"
                + "'" +MemberVIP + "' ,"
                + "'" +Memberdebt + "' "
                + ")";
        System.out.println(qu);
        if (handler.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Message");
            alert.setContentText("ADDED");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Message");
            alert.setContentText("failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
}
