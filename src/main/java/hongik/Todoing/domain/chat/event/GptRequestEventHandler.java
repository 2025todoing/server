package hongik.Todoing.domain.chat.event;

import hongik.Todoing.domain.chat.event.GptRequestEvent;
import hongik.Todoing.domain.chat.service.OpenAiService;
import hongik.Todoing.domain.chat.store.ChatResultStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
@RequiredArgsConstructor
public class GptRequestEventHandler {

    private final OpenAiService openAiService;
    private final ThreadPoolExecutor llmExecutor;
    private final ChatResultStore chatResultStore;

    @EventListener
    public void handleGptRequest(GptRequestEvent event) {
        llmExecutor.submit(() -> {
            System.out.println("\n🔥[EVENT FIRED] user = " + event.userId() +
                    ", messages = " + event.messages().size());
            String result = openAiService.ask(event.userId(), event.messages()).prompt();
            chatResultStore.save(event.userId(), result);
        });
    }
}
