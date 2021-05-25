import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.util.List;
import java.util.Scanner;

public class s3_permissions {

    public static void main(String[] args) {

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the bucket name in which you want to disable the public access: ");
        String bucket_name = sc.nextLine();
        Bucket b = createBucket(bucket_name);
        if (b == null) {
            System.out.println("Error creating bucket!\n");
        } else {
            System.out.println("Bucket successfully created!!\n");
        }

        System.out.println("Do you want to disable the public access?: y/n?");
        String choice = sc.nextLine();
        if(choice.equals("y") && !choice.equals("n") ) {


            Boolean globalFlag = Boolean.TRUE;

            PublicAccessBlockConfiguration accessBlockConfiguration = new PublicAccessBlockConfiguration();
            accessBlockConfiguration.setBlockPublicAcls(globalFlag);
            accessBlockConfiguration.setBlockPublicPolicy(globalFlag);
            accessBlockConfiguration.setIgnorePublicAcls(globalFlag);
            accessBlockConfiguration.setRestrictPublicBuckets(globalFlag);
            SetPublicAccessBlockRequest setPublicAccessBlockRequest = new SetPublicAccessBlockRequest();
            setPublicAccessBlockRequest.setBucketName(bucket_name);
            setPublicAccessBlockRequest.setPublicAccessBlockConfiguration(accessBlockConfiguration);

            s3.setPublicAccessBlock(setPublicAccessBlockRequest);
            System.out.format("Public access is disabled for the %s bucket!", bucket_name);
            System.out.println("/n");
        }
        else if(!choice.equals("y") && !choice.equals("n")){
            System.out.println("Invalid entry!");
        }
        else
        {
            System.out.println("Bucket created successfully without disabling the public access");
        }

        System.out.println("Do want to give ACL write option to “full-control” for bucket owner?: y/n");
        String choice_acl = sc.nextLine();
        if(choice_acl.equals("y") && !choice_acl.equals("n") ) {
            final AccessControlList acl = s3.getBucketAcl(bucket_name);
            acl.grantAllPermissions(new Grant(new CanonicalGrantee(acl.getOwner().getId()), Permission.FullControl));
            s3.setBucketAcl(bucket_name, acl);
            System.out.println("ACL write option changed to 'full-control' for bucket owner!");
        }

    }

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
    }
