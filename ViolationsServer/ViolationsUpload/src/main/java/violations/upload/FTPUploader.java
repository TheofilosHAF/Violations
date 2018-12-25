package violations.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import violations.utils.ViolationEvent;
import violations.utils.Violations;

public class FTPUploader {

    FTPClient ftp = null;

    public FTPUploader(String host, String user, String pwd) throws Exception{
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter("upload.log")));
        int reply;
        ftp.connect(host);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(user, pwd);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }
    public void uploadFile(String localFileFullName, String fileName, String hostDir)
            throws Exception {
        try(InputStream input = new FileInputStream(new File(localFileFullName))){
            this.ftp.storeFile(hostDir + fileName, input);
        }
    }

    public void disconnect(){
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }

    public static void writeViolationsToFile(String fileName) {
        Violations violations = ViolationEvent.getViolations();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = gson.toJson(violations);
        Path path = Paths.get(fileName);
        try {
            Files.write(path, str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
