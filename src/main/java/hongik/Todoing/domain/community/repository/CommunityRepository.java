package hongik.Todoing.domain.community.repository;


import hongik.Todoing.domain.community.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

}
