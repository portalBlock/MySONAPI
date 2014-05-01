package net.portalblock.mysonapi;

/**
 * Created by portalBlock on 4/29/2014.
 */
public class MySQLInfo {

    private String username, password, host, database;
    private int port;

    @Deprecated
    public MySQLInfo(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public MySQLInfo(String username, String password, String host, int port, String database) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        if(database.equalsIgnoreCase("")){
            this.database = null;
        }else{
            this.database = database;
        }

    }

    public String getURLWithoutDatabase(){
        return "jdbc:mysql://"+host+":"+port;
    }

    public String getURLWithDatabase(){
        return "jdbc:mysql://"+host+":"+port+"/"+database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() { return database; }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
