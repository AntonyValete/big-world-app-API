package br.com.yugitilidades.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CardRaceEnum {
    CONTINUOUS("continuous"),
    ZOMBIE("zombie"),
    FIEND("fiend"),
    NORMAL("normal"),
    QUICK_PLAY("quick-play"),
    ROCK("rock"),
    WARRIOR("warrior"),
    WINGED_BEAST("winged beast"),
    SPELLCASTER("spellcaster"),
    BEAST("beast"),
    FAIRY("fairy"),
    EQUIP("equip"),
    FIELD("field"),
    FISH("fish"),
    BEAST_WARRIOR("beast-warrior"),
    THUNDER("thunder"),
    MACHINE("machine"),
    SEA_SERPENT("sea serpent"),
    AQUA("aqua"),
    PLANT("plant"),
    DRAGON("dragon"),
    REPTILE("reptile"),
    COUNTER("counter"),
    PSYCHIC("psychic"),
    INSECT("insect"),
    PYRO("pyro"),
    DINOSAUR("dinosaur"),
    WYRM("wyrm"),
    CYBERSE("cyberse"),
    RITUAL("ritual"),
    DIVINE_BEAST("divine-beast"),
    CREATOR_GOD("creator god"),
    CYVERSE("cyverse"),
    ILLUSION("illusion");

    private final String value;

    CardRaceEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CardRaceEnum fromValue(String value) {
        for (CardRaceEnum race : CardRaceEnum.values()) {
            if (race.value.equalsIgnoreCase(value)) {
                return race;
            }
        }
        throw new IllegalArgumentException(
            "Race value of " + value + " is invalid. Please use a correct race value. " +
            "Race accepts 'continuous', 'zombie', 'fiend', 'normal', 'quick-play', 'rock', 'warrior', 'winged beast', " +
            "'spellcaster', 'beast', 'fairy', 'equip', 'field', 'fish', 'beast-warrior', 'thunder', 'machine', " +
            "'sea serpent', 'aqua', 'plant', 'dragon', 'reptile', 'counter', 'psychic', 'insect', 'pyro', " +
            "'dinosaur', 'wyrm', 'cyberse', 'ritual', 'divine-beast', 'creator god', 'cyverse', 'illusion' and is not case sensitive."
        );
    }
}