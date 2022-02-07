package com.geekhub.mylogger;

import com.geekhub.annotation.Injectible;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class MyLogger {
    private LoggerType level;
    private Exception e;
    private String message;
    private File file;
    private ZonedDateTime zonedDateTime;
    private List<String> loggerMessagesArray = new ArrayList<>();
    private List<MyLogger> logs = new ArrayList<>();
    @Injectible
    private String storageType;

    public MyLogger(){}

    public MyLogger(LoggerType level, String message, ZonedDateTime zonedDateTime) {
        this.level = level;
        this.message = message;
        this.zonedDateTime = zonedDateTime;
    }

    public List<String> getLoggerMessagesArray() {
        return loggerMessagesArray;
    }

    public void setLoggerMessagesArray(List<String> loggerMessagesArray) {this.loggerMessagesArray = loggerMessagesArray;}

    public LoggerType getLevel() {
        return level;
    }

    public void setLevel(LoggerType level) {
        this.level = level;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {this.e = e; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {this.message = message;}

    public ZonedDateTime getZonedDateTime() {return zonedDateTime; }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {this.zonedDateTime = zonedDateTime;}

    public List<MyLogger> getLogs() {return logs;}

    public void setLogs(List<MyLogger> logs) {this.logs = logs;}

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /** Gets log`s creation time */
    public String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss:SS");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+2"));
        ZonedDateTime zonedDateTime = ZonedDateTime.of(now, ZoneId.of("UTC+2"));
        return zonedDateTime.format(formatter);
    }
    /** Logs an exception */
    public void log(LoggerType level, Exception e, String message) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(getDateTime());
        if (level == LoggerType.ERROR) {
            String s = "[{" + level + "}] {" + e.getClass() + "}: {" + message + "}" + '\n' + "{" + e.getStackTrace() + "}: {" + zonedDateTime + "}";
            MyLogger log = new MyLogger(level, s, zonedDateTime);
            if(getStorageType().equals("file")) {
                writeToFile(file, log);
            }
            else if(getStorageType().equals("memory")) {
                logs.add(log);
            }
            else if(getStorageType().equals("both")) {
                writeToFile(file, log);
                logs.add(log);
            }
        }
    }
    /** Logs a message */
    public void log(LoggerType level, Object o, String message) {
        String className = o.getClass().getSimpleName();
        String s = "[{" + level + "}] {" + className + "}: {" + message + "}: {" + getDateTime() + "}";
        MyLogger log = new MyLogger(level, s, zonedDateTime);
        writeToFile(file, log);
    }
    /** Writes a log to file */
    public void writeToFile(File file, MyLogger log){
        if(isNull(file) || !file.exists()) {createFile();}
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.valueOf(log.level) + " " + log.message + " " + log.zonedDateTime);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /** Reads a log(logs) from a file */
    public List<MyLogger> readFromFile(File file){
        List<MyLogger> logsFromFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file));){
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] strings = line.split(" ");
                LoggerType type = LoggerType.valueOf(strings[0]);
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(strings[2]);
                MyLogger log = new MyLogger(type, strings[1], zonedDateTime);
                logsFromFile.add(log);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return logsFromFile;
    }
    /** Creates a file for logs */
    public File createFile(){
        file = new File("src/main/java/com.geekhub/myLogger/logs.txt");
        return file;
    }
    /** Sorts logs from a file by their creation date */
    public void sortLogsByDate() {
        if(getStorageType().equals("file")) {
            readFromFile(file).stream().sorted(Comparator.comparing(MyLogger::getZonedDateTime)).collect(Collectors.toList());
        }
        else if(getStorageType().equals("memory")) {
            logs.stream().sorted(Comparator.comparing(MyLogger::getZonedDateTime)).collect(Collectors.toList());
        }
        else if(getStorageType().equals("both")) {
            logs.removeAll(readFromFile(file));
            logs.stream().sorted(Comparator.comparing(MyLogger::getZonedDateTime)).collect(Collectors.toList());
        }
    }
    /** Sorts logs from a file by their status */
    public void selectLogsByStatus() {
        if(getStorageType().equals("file")) {
            readFromFile(file).stream().sorted(Comparator.comparing(MyLogger::getLevel)).collect(Collectors.toList());
        }
        else if(getStorageType().equals("memory")) {
            logs.stream().sorted(Comparator.comparing(MyLogger::getLevel)).collect(Collectors.toList());
        }
        else if(getStorageType().equals("both")) {
            logs.removeAll(readFromFile(file));
            logs.stream().sorted(Comparator.comparing(MyLogger::getLevel)).collect(Collectors.toList());
        }
    }

    /** Get to notice about where the logs will be save */
    public String getStorageType() {
        Field[] fields = MyLogger.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if(fields[i].isAnnotationPresent(Injectible.class)) {
                FileInputStream fileInputStream;
                Properties property = new Properties();
                try {
                    fileInputStream = new FileInputStream("Homework/domain/src/main/resources/application.properties");
                    property.load(fileInputStream);
                    storageType = property.getProperty("logger.storage.type");
                } catch (FileNotFoundException ex) {
                    System.err.println("There is no file!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }else {
                System.out.println("You should mark a field with @Injectible annotation");
            }

        }
        return storageType;
    }
}
