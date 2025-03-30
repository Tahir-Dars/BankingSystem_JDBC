package BankingSystemJDBC;

import java.security.SecureRandom;
import java.sql.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class accounts {
    accounts(Connection connection, Scanner scanner) {
        System.out.println("Welcome to the MST4 Baking Services");
        while (true) {
            System.out.println("Choose any of the following\n1.Open Account\n2.Get Account Number\n3.See if account exists\n4.Exit the System");
            int choice4 = scanner.nextInt();
            switch (choice4) {
                case 1:
                    bankingApp.wait3();
                    openAccount(connection, scanner);
                    bankingApp.wait3();
                    break;
                case 2:
                    bankingApp.wait3();
                    getNumber(connection, scanner);
                    bankingApp.wait3();
                    break;
                case 3:
                    bankingApp.wait3();
                    acountexis(connection, scanner);
                    bankingApp.wait3();
                    break;
                case 4:
                    System.out.println();
                    bankingApp.wait3();
                    return;
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        }
    }
    public static void openAccount(Connection connection,Scanner scanner){
        scanner.nextLine();
        String query="INSERT INTO account (email_id, accountNumber, balance, pin) VALUES (?,?,?,?)";
        System.out.println("Enter the emailID: ");
        String emailID = scanner.nextLine();

        long accountNumber=generateUnique19DigitNumber(connection);

        System.out.println("Your account number is : "+accountNumber);

        long balance=0;
        System.out.println("Your balance is: "+balance);
        System.out.print("Enter a 5 digit pin: ");
        int pin=scanner.nextInt();
        scanner.nextLine();
        while (length(pin)!=5){
            System.out.println("Enter a 5 digit pin: ");
             pin=scanner.nextInt();
        }
        try {
            PreparedStatement preparedStatement =  connection.prepareStatement(query);
            preparedStatement.setString(1,emailID);
            preparedStatement.setLong(2,accountNumber);
            preparedStatement.setLong(3, balance);
            preparedStatement.setInt(4,pin);
           int rowsAffected= preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("Account has opened ! ");
            }
            accountManager accountManager1=new accountManager(connection,scanner);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void getNumber(Connection connection,Scanner scanner){
        scanner.nextLine();
        System.out.print("Enter the email ID: ");
        String emailID= scanner.nextLine();
        System.out.print("Enter the 5 digit pin: ");
        int pin= scanner.nextInt();
        if (length(pin)!=5){
            System.out.print("Enter 5 digit pin:");
            pin= scanner.nextInt();
        }
        long getnumber=0;
        String query="SELECT email_id, pin, accountNumber FROM account ";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                if (emailID.equals(resultSet.getString("email_id")) & pin==resultSet.getInt("pin")){
                    getnumber=resultSet.getLong("accountNumber");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Account Number: "+getnumber);
    }
    public static void acountexis(Connection connection,Scanner scanner){
        scanner.nextLine();
        System.out.print("Enter the email ID: ");
        String emailID= scanner.nextLine();
        System.out.print("Enter the 5 digit pin: ");
        int pin= scanner.nextInt();
        if (length(pin)!=5){
            System.out.print("Enter 5 digit pin:");
            pin= scanner.nextInt();
        }
        long getnumber=0;
        String query="SELECT email_id, pin FROM account ";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                if (emailID.equals(resultSet.getString("email_id")) & pin==resultSet.getInt("pin")){
                    System.out.println("Account Exists ! ");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static int length(int pin){
        int i=0;
        while (pin!=0){
             pin=pin/10;
             i++;
        }
        return i;
    }
    public static long generateUnique19DigitNumber(Connection connection) {
        String query="SELECT accountNumber FROM account";
        Set<Long> generatedNumbers = new HashSet<>();
        try {
            Statement statement= connection.createStatement();
            ResultSet resSet=statement.executeQuery(query);
            while(resSet.next()){
                generatedNumbers.add(resSet.getLong("accountNumber"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SecureRandom random = new SecureRandom();
        long number;
        do {
            number = 1_000_000_000_000_000_000L + (Math.abs(random.nextLong()) % 9_000_000_000_000_000_000L);
        } while (!generatedNumbers.add(number));
        return number;
    }
    public static String emailReturn(Scanner scanner){
        System.out.println("Enter the emailID: ");
        String emailID = scanner.nextLine();
        return emailID;
    }
}
