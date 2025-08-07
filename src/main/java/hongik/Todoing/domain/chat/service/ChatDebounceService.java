package hongik.Todoing.domain.chat.service;

import hongik.Todoing.domain.chat.dto.ChatRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ChatDebounceService {

    private final OpenAiService openAiService;
    private final ThreadPoolExecutor llmExecutor;

    private final Map<String, List<ChatRequestDTO.Message>> buffer = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> debounceTimers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // 프론트는 여기에 저장된 결과를 Polling으로 GET 요청해서 받아감 (임시 저장소)
    private final Map<String, String> resultStore = new ConcurrentHashMap<>();

    public void receiveUserMessage(String userId, ChatRequestDTO.Message message) {
        buffer.computeIfAbsent(userId, k -> new ArrayList<>()).add(message);

        // 타이머 리셋 (이미 타이머가 있으면 취소해서 초기화)
        ScheduledFuture<?> old = debounceTimers.get(userId);
        if (old != null) old.cancel(false);

        // 새로운 타이머 시작 (사용자가 1초 안에 또 다른 메세지를 보내면 다시 타이머 시작하는거고, 1초 후에 아무 메세지가 없으면 LLM 호출)
        ScheduledFuture<?> newTimer = scheduler.schedule(() -> {
            List<ChatRequestDTO.Message> messages = buffer.remove(userId);
            if (messages == null || messages.isEmpty()) return;

            llmExecutor.submit(() -> {
                String result = openAiService.ask(userId, messages).getPrompt();
                resultStore.put(userId, result);
            });
        }, 1, TimeUnit.SECONDS);

        debounceTimers.put(userId, newTimer);
    }

    public String getResult(String userId) {
        return resultStore.getOrDefault(userId, "아직 응답 준비 중...");
    }
}
