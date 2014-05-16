package net.portalblock.mysonapi;

import net.portalblock.mysonapi.internals.BlankDatabaseException;
import org.json.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by portalBlock on 4/29/2014.
 */
public class MySONAPI {

    private MySQLInfo info;
    private Connection conn;

    public MySONAPI(MySQLInfo info){
        this.info = info;
    }

    public void start() throws ClassNotFoundException, IllegalAccessException, InstantiationException{
        Class.forName("com.mysql.jdbc.Driver").newInstance();

    }

    @Deprecated
    public void connect(String database) throws SQLException{
        conn = DriverManager.getConnection(info.getURLWithoutDatabase()+"/"+database, info.getUsername(), info.getPassword());
    }

    public void connect() throws SQLException, BlankDatabaseException{
        if(info.getDatabase() == null){
            throw new BlankDatabaseException("Empty database in MySQLInfo, please use new constructor.");
        }
        conn = DriverManager.getConnection(info.getURLWithDatabase(), info.getUsername(), info.getPassword());
    }

    public JSONArray getAllRows(String tableName) throws SQLException, JSONException{
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM `"+tableName+"`;");
        ResultSet set = statement.executeQuery();
        ResultSetMetaData data = set.getMetaData();
        int count = data.getColumnCount();
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"row\":[");
        while(set.next()){
            jsonBuilder.append("{ ");
            for(int i = 1; i < count; i++){
                //Start key JSON
                jsonBuilder.append(" \"");
                jsonBuilder.append(data.getColumnLabel(i));
                jsonBuilder.append("\" : ");
                //Start value JSON
                jsonBuilder.append("\"");
                jsonBuilder.append(set.getString(i));
                jsonBuilder.append("\"");
                if(i < count){
                    jsonBuilder.append(",");
                }

            }
            jsonBuilder.append("}");
            if(!set.isLast()){
                jsonBuilder.append(",");
            }
            //{ "key1":"value1" , "key2":"value2" },
        }
        return new JSONObject(jsonBuilder.toString()+"]}").getJSONArray("row");
    }

    public JSONArray getRow(String tableName, String key, String value) throws SQLException, JSONException{
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM `"+tableName+"` WHERE `"+key+"`=?;");
        //statement.setString(1, tableName);
        //statement.setString(1, key);
        statement.setString(1, value);
        ResultSet set = statement.executeQuery();
        ResultSetMetaData data = set.getMetaData();
        int count = data.getColumnCount();
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"row\":[");
        while(set.next()){
            jsonBuilder.append("{ ");
            for(int i = 1; i <= count; i++){
                //Start key JSON
                jsonBuilder.append(" \"");
                jsonBuilder.append(data.getColumnLabel(i));
                jsonBuilder.append("\" : ");
                //Start value JSON
                jsonBuilder.append("\"");
                jsonBuilder.append(set.getString(i));
                jsonBuilder.append("\"");
                if(i < count){
                    jsonBuilder.append(",");
                }

            }
            jsonBuilder.append("}");
            if(!set.isLast()){
                jsonBuilder.append(",");
            }
            //{ "key1":"value1" , "key2":"value2" },/
        }
        return new JSONObject(jsonBuilder.toString()+"]}").getJSONArray("row");
    }

    private void addRow(JSONObject rowObject) throws JSONException{
        Map<String, String> data = new HashMap<>();
        data = parse(rowObject, data);
    }

    public void updateRow(String tableName, String key, String value, JSONObject rowData) throws SQLException, JSONException{
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE `"+tableName+"` SET ");
        Map<String, String> data = new HashMap<>();
        data = parse(rowData, data);
        int i = 0;
        for(Map.Entry<String, String> e : data.entrySet()){
            queryBuilder.append("`"+e.getKey()+"`='"+e.getValue()+"'");
            if(i < data.size()-1){
                queryBuilder.append(", ");
            }
            i++;
        }
        queryBuilder.append(" WHERE `"+key+"`='"+value+"';");
        System.out.println(queryBuilder.toString());
        PreparedStatement statement = conn.prepareStatement(queryBuilder.toString());
        statement.execute();
    }

    public JSONObject toJSONData(Map<String, String> data){
        return new JSONObject(data);
    }

    private Map<String,String> parse(JSONObject json , Map<String,String> out) throws JSONException{
        Iterator<String> keys = json.keys();
        while(keys.hasNext()){
            String key = keys.next();
            String val = null;
            try{
                JSONObject value = json.getJSONObject(key);
                parse(value,out);
            }catch(Exception e){
                val = json.getString(key);
            }

            if(val != null){
                out.put(key,val);
            }
        }
        return out;
    }

}
