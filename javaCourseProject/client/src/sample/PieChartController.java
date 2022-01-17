package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

public class PieChartController
{
    @FXML
    private PieChart diagram;

    @FXML
    void initialize()
    {
        int less100 = 4;
        int bigger100 = 2;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Меньше 100р", less100),
                new PieChart.Data("Больше 100р", bigger100)
        );

        diagram.setData(pieChartData);
    }
}
