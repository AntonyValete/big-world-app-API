package br.com.yugitilidades.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CardAttributeEnum {
    DARK("dark"),
    EARTH("earth"),
    FIRE("fire"),
    LIGHT("light"),
    WATER("water"),
    WIND("wind"),
    DIVINE("divine");

    private final String value;

    CardAttributeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CardAttributeEnum fromValue(String value) {
        for (CardAttributeEnum attribute : CardAttributeEnum.values()) {
            if (attribute.value.equalsIgnoreCase(value)) {
                return attribute;
            }
        }
        throw new IllegalArgumentException(
            "Attribute value of " + value + " is invalid. Please use a correct attribute value. " +
            "Attribute accepts 'dark', 'earth', 'fire', 'light', 'water', 'wind' or 'divine' and is not case sensitive."
        );
    }
}
