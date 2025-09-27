package hongik.Todoing.domain.order.dto.pass.response;


public record GetPassInfoResponse(Long passId, Integer usedCount, Integer limitCount, String itemName) {
}
