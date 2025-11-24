package hongik.Todoing.domain.chat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hongik.Todoing.domain.chat.dto.request.ChatRequestDTO;
import hongik.Todoing.domain.chat.dto.response.ChatResponseDTO;
import hongik.Todoing.domain.chat.dto.response.ChatResultDTO;
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
import java.util.UUID;
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

    public ChatResultDTO getResult(String userId) {
        String raw = chatResultStore.get(userId);

        if (raw == null) {
            return null;
        }

        // 일회성 데이터이므로 즉시 제거
        chatResultStore.clear(userId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(raw); // JSON 문자열 → JSON 객체

            String type = root.get("type").asText(); // type 읽기

            // ✔ 일반 대화 응답
            if (type.equals("response")) {
                String content = root.get("content").asText();

                return new ChatResultDTO(
                        UUID.randomUUID().toString(),
                        "response",       // 전체 JSON
                        content    // 프론트에서 말풍선에 넣을 내용
                );
            }

            // ✔ 계획(plan) 타입 → raw JSON 전체를 content로 보내서 프론트에서 다시 parse
            if (type.equals("plan")) {
                return new ChatResultDTO(
                        UUID.randomUUID().toString(),
                        "plan",
                        raw
                );
            }

        } catch (Exception e) {
            System.out.println("JSON 파싱 오류: " + e.getMessage());
        }

        // ✔ JSON 파싱 실패 시 fallback
        return new ChatResultDTO(
                UUID.randomUUID().toString(),
                raw,
                raw
        );
    }




}
