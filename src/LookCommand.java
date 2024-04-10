
class LookCommand extends Command {

    LookCommand() {
    }

    public String execute() {
        Room currRoom = GameState.instance().getAdventurersCurrentRoom();
        return "\n" + currRoom.describe(true) + "\n";
    }
}
