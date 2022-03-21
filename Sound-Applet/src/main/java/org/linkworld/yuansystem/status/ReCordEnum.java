package org.linkworld.yuansystem.status;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/28
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ReCordEnum {
    RE_CORD_A(1,"元音a"),
    RE_CORD_B(2,"唱段"),
    RE_CORD_C(3,"元音u"),
    RE_CORD_D(4,"元音i")
    ;

    private int type;
    private String description;


    public static ReCordEnum getEnumByType(Integer type) {
        for (ReCordEnum value : ReCordEnum.values()) {
            if(value.getType()==type) {
                return value;
            }
        }
        return null;
    }
}
