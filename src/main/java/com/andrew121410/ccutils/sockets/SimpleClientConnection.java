package com.andrew121410.ccutils.sockets;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class SimpleClientConnection {
    private SimpleSocketHandler simpleSocketHandler;
    private String message;
}
