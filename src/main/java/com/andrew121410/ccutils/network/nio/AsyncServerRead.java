package com.andrew121410.ccutils.network.nio;

import org.json.simple.JSONObject;

import java.nio.channels.AsynchronousSocketChannel;

public interface AsyncServerRead {

    public void onRead(AsynchronousSocketChannel channel, Integer result, JSONObject jsonObject);
}