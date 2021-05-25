import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;

public class s3_uploadFile {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the name of the bucket in which you want to upload the file:");
        String bucket_name = sc.nextLine();

        System.out.println("Enter the file path: ");

        String file_path = "/Users/sarvi/Downloads/Prashant.txt";
        String key_name = Paths.get(file_path).getFileName().toString();

        System.out.format("Uploading the file to the S3 bucket %s...\n", bucket_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        try {
            s3.putObject(bucket_name, key_name, new File(file_path));
        } catch (AmazonServiceException exp) {
            System.err.println(exp.getErrorMessage());
            System.exit(1);
        }
        System.out.println("File uploaded successfully!");
    }
}