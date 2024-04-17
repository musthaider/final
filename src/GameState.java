
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class GameState {

    public static class IllegalSaveFormatException extends Exception {
        public IllegalSaveFormatException(String e) {
            super(e);
        }
    }

    private static GameState theInstance;
    public static final String DEFAULT_SAVE_FILE = "zork";
    public static final String sav = ".sav";
    private Dungeon dungeon;
    private Room adventurersCurrentRoom;
    private ArrayList<Item> inventory;
    private HashSet<Room> visitedRooms;
    private int playerScore;
    private int playerHealth;
    private ArrayList<String> log;
    static String logEntry;

    static synchronized GameState instance() {
        if (theInstance == null) {
            theInstance = new GameState();
        }
        return theInstance;
    }

    private GameState() {
        this.inventory = new ArrayList<Item>();
        this.visitedRooms = new HashSet<Room>();
        this.log = new ArrayList<String>();
    }

    void restore(String filename) throws FileNotFoundException,
        IllegalSaveFormatException, Dungeon.IllegalDungeonFormatException {

        Scanner s = new Scanner(new FileReader(filename));

        if (!s.nextLine().equals("Zork III save data")) {
            throw new IllegalSaveFormatException("Save file not compatible.");
        }

        String dungeonFileLine = s.nextLine();

        if (!dungeonFileLine.startsWith("Dungeon file: ")) {
            throw new IllegalSaveFormatException("No '" +
                "Dungeon file:' after version indicator.");
        }

        this.dungeon = new Dungeon(dungeonFileLine.substring(
            "Current room: ".length()), false);

        s.nextLine();  // Throw away "Room states:".
        String line = s.nextLine();
        while (!line.equals("===")) {
            System.out.println("Room is: " + line.substring(0,line.length()-1));
            Room r = dungeon.getRoom(
                line.substring(0,line.length()-1));
            String beenHereOrNotStr = s.nextLine();
            boolean beenHereOrNot = Boolean.parseBoolean(
                beenHereOrNotStr.substring("beenHere=".length()));
            if (beenHereOrNot) {
                GameState.instance().visit(r);
            }
            line = s.nextLine();  // Maybe throw away "---".
            if (line.startsWith("Contents: ")) {
                String contentsList = line.substring("Contents: ".length());
                String[] contentsItems = contentsList.split(",");
                for (String itemName : contentsItems) {
                    try {
System.out.println("item name is: " + itemName);
System.out.println("dungeon is: " + dungeon);
System.out.println("room is: " + r);
                        r.add(dungeon.getItem(itemName));
System.out.println("dude");
                    } catch (Item.NoItemException e) {
                        throw new IllegalSaveFormatException("No such item '" +
                            itemName + "'");
                    }
                }
                line = s.nextLine();   // Throw away "---"
            }

            line = s.nextLine();
        }
        line = s.nextLine();
        this.adventurersCurrentRoom = dungeon.getRoom(
            line.substring("Current room: ".length()));
        if (s.hasNext()) {
            String inventoryList = s.nextLine().substring(
                "Inventory: ".length());
            String[] inventoryItems = inventoryList.split(",");
            for (String itemName : inventoryItems) {
                try {
                    this.addToInventory(dungeon.getItem(itemName));
                } catch (Item.NoItemException e) {
                    throw new IllegalSaveFormatException("No such item '" +
                        itemName + "'");
                }
            }
        }
    }

    void store(String saveName) throws IOException {
        String filename = this.getFullSaveName(saveName);
        PrintWriter w = new PrintWriter(new FileWriter(filename));
        w.println("Zork III save data");
        w.println("Dungeon file: " + this.getDungeon().getFilename());
        w.println("Room states:");
        Iterator<Room> rooms = this.dungeon.rooms.values().iterator();
        while (rooms.hasNext()) {
            Room room = rooms.next();
            w.println(room.getName() + ":");
            if (this.visitedRooms.contains(room)) {
                w.println("beenHere=true");
            } else {
                w.println("beenHere=false");
            }
            if (room.contents.size() > 0) {
                w.print("Contents: ");
                for (int i=0; i<room.contents.size()-1; i++) {
                    w.print(room.contents.get(i).getPrimaryName() + ",");
                }
                w.println(room.contents.get(
                    room.contents.size()-1).getPrimaryName());
            }
            w.println("---");
        }
        w.println("===");
        w.println("Current room: " +
            this.getAdventurersCurrentRoom().getName());
        if (this.inventory.size() > 0) {
            w.print("Inventory: ");
            for (int i=0; i<inventory.size()-1; i++) {
                w.print(inventory.get(i).getPrimaryName() + ",");
            }
            w.println(inventory.get(inventory.size()-1).getPrimaryName());
        }
        w.println("Health: " + getPlayerHealth());
        w.println("Score: " + getPlayerScore());
 
        w.close();
    }

    String getFullSaveName(String saveName) {
        if (!saveName.endsWith(".sav")) {
            saveName += ".sav";
        }
        if (!saveName.contains("files")) {
            saveName = "../files/" + saveName;
        }
        return saveName;
    }

    void initialize(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.adventurersCurrentRoom = dungeon.getEntry();
        this.playerHealth = 100;
        this.logEntry = logEntry;
        this.playerScore = playerScore;
    }

    ArrayList<String> getInventoryNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (Item item : this.inventory) {
            names.add(item.getPrimaryName());
        }
        return names;
    }

    void addToInventory(Item item) /* throws TooHeavyException */ {
        this.inventory.add(item);
    }

    void removeFromInventory(Item item) {
        this.inventory.remove(item);
    }

    Item getItemInVicinityNamed(String name) throws Item.NoItemException {

        // First, check inventory.
        for (Item item : this.inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }

        // Next, check room contents.
        for (Item item : this.adventurersCurrentRoom.getContents()) {
            if (item.goesBy(name)) {
                return item;
            }
        }

        throw new Item.NoItemException();
    }

    Item getItemFromInventoryNamed(String name) throws Item.NoItemException {

        for (Item item : this.inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new Item.NoItemException();
    }

    public boolean isItemInPlayerInventory(String itemName) throws Item.NoItemException {
		Item item = getDungeon().getItem(itemName);
		if (inventory.contains(item)) {
			return true;
		}
		return false;
	}

    int getAdventurersCurrentWeight() {
        int total = 0;
        for (Item item : this.inventory) {
            total += item.getWeight();
        }
        return total;
    }

    Room getAdventurersCurrentRoom() {
        return this.adventurersCurrentRoom;
    }

    void setAdventurersCurrentRoom(Room room) {
        this.adventurersCurrentRoom = room;
    }

    Dungeon getDungeon() {
        return this.dungeon;
    }

    void setDungeon(Dungeon d) {
        this.dungeon = d;
    }

    boolean hasBeenVisited(Room r) {
        return this.visitedRooms.contains(r);
    }

    void visit(Room r) {
        this.visitedRooms.add(r);
    }

    public void setPlayerScore(int newScore) {
		this.playerScore = newScore;
	}
    
    public int getPlayerScore() {
		return playerScore;
	}
    
    public int getPlayerHealth() {
		return playerHealth;
	}

    public void setPlayerHealth(int playerHealth) {
		this.playerHealth = playerHealth;
	}

    public void logAction(String logString) {
		log.add(logString);
	}

}
