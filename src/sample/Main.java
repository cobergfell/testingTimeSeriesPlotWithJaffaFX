/*
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
*/

package sample;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javafx.application.Application;
import javafx.css.converter.StringConverter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.time.LocalDateTime;


public class Main extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
/*        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Path to the FXML File
        //String fxmlDocPath = "D:\\Data\\NOVI\\backend\\aSecondJavaFXtest\\src\\sample\\FxFXMLExample1.fxml";
        String fxmlDocPath = "D:\\Data\\NOVI\\backend\\aThirdJavaFXtest\\src\\sample\\FxFXMLExample3.fxml";
        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

        // Create the Pane and all Details
        VBox root = (VBox) loader.load(fxmlStream);

        // Create the Scene
        Scene scene = new Scene(root);
        // Set the Scene to the Stage
        stage.setScene(scene);
        // Set the Title to the Stage
        stage.setTitle("A simple FXML Example");
        // Display the Stage
        stage.show();*/


        String fileName = "D:\\Data\\NOVI\\backend\\aFirstJavaFXPlot\\src\\sample\\28AP0134_1.txt";

        TimeSeries ts = new TimeSeries(fileName);
        //System.out.println("time series."+ts.readAsciiText());
        ArrayList<GwLevel> timeSeriesArrayList=ts.getTimeSeriesArrayList();
        System.out.println("PiezometerName "+ts.getPiezometerName());
        System.out.println("fileName "+ts.fileName);

        // fill X and Y chart values from groundwater levels
        int Ndata=timeSeriesArrayList.size();
        long[] Xarray= new long[Ndata];
        Float[] Yarray= new Float[Ndata];
        LocalDateTime[] localDateTime= new LocalDateTime[Ndata];
        for(int i=0;i<Ndata;i++){
            Xarray[i]=timeSeriesArrayList.get(i).secondsFromBeginTime;
            Yarray[i]=timeSeriesArrayList.get(i).level;
            localDateTime[i]=timeSeriesArrayList.get(i).localDateTime;

        }
        System.out.println("Xarray.length "+Xarray.length);
        System.out.println("Ndata "+Ndata);
        long xmin=Xarray[0];
        long xmax=Xarray[Ndata-1];
        Float ymin = Collections.min(Arrays.asList(Yarray));
        Float ymax = Collections.max(Arrays.asList(Yarray));




        //Defining the x axis
        NumberAxis xAxis = new NumberAxis(xmin, xmax, (xmax-xmin)/10);
        //xAxis.setLabel("milliseconds since January 1, 1970, 00:00:00 GTM");
        //xAxis.setLabel("Seconds since January 1, 1900, 00:00:00 GTM");

/*        //example 12 from https://www.javatips.net/api/javafx.scene.chart.numberaxis
        xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {

            @Override
            public String toString(Number hr) {
                int adjusted = (hr.intValue() + 24) % 24;
                return String.format("%2d", adjusted);
            }
        });*/

        xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {

            @Override
            public String toString(Number secondsFromBeginTime) {
                LocalDateTime timeOrigin01Jan1900 = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
                LocalDateTime localDateTime = timeOrigin01Jan1900.plus(secondsFromBeginTime.longValue(), ChronoUnit.SECONDS);
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                return formatter.format(localDateTime);
            }
        });

        xAxis.setLabel("Local date");
        //xAxis.getTickLabelRotation();
        xAxis.setTickLabelRotation(25);

        //Setting title to the Stage
        stage.setTitle("Groundwater levels in piezometer "+ts.fileName);

        //Defining the y axis
        //round to closest decimal number

        double ymaxAdjusted;
        ymaxAdjusted=0.1*Math.round((ymax+0.1)*10);
        double yminAdjusted;
        yminAdjusted=0.1*Math.round((ymin-0.1)*10);
        NumberAxis yAxis = new NumberAxis   (yminAdjusted, ymaxAdjusted, 0.1*(ymaxAdjusted-yminAdjusted));

        //NumberAxis yAxis = new NumberAxis   (ymin, ymax, 0.1*(ymax-ymin));

        yAxis.setLabel("Groundwater level (m NAP)");


        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {

            @Override
            public String toString(Number level) {
                //int adjusted = (level.intValue());
                return String.format("%.2f", level);
            }
        });







        //Creating the line chart
        LineChart linechart = new LineChart(xAxis, yAxis);

        //Prepare XYChart.Series objects by setting data
        XYChart.Series series = new XYChart.Series();
        series.setName(ts.getPiezometerName());

        for(int i=0;i<Ndata;i++)
        {
            series.getData().add(new XYChart.Data(Xarray[i], Yarray[i]));
        }

        //Setting the data to Line chart
        linechart.getData().add(series);

        //Creating a Group object
        Group root = new Group(linechart);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        //Setting title to the chart
        linechart.setTitle("Observed groundwater levels in piezometer "+ts.getPiezometerName());
        String title = linechart.getTitle();
        //STIL HAVE TO FORMAT TITLE AND CENTER CHART

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();

    }
}