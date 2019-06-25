package com.fivefivelike.mybaselibrary.http;

//public abstract class TickerWebsocket extends WebSocketClient {
//
//
//    public TickerWebsocket(String serverUri) {
//        super(URI.create(serverUri));
//    }
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        Log.i("TickerWebsocket", "open websocket...");
//        onSubscribe();
//    }
//
//    protected abstract void onSubscribe();
//
//    protected abstract void onSchedule(ScheduledExecutorService scheduler);
//
//    protected abstract void onReconnect();
//
//    @Override
//    public void sendPing() {
//        try {
//            super.sendPing();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void send(byte[] data) {
//        try {
//            super.send(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void send(ByteBuffer bytes) {
//        try {
//            super.send(bytes);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void sendFrame(Collection<Framedata> frames) {
//        try {
//            super.sendFrame(frames);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void sendFrame(Framedata framedata) {
//        try {
//            super.sendFrame(framedata);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void send(String text) {
//        try {
//            super.send(text);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        Log.e("TickerWebsocket", "websocket closed , now reconnect... >>" + reason);
//        onReconnect();
//    }
//
//    @Override
//    public void onError(Exception ex) {
//        Log.e("TickerWebsocket", "websocket error: " + ex.getMessage());
//    }
//
//    public void start() {
//        try {
//            boolean connected = connectBlocking();
//            Log.i("TickerWebsocket", this.getClass().getSimpleName() + " 连接".concat(connected ? "成功" : "失败"));
//        } catch (Throwable e) {
//            e.printStackTrace();
//            Log.i("TickerWebsocket", "启动失败" + e.getMessage());
//            onReconnect();
//        }
//    }
//
//}
