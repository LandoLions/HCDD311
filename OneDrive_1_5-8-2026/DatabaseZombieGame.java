import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class DatabaseZombieGame {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/databases/PlayerInventoryDB"; 
    private static final String USER = "sa"; 
    private static final String PASS = ""; 
    
    //public static void main(String[] args) {
    public DatabaseZombieGame() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection successful.");
            
            // Execute the workflow
            createInventoryTable(conn);
            //insertInventoryTable(conn); // Added call
            //viewInventoryTable(conn);   // Added call
        
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }            
    } 

    public void createInventoryTable(Connection conn) {
        final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS inventory (
                id INT PRIMARY KEY AUTO_INCREMENT,
                category VARCHAR(25), 
                item_name VARCHAR(50), 
                description VARCHAR(255), 
                quantity INT DEFAULT 0, 
                item_value INT DEFAULT 0,  -- Renamed from 'value' to 'item_value'
                image_path VARCHAR(255)
            );
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
            System.out.println("Inventory table verified/created.");
        } catch (SQLException e) {
            System.err.println("SQL Error State: " + e.getSQLState());
            e.printStackTrace();
        }
    }
    
    // 1. Add a dedicated connection method
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // 2. Adjust the Save logic to be more resilient
    public void saveBackpackToDB(LinkedList<InventoryItem> backpack) {
        String deleteSql = "DELETE FROM inventory";
        String insertSql = "INSERT INTO inventory (category, item_name, description, quantity, item_value, image_path) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Use try-with-resources to ensure connection closes even if a crash occurs
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(deleteSql); 

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                for (InventoryItem item : backpack) {
                    // We skip pstmt.setInt(1) because DB handles ID now
                    pstmt.setString(1, item.category);
                    pstmt.setString(2, item.name);
                    pstmt.setString(3, item.description);
                    pstmt.setInt(4, item.quantity);
                    pstmt.setInt(5, item.value); //item_value in the DB
                    pstmt.setString(6, item.imagePath.toString());
                    pstmt.executeUpdate();
                }
            }
            System.out.println("Backpack saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public LinkedList<InventoryItem> loadInventoryFromDB(Connection conn) {
        LinkedList<InventoryItem> loadedBackpack = new LinkedList<>();
        // Match the column names from your createInventoryTable method
        String sql = "SELECT id, category, item_name, description, quantity, item_value, image_path FROM inventory";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // We pull by column name and create the InventoryItem object
                // Note: Converting the INT id to String to match your InventoryItem constructor
                InventoryItem item = new InventoryItem(
                    String.valueOf(rs.getInt("id")),
                    rs.getString("category"),
                    rs.getString("item_name"),
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("item_value"),
                    rs.getString("image_path")
                );
                
                loadedBackpack.add(item);
            }
            System.out.println("Inventory loaded from DB. Total items: " + loadedBackpack.size());

        } catch (SQLException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
        }
        
        return loadedBackpack;
    }
    
    
    
}