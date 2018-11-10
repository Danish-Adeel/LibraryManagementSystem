/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarysystem;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Danish
 */
public class BooklistController implements Initializable {
    
    List<Book> list = new ArrayList<>();

    @FXML
    private TableColumn<Book, String> idcol;
    @FXML
    private TableColumn<Book, String> namecol;
    @FXML
    private TableColumn<Book, String> authorcol;
    @FXML
    private TableColumn<Book, String> publishercol;
   
    @FXML
    private TableColumn<Book, String> lostpricecol;
    @FXML
    private TableColumn<Book, Boolean> availcol;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> tableview;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        intializecol();
        loaddata();
    }    

    private void intializecol() {
        namecol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorcol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publishercol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        lostpricecol.setCellValueFactory(new PropertyValueFactory<>("lostprice"));
        availcol.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
    }

    private void loaddata() {
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "Select * from BOOKS";
        ResultSet rs = handler.execQuery(qu);
        try {
            while(rs.next()){
                
                    String title = rs.getString("title");
                    String id = rs.getString("id");
                    String author = rs.getString("author");
                    String publisher = rs.getString("publisher");
                    String lostprice = rs.getString("lostprice");
                    boolean available = rs.getBoolean("Available");
                    
                    list.add(new Book(id, title, author, publisher, lostprice, available));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableview.getItems().setAll(list);
    }
    @FXML
    private void deleteaction(ActionEvent event) {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        Book selecteddelete = tableview.getSelectionModel().getSelectedItem();
        if (selecteddelete == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText("Book not selected");
            alert.setContentText("Please select the member");
            alert.show();
            return;
        }
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("Confirmation");
        alert2.setHeaderText("Deleting Book");
        alert2.setContentText("Are you sure to delete " + selecteddelete.getTitle() + " ?");
        Optional<ButtonType> answer = alert2.showAndWait();
        if (answer.get() == ButtonType.OK) {
            String qu = "DELETE FROM BOOKS WHERE ID = '" + selecteddelete.getId() + "'";
            if (handler.execAction(qu)) {
                Alert alert4 = new Alert(Alert.AlertType.INFORMATION);
                alert4.setTitle("Deleted");
                alert4.setHeaderText(null);
                alert4.setContentText("Book Deleted");
                alert4.showAndWait();
                list.remove(selecteddelete);
            }else{
                Alert alert5 = new Alert(Alert.AlertType.ERROR);
                alert5.setTitle("Failed");
                alert5.setHeaderText(null);
                alert5.setContentText("Book Deletion failed..");
                alert5.show();
            }

        } else {
            Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
            alert3.setTitle("Cancelled");
            alert3.setHeaderText(null);
            alert3.setContentText("Cancelled");
            alert3.showAndWait();
        }
    }

    @FXML
    private void refreshaction(ActionEvent event) {
        loaddata();
    }
    
}
