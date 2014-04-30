package net.portalblock.mysonapi;

import net.portalblock.mysonapi.internals.BlankDatabaseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

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
            int c = 1;
            for(int i = 1; i < count; i++){
                //Start key JSON
                jsonBuilder.append(" \"");
                jsonBuilder.append(data.getColumnLabel(i));
                jsonBuilder.append("\" : ");
                //Start value JSON
                jsonBuilder.append("\"");
                jsonBuilder.append(set.getString(i));
                jsonBuilder.append("\"");
                if(c < count-1){
                    jsonBuilder.append(",");
                    c++;
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
            int c = 1;
            for(int i = 1; i < count; i++){
                //Start key JSON
                jsonBuilder.append(" \"");
                jsonBuilder.append(data.getColumnLabel(i));
                jsonBuilder.append("\" : ");
                //Start value JSON
                jsonBuilder.append("\"");
                jsonBuilder.append(set.getString(i));
                jsonBuilder.append("\"");
                if(c < count-1){
                    jsonBuilder.append(",");
                    c++;
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

}
