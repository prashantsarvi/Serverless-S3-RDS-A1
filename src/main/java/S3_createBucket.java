import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

import java.util.List;
import java.util.Scanner;


public class S3_createBucket {
    public static Bucket getBucket(String bucket_name) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        Bucket named_bucket = null;
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucket_name)) {
                named_bucket = b;
            }
        }
        return named_bucket;
    }

    public static Bucket createBucket(String bucket_name) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        Bucket b = null;
        if (s3.doesBucketExistV2(bucket_name)) {
            System.out.format("The Bucket with name %s already exists.\n", bucket_name);
            b = getBucket(bucket_name);
        } else {
            try {
                b = s3.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        return b;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the name of the bucket you want to create:");
        String bucket_name = sc.nextLine();

        System.out.format("\nCreating S3 bucket: %s\n", bucket_name);
        Bucket b = createBucket(bucket_name);
        if (b == null) {
            System.out.println("Error creating bucket!\n");
        } else {
            System.out.println("Bucket successfully created!!\n");
        }
    }
}