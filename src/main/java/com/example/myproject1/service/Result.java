package com.example.myproject1.service;

public class Result {
    private Result () {
        throw new IllegalStateException("Utility Class");
    }
    public static final String GREATER_THAN = "Số bạn đoán lớn hơn kết quả.";
    public static final String LESS_THAN = "Số bạn đoán bé hơn kết quả.";
    public static final String BINGO = "Đoán chính xác!";
}
