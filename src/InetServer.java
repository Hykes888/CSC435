import java.io.*; //Get the Input Output libraries
import java.net.*; //Get the Java Networking libraries

/**
 * File: InetServer.java
 *
 * A simple client server java app.
 *
 * The code below is the server side of the java app. It performs all requests sent to in.
 *
 */
class Worker extends Thread { //Class definition.
    Socket sock; //Socket create the connection.
    //Constrctor, args Socket s assigns to local sock.
    Worker(Socket s) {
        sock = s;
    }
    //When you extend thread you have to run the thread and each time a request comes into the server a new thread is created.
    public void run() {
        // These two are the input and output of the socket connection.
        PrintStream out = null;
        BufferedReader in = null;
        //Try/catch allow for error handling.
        try {
            //Create a new input so we are getting the input from the client. This is setup.
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            //Create a new output so we are able to send the output to the client. This is setup
            out = new PrintStream(sock.getOutputStream());
            try {
                String name; //variable
                name = in.readLine(); // read from the input queue
                System.out.println("Looking up " + name); // Print the out put from the what was sent from the client.
                printRemoteAddress(name, out); //Calls method printRemoteAddress with its two arguments.
            } catch (IOException x) { // error handling.
                System.out.println("Server read error");
                x.printStackTrace();
            }
            sock.close(); //close the connection after the work has been finished.
        } catch (IOException ioe) { // error handling
            System.out.println(ioe);
        }
    }
    //This method does a DNS lookup using a java library. Two args are need for this method a String and an output stream.
    static void printRemoteAddress(String name, PrintStream out) {
        try {
            out.println("Looking up " + name + "..."); //Prints line on clients console
            InetAddress machine = InetAddress.getByName(name); // java DNS lookup
            out.println("Host name : " + machine.getHostName()); // Prints line on clients console which is the host name.
            out.println("Host IP : " + toText(machine.getAddress())); // Call method toTest, Prints lines on clients console which will be the IP address of the URL lookup.
        } catch (UnknownHostException ex) { //error handling
            out.println("Failed in attempt to look up " + name);
        }
    }
    //Method that takes an IP address in bytes and formats it into text. Args bytes in an array
    static String toText(byte ip[]) {
        StringBuffer result = new StringBuffer(); // crate a string buffer for the ip address formatting
        //Java for loop that breaks down the IP address into readable text for the output to the client console.
        for (int i = 0; i < ip.length; ++i) {
            if (i > 0) result.append(".");
            result.append(0xff & ip[i]);
        }
        return result.toString(); // returns a string.
    }
}

/**
 * This Class has the main for the server and starts a forever loop that will accept client request and start communication with them using a socket.
 */
public class InetServer {
        public static void main(String a[]) throws IOException {
            int q_len = 6;
            int port = 1865; // Connection port 1865
            Socket sock; // variable of a java socket.

            ServerSocket servsock = new ServerSocket(port, q_len); // crate new socket for the server.

            System.out.println("Tim Heichele Inet server 1.8 starting up, listening to port 1865.\n");
            //Forever loop that will will accept all requests and then create a new thread and perform work for that client.
            while (true) {
                sock = servsock.accept();
                new Worker(sock).start();
            }
        }
    }

