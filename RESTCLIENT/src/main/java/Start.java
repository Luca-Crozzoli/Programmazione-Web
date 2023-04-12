import java.util.Scanner;

public class Start {
    public static void main(String[] args) {


        Scanner keyboardStart = new Scanner(System.in);
        Interface graphicInterface = new Interface();
        String loginResponse = graphicInterface.Login(keyboardStart);

        if (loginResponse != null) {
            String choice = "0";

            while (choice != "1" || choice!= "2") {
                System.out.println("Choose what you want to do");
                System.out.println("--1-- to upload a file");
                System.out.println("--2-- to close all the program");
                choice = keyboardStart.nextLine();

                switch (choice) {
                    case "1":
                        graphicInterface.upload(loginResponse, keyboardStart);
                        break;
                    case "2":
                        System.out.println("Program Terminated");
                        System.exit(0);
                        break;
                }
            }
        }
    }
}
