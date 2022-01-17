package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.FacultyInfo;
import models.UserInfo;

import java.io.IOException;
import java.util.ArrayList;

public class AdminView {


    @FXML
    private Label editLogin;

    @FXML
    private TextField editName;

    @FXML
    private Label editNameErr;

    @FXML
    private CheckBox editUserBlock;

    @FXML
    private Label editUserErr;

    @FXML
    private TextField editUserPass;

    @FXML
    private ComboBox<String> editUserRole;

    @FXML
    private TextField newName;

    @FXML
    private Label newNameErr;

    @FXML
    private TextField searchName;

    @FXML
    private Label searchNameErr;

    @FXML
    private CheckBox searchUserBlock;

    @FXML
    private TextField searchUserLogin;

    @FXML
    private ComboBox<String> searchUserRole;
    @FXML
    private TableView<FacultyInfo> tableViewFaculty;
    @FXML
    private TableColumn<FacultyInfo, String> colFacId;

    @FXML
    private TableColumn<FacultyInfo, String> colFacName;
    @FXML
    private TableView<UserInfo> tableViewUser;
    @FXML
    private TableColumn<UserInfo, String> colUserBlock;

    @FXML
    private TableColumn<UserInfo, String> colUserStudId;

    @FXML
    private TableColumn<UserInfo, String> colUserStudName;

    @FXML
    private TableColumn<UserInfo, String> colUserId;

    @FXML
    private TableColumn<UserInfo, String> colUserLogin;

    @FXML
    private TableColumn<UserInfo, String> colUserRole;

    @FXML
    private Button graphics;

