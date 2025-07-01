package br.com.yugitilidades.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum CardTypeEnum {
    SKILL_CARD("Skill Card"),
    SPELL_CARD("Spell Card"),
    TRAP_CARD("Trap Card"),
    NORMAL_MONSTER("Normal Monster"),
    NORMAL_TUNER_MONSTER("Normal Tuner Monster"),
    EFFECT_MONSTER("Effect Monster"),
    TUNER_MONSTER("Tuner Monster"),
    FLIP_MONSTER("Flip Monster"),
    FLIP_EFFECT_MONSTER("Flip Effect Monster"),
    SPIRIT_MONSTER("Spirit Monster"),
    UNION_EFFECT_MONSTER("Union Effect Monster"),
    GEMINI_MONSTER("Gemini Monster"),
    PENDULUM_EFFECT_MONSTER("Pendulum Effect Monster"),
    PENDULUM_NORMAL_MONSTER("Pendulum Normal Monster"),
    PENDULUM_EFFECT_RITUAL_MONSTER("Pendulum Effect Ritual Monster"),
    PENDULUM_TUNER_EFFECT_MONSTER("Pendulum Tuner Effect Monster"),
    RITUAL_MONSTER("Ritual Monster"),
    RITUAL_EFFECT_MONSTER("Ritual Effect Monster"),
    TOON_MONSTER("Toon Monster"),
    FUSION_MONSTER("Fusion Monster"),
    SYNCHRO_MONSTER("Synchro Monster"),
    SYNCHRO_TUNER_MONSTER("Synchro Tuner Monster"),
    SYNCHRO_PENDULUM_EFFECT_MONSTER("Synchro Pendulum Effect Monster"),
    XYZ_MONSTER("XYZ Monster"),
    XYZ_PENDULUM_EFFECT_MONSTER("XYZ Pendulum Effect Monster"),
    LINK_MONSTER("Link Monster"),
    PENDULUM_FLIP_EFFECT_MONSTER("Pendulum Flip Effect Monster"),
    PENDULUM_EFFECT_FUSION_MONSTER("Pendulum Effect Fusion Monster"),
    TOKEN("Token");

    private final String value;

    CardTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CardTypeEnum fromValue(String value) {
        for (CardTypeEnum type : CardTypeEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
            "Type value of " + value + " is invalid. Please use a correct type value. " +
            "Type accepts: 'Skill Card', 'Spell Card', 'Trap Card', 'Normal Monster', 'Normal Tuner Monster', " +
            "'Effect Monster', 'Tuner Monster', 'Flip Monster', 'Flip Effect Monster', 'Spirit Monster', " +
            "'Union Effect Monster', 'Gemini Monster', 'Pendulum Effect Monster', 'Pendulum Normal Monster', " +
            "'Pendulum Effect Ritual Monster', 'Pendulum Tuner Effect Monster', 'Ritual Monster', " +
            "'Ritual Effect Monster', 'Toon Monster', 'Fusion Monster', 'Synchro Monster', 'Synchro Tuner Monster', " +
            "'Synchro Pendulum Effect Monster', 'XYZ Monster', 'XYZ Pendulum Effect Monster', 'Link Monster', " +
            "'Pendulum Flip Effect Monster', 'Pendulum Effect Fusion Monster', 'Token'. " +
            "Type is not case sensitive."
        );
    }
}
