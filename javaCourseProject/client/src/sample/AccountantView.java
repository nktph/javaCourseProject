package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.CoefInfo;
import models.ScholarshipInfo;
import models.StudentInfo;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class AccountantView {

    @FXML
    private TableColumn<CoefInfo, String> colValueCoef;
    @FXML
    private TableColumn<CoefInfo, String> colDescrCoef;
    @FXML
    private TableColumn<CoefInfo, String> colIdCoef;
    @FXML
    private TableColumn<ScholarshipInfo, String> colCoef;

    @FXML
    private TableColumn<ScholarshipInfo, String> colDate;

//    @FXML
//    private TableColumn<ScholarshipInfo, String> colDeduct;

    @FXML
    private TableColumn<ScholarshipInfo, String> colId;

    @FXML
    private TableColumn<ScholarshipInfo, String> colNOfD;

    @FXML
    private TableColumn<ScholarshipInfo, String> colName;

//    @FXML
//    private TableColumn<ScholarshipInfo, String> colScholSum;

    @FXML
    private TableColumn<ScholarshipInfo, String> colSum;

    @FXML
    private Label errDelCoef;
    @FXML
    private Label errNotSelected;

    @FXML
    private Label errLoadCoef;
    @FXML
    private Label errEditCoef;

    @FXML
    private Label errNewCoef;

    @FXML
    private Label errNewScholarship;
    @FXML
    private Label scholErr;

    @FXML
    private Label errSearchCoef;

    @FXML
    private ComboBox<String> selStud;

    @FXML
    private TableView<ScholarshipInfo> tableView;

    @FXML
    private TableView<CoefInfo> tableViewCoef;

    @FXML
    private TextField textFieldNewValueCoef;

    @FXML
    private TextField textFieldNewCoef;

    @FXML
    private TextField textFieldEditCoef;
    @FXML
    private TextField textFieldValueCoef;
    @FXML
    private TextField monthField;

    @FXML
    private TextField textFieldNOfDays;

    @FXML
    private TextField textFieldScholSum;

    @FXML
    public void initialize()
    {
        colIdCoef.setCellValueFactory(new PropertyValueFactory<CoefInfo,String>("id"));
        colDescrCoef.setCellValueFactory(new PropertyValueFactory<CoefInfo,String>("descr"));
        colValueCoef.setCellValueFactory(new PropertyValueFactory<CoefInfo,String>("value"));
        colId.setCellValueFactory(new PropertyValueFactory<ScholarshipInfo, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<ScholarshipInfo, String>("name"));

        colDate.setCellValueFactory(new PropertyValueFactory<ScholarshipInfo, String>("date"));
        colNOfD.setCellValueFactory(new PropertyValueFactory<ScholarshipInfo, String>("nOfD"));
        colCoef.setCellValueFactory(new PropertyValueFactory<ScholarshipInfo, String>("coef"));
        //colScholSum.setCellValueFactory(new PropertyValueFactory<ScholarshipInfo, String>("scholSum"));
        colSum.setCellValueFactory(new PropertyValueFactory<ScholarshipInfo, String>("scholSum"));
    }

    @FXML
    void loadScholarship(ActionEvent event) throws IOException {
        Connector con = Connector.getInstance();
        con.toServer.println("loadScholarship");
        ArrayList<ScholarshipInfo> al = new ArrayList<>();
        String temp;
        System.out.println("Загружается список (стипендии)");
        temp = con.fromServer.next().strip();
        System.out.println(temp);
        while (!temp.equals("end")) {
            ScholarshipInfo x = ScholarshipInfo.parse(temp);
            al.add(x);
            temp = con.fromServer.next().strip();
            System.out.println(temp);
        }
        ObservableList<ScholarshipInfo> oListScholarship = FXCollections.observableArrayList(al);
        tableView.getItems().setAll(oListScholarship);
    }
    @FXML
    void newScholarship(ActionEvent event) throws IOException {
        if (selStud.getSelectionModel().getSelectedItem() == null || monthField.getText().isEmpty() || textFieldNOfDays.getText().isEmpty()|| /*textFieldDeduct.getText().isEmpty() ||*/ textFieldScholSum.getText().isEmpty())
        {
            errNewScholarship.setVisible(true);
            return;
        }
        try {
            Date date = Date.valueOf(monthField.getText());
            if (date == null)
                return;
            errNewScholarship.setVisible(false);
            Connector con = Connector.getInstance();
            con.toServer.println("insertScholarship");
            con.toServer.println(selStud.getSelectionModel().getSelectedItem());
            con.toServer.println(monthField.getText());
            con.toServer.println(textFieldNOfDays.getText());
//            con.toServer.println(textFieldDeduct.getText());
            con.toServer.println(textFieldScholSum.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void fillStudent() throws IOException {
        Connector con = Connector.getInstance();
        con.toServer.println("updateStudent");
        String facUnCut =con.fromServer.next().strip();
        System.out.println(facUnCut);
        String[] StudList =facUnCut.split("\\.");

        selStud.getItems().clear();
        for (int i=0; i<StudList.length; i++) {
            selStud.getItems().add(StudList[i]);
        }
    }
    @FXML
    void delScholarship(ActionEvent event) throws IOException {
        var seleted = tableView.getSelectionModel().getSelectedItem();
        if (seleted == null)
        {
            scholErr.setVisible(true);
            return;
        }
        scholErr.setVisible(false);
        Connector con = Connector.getInstance();
        con.toServer.println("delScholarship");
        System.out.println(seleted.getId());
        con.toServer.println(seleted.getId());
    }
    @FXML
    void goBack(ActionEvent event) throws Exception {
        ScreenController sc = ScreenController.getInstance();
        Connector con = Connector.getInstance();
        con.commitSuicide();
        sc.activate("loginView");
    }
    @FXML
    void loadCoef(ActionEvent event) throws IOException {
        var selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null)
        {
            errNotSelected.setVisible(true);
            return;
        }
        errNotSelected.setVisible(false);
        Connector con = Connector.getInstance();
        con.toServer.println("loadCoef");
        con.toServer.println(selected.getId());
        ArrayList<CoefInfo> al = new ArrayList<>();
        String temp;
        System.out.println("Гружу список");
        temp = con.fromServer.next().strip();
        System.out.println(temp);
        while (!temp.equals("end")) {
            CoefInfo x = CoefInfo.parse(temp);
            al.add(x);
            temp = con.fromServer.next().strip();
            System.out.println(temp);
        }
        ObservableList<CoefInfo> oListScholarship = FXCollections.observableArrayList(al);
        tableViewCoef.getItems().setAll(oListScholarship);
    }
    @FXML
    void newCoef(ActionEvent event) throws IOException {
        var selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null)
        {
            errNotSelected.setVisible(true);
            return;
        }
        errNotSelected.setVisible(false);
        if (textFieldNewCoef.getText().isEmpty() || textFieldNewValueCoef.getText().isEmpty() )
        {
            errNewCoef.setVisible(true);
            return;
        }
        errNewCoef.setVisible(false);
        Connector con = Connector.getInstance();
        con.toServer.println("insertCoef");
        con.toServer.println(textFieldNewCoef.getText());
        con.toServer.println(textFieldNewValueCoef.getText());
        con.toServer.println(selected.getId());
    }
    @FXML
    void editCoef(ActionEvent event) throws IOException {
        var selected = tableViewCoef.getSelectionModel().getSelectedItem();
        if (selected == null)
        {
            errNotSelected.setVisible(true);
            return;
        }
        errNotSelected.setVisible(false);
        if (textFieldEditCoef.getText().isEmpty() || textFieldValueCoef.getText().isEmpty())
        {
            errEditCoef.setVisible(true);
            return;
        }
        errEditCoef.setVisible(false);
        Connector con = Connector.getInstance();
        con.toServer.println("editCoef");
        con.toServer.println(selected.getId());
        con.toServer.println(textFieldEditCoef.getText());
        con.toServer.println(textFieldValueCoef.getText());
    }

    @FXML
    void delCoef(ActionEvent event) throws IOException {
        var selected = tableViewCoef.getSelectionModel().getSelectedItem();
        if (selected == null)
        {
            errNotSelected.setVisible(true);
            return;
        }
        errNotSelected.setVisible(false);

        Connector con = Connector.getInstance();
        con.toServer.println("delCoef");
        con.toServer.println(selected.getId());
    }

}
