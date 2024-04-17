
public class TransformEvent extends Event {

	private String paramDetails;
	private String targetItem;
	private String newItem;
	private String logEntry;

	public TransformEvent(String targetItem, String newItem) {
		this.targetItem = targetItem;
		this.newItem = newItem;
		this.logEntry = logEntry;
		this.paramDetails = paramDetails;
	}

	@Override
	void trigger() throws Item.NoItemException {

		Item incomingItem = GameState.instance().getDungeon().getItem(newItem);
		Item outgoingItem = GameState.instance().getItemInVicinityNamed(targetItem);
		
		String targetItemOrigin = "";

		if (GameState.instance().isItemInPlayerInventory(targetItem)) {
			GameState.instance().addToInventory(incomingItem);
			GameState.instance().removeFromInventory(outgoingItem);
			targetItemOrigin = "player-inventory";
			
		} else {
			GameState.instance().getAdventurersCurrentRoom().add(incomingItem);
			GameState.instance().getAdventurersCurrentRoom().remove(outgoingItem);
			targetItemOrigin = GameState.instance().getAdventurersCurrentRoom().getName();

		}
		
		GameState.instance().getDungeon().removeItemFromGame(outgoingItem);
		String lead0 = "Event: ";
		String lead1 = "Details: ";
		logEntry = String.format("  %14sTransformed(%s,%s) %n  %14s%sremoved from %s%n", 
				lead0,targetItem,newItem,targetItem,lead1,targetItemOrigin);
		GameState.instance().logAction(logEntry);
		
	}
}
