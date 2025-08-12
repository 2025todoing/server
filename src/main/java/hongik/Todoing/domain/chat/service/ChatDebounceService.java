package hongik.Todoing.domain.chat.service;

import hongik.Todoing.domain.chat.dto.request.ChatRequestDTO;
import hongik.Todoing.domain.chat.dto.response.ChatSubmitResponseDTO;
import hongik.Todoing.domain.chat.event.GptRequestEvent;
import hongik.Todoing.domain.chat.store.ChatResultStore;
import hongik.Todoing.domain.chat.util.ChatBufferManager;
import hongik.Todoing.domain.chat.util.ChatDebounceTimerManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class ChatDebounceService {

    private final ChatBufferManager bufferManager;
    private final ChatDebounceTimerManager timerManager;
    private final ApplicationEventPublisher eventPublisher;
    private final ChatResultStore chatResultStore;

    public ChatSubmitResponseDTO receiveUserMessage(String userId, ChatRequestDTO.Message message) {
        bufferManager.add(userId, message);

        // 타이머 초기화 및 새 작업 예약
        timerManager.reset(userId, () -> {
            List<ChatRequestDTO.Message> messages = bufferManager.drain(userId);
            if (!messages.isEmpty()) {
                eventPublisher.publishEvent(new GptRequestEvent(userId, messages));
            }
        });

        return ChatSubmitResponseDTO.of(userId);
    }

    public String getResult(String userId) {
        return chatResultStore.get(userId);
    }

}
