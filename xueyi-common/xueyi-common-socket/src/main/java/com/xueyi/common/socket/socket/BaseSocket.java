package com.xueyi.common.socket.socket;

import cn.hutool.crypto.digest.DigestUtil;
import com.xueyi.common.socket.annotation.*;
import com.xueyi.common.socket.domain.MyChannelHandlerMap;
import com.xueyi.common.socket.pojo.Session;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

/**
 * socket通用数据处理
 *
 * @author xueyi
 */
public class BaseSocket {

    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam("req") String req, @RequestParam("user") String user, @RequestParam("t") String t, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        session.setSubprotocols("stomp");
        if (!"ok".equals(req)) {
            System.out.println("Authentication failed!");
            session.close();
        }
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers,  @RequestParam("req") String req,@RequestParam("user") String user, @RequestParam("t") String t, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap) {
        System.out.println("new connection");
        System.out.println("open:"+ DigestUtil.md5Hex(session.channel().toString()));
        System.out.println(MyChannelHandlerMap.linkSize());
        MyChannelHandlerMap.put(user,session);
        System.out.println(MyChannelHandlerMap.linkSize());
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("one connection closed");
        System.out.println("OnClose:"+ DigestUtil.md5Hex(session.channel().toString()));
        System.out.println(MyChannelHandlerMap.removeBySession(session));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        MyChannelHandlerMap.removeBySession(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        session.sendText("one message");
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }
}