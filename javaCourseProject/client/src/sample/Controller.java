package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;


public class Controller {

    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label errorText;

//    @FXML
//    private void handleButtonAction(ActionEvent event) throws Exception {
//        ScreenController sc2 = ScreenController.getInstance();
//        sc2.activate("AdminView");
//        return;
//    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (checkInput(loginField.getText(),passwordField.getText())) {

            try {
                Connector mainConnection = Connector.getInstance();
                errorText.setVisible(false);
                mainConnection.toServer.println(loginField.getText()+'.'+passwordField.getText());
                System.out.println("Отправлено: логин - пароль: " +loginField.getText()+" - "+passwordField.getText());
                String[] received = mainConnection.fromServer.next().split("\\.");
                System.out.println("Получил: ");
                for (int i=0; i<received.length;i++)
                {
                    System.out.println(i+". "+ received[i]);
                }
                if (received[0].equals("Успех"))
                {
                    //залогинился
                    ScreenController sc = ScreenController.getInstance();
                    System.out.println("Логин успешен");
                    sc.activate(received[1]);
                }
                else if (received[0].equals("нехорошийчеловек1"))
                {
                    errorText.setText("Неверный логин либо пароль");
                    errorText.setVisible(true);
                    mainConnection.commitSuicide();
                }
                else if (received[0].equals("нехорошийчеловек2"))
                {
                    errorText.setText("Пользователь заблокирован!");
                    errorText.setVisible(true);
                    mainConnection.commitSuicide();
                }
            } catch (IOException e) {
                //e.printStackTrace();
                errorText.setText("Ошибка соединения");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            errorText.setText("Ошибка ввода");
            errorText.setVisible(true);
        }
    }
    private boolean checkInput(String loginf, String pass)
    {
        if (!loginf.matches("^(?![_.])(?!.*[_.]{2})[а-яА-Яa-zA-Z0-9._]+(?<![_.])$"))
            return false;
        if (!pass.matches("^(?![_.])(?!.*[_.]{2})[а-яА-Яa-zA-Z0-9._]+(?<![_.])$"))
            return false;
        return true;
    }
}