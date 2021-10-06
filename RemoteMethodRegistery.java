
package jrmi.lib;

import java.util.HashMap;


public class RemoteMethodRegistery {
    
    HashMap<String, RemoteMethod> methods;
    
    public RemoteMethodRegistery()
    {
        methods = new HashMap<>();
    }
    
    public void Register(String methodName, RemoteMethod rm)
    {
          methods.put(methodName, rm);
    }
    
    public void Unregister(String methodName)
    {
        methods.remove(methodName);
    }
    
    
    public void Call(String method, Object[] input, Object[] output)
    {
        RemoteMethod rm = methods.get(method);
        rm.Call(input, output);
    }
    
    
    
}
