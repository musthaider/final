
import java.util.Hashtable;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class Dungeon {

    public static class IllegalDungeonFormatException extends Exception {
        public IllegalDungeonFormatException(String e) {
            super(e);
        }
    }

    private String title;
    private Room entry;
    Hashtable<String,Room> rooms;
    private Hashtable<String,Item> items;
    private String filename;

    Dungeon(String title, Room entry) {
        this.filename = null;    // null indicates not hydrated from file.
        this.title = title;
        this.entry = entry;
        this.rooms = new Hashtable<String,Room>();
        this.items = new Hashtable<String,Item>();
    }

    /**
     * Read from the .zork filename passed, and instantiate a Dungeon object
     * based on it.
     */
    public Dungeon(String filename) throws FileNotFoundException,
        IllegalDungeonFormatException {

        this(filename, false);
    }

    public Dungeon(String filename, boolean initState)
        throws FileNotFoundException, IllegalDungeonFormatException {

        this.rooms = new Hashtable<String,Room>();
        this.items = new Hashtable<String,Item>();
        this.filename = filename;

        GameState.instance().setDungeon(this);

        Scanner s = new Scanner(new FileReader(filename));
        title = s.nextLine();

        s.nextLine();   // Throw away version indicator.

        // Throw away delimiter.
        if (!s.nextLine().equals("===")) {
            throw new IllegalDungeonFormatException(
                "No '===' after version indicator.");
        }

        // Throw away Items starter.
        if (!s.nextLine().equals("Items:")) {
            throw new IllegalDungeonFormatException("No '" +
                "Items:' line where expected.");
        }

        try {
            // Instantiate items.
            while (true) {
                add(new Item(s));
            }
        } catch (Item.NoItemException e) {  /* end of items */ }

        // Throw away Rooms starter.
        if (!s.nextLine().equals("Rooms:")) {
            throw new IllegalDungeonFormatException(
                "No 'Rooms:' line where expected.");
        }


        try {
            // Instantiate and add first room (the entry).
            entry = new Room(s, initState);
            add(entry);

            // Instantiate and add other rooms.
            while (true) {
                add(new Room(s, initState));
            }
        } catch (Room.NoRoomException e) {  /* end of rooms */ }

        // Throw away Exits starter.
        if (!s.nextLine().equals("Exits:")) {
            throw new IllegalDungeonFormatException(
                "No 'Exits:' line where expected.");
        }

        try {
            // Instantiate exits.
            while (true) {
                // (Note that the Exit constructor takes care of adding itself
                // to its source room.)
                Exit exit = new Exit(s, this);
            }
        } catch (Exit.NoExitException e) {  /* end of exits */ }

        s.close();
    }
    

    public Room getEntry() { return this.entry; }

    public String getTitle() { return this.title; }

    public String getFilename() { return this.filename; }

    public void add(Room room) { this.rooms.put(room.getName(), room); }

    public void add(Item item) { this.items.put(item.getPrimaryName(),item); }

    public Room getRoom(String roomName) {
        return this.rooms.get(roomName); 
    }

    /**
     * Get the Item object whose primary name is passed. This has nothing to
     * do with where the Adventurer might be, or what's in his/her inventory,
     * etc.
     */
    public Item getItem(String primaryItemName) throws Item.NoItemException {
        
System.out.println("Getting " + primaryItemName + "...");
        if (items.get(primaryItemName) == null) {
System.out.println("Nope!");
            throw new Item.NoItemException();
        }
System.out.println("Yep");
        return items.get(primaryItemName);
    }
}
