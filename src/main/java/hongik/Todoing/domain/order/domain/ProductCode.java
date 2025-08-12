package hongik.Todoing.domain.order.domain;

import hongik.Todoing.domain.order.exception.passException.PassTypeMisMatchException;
import lombok.Getter;

@Getter
public enum ProductCode {
    PASS_10("PASS10", "10회 이용권", 10, 8900),
    PASS_20("PASS20", "20회 이용권", 20, 15900),
    PASS_30("PASS30", "30회 이용권", 30, 21900);

    private final String code;
    private final String name;
    private final int limitCount;
    private final int price;

    ProductCode(String code, String name, int limitCount, int price) {
        this.code = code;
        this.name = name;
        this.limitCount = limitCount;
        this.price = price;
    }

    public static ProductCode fromCode(String code) {
        for(ProductCode p : values()) {
            if(p.code.equalsIgnoreCase(code)) return p;
        }

        throw PassTypeMisMatchException.EXCEPTION;
    }

}
