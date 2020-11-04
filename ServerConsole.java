// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  EchoServer server;
  
  public ServerConsole(int port) {
      server = new EchoServer(port);
      try {
          server.listen();
      } catch (IOException e) {
          System.out.println("error.");
      }
  }

@Override
public void display(String message) {
	if (message.startsWith("#")) {
        return;
    }
    System.out.println("SERVER MSG> " + message);
	
}

public void accept() {
    try {
        BufferedReader fromConsole =
                new BufferedReader(new InputStreamReader(System.in));
        String message;

        while ((message = fromConsole.readLine()) != null) {
            server.handleMessageFromServerConsole(message);
            this.display(message);
        }
    } catch (Exception ex) {
        System.out.println
                ("error.");
    }
}

public static void main(String[] args) 
{
	int port = 0;

    try {
        port = Integer.parseInt(args[0]);
    } catch (Throwable t) {
        port = DEFAULT_PORT; 
    }

    ServerConsole serv = new ServerConsole(port);
    serv.accept();

  }  
  
}
