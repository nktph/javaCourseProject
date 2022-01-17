package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.HRInfo;

import java.io.IOException;
import java.util.ArrayList;

public class HRView {

    @FXML
    private TableColumn<HRInfo, String> colFac;

    @FXML
    private TableColumn<HRInfo, String> colId;

    @FXML
    private TableColumn<HRInfo, String> colName;

    @FXML
    private TableColumn<HRInfo, String> colSpeciality;

    @FXML
    private ComboBox<String> editFac;

    @FXML
    private TextField editName;

    @FXML
    private ComboBox<String> editSpec;

    @FXML
    private ComboBox<String> newFac;

    @FXML
    private TextField newName;

    @FXML
    private ComboBox<String> newSpec;

    @FXML
    private ComboBox<String> searchFac;

    @FXML
    private TextField searchName;

    @FXML
    private ComboBox<String> searchSpec;

    @FXML
    private TableView<HRInfo> tableViewEmp;
    @FXML
    public void initialize()
    {
        colId.setCellValueFactory(new PropertyValueFactory<HRInfo,String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<HRInfo,String>("name"));
        colFac.setCellValueFactory(new PropertyValueFactory<HRInfo,String>("faculty"));
        colSpeciality.setCellValueFactory(new PropertyValueFactory<HRInfo,String>("speciality"));
    }

    @FXML
    public void updateFacSpec() throws IOException {
        Connector con = Connector.getInstance();
        con.toServer.println("updateFacSpec");
        String facUnCut =con.fromServer.next().strip();
        System.out.println(facUnCut);
        String[] facList =facUnCut.split("\\.");
        String specUnCut =con.fromServer.next().strip();
        System.out.println(specUnCut);
        String[] specList =specUnCut.split("\\.");

        newFac.getItems().clear();
        editFac.getItems().clear();
        //searchFac.getItems().clear();
        newSpec.getItems().clear();
        editSpec.getItems().clear();
        //searchSpec.getItems().clear();
        fillDropDown(facList, newFac, editFac);
        fillDropDown(specList, newSpec, editSpec);
    }

    private void fillDropDown(String[] facList, ComboBox<String> newFac, ComboBox<String> editFac, ComboBox<String> searchFac) {
        for (int i=0; i<facList.length; i++)
        {
            System.out.println(facList[i]);
            newFac.getItems().add(facList[i]);
            editFac.getItems().add(facList[i]);
            searchFac.getItems().add(facList[i]);
        }
    }
    private void fillDropDown(String[] facList, ComboBox<String> newFac, ComboBox<String> editFac) {
        for (int i=0; i<facList.length; i++)
        {
            System.out.println(facList[i]);
            newFac.getItems().add(facList[i]);
            editFac.getItems().add(facList[i]);
        }
    }

    @FXML
    void delPress(ActionEvent event) throws IOException {
        Connector con = Connector.getInstance();
        if (tableViewEmp.getSelectionModel().getSelectedItem() == null)
            return;
        con.toServer.println("delHR");
        con.toServer.println(tableViewEmp.getSelectionModel().getSelectedItem().getId());
    }

    @FXML
    void editPress(ActionEvent event) throws IOException {
        Connector con = Connector.getInstance();
        if ((tableViewEmp.getSelectionModel().getSelectedItem() == null) )
            return;
        if (editName.getText().isEmpty() || editFac.getSelectionModel().getSelectedItem().isEmpty() || editSpec.getSelectionModel().getSelectedItem().isEmpty())
        {
            return;
        }
        con.toServer.println("editHR");
        con.toServer.println(tableViewEmp.getSelectionModel().getSelectedItem().getId());
        con.toServer.println(editName.getText().replace(' ','_'));
        con.toServer.println(editFac.getSelectionModel().getSelectedItem());
        con.toServer.println(editSpec.getSelectionModel().getSelectedItem());
    }

    @FXML
    void GoBack(ActionEvent event) {
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
    void newPress(ActionEvent event) throws IOException {
        Connector con = Connector.getInstance();
        if (newName.getText().isEmpty() || newFac.getSelectionModel().getSelectedItem().isEmpty() || newSpec.getSelectionModel().getSelectedItem().isEmpty())
        {
            return;
        }
        con.toServer.println("insertHR");
        con.toServer.println(newName.getText().replace(' ','_'));
        con.toServer.println(newFac.getSelectionModel().getSelectedItem());
        con.toServer.println(newSpec.getSelectionModel().getSelectedItem());
    }


    @FXML
    void updateTable(ActionEvent event) {
        try {
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTable() throws IOException {
        loadTable("");
    }

    private void loadTable(String str) throws IOException {
        Connector con = Connector.getInstance();
        con.toServer.println("loadHR");
        ArrayList<HRInfo> al = new ArrayList<>();
        String temp;
        System.out.println("Гружу список");
        temp = con.fromServer.next().strip().replace('_', ' ');
        System.out.println(temp);
        while (!temp.equals("end")) {
            HRInfo x = HRInfo.parse(temp);
            if (str.isEmpty() || !str.equals(x.getFaculty()))
             al.add(x);
            temp = con.fromServer.next().strip().replace('_', ' ');
            System.out.println(temp);
        }
        ObservableList<HRInfo> oListScholarship = FXCollections.observableArrayList(al);
        tableViewEmp.getItems().setAll(oListScholarship);
    }

}
