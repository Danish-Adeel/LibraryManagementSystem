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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Danish
 */
public class VipmemberlistController implements Initializable {
    
    List<Member> list = new ArrayList<>();

    @FXML
    private TableView<Member> tableview;
    @FXML
    private TableColumn<Member, String> idcol;
    @FXML
    private TableColumn<Member, String> namecol;
    @FXML
    private TableColumn<Member, String> contactcol;
    @FXML
    private TableColumn<Member, String> emailcol;
    @FXML
    private TableColumn<Member, String> vipcol;
    @FXML
    private TableColumn<Member, String> debtcol;
    @FXML
    private AnchorPane rootpane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        intializecol();
        loaddata();
        
    }

    private void intializecol() {
        namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactcol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        vipcol.setCellValueFactory(new PropertyValueFactory<>("vip"));
        debtcol.setCellValueFactory(new PropertyValueFactory<>("debt"));
        
    }
    
    private void loaddata() {
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "Select * from MEMBERS m, ISSUEDBOOKS i WHERE m.VIPMEMBER = 'YES' AND i.MEMBERID = m.ID";
        ResultSet rs = handler.execQuery(qu);
        try {
            while(rs.next()){
                
                    String name = rs.getString("name");
                    String id = rs.getString("id");
                    String contact = rs.getString("contact");
                    String email = rs.getString("email");
                    String vip = rs.getString("vipmember");
                    String debt = rs.getString("debt");
                    
                    
                    list.add(new Member(id, name, email, contact, vip, debt));
                    
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableview.getItems().setAll(list);
    }
    @FXML
    private void refreshaction(ActionEvent event) {
        loaddata();
    }
}
