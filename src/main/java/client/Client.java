package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 8080);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        requestGameOptions(out);

        interpretServerMessage(in, stdIn);

        requestUserInput();
        interpretServerMessage(in, stdIn);

    }


    private static void requestGameOptions(PrintWriter out) throws IOException {
        System.out.println("[Client] Ask for game options....");
        out.println("Request: Get-Player-Options");
    }

    //Could say if data from socket starts with "Display:" then print the rest of the line
    private static void interpretServerMessage(BufferedReader in, BufferedReader stdIn) throws IOException {
        String fromServer;

        while ((fromServer = in.readLine()) != null) {
            System.out.println("[Client] Received back from server: " + fromServer);

            if (fromServer.startsWith("Display:")) {
                System.out.println("[Client] received from server: " + fromServer);
            } else if (fromServer.startsWith("Read:")) {
                String input  =  readInputFromCommandLine(stdIn);
                requestValidation(input);
            }
        }
    }

    private static void requestValidation(String input) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("[Client] Sending back the value read in from the command line.");
        out.println("Validate: " + input + "\n");
    }


    private static String readInputFromCommandLine(BufferedReader stdIn) throws IOException {
        String fromUser = stdIn.readLine();

        System.out.println("[Client] Read the following from the user " + fromUser);
        return fromUser;
    }

    private static void requestUserInput() throws IOException {
        Socket socket = new Socket("localhost", 8080);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("[Client] Ask for user to provide input....");
        out.println("Request: Ask-for-input\n");
    }
}
