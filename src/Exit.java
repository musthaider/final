
import java.util.Scanner;

public class Exit {

    class NoExitException extends Exception {}

    private String dir;
    private Room src, dest;

    Exit(String dir, Room src, Room dest) {
        this.dir = dir;
        this.src = src;
        this.dest = dest;
        src.addExit(this);
    }

    Exit(Scanner s, Dungeon d) throws NoExitException,
        Dungeon.IllegalDungeonFormatException {

        String srcTitle = s.nextLine();
        if (srcTitle.equals("===")) {
            throw new NoExitException();
        }
        this.src = d.getRoom(srcTitle);
        this.dir = s.nextLine();
        this.dest = d.getRoom(s.nextLine());
        
        // I'm an Exit object. Add me as an exit to my source Room.
        this.src.addExit(this);

        // throw away delimiter
        if (!s.nextLine().equals("---")) {
            throw new Dungeon.IllegalDungeonFormatException(
                "No '---' after exit.");
        }
    }

    String describe() {
        return "You can go " + this.dir + " to " + this.dest.getName() + ".";
    }

    String getDir() { return this.dir; }
    Room getSrc() { return this.src; }
    Room getDest() { return this.dest; }
}
