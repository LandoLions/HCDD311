import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class InventoryGUI extends JFrame {

    // Removed 'static' from buttons to keep them instance-specific
    public JButton btnHealth, btnAmmo, btnArmor, btnLives, btnMaps, btnRefresh, btnPUSH, btnPOP, btnSearch;
    public static JLabel lblItemImage; // Kept static so Inventory.java can find it
    public JTable itemTable;
    public Inventory bp;

    public InventoryGUI(Inventory existingBackpack) {
    	this.bp = existingBackpack;
    	
    	// 1. Initialize the data!  bp.createItemInventory();
    		//now done in the GameUI
        
        // 2. Setup Frame Basics
        setTitle("PLAYER BACKPACK - LINKED LIST SYSTEM");
        getContentPane().setBackground(new Color(47, 123, 115));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // 3. Setup Image Preview
        setupImagePreview();

        // 4. Setup Table (We do this BEFORE adding button listeners)
        createJTableOnFrame();

        // 5. Setup Filter Buttons
        setupButtonPanel();

        // 6. Setup Push/Pop/Search Panel
        setupActionPanel();

        // 7. Add Table Selection Listener
        setupTableListener();

        // Finalize Frame
        pack();
        setSize(700, 500);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    private void setupImagePreview() {
        lblItemImage = new JLabel(new ImageIcon(), SwingConstants.CENTER);
        lblItemImage.setPreferredSize(new Dimension(200, 200));
        lblItemImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblItemImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Load initial image if backpack isn't empty
        if (!bp.backpack.isEmpty()) {
            updatePreviewImage(bp.backpack.getFirst().imagePath.toString());
        }
        
        add(lblItemImage);
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnRefresh = new JButton("\uD83D\uDD03");
        btnHealth = new JButton("Health");
        btnAmmo = new JButton("Ammo");
        btnArmor = new JButton("Armor");
        btnLives = new JButton("Lives");
        btnMaps = new JButton("Maps");

        buttonPanel.add(new JLabel("Filters: "));
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnHealth);
        buttonPanel.add(btnAmmo);
        buttonPanel.add(btnArmor);
        buttonPanel.add(btnLives);
        buttonPanel.add(btnMaps);

        // Listeners
        btnRefresh.addActionListener(e -> createJTableOnFrame());
        btnHealth.addActionListener(e -> bp.searchByItemCategory("Health", itemTable));
        btnAmmo.addActionListener(e -> bp.searchByItemCategory("Ammo", itemTable));
        btnArmor.addActionListener(e -> bp.searchByItemCategory("Armor", itemTable));
        btnLives.addActionListener(e -> bp.searchByItemCategory("Lives", itemTable));
        btnMaps.addActionListener(e -> bp.searchByItemCategory("Map", itemTable));

        add(buttonPanel);
    }

    private void setupActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setBackground(new Color(161, 201, 79));

        btnPUSH = new JButton("+ PUSH (Loot) +");
        btnPOP = new JButton("- POP (Drop) -");
        
        btnPOP.addActionListener(e -> {
            bp.popItem();
            createJTableOnFrame(); // Refresh table after pop
        });
        
        btnPUSH.addActionListener(e -> {
            // Example loot to test the push functionality
            Object[] loot = {"901", "Map", "Secret Level", "A hidden map", 1, 500, ".\\src\\images\\cloak1.png"};
            bp.pushItem(new InventoryItem(loot));
            createJTableOnFrame();
        });

        actionPanel.add(btnPUSH);
        actionPanel.add(btnPOP);
        add(actionPanel);
    }

    private void setupTableListener() {
        itemTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && itemTable.getSelectedRow() != -1) {
                int modelRow = itemTable.convertRowIndexToModel(itemTable.getSelectedRow());
                String path = itemTable.getModel().getValueAt(modelRow, 6).toString();
                updatePreviewImage(path);
            }
        });
    }

    private void createJTableOnFrame() {
        Object[][] data = bp.getInventoryArray(); // Use the getter we built
        String[] columns = bp.getColumnNames();

        if (itemTable == null) {
            DefaultTableModel model = new DefaultTableModel(data, columns);
            itemTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(itemTable);
            scrollPane.setPreferredSize(new Dimension(600, 150));
            add(scrollPane);
        } else {
            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
            model.setDataVector(data, columns);
        }
    }

    private void updatePreviewImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(new ImageIcon(path)
                .getImage()
                .getScaledInstance(200, 200, Image.SCALE_SMOOTH));
            lblItemImage.setIcon(icon);
        } catch (Exception e) {
            System.out.println("Image not found: " + path);
        }
    }
    
    public void refreshTable() {
        if (itemTable != null) {
            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
            model.setDataVector(bp.getInventoryArray(), bp.getColumnNames());
        }
    }
}