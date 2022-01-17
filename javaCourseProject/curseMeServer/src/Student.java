import models.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class Student {
    String role;
    Socket socket;
    Statement statement;
    Scanner fromClient;
    PrintWriter toClient;
    int userid;
    HashMap<String, commandInt> commandMap = new HashMap<>();

    public Student(Socket socket, Statement statement, Scanner fromClient, PrintWriter toClient, String role, int userid) {
        this.socket = socket;
        this.statement = statement;
        this.fromClient = fromClient;
        this.toClient = toClient;
        this.role = role;
        this.userid =userid;
        commandMap = new HashMap<>();
        commandMap.put("loadStudent",new loadStudent());

        commandMap.put("loadUser",new loadUser());
        commandMap.put("insertUser",new insertUser());
        commandMap.put("editUser", new editUser());
        commandMap.put("delUser", new delUser());

        commandMap.put("loadFaculty",new loadFaculty());
        commandMap.put("insertFaculty", new insertFaculty());
        commandMap.put("editFaculty", new editFaculty());
        commandMap.put("delFaculty", new delFaculty());

        commandMap.put("loadHR", new loadHR());
        commandMap.put("insertHR", new insertHR());
        commandMap.put("editHR", new editHR());
        commandMap.put("delHR", new delHR());
        commandMap.put("updateFacSpec", new updateFacSpec());

        commandMap.put("loadScholarship", new loadScholarship());
        commandMap.put("insertScholarship", new insertScholarship());
        commandMap.put("delScholarship", new delScholarship());

        commandMap.put("loadCoef", new loadCoef());
        commandMap.put("insertCoef", new insertCoef());
        commandMap.put("editCoef", new editCoef());
        commandMap.put("delCoef", new delCoef());

        commandMap.put("updateStudent", new updateStudent());

    }

    public void doWork() throws IOException {
        do
        {
            if (fromClient.hasNext()) {
                String commandPrefix = fromClient.next().strip();
                System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t|------------------------------");
                System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "\t| Выполняю " + commandPrefix);
                try {
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "\t| параметры: " + commandPrefix);
                    System.out.println("\t\t\t\t\t| statement = " + statement);
                    System.out.println("\t\t\t\t\t| toClient = " + toClient);
                    System.out.println("\t\t\t\t\t| fromClient = " + toClient);
                    System.out.println("\t\t\t\t\t| socket = " + socket);
                    System.out.println("\t\t\t\t\t| userid = " + userid);
                    commandMap.get(commandPrefix).execute(statement,fromClient, toClient, socket, userid);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    fromClient = fromClient.reset();
                    System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Конец выполнения комманды");
                    System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| ------------------------------------");
                }
            }
            else
            {
                socket.close();
            }
        }while (!socket.isClosed());
    }
}
interface commandInt{
    public void execute(Statement statement,Scanner fromClient,PrintWriter toClient, Socket socket, int userid) throws SQLException;
}
//--------------------------Student----------------
class loadStudent implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        String year = fromClient.next();
        System.out.println("\t\t\t\t\t| Год " + year);
        ResultSet resultSetNAME = statement.executeQuery("SELECT student_id, students.name, f.name as faculty, s.name as speciality from students INNER JOIN faculty f on f.faculty_id = students.faculty_id INNER JOIN specialities s on s.speciality_id = students.speciality_id where user_id = '" + userid+ "'");
        resultSetNAME.next();
        String name =resultSetNAME.getString("name");
        System.out.println("\t\t\t\t\t| Имя " + name);
        toClient.println(name);
        String facul = resultSetNAME.getString("faculty");
        System.out.println("\t\t\t\t\t| Факультет " + facul);
        toClient.println(facul);
        String spec = resultSetNAME.getString("speciality");
        System.out.println("\t\t\t\t\t| Специальность " + spec);
        toClient.println(spec);
//        String score = resultSetNAME.getString("avgScore");
//        System.out.println("\t\t\t\t\t| Средний балл " + score);
//        toClient.println(score);
        ResultSet resultSet2 = statement.executeQuery("SELECT scholarship.monthYear as date,scholarship.scholSum, scholarship.nOfD, s.avgScore as score, t.coefSum /*,scholarship.deductions*/" +
                "                FROM scholarship INNER JOIN students s on s.student_id = scholarship.student_id" +
                "                LEFT JOIN ( SELECT SUM(coef) as coefSum, record_id FROM coef group by record_id) as t on scholarship.record_id = t.record_id" +
                "        where YEAR(scholarship.monthYear) = '"+year+"'");

        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Обрабатываем запрос, отправляю:");
        StudentInfo.parseResult(resultSet2, toClient);
    }
}
//-------------------------------------------------------------------
//-----------------------------USER---------------------------------
class loadUser implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT users.user_id as user_id, login as login, role as role, isBlocked as isBlocked, s.student_id as studId, s.name as studName FROM users LEFT JOIN students s on users.user_id = s.user_id");
        UserInfo.parseResult(rs,toClient);
    }
}
class insertUser implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {

