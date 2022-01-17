import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.prefs.Preferences;

public class PreferenceTest {

    public int getPort() {
        return port;
    }

    public String getInetAddr() {
        return inetAddr;
    }

    public int getNumOfConnections() {
        return numOfConnections;
    }

    public String getConStr() {
        return conStr;
    }

    public String getDbUs() {
        return dbUs;
    }

    public String getDbPass() {
        return dbPass;
    }

    private int port;
    private String inetAddr;
    private int numOfConnections;
    private String conStr;
    private String dbUs;
    private String dbPass;

    public void setPreference() {
        String appConfigPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "app.properties";

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            System.out.println("Файл настроек не найден, используются стандартные настройки");
        }
        finally {
            port = Integer.parseInt(appProps.getProperty("port", "4848"));
            inetAddr = appProps.getProperty("inetAddress", "127.0.0.1");
            numOfConnections = Integer.parseInt(appProps.getProperty("numOfConnections", "10"));
            conStr = appProps.getProperty("connectionString", "jdbc:jtds:sqlserver://MI-NOTEBOOK-PRO/scholarships;instance=SQLEXPRESS");
            dbUs = appProps.getProperty("dbUser","sa");
            dbPass = appProps.getProperty("dbPass","qwe123");
        }

    }
}