    @FXML
    public void initialize()
    {
        colFacId.setCellValueFactory(new PropertyValueFactory<FacultyInfo,String>("id"));
        colFacName.setCellValueFactory(new PropertyValueFactory<FacultyInfo, String>("name"));

        colUserId.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("id"));
        colUserLogin.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("login"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("role"));
        colUserBlock.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("isBlocked"));
        colUserStudId.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("studId"));
        colUserStudName.setCellValueFactory(new PropertyValueFactory<UserInfo, String>("studName"));



        editUserRole.getItems().add("Admin");
        editUserRole.getItems().add("Student");
        editUserRole.getItems().add("Accountant");
        editUserRole.getItems().add("HR");
        searchUserRole.getItems().add("Admin");
        searchUserRole.getItems().add("Student");
        searchUserRole.getItems().add("Accountant");
        searchUserRole.getItems().add("HR");

        graphics.setOnAction(actionEvent -> { openNewScene("/sample/PieChart.fxml"); });
    }

    //-----------------------UsER------------------------
    public void updateUserTable() throws IOException {
        updateUserTable("");
    }

    public void updateUserTable(String search) throws IOException {
        Connector con = Connector.getInstance();
        con.toServer.println("loadUser");
        ArrayList<UserInfo> al = new ArrayList<>();
        String temp;
        System.out.println("Гружу список");
        temp = con.fromServer.next().strip();
        System.out.println(temp);
        while (!temp.equals("end")) {
            UserInfo x = UserInfo.parse(temp);
            if (search.isEmpty() || search.equals(x.getLogin()))
                al.add(x);
            temp = con.fromServer.next().strip();
            System.out.println(temp);
        }
        ObservableList<UserInfo> oListScholarship = FXCollections.observableArrayList(al);
        tableViewUser.getItems().setAll(oListScholarship);
    }

    public void buttonSearchUserPress(ActionEvent actionEvent) throws IOException {
        if (searchUserLogin.getText().equals(null))
        {
            searchNameErr.setText("Ошибка ввода!");
            searchNameErr.setVisible(true);
            return;
        }
        searchNameErr.setVisible(false);
        updateUserTable(searchUserLogin.getText());
    }
    public void buttonUserUpdatePress(ActionEvent actionEvent) {
        try {
            updateUserTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonUserDelPress(ActionEvent actionEvent) throws IOException {
        UserInfo selected = tableViewUser.getSelectionModel().getSelectedItem();
        Connector con = Connector.getInstance();
        con.toServer.println("delUser");
        con.toServer.println(selected.getId());
    }

    public void buttonEditUserPress(ActionEvent actionEvent) throws IOException {
        UserInfo selected = tableViewUser.getSelectionModel().getSelectedItem();
        Connector con = Connector.getInstance();
        con.toServer.println("editUser");
        con.toServer.println(selected.getId());
        con.toServer.println(editUserPass.getText().strip());
        con.toServer.println(editUserRole.getSelectionModel().getSelectedItem().strip());
        con.toServer.println(editUserBlock.isSelected());
    }
    //----------------------------------------------------
    //----------------------------------------FACULTY---------------------
    public void updateFacultyTable() {
        updateFacultyTable("");
    }

    public void updateFacultyTable(String filtr)
    {
        try {
            Connector con = Connector.getInstance();
            con.toServer.println("loadFaculty");
            ArrayList<FacultyInfo> al = new ArrayList<>();
            String temp;
            System.out.println("Гружу список");
            temp = con.fromServer.next();
            System.out.println(temp);
            while (!temp.equals("end")) {
                FacultyInfo x = FacultyInfo.parse(temp);
                if(x.getName().equals(filtr) || filtr.equals(""))
                al.add(x);
                temp = con.fromServer.next();
                System.out.println(temp);
            }
            ObservableList<FacultyInfo> oListScholarship = FXCollections.observableArrayList(al);
            tableViewFaculty.getItems().setAll(oListScholarship);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void buttonUpdateFacPress(ActionEvent actionEvent) {
        try {
            updateFacultyTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonNewFacPress(ActionEvent actionEvent) throws IOException {
        if (newName.getText().equals(""))
        {
            newNameErr.setText("Поле пустое!");
            newNameErr.setVisible(true);
            return;
        }
        newNameErr.setVisible(false);
        Connector con = Connector.getInstance();
        con.toServer.println("insertFaculty");
        con.toServer.println(newName.getText());
    }
    @FXML
    public void buttonSearchFacPress(ActionEvent actionEvent) throws IOException {
        if (searchName.getText().equals(""))
        {
            searchNameErr.setText("Поле пустое!");
            searchNameErr.setVisible(true);
            return;
        }
        searchNameErr.setVisible(false);
        updateFacultyTable(searchName.getText());
    }
    @FXML
    public void buttonDelFacPress(ActionEvent actionEvent) throws IOException {
        FacultyInfo x = tableViewFaculty.getSelectionModel().getSelectedItem();
        System.out.println(x.getName());
        if (x.getName().equals(""))
        {
            return;
        }
        Connector connector = Connector.getInstance();
        connector.toServer.println("delFaculty");
        connector.toServer.println(x.getName());
    }
    @FXML
    public void mouseTableFaculty(ActionEvent actionEvent)
    {
        FacultyInfo x = tableViewFaculty.getSelectionModel().getSelectedItem();
        System.out.println(x.getName());
        if (x.getName().equals(""))
        {
            return;
        }
        editName.setText(x.getName());
    }
    @FXML
    public void buttonEditFacPress(ActionEvent actionEvent) throws IOException {
        FacultyInfo x = tableViewFaculty.getSelectionModel().getSelectedItem();
        System.out.println(x.getName());
        if (x.getName().equals(""))
        {
            return;
        }
        if (editName.getText().equals(""))
        {
            editNameErr.setText("Поле пустое!");
            editNameErr.setVisible(true);
            return;
        }
        editNameErr.setVisible(false);
        Connector con = Connector.getInstance();
        con.toServer.println("editFaculty");
        con.toServer.println(x.getName());
        con.toServer.println(editName.getText());

    }

    public void buttonExitPress(ActionEvent actionEvent) {
        try {
            ScreenController sc = ScreenController.getInstance();
            Connector con = Connector.getInstance();
            con.commitSuicide();
            sc.activate("loginView");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openNewScene(String window)
    {
        graphics.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try { loader.load(); }
        catch (IOException e) { e.printStackTrace(); }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
