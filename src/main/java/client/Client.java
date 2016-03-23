package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 8080);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        requestGameOptions(out);

        interpretServerMessage(in, stdIn, out);

        requestUserInput();
        interpretServerMessage(in, stdIn, out);
    }


    private static void requestGameOptions(PrintWriter out) throws IOException {
        System.out.println("[Client] Ask for game options....");
        out.println("Request: Get-Player-Options");
    }

    //Could say if data from socket starts with "Display:" then print the rest of the line
    private static void interpretServerMessage(BufferedReader in, BufferedReader stdIn, PrintWriter out) throws IOException {
        String fromServer;

        while ((fromServer = in.readLine()) != null) {
            System.out.println("[Client] Received back from server: " + fromServer);

            JSONObject jsonObject = new JSONObject(fromServer);
            System.out.println("[CLIENT ACTION] " + jsonObject.getString("action"));

            if (jsonObject.getString("action").equals("Display")) {
                System.out.println("[Client] Please choose a player type: (1) " + jsonObject.get("1") + " (2) " + jsonObject.get("2") + " (3) " + jsonObject.get("3"));
            } else if (jsonObject.getString("action").equals("Display-Next-Move")) {
                System.out.println("[Client] Prompt user for next move");
            } else if (jsonObject.getString("action").equals("Read-CL-Input")) {
                String input = readInputFromCommandLine(stdIn);
                requestValidation(input, out);
            }
        }
    }

    private static void requestValidation(String input, PrintWriter hout) throws IOException {
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
