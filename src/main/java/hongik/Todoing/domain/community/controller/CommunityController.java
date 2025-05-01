package hongik.Todoing.domain.community.controller;

import hongik.Todoing.domain.community.service.CommunityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CommunityController {

    private final CommunityService communityService;



    

}
