import java.util.regex.Pattern;

public class EventFactory {

	private static EventFactory theInstance;

	public static synchronized EventFactory instance() {
		if (theInstance == null) {
			theInstance = new EventFactory();
		}
		return theInstance;
	}

	private EventFactory() {
	}

	public Event parse(String target, String eventDescription) {
		String[] parts = eventDescription.split(Pattern.quote("("));
		String event = parts[0];
		String param = parts.length >= 2 ? parts[1].substring(0, parts[1].length()-1) : "";
		switch (event) {
		case "Disappear":
			return new DisappearEvent(target);
		case "Score":
			return new ScoreEvent(Integer.parseInt(param));
		case "Teleport":
			return new TeleportEvent();
		case "Transform":
			return new TransformEvent(target, param);
		case "Wound":
			return new WoundEvent(Integer.parseInt(param));
		default:
			return new UnknownEvent(eventDescription);
		}
	}
	
	public Event parse(String eventDescription) {
		return parse(null, eventDescription);
	}

}
