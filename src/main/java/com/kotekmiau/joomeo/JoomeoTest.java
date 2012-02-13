package com.kotekmiau.joomeo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * Joomeo API test
 *
 */
public class JoomeoTest {

	Logger log = Logger.getLogger(getClass());
	
    private static String endpoint = "http://api.joomeo.com/xmlrpc.php";
    final static String CONFIG_FILE = ".joomeo-config";
    
    private Properties config;
    private XmlRpcClient client;

    public JoomeoTest() {
		
    	/**
    	 * read config (to get apiKey, login, password, spacename)
    	 */
    	config = config();
    	
        XmlRpcClientConfigImpl rpcConfig = new XmlRpcClientConfigImpl();
        try {
            rpcConfig.setServerURL(new URL(endpoint));
            client = new XmlRpcClient();
            client.setConfig(rpcConfig);

            /**
             * joomeo.version.about
             */
            log.info(call("joomeo.version.about", null));
            
            /**
             * system.listMethods
             */
            Object[] l = (Object[]) call("system.listMethods", null);
            log.info(new HashSet(Arrays.asList(l)));
            
            /**
             * system.listMethods
             */
            final HashMap result = (HashMap) call("joomeo.session.init", new HashMap<String, String>() {{
	            put("apikey", config.getProperty("apiKey"));
	            put("spacename", config.getProperty("spacename"));
	            put("login", config.getProperty("login"));
	            put("password", config.getProperty("password"));
			}});
            log.info(result);
            
            Object[] result2 = (Object[]) call("joomeo.user.getCollectionList", new HashMap<String, String>() {{
	            put("apikey", config.getProperty("apiKey"));
	            put("sessionid", ""+result.get("sessionid"));
			}});
            log.info(new HashSet(Arrays.asList(result2)));
            
        } catch (XmlRpcException e) {
        	log.error(e.getMessage(), e);
        } catch (MalformedURLException e) {
        	log.error(e.getMessage(), e);
        }
    }

    private Object call(String method, Map<String, String> params) throws XmlRpcException {
    	if(params != null) {
    		Map params2[] = new Map[1];
    		params2[0] = params;
    		return client.execute(method, params2);
    	}
    	Object[] params1 = null;
		return client.execute(method, params1);    
    }

    private Properties config() {
		FileInputStream in1 = null;
		Properties properties = null;
		try {
			File f = new File(System.getProperty("user.home"), CONFIG_FILE);
			properties = new Properties();
			if (f.createNewFile()) {
				properties.put("apiKey", "API KEY HERE");
				properties.put("login", "LOGIN HERE");
				properties.put("password", "PASSWORD HERE");
				properties.put("spacename", "SPACENAME HERE");
				properties.store(new FileOutputStream(f), "joomeo config");
			}
			in1 = new FileInputStream(f);
			properties.load(in1);
			return properties;
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				in1.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
    }
    public static void main(String[] args) {
        new JoomeoTest();
    }
}
