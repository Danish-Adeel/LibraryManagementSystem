package librarysystem;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public final class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    

    private DatabaseHandler() {
        createConnection();
        tablesetup();
        membertablesetup();
        issuetablesetup();
        
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }
    
    private static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public Connection getConnection() {
        return conn;
    }
    
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler " + ex.getLocalizedMessage());
            return null;
        } 
        return result;
    }
    
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler " + ex.getLocalizedMessage());
            return false;
        } 
    }

    public static void tablesetup(){
        String Table_Name = "BOOKS";
        
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, Table_Name.toUpperCase(), null);
            if(tables.next()){
                System.out.println("Table already exist");
            }
            else{
                stmt.execute("Create table " + Table_Name + "("+
                            "id varchar(20) primary key,"
                        +   "title varchar(200),"
                        +   "author varchar(200),"
                        +   "publisher varchar(200),"
                        +   "lostprice varchar(200),"
                        +   "Available boolean default true" + ")");
            }
                
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public static void membertablesetup(){
        String Table_Name = "MEMBERS";
        
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, Table_Name.toUpperCase(), null);
            if(tables.next()){
                System.out.println("Table already exist");
            }
            else{
                stmt.execute("Create table " + Table_Name + "("+
                            "id varchar(20) primary key,"
                        +   "name varchar(200),"
                        +   "contact varchar(200),"
                        +   "email varchar(200),"
                        +   "vipmember varchar(200),"
                        +   "debt varchar(200)" + ")");
            }
                
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public static void issuetablesetup(){
        String Table_Name = "ISSUEDBOOKS";
        
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, Table_Name.toUpperCase(), null);
            if(tables.next()){
                System.out.println("Table already exist");
            }
            else{
                stmt.execute("Create table " + Table_Name + "( "
                        +   "bookid varchar(20),"
                        +   "memberid varchar(20) primary key,"
                        +   "issuetime timestamp default current_timestamp,"
                        +   "renew_count integer default 0,"
                        +   "foreign key (bookid) references BOOKS(id),"
                        +   "foreign key (memberid) references MEMBERS(id)" + ")");
            }
                
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    
    
}
