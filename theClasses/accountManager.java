package BankingSystemJDBC;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class accountManager {
    accountManager(Connection connection,Scanner scanner){
        System.out.println("Welcome to the MST4 Baking Services");
        while(true){
        System.out.println("choose any of them : \n1.Credit_money\n2.Debit_Money\n3.Account-Account Transfer\n4.check_balance\n5.Exit the System.");
        int choice=scanner.nextInt();
        switch (choice){
            case 1:
                creditMoney(connection);
                break;
            case 2:
                codeForDebit(connection);
                break;
            case 3:
                transfer(connection);
                break;
            case 4:
                check_balance(connection);
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid input ");
                break;
        }
    }
    }
    public static void creditMoney(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        long tellAccNumber = getValidAccountNumber(scanner);
//        System.out.println("after scene");
        boolean accountCheck;
        if (tellAccNumber != -1) {
            accountCheck = accountExist(connection, tellAccNumber);
            if (!accountCheck) {
                System.out.println("No such account found ...!!!");
                return;
            }
        } else {
            System.out.println("Issue in your input ...!!1");
            return;
        }
        try {
            connection.setAutoCommit(false);
            String fetchBalanceQ = "SELECT balance FROM account WHERE accountNumber=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(fetchBalanceQ)) {
                preparedStatement.setLong(1, tellAccNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    long fetchBalance = -1;
                    long enteramount = -1;
                    long newBalance;
                    while (resultSet.next()) {
                        fetchBalance = resultSet.getLong("balance");
                        enteramount = getValidbalance(scanner);
                    }
                    if (enteramount==-1){
                        return;
                    }
                    newBalance = fetchBalance + enteramount;
                    String addAmountQ = "UPDATE account SET balance=? WHERE accountNumber=?";
                    try (PreparedStatement preparedStatement1 = connection.prepareStatement(addAmountQ)) {
                        preparedStatement1.setLong(1, newBalance);
                        preparedStatement1.setLong(2, tellAccNumber);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            connection.commit();
                            System.out.println("Amount added successfully....!!!!!");
                        } else {
                            connection.rollback();
                            System.out.println("Amount not added due to some issue..!!!!!");
                        }
                    } catch (SQLException e) {
                        try {
                            connection.rollback();
                            System.out.println("Error occurred. Transaction rolled back.");
                        } catch (SQLException rollbackEx) {
                            rollbackEx.printStackTrace();
                        }
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void codeForDebit(Connection connection){
        Scanner scanner=new Scanner(System.in);
        System.out.print("Enter emailID:");
        String emailID=scanner.nextLine();
        System.out.print("Enter pin:");
        String pin=scanner.nextLine();
        long existingBalance=0;
        long remainingBalance = 0;
        String balanceQuery="SELECT balance FROM account where email_id = ? AND pin = ?";
        try {
            connection.setAutoCommit(false);

            try(PreparedStatement stmt = connection.prepareStatement(balanceQuery)) {
                stmt.setString(1, emailID);
                stmt.setString(2, pin);
                try(ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()) {
                    System.out.print("Account present .....!\nEnter the balance: ");
                    long balance = scanner.nextLong();
                    scanner.nextLine();
                    existingBalance = resultSet.getLong("balance");
                    int attempts = 0;
                    while (balance > existingBalance) {
                        System.out.println("Withdrawal amount exceeds the existing amount");
                        System.out.println("Existing amount: " + existingBalance);
                        System.out.print("Enter a valid withdrawal amount:( (or 0 to cancel))");
                        balance = scanner.nextLong();
                        attempts++;
                        if (attempts >= 3 | balance==0) {
                            System.out.println("attempts chances missed or canceled. Transaction canceled.");
                            return;  // Exit function after 3 failed attempts
                        }
                    }
                    scanner.nextLine();
                    remainingBalance = existingBalance - balance;
                }}
                String remainingAmountQ = "UPDATE account SET balance = ? where email_id =? AND pin = ? ";
               try( PreparedStatement preparedStatement = connection.prepareStatement(remainingAmountQ)){
                preparedStatement.setLong(1, remainingBalance);
                preparedStatement.setString(2, emailID);
                preparedStatement.setString(3, pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    connection.commit();
                    System.out.println("Amount transferred");
                }
                else {
                    connection.rollback();
                    System.out.println("Transaction failed. Rolling back.");
                }

            }
               catch (SQLException e){
                   try {
                       connection.rollback();
                       System.out.println("Error occurred. Transaction rolled back.");
                   } catch (SQLException rollbackEx) {
                       rollbackEx.printStackTrace();
                   }
                   throw new RuntimeException(e);
               }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
                scanner.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void transfer(Connection connection){
        Scanner scanner=new Scanner(System.in);
        long accounNumber= getValidAccountNumber(scanner);
        System.out.print("Enter your pin: ");
        String pin=scanner.nextLine();
        if (pin.length()!=5){
            System.out.print("Enter 5 digit pin:");
            pin= scanner.nextLine();
        }
        String query="SELECT accountNumber, pin FROM account ";
        try {
            connection.setAutoCommit(false);
           try (Statement statement=connection.createStatement()){
            try(ResultSet resultSet=statement.executeQuery(query)){
                long accounNumber1=-1;
            while(resultSet.next()){
                 accounNumber1=resultSet.getLong("accountNumber");
                String pin1=resultSet.getString("pin");
                if (accounNumber1==accounNumber & pin.equals(pin1)){
                    System.out.println("Login Sucessfull");
                    break;
                }
            }
                System.out.print("Tranfer to bank Account : ");
                long accounNumber2= getValidAccountNumber(scanner);
                System.out.print("Enter the balance to be tranfered : ");
                long balance=scanner.nextLong();
                String account1balance="UPDATE account SET balance = balance - ? WHERE accountNumber=?";
                String account2balance="UPDATE account SET balance = balance + ? WHERE accountNumber=?";
                try(PreparedStatement preparedStatement=connection.prepareStatement(account1balance);
                    PreparedStatement preparedStatement1=connection.prepareStatement(account2balance)){
                  preparedStatement.setLong(1,balance);
                  preparedStatement.setLong(2,accounNumber);
                  preparedStatement1.setLong(1,balance);
                    preparedStatement1.setLong(2,accounNumber2);
                    int rowsAffected=preparedStatement.executeUpdate();
                    int rowsAffected1=preparedStatement1.executeUpdate();
                    if (rowsAffected>0 & rowsAffected1>0){
                        connection.commit();
                        System.out.println(balance+" Transfered successfully...!!!");
                    }
                    else {
                        try {
                            connection.rollback();
                            System.out.println("Transfer Failed ");
                        } catch (SQLException rollbackEx) {
                            rollbackEx.printStackTrace();
                        }
                    }
                }
                catch(SQLException e){
                    connection.rollback();
                }
        }
           }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void check_balance(Connection connection){
        Scanner scanner=new Scanner(System.in);
        long accounNumber= getValidAccountNumber(scanner);
        System.out.print("Enter your pin: ");
        String pin=scanner.nextLine();
        if (pin.length()!=5){
            System.out.print("Enter 5 digit pin:");
            pin= scanner.nextLine();
        }
        String checkBalanceQ="SELECT balance FROM ACCOUNT WHERE accountNumber= ? AND pin= ?";
        try(PreparedStatement preparedStatement=connection.prepareStatement(checkBalanceQ)){
            preparedStatement.setLong(1,accounNumber);
            preparedStatement.setString(2,pin);
           try(ResultSet resultSet= preparedStatement.executeQuery()){
               while(resultSet.next()){
                   System.out.println("Net Balance: "+resultSet.getLong("balance"));
               }
           }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static boolean acountexists(Scanner scanner , Connection connection ){
        System.out.print("Enter Your email ID: ");
        String emailID= scanner.nextLine();
        System.out.print("Enter the 5 digit pin of your account : ");
        String pin= scanner.nextLine();
        if (pin.length()!=5){
            System.out.print("Enter 5 digit pin:");
            pin= scanner.nextLine();
        }
        long getnumber=0;
        String query="SELECT email_id, pin FROM account ";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                if (emailID.equals(resultSet.getString("email_id")) & pin.equals(resultSet.getString("pin"))){
                    System.out.println("Account Exists ! ");
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
        public static int length(int pin){
            int i=0;
            while (pin!=0){
                pin=pin/10;
                i++;
            }
            return i;
        }
    public static long getValidAccountNumber(Scanner scanner) {
        int attempts = 0;
        long accountNumber = -1;
        while (accountNumber < 0) {
            if (attempts >= 3) {
                System.out.println("Too many failed attempts. Operation canceled.");
                return -1;
            }
            System.out.print("Enter a valid account number: ");
            if (scanner.hasNextLong()) {
                accountNumber = scanner.nextLong();
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
            }
            attempts++;
            scanner.nextLine();
        }
        return accountNumber;
    }
    public static long getValidbalance(Scanner scanner) {
        int attempts = 0;
        long enterbalance = -1;
        while (enterbalance < 0) {
            if (attempts >= 3) {
                System.out.println("Too many failed attempts. Operation canceled.");
                return -1;
            }
            System.out.print("Enter a valid balance: ");
            if (scanner.hasNextLong()) {
                enterbalance = scanner.nextLong();
                if (enterbalance == 0) {
                    System.out.println("Invalid input! Balance cannot be 0. Please enter a positive number.");
                    enterbalance = -1;
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
            }
            attempts++;
            scanner.nextLine();
        }
        return enterbalance;
    }

    public static boolean accountExist(Connection connection , long tellAccNumber){
        String searchAccountNumber="SELECT accountNumber FROM account";
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet=stmt.executeQuery(searchAccountNumber);
            while (resultSet.next()){
                if (Objects.equals(tellAccNumber, resultSet.getLong("accountNumber"))){
                    System.out.println("Account found...!");
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    }


