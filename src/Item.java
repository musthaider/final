
import java.util.Scanner;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;

public class Item {

    static class NoItemException extends Exception {}

    private String primaryName;
    private int weight;
    private Hashtable<String,String> messages;
    private Set<String> aliases;


    Item(Scanner s) throws NoItemException,
        Dungeon.IllegalDungeonFormatException {

        this.messages = new Hashtable<String,String>();
        this.aliases = new HashSet<String>();

        // Read item name.
        String names[] = s.nextLine().split(",");
        if (names[0].equals("===")) {
            throw new NoItemException();
        }
        primaryName = names[0];
        for (int i=1; i<names.length; i++) {
            this.aliases.add(names[i]);
        }

        // Read item weight.
        weight = Integer.valueOf(s.nextLine());

        // Read and parse verbs lines, as long as there are more.
        String verbLine = s.nextLine();
        while (!verbLine.equals("---")) {
            if (verbLine.equals("===")) {
                throw new Dungeon.IllegalDungeonFormatException("No '" +
                    "---" + "' after item.");
            }
            String[] verbParts = verbLine.split(":");
            this.messages.put(verbParts[0],verbParts[1]);
            
            verbLine = s.nextLine();
        }
    }

    int getWeight() {
        return weight;
    }

    boolean goesBy(String name) {
        if (this.primaryName.equals(name)) {
            return true;
        }
        for (String alias : this.aliases) {
            if (alias.equals(name)) {
                return true;
            }
        }
        return false;
    }

    String getPrimaryName() { return primaryName; }

    public String getMessageForVerb(String verb) {
        return this.messages.get(verb);
    }

    public String toString() {
        return primaryName;
    }
}
