
public class UnknownEvent extends Event {

	private String unknownEventDesc;
	
	public UnknownEvent(String unknownEventDesc) {
		this.unknownEventDesc = unknownEventDesc;
	}

	@Override
	void trigger() {
		GameState.logEntry = "Unsuccessful Event trigger: " + this.unknownEventDesc;
		GameState.instance().logAction(GameState.logEntry);
	}

}
