
package jrmi.lib.net;

import java.io.Serializable;


public class RMIRequest implements Serializable 
{
    public String    methodName;
    public Object[]  inputs;
    public Object[]  outputs;
    
}
