
public class InventoryItem {
/**** InventoryItem is a helper class to manage inventory ****/
	
		public String id;
	    public String category;
	    public String name;
	    public String description;
	    public int quantity;
	    public int value;
	    public Object imagePath; // Assuming getItemImage returns an Object or String
	    
	    /*** below would be the constructor for the helper class.
	     * because of time purposes we'll initialize it here
	    public Item(String id, String category, String name, String description, int quantity, int value, Object imagePath) {
	    ***/
	    public InventoryItem(String id, String category, String name, String description, int quantity, int value, Object imagePath) {
	        this.id = id;
	        this.category = category;
	        this.name = name;
	        this.description = description;
	        this.quantity = quantity;
	        this.value = value;
	        this.imagePath = imagePath;
	    }
	    
	    // New constructor that accepts a GameUI Object[] directly
	    public InventoryItem(Object[] data) {
	        this.id = (String)data[0];
	        this.category = (String)data[1];
	        this.name = (String)data[2];
	        this.description = (String)data[3];
	        this.quantity = (int)data[4];
	        this.value = (int)data[5];
	        this.imagePath = (String)data[6];
	    }
	    
}// end of helper class Item
