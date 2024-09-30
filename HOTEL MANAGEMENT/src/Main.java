//import com.mysql.cj.jdbc.Driver;

//import com.mysql.cj.jdbc.Driver;

import java.sql.* ;
import java.util.Scanner;


/*
To establish a JDBC (Java Database Connectivity) connection in Java, you can follow these steps:

1. Import Necessary Libraries:

Import the required JDBC libraries into your Java program. These libraries are typically included in the Java Standard Library, so you don't need to download or install them separately.

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;

2. Load the JDBC Driver:

Load the JDBC driver for the specific database you want to connect to. This step is necessary to register the driver class with the DriverManager.

try {

Class.forName("com.mysql.jdbc.Driver"); // Replace with your database driver class

} catch (ClassNotFoundException e) {

e.printStackTrace();

}

Replace `"com.mysql.jdbc.Driver"` with the appropriate driver class for your database. For example, for MySQL, you would use `"com.mysql.cj.jdbc.Driver"`.

3. Create a Connection URL:

Define the database connection URL, which includes details like the database's host, port, database name, and any additional parameters.

String url = "jdbc:mysql://localhost:3306/your_database";

String username = "your_username";

String password = "your_password";

Modify the URL, username, and password to match your specific database configuration.

4. Establish the Connection:

Use the `DriverManager.getConnection()` method to establish a connection to the database.

try {

Connection connection = DriverManager.getConnection(url, username, password);

// You now have a valid database connection in the 'connection' variable

} catch (SQLException e) {

e.printStackTrace();

}

Be sure to handle exceptions properly to deal with connection errors.

5. Perform Database Operations:

Once you have a connection, you can use it to execute SQL queries and perform database operations. You'll typically create `Statement` or `PreparedStatement` objects to execute SQL commands and retrieve results.

6. Close the Connection:

After you're done with the database operations, it's crucial to close the connection to release resources. Use the `connection.close()` method to do this.

Here's a complete example:

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;

public class JDBCExample {

public static void main(String[] args) {

String url = "jdbc:mysql://localhost:3306/your_database";

String username = "your_username";

String password = "your_password";

try {

Class.forName("com.mysql.cj.jdbc.Driver");

Connection connection = DriverManager.getConnection(url, username, password);

// Perform database operations here

connection.close(); // Close the connection when done

} catch (ClassNotFoundException | SQLException e) {

e.printStackTrace();

}

}

}

Remember to replace `"com.mysql.cj.jdbc.Driver"` with the appropriate driver for your database, and customize the URL, username, and password as needed for your specific database setup
*/

public class Main {
    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username="root";
    private static final String password="sunny@123";
    private static void reserveRoom(Connection connection, Scanner scanner) {
       try {
//           i need to find the error in first 3 line
//           String guest_name = scanner.nextLine();
            String guest_name = scanner.next();
            scanner.nextLine();
           System.out.println("enter the room number ");
           int room_number = scanner.nextInt();
           System.out.println("enter contact number ");
           String contact_number = scanner.next();

           String query = "INSERT INTO reservation(guest_name, room_number, contact_number) " +
                   "VALUES('" + guest_name + "', " + room_number + ", '" + contact_number + "')";
           try (Statement st = connection.createStatement()) {
               int affectedRow = st.executeUpdate(query);

               if (affectedRow > 0) {
                   System.out.println("Reservation successful");
               }else {
                   System.out.println("reservation not successful");
               }

           }
//           catch (SQLException e) {
//               System.out.println(e.getMessage());
//               e.printStackTrace();
//           }
       }catch(SQLException e){
           System.out.println(e.getMessage());
           e.printStackTrace();
       }
    }

    private static void viewReservation(Connection connection) throws SQLException {
        String query = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservation";

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);

            System.out.println("Current Reservations:");
            System.out.println("+-----------------+-------------------+----------------------+----------------+---------------------+");
            System.out.println("| reservation_id  |    guest_name     |      room_number     | contact_number |  reservation_date   |");
            System.out.println("+-----------------+-------------------+----------------------+----------------+---------------------+");

            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                String guestName = rs.getString("guest_name");
                int roomNumber = rs.getInt("room_number");
                String contactNumber = rs.getString("contact_number");
                String reservationDate = rs.getString("reservation_date");

                // Print each row of data in the table
                System.out.printf("| %-15d | %-17s | %-20d | %-14s | %17s |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+-----------------+-------------------+----------------------+----------------+---------------------+");
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner){
        System.out.println("enter reservation id");
        int reservationId=scanner.nextInt();
        System.out.println("enter guest name");
        String guestName=scanner.nextLine();

        String query="select room_number from hotel_db where reservation_id="+reservationId+" and guest_name='"+guestName+"'";
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            if (rs.next()){
                int roomNumber=rs.getInt("room_number");
                System.out.println("room number for reservation id "+reservationId+"and guest "+guestName+"is "+roomNumber);
            } else {
                System.out.println("reservation not found for the giver id and name");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner){
        try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = scanner.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = scanner.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservation SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservation WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation deleted successfully!");
                } else {
                    System.out.println("Reservation deletion failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservation WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }


    public static void exit() {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            try {
                Thread.sleep(1000);
                i--;
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }

        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }




    public static void main(String[] args){
       try{
           Class.forName("com.mysql.cj.jdbc.Driver");
           System.out.println("driver load successful");
       }catch (ClassNotFoundException e){
           System.out.println(e.getMessage());
       }

        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("connection created successfully");
            while(true){
               System.out.println();
               System.out.println("HOTEL MANAGEMENT SYSTEM");
               Scanner scanner =new Scanner(System.in);

               System.out.println("1.reserve a room");
               System.out.println("2.view reservation");
               System.out.println("3.get room number");
               System.out.println("4.update reservation");
               System.out.println("5.delete reservation");
               System.out.println("0.exit");
               System.out.println("enter your choice");
              int choice=scanner.nextInt();
               switch (choice){
                   case 1:
                       reserveRoom(connection,scanner);
                       break;
                   case 2:
                       viewReservation(connection);
                       break;
                   case 3:
                       getRoomNumber(connection,scanner);
                       break;
                   case 4:
                       updateReservation(connection,scanner);
                       break;
                   case 5:
                       deleteReservation(connection,scanner);
                       break;
                   case 0:
                       exit();
                       scanner.close();
                       break;
                   default:
                       System.out.println("invalid choice . Enter again");
               }
           }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }
}