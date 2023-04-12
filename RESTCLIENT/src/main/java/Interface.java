import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Interface {
    public Interface() {
    }

    public static String Login(Scanner keyboard) {
        String username = "";
        String password = "";
        AtomicBoolean proceed = new AtomicBoolean(true);

        while (proceed.get()) {
            System.out.println("---Login as an uploader---");

            System.out.println("Enter username");
            username = keyboard.nextLine();

            System.out.println("Enter password");
            password = keyboard.nextLine();

            System.out.println("You insert username: " + username + " password: " + password);
            System.out.println(" \n" +
                    "Do you want to proceed? y or n");
            String input = keyboard.nextLine();

            if (input.equals("y")) {
                System.out.println("Login confirmed waiting to server response ...");
                proceed.set(false);
            } else {
                System.out.println("Insert again");
            }
        }

        return ClientRest.LoginRest(username, password);
    }

    public static void upload(String loginResponse, Scanner keyboard) {
        String usernameConsumer = "";
        String nameConsumer = "";
        String emailConsumer = "";
        AtomicBoolean proceed = new AtomicBoolean(true);

        while (proceed.get()) {
            System.out.println("---Upload file---");
            System.out.println("Enter the consumer's details");


            System.out.println("Username consumer ");
            usernameConsumer = keyboard.nextLine();

            System.out.println("Name consumer");
            nameConsumer = keyboard.nextLine();

            System.out.println("Email consumer");
            emailConsumer = keyboard.nextLine();

            System.out.println("You insert the consumer"
                    + "\n username:" + usernameConsumer + "\n name: " + nameConsumer + "\n email: " + emailConsumer);
            System.out.println(" \n" +
                    "Do you want to proceed? y or n");
            String input = keyboard.nextLine();

            if (input.equals("y")) {
                System.out.println("You confirmed the user");
                proceed.set(false);
            } else {
                System.out.println("You don't have confirmed the user");
                proceed.set(true);
            }
        }

        proceed.set(true);

        String path = "";
        String fileName = "";
        String file = "";
        String hashtag = "";

        while (proceed.get()) {
            System.out.println("Insert path file (remember to add the file extension at the end!!): ");
            path = keyboard.nextLine();
            file = ClientRest.encodeFileToBase64BinaryRest(path);
            System.out.println("Insert name of the file: ");
            fileName = keyboard.nextLine();
            System.out.println("Insert hashtag: ");
            hashtag = keyboard.nextLine();

            System.out.println("You have inserted the file with path: " + path + " Name: " + fileName + "Hashtag: " + hashtag);
            System.out.println(" \n" +
                    "Do you want to proceed? y or n");
            String input = keyboard.nextLine();
            if (input.equals("y")) {
                System.out.println("You confirmed the document");
                proceed.set(false);
            } else {
                System.out.println("Reinsert the data for the file");
            }
        }
        ClientRest.UploadRest(usernameConsumer, nameConsumer, emailConsumer, file, fileName, hashtag, loginResponse);

    }
}
