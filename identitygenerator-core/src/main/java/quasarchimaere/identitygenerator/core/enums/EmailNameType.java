package quasarchimaere.identitygenerator.core.enums;

import java.io.Serializable;

public enum EmailNameType implements Serializable {
    FIRSTLETTER(0),
    FULLNAME(1),
    NONAME(2);

    private int value;

    EmailNameType(int value){
        this.value = value;
    }
}
