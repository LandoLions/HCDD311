import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Item extends JFrame{
	
	/*** Week 14 - for video #12
	 * 	0. comment out or remove depreciated code (###)
	 *  
	 *  1. Push and Pop items from a Stack
	 *  
	 *  2. What are the necessary column headings in the item_data_table??
	 * 			-- Category, Name, Description, Quantity, Value
	 * 			- Is it left up to the columns read from the database??
	 */	

	public String[] columnNames;	
	public Object[][] itemData; // = new Object[7][10];
			
	public static Player ply; // ###
	
	//Constructor method
	public Item() {
		columnNames = new String[7]; 
		// AND / OR fetch the database connection here.
		// For HCDD 311 we'll initialize the array here.
        itemData = new Object[10][7];
	}
	
	// =================================================================
    // Populate the ITEM inventory data table (currently stored list)
	// =================================================================
	public Object createItemInventory() {
		columnNames = new String[] {"ItemID", "Category", "Name", "Description", "Quantity", "Value", "Image Path"};
		
	    itemData = new Object[][] {
	        {"001", "Health", "sHP", "Small health pack", 1, 10, getItemImage(0)},
	        {"002", "Health", "mHP", "Medium health pack", 1, 25, getItemImage(1)}, //... etc
	        {"003", "Health", "xHP", "Large health pack", 1, 50, getItemImage(2)},
	        {"101", "Ammo", "pistolRounds", "6 rounds for a pistol weapon", 1, 6, getItemImage(3)},
	        {"102", "Ammo", "rubberChicken", "A rubber chicken", 1, 1, getItemImage(4)},
	        {"103", "Ammo", "grenade", "Grenade", 0, 50, getItemImage(5)},
	        {"201", "Lives", "1up", "Increase lives by 1", 0, 1, getItemImage(6)},
	        {"301", "Armor", "Jacket", "Comfy warm jacket", 1, 25, getItemImage(7)},
	        {"302", "Armor", "BPvest", "Bullet proof vest", 0, 35, getItemImage(8)},
	        {"303", "Armor", "ZombieCloak", "Umm...take a guess what this is made of", 0, 75, getItemImage(9)},	    
	    };
	    
		return itemData;
	}
	
	// Example use of an ArrayList<> 
	public String getItemImage(int position) {
		
		ArrayList<String> itemImage = new ArrayList<String>();
		itemImage.add(".\\src\\images\\sHP.png");
		itemImage.add(".\\src\\images\\mHP.png");
		itemImage.add(".\\src\\images\\xHP.png");
		itemImage.add(".\\src\\images\\pistolRounds.jpeg");
		itemImage.add(".\\src\\images\\rubberChicken.jpg");
		itemImage.add(".\\src\\images\\grenade");
		itemImage.add(".\\src\\images\\1up.jpg");
		itemImage.add(".\\src\\images\\jacket.png");
		itemImage.add(".\\src\\images\\bpvest.png");
		itemImage.add(".\\src\\images\\cloak1.png");
				
		return itemImage.get(position);
	}
	
	// Getter to access current inventory data for JTable / UI
    public Object[][] getItemData() {
        return itemData;
    }
	
	public void searchByItemCategory(String category, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear current view

        //for (Object[] row : fullItemData_table) {
        for (Object[] row : itemData) {
            if (row[1].toString().equalsIgnoreCase(category)) {
                model.addRow(row);
            }
        }
        
     // Check if we actually found any items before trying to update the image
        if (model.getRowCount() > 0) {
            //int lastRowIndex = model.getRowCount() - 1;
            int firstRowIndex = 0;
            
            Object pathValue = model.getValueAt(firstRowIndex, 6); 
            
            if (pathValue != null) {
                String lblItemImagePath = pathValue.toString();
                
                ImageIcon icon = new ImageIcon(new ImageIcon(lblItemImagePath)
                    .getImage()
                    .getScaledInstance(200, 200, Image.SCALE_SMOOTH)); // SCALE_SMOOTH looks a bit cleaner
                    
                ItemGUI.lblItemImage.setIcon(icon);
            }
        } else {
            // Optional: Clear the image if no items are found
            ItemGUI.lblItemImage.setIcon(null);
        }
        
    }
	
	/** **** PUSH (Stack) items into the Player's inventory **** */
	/**
     * @param newItem add 1D Object array containing {ID, Category, Name, etc.}
     */
    public void pushItem(Object[] newItem) {
        // Create a new array with one extra slot
        Object[][] newArray = new Object[itemData.length + 1][7];

        // Copy existing items to the new array
        for (int i = 0; i < itemData.length; i++) {
            newArray[i] = itemData[i];
        }

        // Add the new item to the very last index (the top of the stack)
        newArray[newArray.length - 1] = newItem;

        // Update the Item class reference to 'itemData'
        this.itemData = newArray;
        
        System.out.println("Item added. New inventory size: " + itemData.length);
    }
    
    /** **** POP (Stack) last item from the Player's inventory **** */
    public Object[] popItem() {
        if (itemData.length == 0) {
            System.out.println("Inventory is empty!");
            return null;
        }

        // 1. Grab the last item to return it
        Object[] poppedItem = itemData[itemData.length - 1];

        // 2. Create a smaller array
        Object[][] newArray = new Object[itemData.length - 1][7];

        // 3. Copy everything except the last item
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = itemData[i];
        }

        // 4. Update the reference
        this.itemData = newArray;

        System.out.println(poppedItem);
        return poppedItem;
    }

	
    /**  ---- Search User Input from TextField on ItemGUI Class ---- */
    /**  currently inactive **/
    /**
    public void search(String item) {
		System.out.println("You searched: " + item);
		for (int i = 0; i < backpack.size(); i++) {
			if (backpack.get(i).equals(item)) {
				System.out.println("1 to 1, exact match search: " + item + " found!");
			}		
		}
		
		//FIRST 2 LETTERS
		for (String x : backpack) {
			String L2search = item.substring(0, item.length());
			if (x.substring(0, item.length()).equals(L2search)) {
				System.out.println("First 2 letters of search string match items: " + x + "  found!");
			}
		}
		
		//Find index value where the search string starts
		for (String str : backpack) {
			int index = str.indexOf(item);
			if (index >= 0) {
				System.out.println("Item name containing '" + item +  "' found in word: " + str);
				System.out.println("At letter position # " + index);
			}
		}
		
		//Boolean check if search string is found in backpack item
		for (String str : backpack) {
			boolean found = str.contains(item);
			if (found) {
				System.out.println(str + " found!! [Boolean search]");
			}
		}
		
		//Remove case sensitivity in a search
		for (String str : backpack) {
			if (str.toLowerCase().contains(item.toLowerCase())) {
				System.out.println(str + " found! [Lower case search]");
			}
		}		
		
	} 
	*/   
		
	/**  ------------------------------------------- */
	/** ?? Move methods below to a 'Game Engine' class */
	public static void incPlyHealth() {
		//ply.playerHealth +=healthItem;
		//return (ply.playerHealth);		
	}
	
	public static void incWpnAmmo() {
		//select which weapon to increase ammo on
		//i.e. pistolAmmo += ammoItem;
	}
	
	public static void incPlyLives() {
		//ply.lives +=oneUp;
		//return (ply.lives);
	}
	
} //end of Item class

/** For the Future **/
/** ******************
 * A Better Way is to use an ArrayList, 
 * if the plan is adding and removing items frequently, 
 * using a raw Object[][] array is inefficient because 
 * we have to rebuild the array every time. 
 * An ArrayList<Object[]>, can handle the resizing.
 */

//import java.util.ArrayList;
//import java.util.Arrays;

// Convert current Object array to a List
//ArrayList<Object[]> itemList = new ArrayList<>(Arrays.asList(level1Items));

// To "Pop" (Remove last item)
//Object[] removed = itemList.remove(itemList.size() - 1);