        String login =fromClient.next();
        System.out.println("\t\t\t\t\t| Логин" + login);
        String pass = fromClient.next();
        System.out.println("\t\t\t\t\t| Пароль" + pass);
        String role = fromClient.next();
        System.out.println("\t\t\t\t\t| Роль" + role);
        boolean isBlocked = Boolean.parseBoolean(fromClient.next());
        statement.executeUpdate("INSERT INTO users (login, password, role, isBlocked) VALUES ('"+login+"','"+pass+"','"+role+"','"+isBlocked+"')");

    }
}
class editUser implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {

        String id =fromClient.next().strip();
        System.out.println("\t\t\t\t\t| Номер " + id);
        String pass = fromClient.next().strip();
        System.out.println("\t\t\t\t\t| Пароль " + pass);
        String role = fromClient.next().strip();
        System.out.println("\t\t\t\t\t| Роль " + role);
        String isBlocked = fromClient.next().strip();
        statement.executeUpdate("UPDATE users SET password = '"+pass+"', role = '"+role+"', isBlocked = '"+isBlocked+"' " +
                                    "WHERE users.user_id = '"+id+"'");

    }
}
class delUser implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        String id =fromClient.next().strip();
        System.out.println("\t\t\t\t\t| Выбран " + id);
        statement.executeUpdate("DELETE users WHERE users.user_id = '"+id+"'");
    }
}
//------------------------------------------------------------------------------------------
//--------------------------------FACULTY------------------------
class loadFaculty implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT faculty.faculty_id, faculty.name FROM faculty");
        FacultyInfo.parseResult(rs,toClient);
    }
}
class insertFaculty implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        String facName = fromClient.next();
        System.out.println("\t\t\t\t\t| Название факультета" + facName);
        statement.executeUpdate("INSERT INTO faculty (name) VALUES ('"+facName+"')");
    }
}
class delFaculty implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        String facName = fromClient.next();
        System.out.println("\t\t\t\t\t| Название факультета" + facName);
        statement.executeUpdate("DELETE faculty WHERE faculty.name = '"+facName+"'");
    }
}
class editFaculty implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        String facName = fromClient.next().strip();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| ИмяНаПоменять - " + facName);
        String facNameFrom = fromClient.next().strip();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| ИмяCПоменять - " + facNameFrom);
        statement.executeUpdate("UPDATE faculty SET name = '"+facNameFrom+"' WHERE name = '"+facName+"'");
    }
}
//-----------------------------------------------------------------------------------------------------


// --------------------------------------HR------------------------------------------------------
class loadHR implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT student_id as id, students.name as name, f.name as fac, s.name as spec FROM students INNER JOIN specialities s on s.speciality_id = students.speciality_id inner join faculty f on f.faculty_id = students.faculty_id");
        HRInfo.parseResult(rs,toClient);
    }
}
class insertHR implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        String name = fromClient.next().strip();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Имя: "+name);
        String fac = fromClient.next().strip();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Факультет: "+fac);
        String spec = fromClient.next().strip();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Специальность: "+spec);
        statement.executeUpdate("INSERT INTO users (login, password, role, isBlocked) VALUES ('"+name+"', '123', 'Student', '0')");
        statement.executeUpdate("INSERT INTO students (user_id, name, faculty_id, speciality_id) VALUES ((SELECT user_id FROM users WHERE login ='"+name+"'), '"+name+"', (SELECT faculty_id FROM faculty WHERE faculty.name = '"+fac+"'), (SELECT speciality_id FROM specialities WHERE specialities.name = '"+spec+"'))");
    }
}
class editHR implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        int id = fromClient.nextInt();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Номер: "+id);
        String name = fromClient.next();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Имя: "+name);
        String fac = fromClient.next();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Факультет: "+fac);
        String spec = fromClient.next();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Специальность: "+spec);
        statement.executeUpdate("UPDATE students SET name = '"+name+"', faculty_id = (SELECT faculty_id FROM faculty WHERE faculty.name = '"+fac+"'), speciality_id = (SELECT speciality_id FROM specialities WHERE specialities.name = '"+spec+"') WHERE student_id = '"+id+"'");
    }
}
class delHR implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        int id = fromClient.nextInt();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Номер: "+id);
        statement.executeUpdate("DELETE FROM students WHERE student_id = '"+id+"'");
    }
}
class updateStudent implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        ResultSet facRS = statement.executeQuery("SELECT students.name as name FROM students");
        String facList = "";
        while (facRS.next())
        {
            facList += facRS.getString("name").strip() + ".";
        }
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Лист студентов: "+facList);
        toClient.println(facList.strip());
    }
}


