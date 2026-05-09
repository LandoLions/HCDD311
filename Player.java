import java.util.*;
import javax.swing.*;

public class Player {

	// variables related to Player
	public static String playerName = "";  //character type
    public int playerHealth;
    public int lives; //default 3
    public int playerGold;
    public int score;
    public static int exp;
    //boolean alive = true;
        
    public static Scanner scnr = new Scanner(System.in);
	
    public Player() {
    	//System.out.println("Please enter your name: ");
        //scnr.nextLine();
        //playerName = scnr.next(); 
        
        playerName = JOptionPane.showInputDialog("Please enter your name: ");
    	
        lives = 2; //Player picks up life #3 in the first scene.
        playerHealth = 65;
        playerGold = 50;
    }
    
    public void playerStatus(){
        System.out.println("\n" + playerName + "'s Status");
        System.out.println("*****************");
        System.out.println("Health: " + playerHealth);
        System.out.println("Exp: " + exp);
        System.out.println("Current Lives: " + lives);
        System.out.println("Zombies Killed: " + score);
        System.out.println("*****************"); 
          
      }
    
    
    public String convPH() {
    	// Unicode escape sequence code for a heart is \u2665
    	String PH = "";
    	int plyHlth = (int)playerHealth;
    	
    	if (plyHlth>80 && plyHlth<100) {
    		PH = "\u2665" + "\u2665" +"\u2665" + "\u2665" + "\u2665";
    	}
    	else if (plyHlth>60 && plyHlth<81){
    		
    		PH = "\u2665" + "\u2665" +"\u2665" + "\u2665";
    	}
    	else {
    		
    		PH = "\u2620"; //unicode for skull and crossbones
    		    	
    	}
    	
    	//char c=(char)a;
    	return PH;
    }
    
    public double updatePH() {
    	return playerHealth;
    }
    
}
