import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class GameUI {
	
	/****
	 * Code in place for GUI color option for Mac and Windows users ***/

	//CLASS (INHERITED) VARIABLES	
	Player plyr = new Player();
	public LevelOne lvl1 = new LevelOne();
	
	public Inventory bp = new Inventory();
	public InventoryGUI ig;
	
	public DatabaseZombieGame zgDB;
	
	//GameUI Global Variables
	private JButton btnMap = new JButton("Map");
	
    public static void main(String[] args) {
        //zgDB = new DatabaseZombieGame();
    	SwingUtilities.invokeLater(() -> new GameUI().createUI());
    }

    public void createUI() {
    	    	
        bp.createItemInventory(); // Initialize the backpack used by the ItemGUI specifically and just once
        zgDB = new DatabaseZombieGame();
        try (Connection conn = zgDB.getConnection()) {
            zgDB.saveBackpackToDB(bp.backpack);
        	bp.backpack = zgDB.loadInventoryFromDB(conn);
        } catch (SQLException e) { 
            // Handle no-save-file-found scenario
        }
        
    	JFrame frame = new JFrame("Pirates of the Dead");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // --- TOP PANEL (Title + Buttons) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Pirates of the Dead", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton btnItems = new JButton("Items");
        JButton btn1 = new JButton("Upgrade");
        JButton btn2 = new JButton("Pause");
        buttonPanel.add(btnItems);
        buttonPanel.add(btn1);
        buttonPanel.add(btn2);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        /************************************/
        btnItems.addActionListener(e -> {
            ig = new InventoryGUI(bp); //show the item/inventory screen            
        });
        /************************************/
        
        frame.add(topPanel, BorderLayout.NORTH);

        // --- CENTER: LAYERED GAME AREA ---
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(900, 450));

        // Background image
        String imgPath = "./src/images/zombieLevel1-graveyard.png";
        ImageIcon icon = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(900, 450, Image.SCALE_SMOOTH));
        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 900, 450);
        layeredPane.add(background, Integer.valueOf(0)); // bottom layer

        // --- Transparent Health Bar Overlay ---
        JPanel healthOverlay = new JPanel();
        healthOverlay.setLayout(new BorderLayout());
        healthOverlay.setBackground(new Color(0, 0, 0, 120)); // semi-transparent black
        healthOverlay.setBounds(20, 20, 200, 40);

        JLabel healthLabel = new JLabel(plyr.playerName + "  Health:");
        healthLabel.setForeground(Color.WHITE);

        JProgressBar healthBar = new JProgressBar(0, 100);
        if (plyr.playerHealth > 50) {
        	healthBar.setValue(plyr.playerHealth);
            healthBar.setForeground(new Color(42, 255, 100, 255));
            
            healthBar.setUI(new BasicProgressBarUI() {
            	protected Color getSelectionBackground() {return Color.BLACK;}
            	protected Color getSelectionForeground() {return Color.WHITE;}
            });
            healthBar.setForeground(Color.GREEN);
        }
        else if (plyr.playerHealth > 25 && plyr.playerHealth <= 50) {
        	healthBar.setValue(plyr.playerHealth);
            healthBar.setForeground(new Color(255, 109, 0, 255));
            
            healthBar.setUI(new BasicProgressBarUI() {
            	protected Color getSelectionBackground() {return Color.BLACK;}
            	protected Color getSelectionForeground() {return Color.WHITE;}
            });
            healthBar.setForeground(Color.ORANGE);
        }
        else {
        	healthBar.setValue(plyr.playerHealth);
        	healthBar.setForeground(new Color(255, 42, 204, 255));
        	
        	healthBar.setUI(new BasicProgressBarUI() {
            	protected Color getSelectionBackground() {return Color.BLACK;}
            	protected Color getSelectionForeground() {return Color.WHITE;}
            });
            healthBar.setForeground(Color.RED);
        }

        healthOverlay.add(healthLabel, BorderLayout.NORTH);
        healthOverlay.add(healthBar, BorderLayout.CENTER);

        layeredPane.add(healthOverlay, Integer.valueOf(1)); // overlay layer

        // --- Transparent Gold Counter Overlay ---
        JPanel goldOverlay = new JPanel();
        goldOverlay.setBackground(new Color(0, 0, 0, 120));
        goldOverlay.setBounds(700, 20, 150, 40);

        JLabel goldLabel = new JLabel("Gold: " + plyr.playerGold);
        goldLabel.setForeground(Color.YELLOW);
        goldOverlay.add(goldLabel);

        layeredPane.add(goldOverlay, Integer.valueOf(1));
        
/************************************************/
        // INTERACTIVE Objects on the game perspective screen 
