package hongik.Todoing.domain.chat.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
public class ChatDebounceTimerManager {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<String, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();

    public void reset(String userId, Runnable task) {
        if (timers.containsKey(userId)) {
            timers.get(userId).cancel(false);
        }

        ScheduledFuture<?> future = scheduler.schedule(task, 1, TimeUnit.SECONDS);
        timers.put(userId, future);
    }
}
