package jiandgyu.jimechu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jiandgyu.jimechu.domain.Menu;
import jiandgyu.jimechu.domain.Topic;
import jiandgyu.jimechu.dto.*;
import jiandgyu.jimechu.service.MenuService;
import jiandgyu.jimechu.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "C | Topic API", description = "주제 API")
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;
    private final MenuService menuService;

    /**
     * Topic 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "new", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Topic 생성", description = "특정 Member의 새로운 Topic을 생성합니다.")
    public Map<String, String> createTopic(@RequestBody TopicCreateDTO topicCreateDTO) {
        Long topicId = topicService.createTopic(topicCreateDTO.getMemberId(), topicCreateDTO.getTitle(), topicCreateDTO.isPublic(), null);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Topic 생성 성공!");
        response.put("topicId", String.valueOf(topicId));
        return response;
    }

    /**
     * Topic + Menu 생성 (JSON 요청 처리)
     */
    @PostMapping(value = "new-with-menus", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Topic + Menu 생성", description = "특정 Member의 새로운 Topic과 Menu들을 생성합니다.")
    public Map<String, String> createTopicAndMenus(@RequestBody TopicAndMenuCreateDTO topicAndMenuCreateDTO) {
        Long topicId = topicService.createTopic(topicAndMenuCreateDTO.getMemberId(), topicAndMenuCreateDTO.getTitle(), topicAndMenuCreateDTO.isPublic(), topicAndMenuCreateDTO.getMenus_name());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Topic + Menu 생성 성공!");
        response.put("topicId", String.valueOf(topicId));
        return response;
    }

    /**
     * 전체 Topic 조회 (JSON 요청 처리)
     */
    @GetMapping(produces = "application/json")
    @Operation(summary = "전체 Topic 조회", description = "전체 Topic을 조회합니다.")
    public List<TopicDTO> getAllTopics() {
        List<Topic> topics = topicService.findTopics();

        return topics.stream()
                .map(TopicDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 Topic의 menus 조회 (JSON 요청 처리)
     */
    @GetMapping(value = "{topicId}/menus", produces = "application/json")
    @Operation(summary = "특정 Topic의 Menus 조회", description = "특정 Topic의 Menu 목록을 반환합니다.")
    public List<MenuDTO> getMenusByTopic(@PathVariable("topicId") Long topicId) {
        List<Menu> menus = menuService.getMenusByTopic(topicId);

        return menus.stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Topic Title 수정 (JSON 요청 처리)
     */
    @PatchMapping(value = "{topicId}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Topic Title 수정", description = "특정 Topic의 Title을 수정합니다.")
    public Map<String, String> updateTopicTitle(@PathVariable("topicId") Long topicId, @RequestBody TopicUpdateDTO topicUpdateDTO) {
        topicService.updateTopicTitle(topicId, topicUpdateDTO.getTitle());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Topic Title 수정 성공!");
        response.put("editedTopicTitle", topicUpdateDTO.getTitle());
        return response;
    }


    /**
     * 특정 Topic 삭제 (JSON 요청 처리)
     */
    @DeleteMapping(value = "{topicId}", produces = "application/json")
    @Operation(summary = "Topic 삭제", description = "특정 Topic과 Menus를 모두 삭제합니다.")
    public Map<String, String> deleteTopic(@PathVariable("topicId") Long topicId) {

        String deletedTopicTitle = topicService.getTopicTitleById(topicId);
        topicService.deleteTopicAndMenus(topicId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Topic 삭제 성공!");
        response.put("deletedTopicTitle", deletedTopicTitle);
        return response;
    }
}
