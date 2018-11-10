/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import librarysystem.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author Danish
 */
public class MainController implements Initializable {

    @FXML
    private Text bookname;

    @FXML
    private Text bookauthor;
    @FXML
    private Text bookstatus;

    DatabaseHandler handler;

    @FXML
    private TextField booknameinput;
    @FXML
    private HBox bookinfo;
    @FXML
    private TextField memberidinput;
    @FXML
    private Text membername;
    @FXML
    private Text membercontact;

    String bookid;
    @FXML
    private TextField bookID;
    @FXML
    private ListView<String> issuedatalist;

    boolean rsubmission = false;
    @FXML
    private StackPane rootPane;
    @FXML
    private Text losprice;
    @FXML
    private Button exit;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getInstance();
        
        

    }

    @FXML
    private void loadaddbook(ActionEvent event) {
        loadwindow("/librarysystem/AddBook.fxml", " Add the Book");
    }

    @FXML
    private void loadviewbook(ActionEvent event) {
        loadwindow("/librarysystem/booklist.fxml", "View list");
    }

    @FXML
    private void bookrented(ActionEvent event) {
        loadwindow("/librarysystem/issuedbooks.fxml", "issuedbookslist");
    }

    @FXML
    private void loadvipmember(ActionEvent event) {
        loadwindow("/librarysystem/vipmemberlist.fxml", "View Member");
    }

    @FXML
    private void loadaddmember(ActionEvent event) {
        loadwindow("/librarysystem/addmember.fxml", "Add Member");
    }

    @FXML
    private void loadviewmember(ActionEvent event) {
        loadwindow("/librarysystem/memberlist.fxml", "View list");
    }

    void loadwindow(String location, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadbookinfo(ActionEvent event) {
        clearbookdata();
        String name = booknameinput.getText();
        String qu = "SELECT * FROM BOOKS WHERE TITLE = '" + name + "'";
        ResultSet rs = handler.execQuery(qu);
        boolean ck = false;
        try {
            while (rs.next()) {
                String bname = rs.getString("title");
                String bauthor = rs.getString("author");
                String bid = rs.getString("id");
                String price = rs.getString("lostprice");
                boolean status = rs.getBoolean("Available");
                ck = true;

                bookid = bid;
                
                bookname.setText(bname);
                bookauthor.setText(bauthor);
                String bstatus = (status) ? "Available" : "Not Availble";
                bookstatus.setText(bstatus);
                losprice.setText(price);
            }
            if (!ck) {
                bookname.setText("No Such Book Available");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void clearbookdata() {
        bookname.setText("");
        bookauthor.setText("");
        bookstatus.setText("");
    }

    void clearmemberdata() {
        membername.setText("");
        membercontact.setText("");

    }

    @FXML
    private void loadmemberinfo(ActionEvent event) {
        clearmemberdata();
        String id = memberidinput.getText();
        String qu = "SELECT * FROM MEMBERS WHERE ID = '" + id + "'";
        ResultSet rs = handler.execQuery(qu);
        boolean ck = false;
        try {
            while (rs.next()) {
                String mname = rs.getString("name");
                String mcontact = rs.getString("contact");
                ck = true;

                membername.setText(mname);
                membercontact.setText(mcontact);

            }
            if (!ck) {
                membername.setText("No Such Member here");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void issueoperation(ActionEvent event) {
        String memberid = memberidinput.getText();
        String bid = bookid;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm issue..");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to issue " + bookname.getText() + " to " + membername.getText() + " ?");

        //System.out.println(memberid);
        //System.out.println(bid);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String qu1 = "INSERT INTO ISSUEDBOOKS(BOOKID, MEMBERID) VALUES("
                    + "'" + bid + "',"
                    + "'" + memberid + "')";
            String qu2 = "UPDATE BOOKS SET Available = false WHERE id = '" + bid + "'";
            
            
            
            
                    

            if (handler.execAction(qu1) && handler.execAction(qu2)) {
                
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Issued");
                alert2.setHeaderText("ok");
                alert2.setContentText("Book Issued ");
                alert2.showAndWait();

            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Failed");
                alert2.setHeaderText("fail");
                alert2.setContentText("Book Issued failed ");
                alert2.showAndWait();
            }
        }

    }

    @FXML
    private void renewbookinfo(ActionEvent event) {
        List<String> issuelist = new ArrayList<>();

        String id = bookID.getText();
        String qu = "SELECT * FROM ISSUEDBOOKS WHERE BOOKID ='" + id + "'";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String ibookid = id;
                String imemberid = rs.getString("memberid");
                Timestamp issuetime = rs.getTimestamp("issuetime");
                int irenewcount = rs.getInt("renew_count");

                issuelist.add("Issue Date and Time : " + issuetime.toGMTString());
                issuelist.add("Number of time it renewed : " + irenewcount);

                issuelist.add("Book Information...");

                qu = "SELECT * FROM BOOKS WHERE ID = '" + ibookid + "'";
                ResultSet rs2 = handler.execQuery(qu);
                while (rs2.next()) {
                    issuelist.add("     Book Name : " + rs2.getString("title"));
                    issuelist.add("     Book ID : " + rs2.getString("id"));
                    issuelist.add("     Book Author : " + rs2.getString("author"));
                    issuelist.add("     Book publisher : " + rs2.getString("publisher"));
                }

                qu = "SELECT * FROM MEMBERS WHERE ID = '" + imemberid + "'";
                rs2 = handler.execQuery(qu);

                issuelist.add("Member Information...");
                while (rs2.next()) {
                    issuelist.add("     Member Name : " + rs2.getString("name"));
                    issuelist.add("     Member ID : " + rs2.getString("id"));
                    issuelist.add("     Member Mobile : " + rs2.getString("contact"));
                    issuelist.add("     Member Email : " + rs2.getString("email"));

                    rsubmission = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        issuedatalist.getItems().setAll(issuelist);
    }

    @FXML
    private void booksubmission(ActionEvent event) {
        if (rsubmission) {
            String id = bookID.getText();
            
            String qu = "DELETE FROM ISSUEDBOOKS WHERE BOOKID = '" + id + "'";
            String qu2 = "UPDATE BOOKS SET AVAILABLE = TRUE WHERE ID = '" + id + "'";

            if (handler.execAction(qu) && handler.execAction(qu2)) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Success");
                alert2.setHeaderText("ok");
                alert2.setContentText("Book Submitted ");
                alert2.showAndWait();
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Failed");
                alert2.setHeaderText("error");
                alert2.setContentText("Book Submission failed..");
                alert2.showAndWait();
            }

        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Failed");
            alert2.setHeaderText("error");
            alert2.setContentText("Please select a book..");
            alert2.showAndWait();
            return;
        }
    }

    @FXML
    private void renewaction(ActionEvent event) {
        if (!rsubmission){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Failed");
            alert2.setHeaderText("Sorry");
            alert2.setContentText("Please select a book to renew..");
            alert2.showAndWait();
            return;
        }
        String qu = "UPDATE ISSUEDBOOKS SET ISSUETIME = CURRENT_TIMESTAMP, RENEW_COUNT = RENEW_COUNT+1 WHERE BOOKID = '"+ bookID.getText() +"'";
        //System.out.println(qu);
        if(handler.execAction(qu)){
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Success");
                alert2.setHeaderText("ok");
                alert2.setContentText("Book Renewed ");
                alert2.showAndWait();
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Failed");
                alert2.setHeaderText("ok");
                alert2.setContentText("Book Renew failed ");
                alert2.showAndWait();
        }
    }

    @FXML
    private void fullscreenbutton(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void onexit(ActionEvent event) {
        Platform.exit();
    }

   

}
