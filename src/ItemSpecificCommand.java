import java.util.regex.Pattern;

class ItemSpecificCommand extends Command {

    private String verb;
    private String noun;
                        

    ItemSpecificCommand(String verb, String noun) {
        this.verb = verb;
        this.noun = noun;
    }

    public String execute() {
        
        Item forItem = null;
        try {
            forItem = GameState.instance().getItemInVicinityNamed(noun);
        } catch (Item.NoItemException e) {
            return "There's no " + noun + " here.";
        }
        
	String msg = forItem.getMessageForVerb(verb);
	if (forItem.hasEvent(verb)) {
		try {
			EventActivator EA = new EventActivator("*" + forItem.getPrimaryName() + "*" + forItem.getEvDetails(verb));

        EA.activate();
     } catch (Item.NoItemException e) {
	e.printStackTrace();
	}
	}
        return (msg == null ? 
            "Sorry, you can't " + verb + " the " + noun + "." : msg) + "\n";
    }
}
