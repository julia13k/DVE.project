package mylogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyLogger {
    private LoggerType level;
    private Exception e;
    private String message;
    private List<String> loggerMessagesArray = new ArrayList<>();

    public List<String> getLoggerMessagesArray() {
        return loggerMessagesArray;
    }

    public void setLoggerMessagesArray(List<String> loggerMessagesArray) {
        this.loggerMessagesArray = loggerMessagesArray;
    }

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

    public void log(LoggerType level, Exception e, String message) {
        if (level == LoggerType.ERROR) {
            System.out.println("[{" + level + "}] {" + e.getClass() + "}: {" + message + "}" + '\n' + "{" + e.getStackTrace() + "}");
        }
        addLoggerMessage(message);
    }

    public void log(LoggerType level, Object o, String message) {
        String className = o.getClass().getSimpleName();
        System.out.println("[{" + level + "}] {" + className + "}: {" + message + "}");
        addLoggerMessage(message);
    }

    public void addLoggerMessage(String message) {
        loggerMessagesArray.add(message);
    }

    public void getAllLogs() {
        for (int i = 0; i < loggerMessagesArray.size(); i++){
            System.out.println(loggerMessagesArray.get(i));
        }
    }
}
