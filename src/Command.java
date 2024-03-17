
// For now, only direction commands and "save". If the "direction" is bogus,
// then this effectively doubles as an UnknownCommand (to be a subclass
// later).
public class Command {

    private String dir;     // for now, this class is only for direction 
                            // commands, plus "save" which is kind of a weird
                            // special case.

    Command(String dir) {
        this.dir = dir;
    }

    public String execute() {
        if (dir.equals("save")) {
            try {
                System.out.print("Enter save name: ");
                java.util.Scanner s = new java.util.Scanner(System.in);
                String saveName = s.nextLine();
                GameState.instance().store(saveName);
                return "Data saved to " +
                    GameState.instance().getFullSaveName(saveName) + ".\n";
            } catch (Exception e) {
                System.err.println("Couldn't save!");
                e.printStackTrace();
                return "";
            }
        } else if (this.dir.equals("n") || this.dir.equals("w") ||
            this.dir.equals("e") || this.dir.equals("s") ||
            this.dir.equals("u") || this.dir.equals("d")) {
            Room currentRoom = 
                GameState.instance().getAdventurersCurrentRoom();
            Room nextRoom = currentRoom.leaveBy(this.dir);
            if (nextRoom != null) {
                GameState.instance().setAdventurersCurrentRoom(nextRoom);
                return "\n" + nextRoom.describe() + "\n";
            } else {
                return "Sorry, you can't go " + dir + " from " +
                    currentRoom.getName() + ".\n";
            }
        }
        return "I'm sorry, I don't understand the command '" + dir + "'.\n";
    }
}
