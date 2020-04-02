package org.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9999);
        Socket s = ss.accept();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String mess = bfr.readLine();
        System.out.println(mess);
        String[] a = mess.split(" ");
        System.out.println(a[1]);
    }
}
