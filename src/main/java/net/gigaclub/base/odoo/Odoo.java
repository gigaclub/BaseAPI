package net.gigaclub.base.odoo;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Odoo {

    private String hostname;
    private String database;
    private String username;
    private String password;
    private XmlRpcClient client;
    private XmlRpcClientConfigImpl common_config;
    private XmlRpcClient models;
    private int uid;

    public Odoo(String hostname, String database, String username, String password) {
        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;

        this.client = new XmlRpcClient();
        this.common_config = new XmlRpcClientConfigImpl();

        this.setUp();
    }

    private void setUp() {
        try {
            this.common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", this.hostname)));
            this.client.execute(this.common_config, "version", new ArrayList<>());
            ArrayList<Object> array = new ArrayList<Object>();
            array.add(this.database);
            array.add(this.username);
            array.add(this.password);
            array.add(new ArrayList<Object>());
            this.uid = (int) client.execute(this.common_config, "authenticate", array);
            this.models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", hostname)));
                }});
            }};
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public List<Object> search(String model, List<Object> domain, HashMap<Object, Object> condition) {
        try {
            return Arrays.asList((Object[]) this.models.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password,
                    model, "search", domain, condition
            )));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Object> search(String model, List<Object> domain) {
        try {
            return Arrays.asList((Object[]) this.models.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password,
                    model, "search", domain
            )));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int search_count(String model, List<Object> domain) {
        try {
            return (int) this.models.execute("execute_kw", Arrays.asList(
                this.database, this.uid, this.password,
                model, "search_count", domain
            ));
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Object> read(String model, List<Integer> ids, HashMap<Object, Object> fields) {
        try {
            return Arrays.asList(
                (Object[])this.models.execute("execute_kw", Arrays.asList(
                        this.database, this.uid, this.password,
                        model, "read", ids, fields
                ))
            );
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Object> read(String model, List<Integer> ids) {
        try {
            return Arrays.asList(
                    (Object[])this.models.execute("execute_kw", Arrays.asList(
                            this.database, this.uid, this.password,
                            model, "read", ids
                    ))
            );
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Map<String, Map<String, Object>> fields_get(String model, List<Object> domain, HashMap<Object, Object> condition) {
        try {
            return (Map<String, Map<String, Object>>) this.models.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password,
                    model, "fields_get", domain, condition
            ));
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Map<String, Object>>();
    }

    public List<Object> search_read(String model, List<Object> domain, HashMap<Object, Object> condition) {
        try {
            return Arrays.asList((Object[]) this.models.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password,
                    model, "search_read", domain, condition
            )));
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int create(String model, List<Object> parameters) {
        try {
            return (int) this.models.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password,
                    model, "create", parameters
            ));
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void write(String model, List<Object> parameters) {
        try {
            this.models.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password,
                    model, "write", parameters
            ));
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
    }

    public List<Object> name_get(String model, List<Object> domain) {
        try {
            Arrays.asList((Object[]) this.models.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password,
                    model, "name_get", domain
            )));
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void unlink(String model, ArrayList<Object> domain) {
        try {
            this.models.execute("execute_kw", Arrays.asList(
                    this.database, this.uid, this.password,
                    model, "unlink", domain
            ));
        } catch(XmlRpcException e) {
            e.printStackTrace();
        }
    }

}