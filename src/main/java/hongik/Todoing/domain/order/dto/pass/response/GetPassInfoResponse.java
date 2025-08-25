package hongik.Todoing.domain.order.dto.pass.response;

import lombok.Getter;

@Getter
public record GetPassInfoResponse(Long passId, Integer usedCount, Integer limitCount, String itemName) {
}
