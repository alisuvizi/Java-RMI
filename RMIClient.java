
package jrmi.lib.client;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.Socket;

import jrmi.lib.net.RMIRequest;


public class RMIClient 
{
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    
    public RMIClient(String host, int port)
    {
        try
        {
            socket = new Socket(host,port);
            socket.setSoTimeout(1500);
            
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            socket.close();
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void Call(String methodName, Object[] inputs, Object[] outputs) throws IOException, ClassNotFoundException
    {
        RMIRequest rr = new RMIRequest();
        
        rr.inputs = inputs;
        rr.outputs = outputs;
        rr.methodName = methodName;
        
        oos.writeObject(rr);
        oos.flush();
        
        rr = (RMIRequest)ois.readObject();
        
        if(outputs != null)
        {
         for(int i=0;i<outputs.length;i++)
            outputs[i] = rr.outputs[i];
        }
    }

}
