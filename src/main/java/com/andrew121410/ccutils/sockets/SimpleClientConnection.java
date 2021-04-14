package com.andrew121410.ccutils.sockets;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class SimpleClientConnection {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String message;
}
