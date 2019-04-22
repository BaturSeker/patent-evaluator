package com.patent.evaluator.util;

import com.patent.evaluator.dto.combo.ComboResponse;

import java.util.ArrayList;
import java.util.List;

public final class ComboResponseBuilder {

    private ComboResponseBuilder() {
    }

    public static List<ComboResponse> buildComboResponseList(List<Object[]> resultList) {
        List<ComboResponse> comboResultList = new ArrayList<>();

        for (Object[] resultItem : resultList) {
            ComboResponse comboResponse = new ComboResponse();
            comboResponse.setValue(String.valueOf(resultItem[0]));
            comboResponse.setText(String.valueOf(resultItem[1]));
            comboResultList.add(comboResponse);
        }

        return comboResultList;
    }

    public static List<ComboResponse> buildUniqueItemedComboResponseList(List<Object[]> resultList) {
        List<ComboResponse> comboResultList = new ArrayList<>();

        for (Object[] resultItem : resultList) {
            ComboResponse comboResponse = new ComboResponse();
            comboResponse.setValue(String.valueOf(resultItem[0]));
            comboResponse.setText(String.valueOf(resultItem[1]));
            if (!comboResultList.contains(comboResponse)) {
                comboResultList.add(comboResponse);
            }
        }

        return comboResultList;
    }

    public static List<ComboResponse> buildEnumComboResponseList(Enum[] resultList) {
        List<ComboResponse> comboResultList = new ArrayList<>();

        for (Enum resultItem : resultList) {
            ComboResponse comboResponse = new ComboResponse();
            comboResponse.setValue(String.valueOf(resultItem.ordinal() + 1));
            comboResponse.setText(String.valueOf(resultItem.toString()));
            comboResultList.add(comboResponse);
        }

        return comboResultList;
    }
}