//-----------------------------------------------------------------------------------------------------
//------------------------------Coef-------------
class loadCoef implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        int selectedId = fromClient.nextInt();
        System.out.println("\t\t\t\t\t| Выбранный пользователь" + selectedId);
        ResultSet rs = statement.executeQuery("SELECT coef.coef_id as id, coef.description as descr,coef.coef FROM coef WHERE record_id = '"+selectedId +"'");
        CoefInfo.parseResult(rs,toClient);
    }
}
class insertCoef implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        String descr = fromClient.next();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Описание: "+descr);
        String sum = fromClient.next();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Сумма: "+sum);
        String idSchol = fromClient.next();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Id чела: "+idSchol);
        statement.executeUpdate("INSERT INTO coef (description, coef, record_id) VALUES ('"+descr+"', '"+sum+"', '"+idSchol+"')");
    }
}
class editCoef implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        int id = fromClient.nextInt();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Номер: "+id);
        String descr = fromClient.next();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Описание: "+descr);
        String sum = fromClient.next();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Сумма: "+sum);
        statement.executeUpdate("UPDATE coef SET description = '"+descr+"', coef = '"+sum+"' WHERE coef_id = '"+id+"'");
    }
}
class delCoef implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        int id = fromClient.nextInt();
        System.out.println("\t\t\t\t\t| Выбран пользователь" + id);
        statement.executeUpdate("DELETE FROM coef WHERE coef_id = '"+id+"'");
    }
}
//---------------------------------------------------------
class updateFacSpec implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        ResultSet facRS = statement.executeQuery("SELECT faculty.name as name FROM faculty");
        String facList = "";
        while (facRS.next())
        {
            facList += facRS.getString("name").strip() + ".";
        }
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Лист факультетов: "+facList);
        ResultSet specRS = statement.executeQuery("SELECT specialities.name as name FROM specialities");
        String specList = "";
        while (specRS.next())
        {
            specList += specRS.getString("name").strip() + ".";
        }
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Лист специальностей: "+specList);
        toClient.println(facList.strip());
        toClient.println(specList.strip());
    }
}
//------------------------------------------------
//------------------------------Scholarship-------------
class loadScholarship implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT scholarship.record_id as id, s.name as name, scholarship.monthYear as date, scholarship.nOfD, s.avgScore, t.coefSum ,scholarship.scholSum" +
                "  FROM scholarship INNER JOIN students s on s.student_id = scholarship.student_id " +
                "LEFT JOIN ( SELECT SUM(coef) as coefSum, record_id FROM coef group by record_id) as t on scholarship.record_id = t.record_id");
        ScolarshipInfo.parseResult(rs,toClient);
    }
}
class insertScholarship implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        String name = fromClient.next().strip();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Студент: "+name);
        String monthDay = fromClient.next().strip();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| месяц: "+monthDay);
        int nOfD = fromClient.nextInt();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| кол-во дней: "+nOfD);
        Double scholSum = fromClient.nextDouble();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Стипендия: "+scholSum);
        statement.executeUpdate("INSERT INTO scholarship (scholarship.monthYear, scholarship.student_id, scholarship.nOfD, scholarship.scholSum) VALUES ('"+monthDay+"', (SELECT students.student_id FROM students Where name = '"+name+"'), '"+nOfD+"', '"+scholSum+"') ");
    }
}

class delScholarship implements commandInt
{
    @Override
    public void execute(Statement statement,Scanner fromClient ,PrintWriter toClient , Socket socket, int userid) throws SQLException {
        int id = fromClient.nextInt();
        System.out.println(socket.getInetAddress() +":"+socket.getPort() +"\t| Номер: "+id);
        statement.executeUpdate("DELETE FROM scholarship WHERE scholarship.record_id = '"+id+"'");
    }
}

//--------------------------------------------------