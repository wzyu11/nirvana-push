package com.nirvana.push.client.oio;

import com.nirvana.push.core.message.DefaultCardBox;
import com.nirvana.push.core.message.MessageLevel;
import com.nirvana.push.core.message.Package;
import com.nirvana.push.core.message.PackageType;
import com.nirvana.push.protocol.PayloadPart;
import com.nirvana.push.protocol.ProtocolPackage;

import java.io.*;
import java.net.Socket;

/**
 * 简单的远程订阅者。
 * Created by Nirvana on 2017/8/17.
 */
public class SocketSubscriber {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 32222);
        OutputStream outputStream = socket.getOutputStream();

        String string = "!";
        Package pkg = new Package(PackageType.SUBSCRIBE, MessageLevel.NO_CONFIRM, null, false, new DefaultCardBox(string));
        ProtocolPackage basePackage = ProtocolPackage.fromPackage(pkg);
        basePackage.output(outputStream);

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String msg = reader.readLine();
            System.out.println(msg);
        }
    }

}
