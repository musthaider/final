
class SaveCommand extends Command {

    static String DEFAULT_SAVE_FILENAME = "zork";

    private String saveFilename;

    SaveCommand(String saveFilename) {
        if (saveFilename == null || saveFilename.length() == 0) {
            this.saveFilename = DEFAULT_SAVE_FILENAME;
        } else {
            this.saveFilename = saveFilename;
        }
    }

    public String execute() {
        try {
            GameState.instance().store(saveFilename);
            return "Data saved to " + saveFilename + ".sav.\n";
        } catch (Exception e) {
            System.err.println("Couldn't save!");
            e.printStackTrace();
            return "";
        }
    }
}
