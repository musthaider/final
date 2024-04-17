import java.io.*;

public class DieEvent extends Event {
    private String message = "Perished";
  
    public DieEvent() {
    }

    @Override
    void trigger() {
        GameState instance = GameState.instance();
        try {
            instance.restore(instance.DEFAULT_SAVE_FILE + instance.sav);
        } catch (IOException | GameState.IllegalSaveFormatException | Dungeon.IllegalDungeonFormatException e) {}
        System.out.println("Restarting the game from the start");
        System.out.println(instance.getAdventurersCurrentRoom().getName());
    }

}

