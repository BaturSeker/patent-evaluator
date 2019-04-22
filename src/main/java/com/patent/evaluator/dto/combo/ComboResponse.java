package com.patent.evaluator.dto.combo;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ComboResponse {

    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
