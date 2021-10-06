
package jrmi.lib.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import jrmi.lib.RemoteMethodRegistery;
import jrmi.lib.net.RMIRequest;


public class RMIClientService  {
    
    Socket socket;
    RemoteMethodRegistery methodRegistery;
    boolean run;
    Thread  thread;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public RMIClientService(Socket socket, RemoteMethodRegistery rm, boolean autorun) 
    {
        this.socket = socket;
        this.methodRegistery = rm;
        this.run = false;
        try
        {
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());

            this.thread = new Thread(new Runnable()
            {
                @Override
                public void run() {
                    while(run &&  RMIClientService.this.socket.isConnected())
                    {
                        try
                        {
                            RMIRequest rr = (RMIRequest)ois.readObject();
                            
                            if(rr != null)
                            {
                                try
                                {
                                    methodRegistery.Call(rr.methodName, rr.inputs, rr.outputs);
                                    
                                } catch(Exception ex)
                                {
                                    ex.printStackTrace();
                                } finally
                                {
                                    oos.writeObject(rr);
                                }
                            }

                        } catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }

                    }
                }
            });

            if(autorun)
                start();
            
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void start()
    {
        try
        {
            this.run = true;
            this.thread.start();
            
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
            this.thread.interrupt();
            this.thread = null;
            
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
