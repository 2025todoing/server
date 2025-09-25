package hongik.Todoing.domain.order.adaptor;

import hongik.Todoing.domain.order.domain.pass.ProductCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderUuidGenerator {

    // 주문 Ready 시 UUID를 생성하여 partnerOrderId로 설정합니다.
    /*
    * 주문 번호 생성 로직
    * 포맷은 TTYYMMDDXXXXX 입니다.
    * T: Type (P1: Pass_10, P2: Pass_20, P3: Pass_30)
    * YY: 년도
    * MM: 월
    * DD: 일
    * xxxxx: 5자리 영문자 + 숫자
     */
    public String forUser(Long userId, String itemName) {

        // 유저 아이디와 아이템 이름을 기반으로 주문 번호를 생성합니다.

        // 타입 포맷 생성
        String typePart = ProductCode.getCodeIdByName(itemName);


        // 날짜 포맷 생성
        LocalDateTime now = LocalDateTime.now();
        String datePart = String.format("%02d%02d%02d", now.getYear() % 100, now.getMonthValue(), now.getDayOfMonth());

        // 랜덤 문자열 생성
        String randomPart = generateRandomString(5);
        return typePart + datePart + randomPart; // U는 User를 의미

    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }
}
