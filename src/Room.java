
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

public class Room {

    class NoRoomException extends Exception {}

    private String name;
    private String desc;
    private boolean beenHere;
    private ArrayList<Exit> exits;
    ArrayList<Item> contents;

    Room(String name) {
        this.name = name;
        this.exits = new ArrayList<Exit>();
        this.contents = new ArrayList<Item>();
    }

    Room(Scanner s, boolean initState) throws NoRoomException,
        Dungeon.IllegalDungeonFormatException {

        this.exits = new ArrayList<Exit>();
        this.contents = new ArrayList<Item>();

        this.name = s.nextLine();
        desc = "";
        if (this.name.equals("===")) {
            throw new NoRoomException();
        }
        
        String lineOfDesc = s.nextLine();
        while (!lineOfDesc.equals("---") && !lineOfDesc.equals("===")) {

            if (lineOfDesc.startsWith("Contents: ")) {
                String itemsList = lineOfDesc.substring("Contents: ".length());
                String[] itemNames = itemsList.split(",");
                for (String itemName : itemNames) {
                    try {
                        if (initState) {
                            this.add(GameState.instance().getDungeon().
                                                        getItem(itemName));
                        }
                    } catch (Item.NoItemException e) {
                        throw new Dungeon.IllegalDungeonFormatException(
                            "No such item '" + itemName + "'");
                    }
                }
            } else {
                this.desc += lineOfDesc + "\n";
            }
            lineOfDesc = s.nextLine();
        }

        // throw away delimiter
        if (!lineOfDesc.equals("---")) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                "---" + "' after room.");
        }
    }

    String getName() { return this.name; }

    void setDesc(String desc) { this.desc = desc; }

    public String describe() {
        return describe(false);
    }

    public String describe(boolean fullDesc) {
        String description;
        if (!fullDesc && GameState.instance().hasBeenVisited(this)) {
            description = this.name;
        } else {
            description = this.name + "\n" + this.desc + "\n";
        }
        for (Item item : this.contents) {
            description += "\nThere is a " + item.getPrimaryName() + " here.";
        }
        if (contents.size() > 0) { description += "\n"; }
        if (!beenHere || fullDesc) {
            for (Exit exit : this.exits) {
                description += "\n" + exit.describe();
            }
        }
        GameState.instance().visit(this);
        return description;
    }
    
    public Room leaveBy(String dir) {
        for (Exit exit : exits) {
            if (exit.getDir().equals(dir)) {
                return exit.getDest();
            }
        }
        return null;
    }

    void addExit(Exit exit) {
        this.exits.add(exit);
    }

    void add(Item item) {
        this.contents.add(item);
    }

    void remove(Item item) {
        this.contents.remove(item);
    }

    Item getItemNamed(String name) throws Item.NoItemException {
        for (Item item : this.contents) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new Item.NoItemException();
    }

    ArrayList<Item> getContents() {
        return this.contents;
    }
}
