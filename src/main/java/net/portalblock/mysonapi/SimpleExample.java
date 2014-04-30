package net.portalblock.mysonapi;

import org.json.JSONArray;

/**
 * Created by portalBlock on 4/30/14.
 */
public class SimpleExample {

    public static void main(String[] args){
        //The port that the MySQL Server is running on.
        int port = 3306;

        //Make a new instance of MySQLInfo for storing authentication variables.
        MySQLInfo info = new MySQLInfo("<username>", "<password>", "<host>", port, "<databaseName>");
        //If you wish to specify a database later for some reason: ("<username>", "<password>", "<host>", port)

        //Make a new instance of the API while passing the authentication variables into it. (Make a new instance for every database you need.)
        MySONAPI api = new MySONAPI(info);
        try{
            //Start the API to load all needed drivers.
            api.start();

            //Connect to the database specified in the MySQLInfo variable earlier on.
            api.connect(); //Or if you did not specify a database in MySQLInfo before: api.connect("databaseName");


            //Begin the fun!
            System.out.println("Get all data from a table (Same as running SELECT * FROM `table`) \n");
            JSONArray array = api.getAllRows("table");
            for(int i = 0; i < array.length(); i++){
                System.out.print("Key: "+array.getJSONObject(i).getString("id"));//The variable 'id' is the name of the "key" column.
                System.out.println(", Value: "+array.getJSONObject(i).getString("name"));//The variable 'value' is the name of a column int the database.
            }

            System.out.println("\n \nGet a specific row or set of rows. (Same as running \"SELECT * FROM `table` WHERE `key`='data')\n");
            array = api.getRow("table", "id", "1");
            for(int i = 0; i < array.length(); i++){
                System.out.print("Key: "+array.getJSONObject(i).getString("id"));//The variable 'id' is the name of the "key" column.
                System.out.println(", Value: "+array.getJSONObject(i).getString("name"));//The variable 'value' is the name of a column int the database.
            }

        }catch (Exception e){
            //There is the possibility of at least 6 exceptions being thrown here, you may handle them individually if you want.

            /*
            Here is a list of the ones the API may throw directly:

            ClassNotFoundException
            IllegalAccessException
            InstantiationException
            SQLException
            BlankDatabaseException (If you did not use the correct constructor and did not connect using MyJSONAPI#connect(String databaseName)
            JSONException
             */
            e.printStackTrace();
        }
    }

}
