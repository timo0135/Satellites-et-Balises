package eventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler {

	private final Map<Class<? extends AbstractEvent>, List<Object>> listeners = new HashMap<>();

	public void registerListener(Class<? extends AbstractEvent> eventClass, Object listener) {
      List<Object> list = listeners.computeIfAbsent(eventClass, k -> new ArrayList<>());
      list.add(listener);
	}

	public void send(AbstractEvent event) {
		for (Object listener : listeners.getOrDefault(event.getClass(), List.of())) {
			event.sendTo(listener);
		}
	}
}
