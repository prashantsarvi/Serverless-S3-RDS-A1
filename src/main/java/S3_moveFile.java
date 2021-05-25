import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;

import java.util.Scanner;

public class S3_moveFile {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the bucket name from where you want to move the file:");
        String from_bucket = sc.nextLine();

        System.out.println("Enter the bucket name where you want the file to be moved:");
        String to_bucket = sc.nextLine();

        System.out.println("Enter the file name to be moved:");
        String object_key = sc.nextLine();

        System.out.format("Moving File %s from bucket %s to %s\n",
                object_key, from_bucket, to_bucket);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        try {
            s3.copyObject(from_bucket, object_key, to_bucket, object_key);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        String bucket_file_delete = from_bucket;

        try {
            DeleteObjectsRequest dor = new DeleteObjectsRequest(bucket_file_delete)
                    .withKeys(object_key);
            s3.deleteObjects(dor);
        } catch (AmazonServiceException exp) {
            System.err.println(exp.getErrorMessage());
            System.exit(1);
        }
        System.out.println("File moved successfully!");
    }
}