/************************************************/
        JButton mapInteract = new JButton();
        mapInteract.setBackground(new Color(0, 0, 0, 120));
        mapInteract.setBounds(480, 250, 80, 60);
        mapInteract.setOpaque(false);
        mapInteract.setContentAreaFilled(false);
        mapInteract.setBorderPainted(false);
        layeredPane.add(mapInteract, JLayeredPane.PALETTE_LAYER);
        
        mapInteract.addActionListener(e -> {
            System.out.println("Map interaction triggered at [480, 250]!");
            // Example: Open a dialog or update a label
            //lbldialog.setText("You found a map to Level 1");
            JOptionPane.showMessageDialog(null, "You discovered a secret location!");
            //itm.pickup("map");
            btnMap.setVisible(true);
            // *** search and remove map from level1Items array
            
            //Temporary for HCDD 311
            //Define an Item on LevelOne and PUSH onto itemData
            Object[] loot = {"901", "Map", "Level 1", "Map of Level 1", 1, 1, ".\\src\\images\\level1map.png"};
            bp.pushItem(new InventoryItem(loot)); // PUSH it into the inventory
            
            // If the window is currently open, tell it to refresh!
            if (ig != null && ig.isVisible()) {
                ig.refreshTable();
            }
        });
        
        mapInteract.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Make it more opaque or change color on hover
                mapInteract.setBackground(new Color(255, 255, 255, 180));
                mapInteract.setBorderPainted(true);
                mapInteract.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Return to original transparency
                mapInteract.setBackground(new Color(0, 0, 0, 120));
                mapInteract.setBorderPainted(false);
                mapInteract.repaint();
            }
        });
        
        // INTERACTIVE - Item Class Buttons
        /*************************************/
        JButton itemInteract = new JButton();
        itemInteract.setBackground(new Color(10, 0, 0, 120));
        itemInteract.setBounds(470, 350, 90, 40);
        itemInteract.setContentAreaFilled(false);
        itemInteract.setBorderPainted(false);
        layeredPane.add(itemInteract, JLayeredPane.PALETTE_LAYER);
        
        itemInteract.addActionListener(e -> {
            System.out.println("Item interaction triggered!");
            //Clicking the button "pushes" the item onto the stack.
            // *** search and remove map from level1Items array
            
            //Temporary for HCDD 311
            //Define an Item on LevelOne and PUSH onto itemData
            Object[] loot = {"004", "Health", "mHP", "Medium health pack", 1, 25, ".\\src\\images\\mHP.png"};
            bp.pushItem(new InventoryItem(loot)); // PUSH it into the inventory
            
            // If the window is currently open, tell it to refresh!
            if (ig != null && ig.isVisible()) {
                ig.refreshTable();
            }
        });
        
        itemInteract.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Make it more opaque or change color on hover
            	itemInteract.setBackground(new Color(255, 255, 255, 180));
            	itemInteract.setBorderPainted(true);
            	itemInteract.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Return to original transparency
            	itemInteract.setBackground(new Color(0, 0, 0, 120));
            	itemInteract.setBorderPainted(false);
            	itemInteract.repaint();
            }
        });
        
        JButton livesInteract = new JButton();
        livesInteract.setBackground(new Color(0, 0, 0, 120));
        livesInteract.setBounds(760, 250, 70, 50);
        livesInteract.setContentAreaFilled(false);
        livesInteract.setBorderPainted(false);
        layeredPane.add(livesInteract, JLayeredPane.PALETTE_LAYER);
        
        livesInteract.addActionListener(e -> {
            System.out.println("You got a life!");
            //Clicking the button "pushes" the item onto the stack.
            // *** search and remove map from level1Items array
            
            //Temporary for HCDD 311
            //Define an Item on LevelOne and PUSH onto itemData
            Object[] loot = {"202", "Lives", "1up", "Increase lives by 1", 0, 1, ".\\src\\images\\1up.jpg"};
            bp.pushItem(new InventoryItem(loot)); // PUSH it into the inventory
            
            // If the window is currently open, tell it to refresh!
            if (ig != null && ig.isVisible()) {
                ig.refreshTable();
            }
        });
        
        livesInteract.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Make it more opaque or change color on hover
            	livesInteract.setBackground(new Color(255, 255, 255, 180));
            	livesInteract.setBorderPainted(true);
            	livesInteract.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Return to original transparency
            	livesInteract.setBackground(new Color(0, 0, 0, 120));
            	livesInteract.setBorderPainted(false);
            	livesInteract.repaint();
            }
        });
        
        
        /*************************************/
        // --- Transparent Weapon Info Overlay ---
        JPanel weaponOverlay = new JPanel();
        weaponOverlay.setLayout(new GridLayout(2, 1));
        weaponOverlay.setBackground(new Color(0, 0, 0, 120));
        weaponOverlay.setBounds(20, 360, 100, 100);

        String wpnImgPath = "./src/images/cutlass.png";
        ImageIcon wpnIcon = new ImageIcon(new ImageIcon(wpnImgPath).getImage().getScaledInstance(50, 100, Image.SCALE_SMOOTH));
        JLabel weaponLabel = new JLabel(wpnIcon);
        JLabel damageLabel = new JLabel("DMG: 25");
        //weaponLabel.setForeground(Color.WHITE);
        damageLabel.setForeground(Color.WHITE);

        weaponOverlay.add(weaponLabel);
        weaponOverlay.add(damageLabel);

        layeredPane.add(weaponOverlay, Integer.valueOf(1));

        frame.add(layeredPane, BorderLayout.CENTER);

        JPanel inventory = new JPanel(new BorderLayout());
        //JButton btnMap = new JButton("Map");
        btnMap.setVisible(false);
        JLabel lbldialog = new JLabel("ouput appears here");
        inventory.add(btnMap, BorderLayout.WEST);
        inventory.add(lbldialog, BorderLayout.EAST);
        frame.add(inventory, BorderLayout.SOUTH);        
        
        frame.setVisible(true);
    }
}
