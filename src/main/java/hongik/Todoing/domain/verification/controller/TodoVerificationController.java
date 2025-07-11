package hongik.Todoing.domain.verification.controller;

import com.google.cloud.vision.v1.EntityAnnotation;
import hongik.Todoing.domain.verification.dto.TextAnnotationDto;
import hongik.Todoing.domain.verification.service.VisionService;
import hongik.Todoing.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class TodoVerificationController {

    private final VisionService visionService;

    @Operation(summary = "사진 인증 요청 컨트롤러")
    @PostMapping("/{todoId}/vision")
    public ApiResponse<?> verifyTextInImage(@PathVariable Long todoId,
                                            @RequestParam("image")MultipartFile image) throws IOException {
        List<EntityAnnotation> texts = visionService.detectText(image);

        if(!texts.isEmpty()) {
            String detetedText = texts.get(0).getDescription();
            System.out.println("결과로 나온 글은 " + detetedText);
        }
        return ApiResponse.onSuccess(texts);
    }

    @Operation(summary = "사진 인증 - 글 감지 테스트 컨트롤러")
    @PostMapping("/test")
    public ApiResponse<?> verifyTextTest(@RequestPart("image")MultipartFile image) throws IOException {
        List<EntityAnnotation> texts = visionService.detectText(image);

        List<TextAnnotationDto> result = texts.stream()
                .map(t -> new TextAnnotationDto(t.getDescription(), t.getLocale()))
                .toList();

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "사진 인증 - 라벨 감지 테스트 컨트롤러")
    @PostMapping("/test/labels")
    public ApiResponse<?> detectLabelsTest(@RequestPart("image")MultipartFile image) throws IOException {
        List<EntityAnnotation> labels = visionService.detectLabels(image);

        List<String> labelDescriptions = labels.stream()
                .map(EntityAnnotation::getDescription)
                .toList();

        System.out.println(labelDescriptions);

        return ApiResponse.onSuccess(labelDescriptions);
    }

}
