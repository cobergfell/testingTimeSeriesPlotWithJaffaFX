package sample;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.FileNotFoundException;
import java.time.*;

public class TimeSeries
{
    String fileName = new String();
    private File filePath = new File(fileName);
    private Scanner scanner;
    private String line;
    private String piezometerName = "default";
    private double X = 0.0;
    private double Y = 0.0;
    private double Z = 0.0;
    private int screenNumber = 1;
    private ArrayList<GwLevel> timeSeriesArrayList = new ArrayList<GwLevel>();
    LocalDateTime timeOrigin01Jan1900 = LocalDateTime.of(1900, 1, 1, 0, 0, 0);

    // Constructor
    public TimeSeries(String fileName)
    {
        this.fileName = fileName;
        File filePath = new File(fileName);
        this.filePath = filePath;
        this.piezometerName = piezometerName;
        this.X = 0.0;
        this.Y = 0.0;
        this.Z = 0.0;
        this.timeSeriesArrayList = readAsciiText();
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double z) {
        Z = z;
    }

    public File getFilePath() {
        return filePath;
    }

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }

    public String getPiezometerName() {
        return piezometerName;
    }

    public void setPiezometerName(String piezometerName) {
        this.piezometerName = piezometerName;
    }


    public ArrayList<GwLevel> getTimeSeriesArrayList() {
        return timeSeriesArrayList;
    }

    public ArrayList<GwLevel> readAsciiText()
    {

        try
        {
            scanner = new Scanner(filePath);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        boolean hasNextLine = false;
        int counter = -1;
        while (hasNextLine = scanner.hasNextLine()) {
            counter++;
            line = scanner.nextLine();
            System.out.println(line);

            if (counter == 0)
            {
                System.out.println("Header:" + line);
            }
            else if (counter == 1)
            {
                String[] metadataStr = line.split(",");
                try
                {
                    this.piezometerName = metadataStr[0];
                    this.screenNumber = Integer.valueOf(metadataStr[1]);
                    this.X = Double.valueOf(metadataStr[2]);
                    this.Y = Double.valueOf(metadataStr[3]);
                    this.Y = Double.valueOf(metadataStr[4]);
                } catch (Exception e1)
                {
                    System.out.println("Invalid coordinates" + line);
                }
            }
            else if (counter == 2)
            {
                System.out.println(line);
            }
            else if (counter == 3)
            {
                System.out.println(line);
            }
            else {
                String[] recordStrList = line.split(",");
                String dateTimeStr = recordStrList[0];
                String valueStr = recordStrList[1];
                Float level = Float.valueOf(valueStr);
                String[] dateTimeStrList = dateTimeStr.split(" ");
                String dateStr = dateTimeStrList[0];
                String[] dateStrList = new String[3];
                dateStrList[0] = dateStr.split("-")[0];
                dateStrList[1] = dateStr.split("-")[1];
                dateStrList[2] = dateStr.split("-")[2];

                String timeStr = dateTimeStrList[1];
                String[] timeStrList = new String[2];
                timeStrList[0] = timeStr.split(":")[0];
                timeStrList[1] = timeStr.split(":")[1];


                // create an object of Calendar class.
                Calendar calendar = Calendar.getInstance();
                // Set year, month and date
                int year = Integer.parseInt(dateStrList[2]);
                int month = Integer.parseInt(dateStrList[1]);
                int day = Integer.parseInt(dateStrList[0]);
                int hour = Integer.parseInt(timeStrList[0]);
                int minute = Integer.parseInt(timeStrList[1]);
                int second = 0;

                LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
                long secondsFromBeginTime = timeOrigin01Jan1900.until( localDateTime, ChronoUnit.SECONDS );
                System.out.println("secondsFromBeginTime"+secondsFromBeginTime);


/*                calendar.set(Calendar.YEAR, yearNumb);
                calendar.set(Calendar.MONTH, monthNumb);
                calendar.set(Calendar.DATE, dayNumb);
                calendar.set(Calendar.HOUR, hourNumb);
                calendar.set(Calendar.MINUTE, minuteNumb);

                // util date object
                java.util.Date dt = calendar.getTime();//getTime method of Calendar class  returns a date
                long timeNumLong = dt.getTime();//getTime method of date class returns a long integer*/

                System.out.println("localDateTime: " +localDateTime);
                System.out.println("secondsFromBeginTime: " + secondsFromBeginTime);
                GwLevel levelEvent = new GwLevel(secondsFromBeginTime,localDateTime,level);
                this.timeSeriesArrayList.add(levelEvent);
            }

        }
        scanner.close();
        return timeSeriesArrayList;
    }

}

    // creating a Calendar object
    //https://www.tutorialspoint.com/create-a-date-object-using-the-calendar-class-in-java
/*import java.util.Calendar;
    public class Demo {
        public static void main(String[] args) {
            Calendar calendar = Calendar.getInstance();
            // Set year, month and date
            calendar.set(Calendar.YEAR, 2018);
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DATE, 18);
            // util date object
            java.util.Date dt = calendar.getTime();
            System.out.println("Date: "+dt);
        }
    }*/


