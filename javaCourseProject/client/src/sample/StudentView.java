package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.StudentInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StudentView {
    @FXML
    private Label labelFIO;
    @FXML
    private Label labelFacultyName;
    @FXML
    private Label labelSpeciality;
    @FXML
    private ChoiceBox choiceBoxYear;
    @FXML
    private TableView<StudentInfo> tableViewMain;
    @FXML private TableColumn<StudentInfo, String> monthC;
    @FXML private TableColumn<StudentInfo, String> scholSumC;
    @FXML private TableColumn<StudentInfo, String> nOfDaysC;
    @FXML private TableColumn<StudentInfo, String> CoefC;
    @FXML private TableColumn<StudentInfo, String> scoreC;
    @FXML private TableColumn<StudentInfo, String> sumC;

    @FXML
    private DatePicker mothFil;
    @FXML
    private TextField scholSumFil;
    @FXML
    private TextField nOfDayFil;
    @FXML
    private TextField coefFil;
//    @FXML
//    private TextField deductFil;
    @FXML
    private TextField sumFil;

    @FXML
    private TextField scoreFil;



    @FXML
    public void initialize()
    {
        monthC.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("month"));
        scoreC.setCellValueFactory(new PropertyValueFactory<StudentInfo, String> ("avgScore"));
        //scholSumC.setCellValueFactory(new PropertyValueFactory<StudentInfo, String> ("scholSum"));
        nOfDaysC.setCellValueFactory(new PropertyValueFactory<StudentInfo, String> ("nOfDays"));
        CoefC.setCellValueFactory(new PropertyValueFactory<StudentInfo, String> ("coef"));
        //deductC.setCellValueFactory(new PropertyValueFactory<StudentInfo, String> ("deductions"));
        sumC.setCellValueFactory(new PropertyValueFactory<StudentInfo, String> ("scholSum"));
        choiceBoxYear.getItems().add("2020");
        choiceBoxYear.getItems().add("2019");
        choiceBoxYear.getItems().add("2018");
        choiceBoxYear.getItems().add("2017");
        choiceBoxYear.setValue("2020");
    }

    public void onLoad()
    {
        try {
            Connector con = Connector.getInstance();
            con.toServer.println("loadStudent");
            System.out.println("Выбран год - " + (String) choiceBoxYear.getSelectionModel().selectedItemProperty().get());
            con.toServer.println((String) choiceBoxYear.getSelectionModel().selectedItemProperty().get());
            labelFIO.setText(con.fromServer.next());
            labelFacultyName.setText(con.fromServer.next());
            labelSpeciality.setText(con.fromServer.next());
            ArrayList<StudentInfo> al = new ArrayList<>();
            String temp;
            System.out.println("Гружу список");
            temp = con.fromServer.next();
            System.out.println(temp);
            while (!temp.equals("end")) {
                StudentInfo x = StudentInfo.parse(temp);
                al.add(x);

                temp = con.fromServer.next();
                System.out.println(temp);
            }
            ObservableList<StudentInfo> oListScholarship = FXCollections.observableArrayList(al);
            tableViewMain.getItems().setAll(oListScholarship);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void heh(ActionEvent event)
    {
        System.out.println("Загрузил таблички");
        onLoad();
    }
    @FXML
    private void exitButtonAction(ActionEvent event) {
        try {
            ScreenController sc = ScreenController.getInstance();
            Connector con = Connector.getInstance();
            con.commitSuicide();
            sc.activate("loginView");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void updateButtonAction(ActionEvent event) {
        try {
            onLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void saveToFileButtonAction(ActionEvent event) {
        var x = tableViewMain.getItems();
        try {
            File myObj = new File(labelFIO.getText().strip()+'_'+(String) choiceBoxYear.getSelectionModel().selectedItemProperty().get()+".txt");
            if (myObj.createNewFile()) {
                System.out.println("Файл создан: " + myObj.getName());
            } else {
                System.out.println("Файл уже существует");
            }
            FileWriter myWriter = new FileWriter(myObj);
            for (int i = 0;i<x.size();i++)
            {
                System.out.println("Пишу "+x.get(i).toString() );
                myWriter.write(x.get(i).toString()+"\n");
            }
            myWriter.flush();
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
