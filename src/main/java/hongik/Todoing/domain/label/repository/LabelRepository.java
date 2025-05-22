package hongik.Todoing.domain.label.repository;

import hongik.Todoing.domain.label.domain.Label;
import hongik.Todoing.domain.label.domain.LabelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {
    Label findByLabelId(Long labelId);

    Optional<Label> findByLabelName(LabelType labelName);
    boolean existsByLabelName(LabelType labelType);
}