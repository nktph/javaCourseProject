import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        //вынести в конфиг
        PreferenceTest test = new PreferenceTest();
        test.setPreference();
        boolean reloaded = true;

        try
        {
                ServerSocket ss = new ServerSocket(test.getPort(),test.getNumOfConnections(),InetAddress.getByName(test.getInetAddr()));
                GetConnection getConnection = new GetConnection(ss, test.getConStr(), test.getDbUs(), test.getDbPass());
                new Thread(getConnection).start();
                System.out.println("Сервер запущен");

                while (true)
                {
                    Scanner sc = new Scanner(System.in);
                    String command = sc.next();

                    if (command.equals("exit"))
                        break;
                    if (command.equals("showConnections"))
                        System.out.println("Количество подключений: " +StartConnection.numberOfConnections);
                    if (command.equals("showProperties"))
                    {
                        System.out.println("Настройки: ");
                        System.out.println("Порт: " + test.getPort());
                        System.out.println("Адрес: " + test.getInetAddr());
                        System.out.println("Максимальное кол-во параллельных соединений: " + test.getNumOfConnections());
                        System.out.println("Строка подключения к БД: " + test.getConStr());
                        System.out.println("Пользователь БД: " + test.getDbUs());
                    }
                }
                getConnection.destroy();
        }
        catch (Exception exception)
        {
            System.out.print(exception.getMessage());
        }
    }
}

class StartConnection implements Runnable
{
    Socket socket;
    Statement statement;
    public static int numberOfConnections=0;

    public StartConnection(Socket socket, Statement statement)
    {
        this.socket = socket;
        this.statement = statement;
        numberOfConnections++;
    }

    @Override
    public void run()
    {
        try {
            Scanner fromClient = new Scanner(socket.getInputStream());
            PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);

            String command = fromClient.next();
            System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Принял строку "+ command);

            String[] logAndPass = command.split("\\.");
            //проверка логина
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users where login = '" + logAndPass[0] + "' AND password = '" + logAndPass[1] + "'");
            resultSet.next();
            if (!resultSet.isFirst())
            {
                toClient.println("нехорошийчеловек1");
                System.out.println(socket.getInetAddress()+":"+socket.getPort() + "\t| Неправильный логин/пароль. Отключение");
            }
            else if (resultSet.getString("isBlocked").equals("1"))
            {
                toClient.println("нехорошийчеловек2");
                System.out.println(socket.getInetAddress()+":"+socket.getPort() + "\t| Пользователь заблокирован. Отключение");
            }
            else
            {
                System.out.println(socket.getInetAddress()+":"+socket.getPort() + "\t| Пользователь зашел");
                int userid = resultSet.getInt("user_id");
                System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| id пользователя "+userid);
                String role = resultSet.getString("role");
                System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Роль "+role);
                toClient.println("Успех."+role.strip()+"View");
                //работаем
                Student robot = new Student(socket, statement,fromClient,toClient,role,userid); // создаем роботягу
                robot.doWork(); //заставляем работать
                //работа закончена
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            System.out.println(socket.getInetAddress()+":"+socket.getPort() + "\t| Работа окончена");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            numberOfConnections--;
        }
    }
}


class GetConnection implements Runnable
{
    private final String connectionString;
    private final String dbUser;
    private final String dbPass;
    private ServerSocket serverSocket;
    private boolean live = true;

    GetConnection(ServerSocket serverSocket, String ConnectionString, String dbUser, String dbPass)
    {
        this.serverSocket = serverSocket;
        this.connectionString = ConnectionString;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    public void destroy()
    {
        live = false;
    }

    public void run()
    {
        while (live)
        {
            try
            {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                Connection connection = DriverManager.getConnection(connectionString,dbUser,dbPass);
                Statement statement = connection.createStatement();
                statement.execute("USE scholarships");
                System.out.println("Ожидание запроса по адресу " + serverSocket.getInetAddress() + ':' + serverSocket.getLocalPort());
                Socket socket = serverSocket.accept();
                System.out.println("Принят запрос от " + socket.getInetAddress());
                new Thread(new StartConnection(socket, statement)).start();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

        }
    }
}