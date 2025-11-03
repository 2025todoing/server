package hongik.Todoing.domain.verification.service;

import com.google.cloud.vision.v1.EntityAnnotation;
import hongik.Todoing.domain.label.domain.LabelType;
import hongik.Todoing.domain.label.repository.LabelRepository;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.order.adaptor.PassAdaptor;
import hongik.Todoing.domain.order.domain.pass.Pass;
import hongik.Todoing.domain.order.validator.PassValidator;
import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.domain.todo.repository.TodoRepository;
import hongik.Todoing.domain.verification.Adaptor.VerificationAdaptor;
import hongik.Todoing.domain.verification.domain.Verification;
import hongik.Todoing.domain.verification.domain.VerificationType;
import hongik.Todoing.domain.verification.dto.VerificationResponse;
import hongik.Todoing.domain.verification.validator.VerificationValidator;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerificationService {

    private final VerificationAdaptor verificationAdaptor;
    private final VerificationValidator verificationValidator;
    private final VisionService visionService;
    private final PassAdaptor passAdaptor;
    private final TodoRepository todoRepository;
    private final LabelRepository labelRepository;
    private final PassValidator passValidator;

    // 인증 요청 처리
    @Transactional
    public VerificationResponse verifyTodoImage(Member member, Long todoId, MultipartFile image) {
        //Todo 조회
        Optional<Todo> todoOptional = todoRepository.findByTodoId(todoId);

        Todo todo = todoOptional.orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        // 인증 가능한 투두인가
        verificationValidator.validVerification(member, todo);

        // vision api 호출(텍스트 + 라벨)
        List<EntityAnnotation> textAnnotations;
        List<EntityAnnotation> labelAnnotations;

        try {
            textAnnotations = visionService.detectText(image);
            labelAnnotations = visionService.detectLabels(image);
        } catch (Exception e) {
            throw new RuntimeException("Vision API 호출 실패", e);
        }

        // 결과 분석 -> 인증 성공/실패 여부 확인
        double confidence = calculateConfidence(todo, textAnnotations, labelAnnotations);
        boolean success = confidence >= 0.5; // 신뢰도 0.5 이상이면 성공으로 간주

        // 인증 기록 저장
        Verification verification = Verification.builder()
                .type(VerificationType.PHOTO)
                .build();
        verificationAdaptor.save(verification);


        // Pass 차감
        if(success) {
            // 사용권이 남아있는 PASS && 가장 만들어진지 오래된 PASS
            Pass pass = passAdaptor.findByUserId(member.getId())
                    .stream()
                    .filter(p -> p.remainingCount() > 0)
                    .min((p1, p2) -> p1.getCreatedAt().compareTo(p2.getCreatedAt()))
                    .orElseThrow(() -> new GeneralException(ErrorStatus.PASS_NOT_AVAILABLE));

            pass.consume(member.getId(),passValidator);

        }

        // Todo 완료 처리

        if(success){
            todo.updateComplete(true);
        }

        log.info("인증 결과 | member={}, todo={}, success={}, confidence={}",
                member.getId(), todo.getTodoId(), success, confidence);

        VerificationResponse response = VerificationResponse.from(verification, todo, success, confidence);

        return response;

    }

    /**
     * 결과 분석 로직
     * 카테고리 내용 기반으로 텍스트/라벨 매칭 확률 계산
     */

    private double calculateConfidence(Todo todo, List<EntityAnnotation> texts, List<EntityAnnotation> labels) {
        double score = 0.0;

        LabelType label = labelRepository.findById(todo.getLabelId()).orElseThrow().getLabelName();

        String category = label.toString();
        String content = todo.getContent().toLowerCase();

        // 텍스트 매칭 점수 계산
        for (EntityAnnotation text : texts) {
            String detected = text.getDescription().toLowerCase();

            // 내용 일부 포함 시 +0.3
            if(detected.contains(content))
                score += 0.3;

            // ㅋ테고리 키워드 포함 시 +0.4
            if(detected.contains(category))
                score += 0.4;

        }

        //라벨 분석
        for(EntityAnnotation labelText : labels) {

            String labelDescription = labelText.getDescription().toLowerCase();

            if(labelDescription.contains(category))
                score  += 0.4;

            if(content.contains(labelDescription))
                score += 0.3;

        }

        // 최대 1.0으로 제한
        return Math.min(score, 1.0);
    }


    // vision api 결과 해석 로직 부분
    private boolean analyzeResult(List<EntityAnnotation> textAnnotations,
                                  List<EntityAnnotation> labelAnnotations,
                                  Todo todo) {
        // Todo 카테고리와 연관도가 존재하면 인증 성공
        String todoCategory = todo.getContent();

        return true;


    }
}
