package com.springframework.order.utils;

public enum OrderStatusEnum {

    CREATED("CREATED"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatusEnum fromValue(String text) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (String.valueOf(status.value).equals(text)) {
                return status;
            }
        }
        return null;
    }

}
