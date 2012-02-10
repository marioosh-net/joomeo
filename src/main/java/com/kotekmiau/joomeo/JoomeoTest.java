package com.kotekmiau.joomeo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * Joomeo API test
 *
 */
public class JoomeoTest {

    private static String endpoint = "https://api.joomeo.com/xmlrpc.php";
    private XmlRpcClient client;

    public JoomeoTest() {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(endpoint));
            client = new XmlRpcClient();
            client.setConfig(config);

            Object[] params = null;// = new Object[]{new Integer(33), new Integer(9)};
            System.out.println(call("joomeo.version.about", params));
            System.out.println(call("system.listMethods", params));
        } catch (XmlRpcException ex) {
            Logger.getLogger(JoomeoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(JoomeoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Object call(String method, Object[] params) throws XmlRpcException {
        return client.execute(method, params);
    }

    public static void main(String[] args) {
        new JoomeoTest();
    }
}
