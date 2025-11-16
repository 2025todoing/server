package hongik.Todoing.domain.chat.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatResultStore {

    private final Map<String, String> store = new ConcurrentHashMap<>();

    public void save(String userId, String response) {
        store.put(userId, response);
        System.out.println("🔥[STORE SAVE] user=" + userId + " 저장값=" + response);

    }

    public String get(String userId) {
        String value = store.get(userId);
        System.out.println("🔥[STORE GET] user=" + userId + " 반환값=" + value);
        return value != null ? value : "아직 응답 준비 중...";
    }

    public void clear(String userId) {
        store.remove(userId);
    }
}
