import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class ItemGUI extends JFrame {

	public static JButton btnHealth, btnAmmo, btnArmor, btnLives; // ###
	public static JLabel lblItemImage;
	public ImageIcon icon;
	
	public JTable itemTable;
	public static Item backpack = new Item();
	
	public ItemGUI() {
		/** Constructor function that creates the mini Frame for
		 *  the player to choose an item from buttons. **/
		//backpack.createItemInventory();
		backpack.getItemData();
		createJTableOnFrame(); //since UI is a different class than the 
		//mechanics of the Item class now get the data first. Since ItemGUI
		//is basically an overlay now.
		/** move the itemTable.getSelectionModel()...action listener here when all complete */
		
		final int FRAME_WIDTH = 700;
	    final int FRAME_HEIGHT = 500;
	    String labels[] = { "+HEALTH", "+AMMO", "+ARMOR", "+LIVES" };
	        
	    Container c = getContentPane();
	    BoxLayout layout = new BoxLayout(c, BoxLayout.Y_AXIS);
	    c.setLayout(layout);
	    c.setBackground(new Color(47, 123, 115)); // Red, Green, Blue       
	    
        /*******  Display Image for Item *******
         * Current default state is the image associated
         * to the item at the second row of the master list/table.
         * Because the rowIndex is set to the value of 1
         ***************************************/
        int rowIndex = 0; // Index of the desired row (e.g., the second row)
        // Direct access to the row and temporarily store the row into a 1D array.
        Object[] tempArray = backpack.itemData[rowIndex]; 

        // To make a quick backup copy of the row (recommended if planning to modify the new array)
        Object[] oneDArrayCopy = java.util.Arrays.copyOf(backpack.itemData[rowIndex], backpack.itemData[rowIndex].length);

        String lblItemImagePath = tempArray[tempArray.length-1].toString(); //pull the image path from the row
        //System.out.println(lblItemImagePath);
        icon = new ImageIcon(new ImageIcon(lblItemImagePath).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));        
        //
        lblItemImage = new JLabel(icon, SwingConstants.CENTER);
        lblItemImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblItemImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(lblItemImage);
        
        /***********************************************
	     * Create JButtons on Frame and Button Actions
	     ***********************************************/
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
         
        JButton btnRefresh = new JButton("\uD83D\uDD03");
        JButton btnHealth = new JButton("Health");
        JButton btnAmmo = new JButton("Ammo");
        JButton btnArmor = new JButton("Armor");
        JButton btnLives = new JButton("Lives");
        JButton btnMaps = new JButton("Maps");
        
        buttonPanel.add(new JLabel("Filters: "));
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnHealth);
        buttonPanel.add(btnAmmo);
        buttonPanel.add(btnArmor);
        buttonPanel.add(btnLives);
        buttonPanel.add(btnMaps);
	    
        buttonPanel.setBackground(Color.white);
        c.add(buttonPanel);
        
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
	    	  @Override
	    	  public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		    // Ensure the UI update happens on the correct thread
	    		    javax.swing.SwingUtilities.invokeLater(() -> {
	    		        createJTableOnFrame();
	    		        //backpack.getItemData();
	    		    });
	    		}
	      });
        
        btnHealth.addActionListener(new java.awt.event.ActionListener() {
	    	  @Override
	          public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 // reset player health  
	    		 //ply.playerHealth = ply.playerHealth + 10;
	    		 backpack.searchByItemCategory("Health", itemTable);
	    	  }
	      });
	    	    
        btnAmmo.addActionListener(new java.awt.event.ActionListener() {
	    	  @Override
	          public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 
	    		 backpack.searchByItemCategory("Ammo", itemTable);
	    	  }
	      });
        
        btnArmor.addActionListener(new java.awt.event.ActionListener() {
	    	  @Override
	          public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 
	    		 backpack.searchByItemCategory("Armor", itemTable);
	    	  }
	      });
        
        btnLives.addActionListener(new java.awt.event.ActionListener() {
	    	  @Override
	          public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 // add 1 to player's life count 
	    		 //ply.lives = ply.lives + 1;
	    		 backpack.searchByItemCategory("Lives", itemTable);
	    	  }
	      });
        
        btnMaps.addActionListener(new java.awt.event.ActionListener() {
	    	  @Override
	          public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 
	    		 backpack.searchByItemCategory("Map", itemTable);
	    	  }
	    });
                
        /****************************************************************
	     * PUSH and POP - STACK example Action Button & Search Text Box
	     ****************************************************************/
        JPanel pushPopSearchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
         
        JButton btnPUSH = new JButton("+ PUSH +"); //no functionality
        JButton btnPOP = new JButton("- POP -");
        JTextField txtSearch = new JTextField("Search Items ...", 20);
        //txtSearch.setFont(new Font("Arial", Font.BOLD, 16));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setFont(new Font(txtSearch.getFont().getName(), Font.ITALIC, txtSearch.getFont().getSize()));
        JButton btnSearch = new JButton("\uD83D\uDD0D"); //alt code for magnifying glass
        
        pushPopSearchPanel.add(btnPUSH);
        pushPopSearchPanel.add(btnPOP);
        pushPopSearchPanel.add(txtSearch);
        pushPopSearchPanel.add(btnSearch);
	    
        pushPopSearchPanel.setBackground(new Color(161, 201, 79));
        
        btnPOP.addActionListener(new java.awt.event.ActionListener() {
	    	  @Override
	          public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 backpack.popItem();
	    		 createJTableOnFrame();
	    	  }
	    });
        
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Search Items ...")) {
                    txtSearch.setText(""); // Clear text
                    txtSearch.setForeground(Color.BLACK); // Set normal color
                    txtSearch.setFont(new Font(txtSearch.getFont().getName(), Font.PLAIN, txtSearch.getFont().getSize())); // Normal font
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Search..."); // Reset placeholder
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setFont(new Font(txtSearch.getFont().getName(), Font.ITALIC, txtSearch.getFont().getSize()));
                }
            }
        });
        
        c.add(pushPopSearchPanel);
        
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
	    	  @Override
	          public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 
	    		 //currently inactive
	    	  }
	    });
      
        /************************************/
        /** *** Other Action Listeners ***  */
        itemTable.getSelectionModel().addListSelectionListener(e -> {
            // Ignore adjusting events
            if (e.getValueIsAdjusting()) return;

            int viewRow = itemTable.getSelectedRow();
            if (viewRow < 0) return;

            // Convert view index → model index (important!)
            int modelRow = itemTable.convertRowIndexToModel(viewRow);

            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();

            Object pathValue = model.getValueAt(modelRow, 6); // Image Path column

            if (pathValue != null) {
                String imagePath = pathValue.toString();

                ImageIcon icon = new ImageIcon(
                    new ImageIcon(imagePath)
                        .getImage()
                        .getScaledInstance(200, 200, Image.SCALE_SMOOTH)
                );

                lblItemImage.setIcon(icon);
            }
        });      
        
        /** Finalize the Frame **/
	    pack();
	    setSize(FRAME_WIDTH, FRAME_HEIGHT);
	    setTitle("ITEM MENU");
	    setVisible(true);	      
	    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
		
	/***********************************************
     * Create JTable on Frame
     ***********************************************/	
	private void createJTableOnFrame() {
	    //backpack.createItemInventory(); // Reset the data source
		backpack.getItemData();

	    // Check if the table already exists
	    if (itemTable == null) {
	        // FIRST TIME INITIALIZATION: Create the table and scroll pane
	        DefaultTableModel tableModel = new DefaultTableModel(backpack.itemData, backpack.columnNames);
	        itemTable = new JTable(tableModel);
	        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	        JScrollPane itemScrollPane = new JScrollPane(itemTable);
	        itemScrollPane.setPreferredSize(new Dimension(250, 375));
	        
	        // Add to the frame once
	        this.add(itemScrollPane, BorderLayout.CENTER);
	    } else {
	        // REFRESH LOGIC: update the data inside the existing model
	        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
	        model.setDataVector(backpack.itemData, backpack.columnNames);
	        
	        // UI needs to redraw the component
	        itemTable.revalidate();
	        itemTable.repaint();
	    }
	}
	
}
