package org.linkworld.yuansystem.check.sequence;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/28
 */


import org.linkworld.yuansystem.check.PasswordCheck;
import org.linkworld.yuansystem.check.PhoneCheck;

import javax.validation.GroupSequence;

@GroupSequence({PhoneCheck.class, PasswordCheck.class})
public interface LoginCheckSequence {
}
