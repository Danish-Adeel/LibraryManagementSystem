/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarysystem;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
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
public class IssuedbooksController implements Initializable {
    
    List<issueinfo> list = new ArrayList<>();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableColumn<issueinfo, String> idcol;
    @FXML
    private TableColumn<issueinfo, String> bookidcol;
    @FXML
    private TableColumn<issueinfo, String> booknamecol;
    @FXML
    private TableColumn<issueinfo, String> holdernamecol;
    @FXML
    private TableColumn<issueinfo, String> dateofissuecol;
    @FXML
    private TableColumn<issueinfo, String> dayscol;
    @FXML
    private TableColumn<issueinfo, Integer> finecol;
    @FXML
    private TableView<issueinfo> tableview;
    
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
        
    }    
    private void initCol() {
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookidcol.setCellValueFactory(new PropertyValueFactory<>("bookid"));
        booknamecol.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        holdernamecol.setCellValueFactory(new PropertyValueFactory<>("holdername"));
        dateofissuecol.setCellValueFactory(new PropertyValueFactory<>("dateofissue"));
        dayscol.setCellValueFactory(new PropertyValueFactory<>("days"));
        finecol.setCellValueFactory(new PropertyValueFactory<>("fine"));
        
    }
    
    
    
    private void loadData() {
        list.clear();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT i.bookid, i.memberid, i.issuetime, m.name, b.title FROM ISSUEDBOOKS i\n"
                + "LEFT OUTER JOIN MEMBERS m\n"
                + "ON m.id = i.memberid\n"
                + "LEFT OUTER JOIN BOOKS b\n"
                + "ON b.id = i.bookid";
        ResultSet rs = handler.execQuery(qu);
        //Preferences pref = Preferences.getPreferences();
        try {
            int counter = 0;
            while (rs.next()) {
                counter += 1;
                String memberName = rs.getString("name");
                String bookID = rs.getString("bookid");
                String bookTitle = rs.getString("title");
                Timestamp issueTime = rs.getTimestamp("issuetime");
                System.out.println("Issued on " + issueTime);
                Integer days = Math.toIntExact(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - issueTime.getTime())) + 1;
                Integer fine = IssuedbooksController.getFineAmount(days);
                issueinfo issueInfo = new issueinfo(counter, bookTitle, bookID, memberName, IssuedbooksController.formatDateTimeString(new Date(issueTime.getTime())), days, fine);
                list.add(issueInfo);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tableview.getItems().setAll(list);
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    public static int getFineAmount(int totalDays) {
        
        Integer fineDays = totalDays - 7;
        int fine = 0;
        if (fineDays > 0) {
            fine = fineDays * 2;
        }
        return fine;
    }
    
    public static String formatDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }
    
    
    public static class issueinfo{
        private int id;
        private String bookname;
        private String bookid;
        private String holdername;
        private String dateofissue;
        private int days;
        private int fine;

        public issueinfo(int id, String bookname, String bookid, String holdername, String dateofissue, int days, int fine) {
            this.id = id;
            this.bookname = bookname;
            this.bookid = bookid;
            this.holdername = holdername;
            this.dateofissue = dateofissue;
            this.days = days;
            this.fine = fine;
        }

       
     
        public int getId() {
            return id;
        }

        public String getBookname() {
            return bookname;
        }

        public String getBookid() {
            return bookid;
        }

        public String getHoldername() {
            return holdername;
        }

        public String getDateofissue() {
            return dateofissue;
        }

        public int getDays() {
            return days;
        }

        public Integer getFine() {
            return fine;
        }
        

    }
    
}
