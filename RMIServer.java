
package jrmi.lib.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import java.util.ArrayList;
import java.util.List;

import jrmi.lib.RemoteMethodRegistery;


public class RMIServer 
{
    ServerSocket  server;
    Thread serverThread;
    List<RMIClientService> rmiClients;
    RemoteMethodRegistery methodRegistery;
    boolean run;
    
    
    public RMIServer(String hostname, int port, RemoteMethodRegistery rm)
    {
        try
        {
            methodRegistery = rm;
            server  = new ServerSocket();
            server.bind(new InetSocketAddress(hostname,port));
            
            run = false;
            rmiClients = new ArrayList<RMIClientService>();

            serverThread = new Thread(new Runnable()
            {
                @Override
                public void run() {
            
                    while(run)
                    {
                        try
                        {
                            Socket socket = server.accept();
                            
                            RMIClientService rcs = new RMIClientService(socket, RMIServer.this.methodRegistery, true);
                            
                            RMIServer.this.rmiClients.add(rcs);
                            
                        } 
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
                
            });
            
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void start()
    {
        try
        {

           this.run = true;
           this.serverThread.start();

        } catch(Exception ex)
        {
           ex.printStackTrace();
        }
    }
    
    
    public void stop()
    {
        try
        {
            this.run = false;
            this.serverThread.interrupt();
            this.server.close();
            
            for(int i=0;i<rmiClients.size();i++)
                rmiClients.get(i).stop();
            
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
}
