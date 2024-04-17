import java.io.*;

public class WinEvent extends Event {
    private static final String VICTORY_SAVE_FILE = "victory";  
    private static final String SAVE_FILE_EXTENSION = ".sav"; 

    private String winMessage = "You have won! Congratulations! Your game has been saved to ";

    public WinEvent() {
    }

    @Override
    void trigger() {
        GameState instance = GameState.instance();
        String saveFilePath = instance.getFullSaveName(VICTORY_SAVE_FILE + SAVE_FILE_EXTENSION);

        try {
            instance.store(saveFilePath);
            System.out.println(winMessage + saveFilePath);
        } catch (IOException e) {
            System.out.println("Failed to save the game: " + e.getMessage());
        }
    }
}

