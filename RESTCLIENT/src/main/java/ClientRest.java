import Entities.Account;
import Entities.Fileup;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ClientRest {
    //https://www.baeldung.com/jersey-jax-rs-client
    //https://docs.oracle.com/javaee/7/tutorial/jaxrs-client001.htm
    //https://docs.oracle.com/javaee/7/api/javax/ws/rs/client/SyncInvoker.html#post-javax.ws.rs.client.Entity-
    //https://docs.oracle.com/javaee/7/api/javax/ws/rs/client/Entity.html#json-T-
    static Client client = ClientBuilder.newClient();
    static WebTarget webTarget = client.target("https://programmazioneweb2020-319008.oa.r.appspot.com/api");

    public static String LoginRest(String username, String password) {
        try {
            Account account = new Account(username, password);
            WebTarget loginWebTarget = webTarget.path("/login");
            Invocation.Builder invocationBuilder = loginWebTarget.request(MediaType.APPLICATION_JSON_TYPE);
            String response = invocationBuilder.post(Entity.json(account), String.class);
            if (response == null) {
                throw new Exception(response);
            }
            return response;
        } catch (Exception exception) {
            System.out.println("Login Error: " + exception.getMessage());
            return null;
        }

    }

    public static void UploadRest(String usernameCons, String nameCons, String emailCons, String file, String name, String hashtag, String loginresponse) {

        Fileup newFile = new Fileup(usernameCons, nameCons, emailCons, file, name, hashtag);
        WebTarget uploadFileWebTarget = webTarget.path("/files/upload");
        Invocation.Builder invocationBuilder = uploadFileWebTarget.request(MediaType.APPLICATION_JSON_TYPE).header("Authorization", "Bearer " + loginresponse);
        String response = invocationBuilder.post(Entity.json(newFile), String.class);

        if (response == null) {
            System.out.println("---Fail to upload file---");
        } else {
            System.out.println("---Upload done for the file:");
            System.out.println("File Name: " + name + "\n" +
                    "Recipient------" + "\n" +
                    "Consumer username: " + usernameCons + "\n" +
                    "Consumer name: " + nameCons + "\n" +
                    "Consumer Email: " + emailCons);
        }
    }

    //https://stackoverflow.com/questions/37066216/java-encode-file-to-base64-string-to-match-with-other-encoded-string/37068149
    //https://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html#readAllBytes(java.nio.file.Path)
    //https://www.baeldung.com/java-base64-encode-and-decode
    //Codificare in base 64 è una best practice
    public static String encodeFileToBase64BinaryRest(String path) {
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String result = Base64.getEncoder().encodeToString(fileContent);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Encoding 64 Error: " + e.getMessage());
            return null;
        }

    }


}
