package hongik.Todoing.domain.chat.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatResultStore {

    private final Map<String, String> store = new ConcurrentHashMap<>();

    /**
     * LLM이 만든 JSON 문자열을 그대로 저장
     */
    public void save(String userId, String response) {
        store.put(userId, response);
        System.out.println("🔥[STORE SAVE] user=" + userId + " 저장값=" + response);
    }

    /**
     * 아직 응답이 없으면 null을 반환해야 폴링이 정상 작동함
     */
    public String get(String userId) {
        String value = store.get(userId);
        System.out.println("🔥[STORE GET] user=" + userId + " 반환값=" + value);
        return value; // null이면 null 그대로 프론트에 전달됨
    }

    /**
     * 응답은 1회성이므로 반환 직후 삭제
     */
    public void clear(String userId) {
        store.remove(userId);
    }
}
