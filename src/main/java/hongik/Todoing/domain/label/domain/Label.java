package hongik.Todoing.domain.label.domain;

import hongik.Todoing.domain.prompt.domain.PromptInput;
import hongik.Todoing.domain.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;

    @Enumerated(EnumType.STRING)
    private LabelType labelName;

}
