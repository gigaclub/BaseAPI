package net.gigaclub.base.odoo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONObject;

/** Odoo is a Java client library for the Odoo ERP. */
public class Odoo {

  /**
   * Hostname of the Odoo server.
   */
  private final String hostname;
  /**
   * Database name of the Odoo server.
   */
  private final String database;
  /**
   * Username of the Odoo server.
   */
  private final String username;
  /**
   * Password of the Odoo server.
   */
  private final String password;
  /**
   * Client for the XML-RPC communication.
   */
  private final XmlRpcClient client;
  /**
   * Common configuration for the XML-RPC communication.
   */
  private final XmlRpcClientConfigImpl commonConfig;
  /**
   * Models configuration for the XML-RPC communication.
   */
  private XmlRpcClient models;
  /**
   * UID of the user.
   */
  private int uid;

  /**
   * Creates a new Odoo instance.
   *
   * @param hostname The hostname of the Odoo instance.
   * @param database The database to connect to.
   * @param username The username to use.
   * @param password The password to use.
   */
  public Odoo(String hostname, String database, String username, String password) {
    this.hostname = hostname;
    this.database = database;
    this.username = username;
    this.password = password;

    this.client = new XmlRpcClient();
    this.commonConfig = new XmlRpcClientConfigImpl();

    this.setUp();
  }

  private void setUp() {
    try {
      this.commonConfig.setServerURL(new URL(String.format("%s/xmlrpc/2/common", this.hostname)));
      this.client.execute(this.commonConfig, "version", new ArrayList<>());
      ArrayList<Object> array = new ArrayList<Object>();
      array.add(this.database);
      array.add(this.username);
      array.add(this.password);
      array.add(new ArrayList<Object>());
      this.uid = (int) client.execute(this.commonConfig, "authenticate", array);
      this.models =
          new XmlRpcClient() {
            {
              setConfig(
                  new XmlRpcClientConfigImpl() {
                    {
                      setServerURL(new URL(String.format("%s/xmlrpc/2/object", hostname)));
                    }
                  });
            }
          };
    } catch (XmlRpcException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public String getHostname() {
    return hostname;
  }

  public String getDatabase() {
    return database;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public XmlRpcClient getClient() {
    return client;
  }

  public XmlRpcClientConfigImpl getCommonConfig() {
    return commonConfig;
  }

  public XmlRpcClient getModels() {
    return models;
  }

  public int getUid() {
    return uid;
  }

  /**
   * Search for a record in the odoo database.
   *
   * @param model The model to search in.
   * @param domain The domain to search for.
   * @param condition The condition to search for.
   * @return The list of ids of the record.
   */
  public List<Object> search(String model, List<Object> domain, Map<Object, Object> condition) {
    try {
      return Arrays.asList(
          (Object[])
              this.models.execute(
                  "execute_kw",
                  Arrays.asList(
                      this.database, this.uid, this.password, model, "search", domain, condition)));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  /**
   * Search for a record in the odoo database.
   *
   * @param model The model to search in.
   * @param domain The domain to search for.
   * @return The list of ids of the record.
   */
  public List<Object> search(String model, List<Object> domain) {
    try {
      return Arrays.asList(
          (Object[])
              this.models.execute(
                  "execute_kw",
                  Arrays.asList(this.database, this.uid, this.password, model, "search", domain)));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  /**
   * Search for the count of records in the odoo database.
   *
   * @param model The model to search in.
   * @param domain The domain to search for.
   * @return The count of records.
   */
  public int search_count(String model, List<Object> domain) {
    try {
      return (int)
          this.models.execute(
              "execute_kw",
              Arrays.asList(this.database, this.uid, this.password, model, "search_count", domain));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * Read a record from the odoo database.
   *
   * @param model The model to read from.
   * @param ids The ids of the record to read.
   * @param fields The fields to read.
   * @return The list of fields of the record.
   */
  public JSONArray read(String model, List<Integer> ids, Map<Object, Object> fields) {
    try {
      return new JSONArray(
          Arrays.asList(
              (Object[])
                  this.models.execute(
                      "execute_kw",
                      Arrays.asList(
                          this.database, this.uid, this.password, model, "read", ids, fields))));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return new JSONArray();
  }

  /**
   * Read a record from the odoo database.
   *
   * @param model The model to read from.
   * @param ids The ids of the record to read.
   * @return The list of fields of the record.
   */
  public JSONArray read(String model, List<Integer> ids) {
    try {
      return new JSONArray(
          Arrays.asList(
              (Object[])
                  this.models.execute(
                      "execute_kw",
                      Arrays.asList(
                          this.database,
                          this.uid,
                          this.password,
                          model,
                          "read",
                          Arrays.asList(ids)))));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return new JSONArray();
  }

  /**
   * Get the list of fields of a record from the odoo database.
   *
   * @param model The model to read from.
   * @param domain The domain to search for.
   * @param condition The condition to search for.
   * @return The list of fields of the record.
   */
  public JSONObject fields_get(String model, List<Object> domain, Map<Object, Object> condition) {
    try {
      return new JSONObject(
          (Map<String, Map<String, Object>>)
              this.models.execute(
                  "execute_kw",
                  Arrays.asList(
                      this.database,
                      this.uid,
                      this.password,
                      model,
                      "fields_get",
                      domain,
                      condition)));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return new JSONObject();
  }

  /**
   * Get the list of data of a record from the odoo database.
   *
   * @param model The model to read from.
   * @param domain The domain to search for.
   * @param condition The condition to search for.
   * @return The list of data of the record.
   */
  public JSONArray search_read(String model, List<Object> domain, Map<Object, Object> condition) {
    try {
      return new JSONArray(
          Arrays.asList(
              (Object[])
                  this.models.execute(
                      "execute_kw",
                      Arrays.asList(
                          this.database,
                          this.uid,
                          this.password,
                          model,
                          "search_read",
                          domain,
                          condition))));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return new JSONArray();
  }

  /**
   * Create a record in the odoo database.
   *
   * @param model The model to create in.
   * @param parameters The parameters to create the record with.
   * @return The id of the created record.
   */
  public int create(String model, List<Object> parameters) {
    try {
      return (int)
          this.models.execute(
              "execute_kw",
              Arrays.asList(this.database, this.uid, this.password, model, "create", parameters));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * Update a record in the odoo database.
   *
   * @param model The model to update in.
   * @param parameters The parameters to update the record with.
   */
  public void write(String model, List<Object> parameters) {
    try {
      this.models.execute(
          "execute_kw",
          Arrays.asList(this.database, this.uid, this.password, model, "write", parameters));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the name of a record from the odoo database.
   *
   * @param model The model to read from.
   * @param ids The ids of the record to read.
   * @return The name of the record.
   */
  public JSONArray name_get(String model, List<Integer> ids) {
    try {
      return new JSONArray(
          Arrays.asList(
              (Object[])
                  this.models.execute(
                      "execute_kw",
                      Arrays.asList(
                          this.database,
                          this.uid,
                          this.password,
                          model,
                          "name_get",
                          Arrays.asList(ids)))));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
    return new JSONArray();
  }

  /**
   * Delete a record in the odoo database.
   *
   * @param model The model to delete from.
   * @param domain The domain to search for.
   */
  public void unlink(String model, List<Object> domain) {
    try {
      this.models.execute(
          "execute_kw",
          Arrays.asList(this.database, this.uid, this.password, model, "unlink", domain));
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }
  }
}
