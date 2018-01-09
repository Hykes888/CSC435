import java.io.*; //Get the Input Output libraries
import java.net.*; //Get the Java Networking libraries

/**
 * This is the client code and it sends requests to the server to be able to do work.
 */
public class InetClient {

    //If there is an input of IP address it will use that IP address of server or it assumes its just localhost.
    public static void main(String[] args) {
        String serverName; //variable
        //If then else statement that again says if I start the client with a server IP address I will be able to use the IP address otherwise its local host as the IP address.
        if(args.length < 1) serverName = "localhost";
        else serverName = args[0];

        System.out.println("Tim Heichele Inet Client, 1.8.\n");
        System.out.println("Using Server: " + serverName + ", Port:1865");
        //Create a new input to the server which it reads input coming from the console.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try{
            String name; //Local variable.
            //A while loop that is forever until you enter quit. But the other function is to enter an IP address or hostname for a the server to look up.
            do{
                System.out.print("Enter a hostname or an IP address, (quit) to end: ");
                System.out.flush();
                name = in.readLine();
                if (name.indexOf("quit") < 0)
                    // Calls method getRemoteAddress, 2 args string and serverName
                    getRemoteAddress(name, serverName);
            } while (name.indexOf("quit") < 0);
            System.out.println("Cancelled by user request.");
        } catch (IOException x) {x.printStackTrace();} //error handling
    }

    //Method that takes an IP address in bytes and formats it into text. Args bytes in an array
    static String toText (byte ip[]) {
        StringBuffer result = new StringBuffer();// crate a string buffer for the ip address formatting
        //Java for loop that breaks down the IP address into readable text for the output to the sever console.
        for (int i=0; i < ip.length; ++i){
            if (i > 0) result.append(".");
            result.append(0xff & ip[i]);
        }
        return result.toString(); // returns a string
    }

    //This method sends the request to the server with two inputs the name and the serverName.
    static void getRemoteAddress(String name, String serverName) {
        //Create local variables
        Socket sock;
        BufferedReader fromServer;
        PrintStream toServer;
        String textFromServer;

        try{
            sock = new Socket(serverName,1865); //Create connection
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //input from the server
            toServer = new PrintStream(sock.getOutputStream()); //sending to the server
            toServer.println(name); // print line on console with local name or requested work
            toServer.flush(); //New line
            //This for is outputting the data it is recieving from the server after it did its work.
            for(int i = 1; i <= 3; i++){
                textFromServer = fromServer.readLine();
                if (textFromServer != null)
                    System.out.println(textFromServer);
            }
            sock.close(); // close socket or connection
        } catch (IOException x){ //error handling
            System.out.println("Socket error.");
            x.printStackTrace();
        }
    }
}
