package hongik.Todoing.domain.label.repository;

import hongik.Todoing.domain.label.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
    Label findByLabelId(Long labelId);

}
