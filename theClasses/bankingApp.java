package BankingSystemJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class bankingApp {
    public static void wait3(){
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.print(".");
            }
            System.out.println();
        }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String url="jdbc:mysql://localhost:3306/bankingSystem";
        String root="root";
       //
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully !");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Connection connection= DriverManager.getConnection(url,root,password);
            System.out.println("Connection Built Successfully !");
         //   user user1=new user(connection,scanner);
            while(true) {
                System.out.println("Choose any of them ...!!!\n1.Accounts opening and details \n2.Accounts Management \n3.Back bankingHomepage\n4.Exit Program");
                int choice3 = scanner.nextInt();
                switch (choice3) {
                    case 1:
                        wait3();
                        accounts account = new accounts(connection, scanner);

                        break;
                    case 2:
                        wait3();
                        accountManager accountManager1 = new accountManager(connection, scanner);

                        break;
                    case 3:
                        wait3();
                        user user2 = new user(connection, scanner);

                        break;
                    case 4:
                        System.out.println("Program Exists");
                        wait3();
                        return;
                    default:
                        System.out.println("Unexpected Value");
                        wait3();
                        break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
