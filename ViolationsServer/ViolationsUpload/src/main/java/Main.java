import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import violations.upload.FTPUploader;
import violations.utils.ViolationEvent;
import violations.utils.Violations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String[] connectionData;
        try {
            connectionData = getConnectionData();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                // get violations
                Violations violations = ViolationEvent.getViolations();
                while (violations == null)
                    violations = ViolationEvent.getViolations();
                // violations to JSON String
                Gson gson = new GsonBuilder().create();
                String str = gson.toJson(violations);
                //System.out.println(str);
                String fileName = "violations.json";
                // open or make violations.json file
                File file = new File(fileName);
                // write violations JSON String in violations.json file
                try (PrintWriter printWriter = new PrintWriter(file)) {
                    printWriter.print(str);
                    // get violations.json file InputStream
                    //InputStream jsonInStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                FTPUploader ftpUploader;
                try {
                    ftpUploader = new FTPUploader(connectionData[0], connectionData[1], connectionData[2]);
                    ftpUploader.uploadFile(fileName, fileName, "/httpdocs/");
                    ftpUploader.disconnect();
                    System.out.println("File uploaded: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // schedule the task to run starting now and then every hour...
        timer.schedule (hourlyTask, 0, 1000*60*10);
    }

    @NotNull
    @Contract(" -> new")
    private static String[] getConnectionData() throws Exception {
        Pattern pattern;
        Matcher matcher;
        final String IPADDRESS_PATTERN =
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IPADDRESS_PATTERN);

        Scanner sc = new Scanner(System.in);
        System.out.print("FTP Server\nset Host: ");
        String host = sc.nextLine();
        matcher = pattern.matcher(host);
        if (!matcher.matches()) throw new Exception("wrong IP syntax");
        System.out.print("set User: ");
        String user = sc.nextLine();
        System.out.print("set Password: ");
        String password = sc.nextLine();
        return new String[]{host, user, password};
    }
}
