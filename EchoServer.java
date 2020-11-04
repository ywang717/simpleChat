// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  String message = msg.toString();
      if (message.startsWith("#")) {
          String[] params = message.substring(1).split(" ");
          if (params[0].equalsIgnoreCase("login") && params.length > 1) {
              if (client.getInfo("username") == null) {
                  client.setInfo("username", params[1]);
              } else {
                  try {
                      client.sendToClient("Your username has already been set!");
                  } catch (IOException e) {
                  }
              }

          }
      } else {
          if (client.getInfo("username") == null) {
              try {
                  client.sendToClient("Please set a username before messaging the server!");
                  client.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          } else {
              System.out.println("Message received: " + msg + " from " + client.getInfo("username"));
              this.sendToAllClients(client.getInfo("username") + " > " + message);
          }
      }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  public void clientConnected(ConnectionToClient client) {
	  String msg = ("The client" + client + " connected.");
	  System.out.println(msg);
	  this.sendToAllClients(msg);
  }
  
  public void clientDisconnected(ConnectionToClient client) {
	  String msg = ("The client" + client + " disconnected");
	  System.out.println(msg);
	  this.sendToAllClients(msg);
  }
  
  public void clientException(ConnectionToClient client, Throwable exception) {
	  String msg = "Client" + client + "is disconnected";
	  System.out.println(msg);
	  this.sendToAllClients(msg);
  }
  
  public void handleMessageFromServerConsole(String message) {
      if (message.startsWith("#")) {
          String[] parameters = message.split(" ");
          String command = parameters[0];
          switch (command) {
              case "#quit":
                  //closes the server and then exits it
                  try {
                      this.close();
                  } catch (IOException e) {
                      System.exit(1);
                  }
                  System.exit(0);
                  break;
              case "#stop":
                  this.stopListening();
                  break;
              case "#close":
                  try {
                      this.close();
                  } catch (IOException e) {
                  }
                  break;
              case "#setport":
                  if (!this.isListening() && this.getNumberOfClients() < 1) {
                      super.setPort(Integer.parseInt(parameters[1]));
                      System.out.println("Port set to " + Integer.parseInt(parameters[1]));
                  } else {
                      System.out.println("Can't set port. Server is connected.");
                  }
                  break;
              case "#start":
                  if (!this.isListening()) {
                      try {
                          this.listen();
                      } catch (IOException e) {
                          //error listening for clients
                      }
                  } else {
                      System.out.println("Client has already connected.");
                  }
                  break;
              case "#getport":
                  System.out.println("The port is " + this.getPort());
                  break;
              default:
                  System.out.println("Invalid command: '" + command+ "'");
                  break;
          }
      } else {
          this.sendToAllClients(message);
      }
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
