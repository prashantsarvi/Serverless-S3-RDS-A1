import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import static com.amazonaws.regions.Regions.US_EAST_1;

public class EncryptionDecryption {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the userID to be inserted in the database:");
        String userID= sc.nextLine();

        System.out.println("Enter the password to be inserted in the database:");
        String password=sc.nextLine();
        String password2="";
        String px= "";

        AmazonS3 ob = AmazonS3ClientBuilder.standard()
                .withRegion(US_EAST_1)
                .build();

        String s = ob.getObjectAsString("s3-java-a1", "Lookup5410.txt");

        System.out.println("Retrieving details from the S3 bucket to encrypt the password.......");
        System.out.println(s);

        Map<String, String> myMap = new HashMap<>();

        String[] pairs = s.split("\\n");
        for (int i=0;i<pairs.length;i++) {
            String pair = pairs[i];
            StringTokenizer st = new StringTokenizer(pair);
            myMap.put(st.nextToken(), st.nextToken());
        }


        System.out.println("Encrypting Password...........");
        for (int i=0;i<password.length();i++) {
            String p =  String.valueOf(password.charAt(i));
            px = myMap.get(p);
            password2 = password2 + px;
        }

        System.out.format("Encrypted Password : %s \n",password2);
        String passwordDecrypt= password;

        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception exp) {
                System.out.println(exp);
            }
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/aws-rds", "root", "database");
            System.out.println("Connection is created successfully!");
            stmt = (Statement) conn.createStatement();

            String query = " insert into encryption (userID, password)"
                    + " values (?, ?)";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, userID);
            preparedStmt.setString (2, password);
            preparedStmt.execute();

            System.out.format("UserID: %s and Encrypted Password: %s inserted successfully!\n", userID,password2);

            System.out.println("Decrypting Password...........");
            for (int i=0;i<password2.length();i++) {
                String p =  String.valueOf(password.charAt(i));
                password = password + px;
            }
            System.out.format("Decrypted Password : %s\n" ,passwordDecrypt);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {}
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("The table has been updated!");
    }
}