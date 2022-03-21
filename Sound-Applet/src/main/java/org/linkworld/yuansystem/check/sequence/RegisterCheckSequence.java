package org.linkworld.yuansystem.check.sequence;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/28
 */

import org.linkworld.yuansystem.check.*;

import javax.validation.GroupSequence;

@GroupSequence({NameCheck.class, PasswordCheck.class, PhoneCheck.class, EmailCheck.class, GroupNumCheck.class})
public interface RegisterCheckSequence {
}
