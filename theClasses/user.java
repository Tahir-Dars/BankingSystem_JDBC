package BankingSystemJDBC;

import java.sql.*;
import java.util.Scanner;
public class user {
    user(Connection connection,Scanner scanner){

        while(true) {
            System.out.println("Now one option please .!!!!\n1.Register as a user\n2.Login\n3.User_Exist\n4.← Back");
            int choice2 = scanner.nextInt();
            switch (choice2) {
                case 1:
                    bankingApp.wait3();
                    register(connection, scanner);
                    System.out.println("Now LogIn:");
                    login(connection, scanner);
                    bankingApp.wait3();
                    break;
                case 2:
                    bankingApp.wait3();
                    login(connection, scanner);
                    break;
                case 3:
                    bankingApp.wait3();
                    user_exist(connection, scanner);
                    break;
                default:
                    bankingApp.wait3();
                    System.out.println("Invalid input ......!!!!!!!!!");
                    bankingApp.wait3();
                    break;
            }
        }
    }
        public static void register(Connection connection,Scanner scanner){
            System.out.println("Register Now !!!!!!!!!");
            System.out.print("Enter email:");
            scanner.nextLine();
            String email = scanner.nextLine();
            System.out.print("Enter password:");
            String password1 = scanner.nextLine();
            String query1="INSERT INTO user(email_id, password) VALUES(?,?)";
            try (PreparedStatement preparedStatement=connection.prepareStatement(query1);){
                preparedStatement.setString(1,email);
                preparedStatement.setString(2,password1);
               int changes= preparedStatement.executeUpdate();
               if (changes>0){
                   System.out.println("Registered ! ");
               }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public static void login( Connection connection,Scanner scanner){
            System.out.print("Enter the emai id: ");
            scanner.nextLine();
            String emailID=scanner.nextLine();
            System.out.print("Enter the password: ");
            String password=scanner.nextLine();
            String query="SELECT email_id, password from user ";
            try(Statement statement=connection.createStatement()){
                try(ResultSet rS=statement.executeQuery(query)){
                while(rS.next()){
                    String emailID2 = rS.getString("email_id");
                    String password2=rS.getString("password");
                    if (emailID2.equals(emailID) & password2.equals(password)){
                        System.out.println("Login successfull !");
                        bankingApp.wait3();
                        break;
                    }
                }
                    System.out.println("Choose any of them: \n1.Credit Card Info\n2.Customer Loans\n3.Financial Servies to specially abled persons\n4. ← back");
                int choice5=scanner.nextInt();
                switch (choice5){
                    case 1:
                        System.out.println("-------------");
                        break;
                    case 2:
                        System.out.println("-------------");
                        break;
                    case 3:
                        System.out.println("-------------");
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid Input.....!!!!!");
                        break;
                }
                   }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public static boolean user_exist(Connection connection,Scanner scanner){
            System.out.print("Enter the EmailID: ");
            scanner.nextLine();
            String emailID=scanner.nextLine();
            String query="SELECT email_id FROM user";
            try(PreparedStatement preparedStatement=connection.prepareStatement(query)){
                try (ResultSet rS = preparedStatement.executeQuery()) {
                    while (rS.next()) if (emailID.equals(rS.getString("email_id"))) {
                        System.out.println("User Exists !!!");
                        return true;
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return false;
        }
    }

