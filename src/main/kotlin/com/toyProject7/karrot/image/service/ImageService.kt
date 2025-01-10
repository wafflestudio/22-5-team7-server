package com.toyProject7.karrot.image.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ObjectIdentifier
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration.ofMinutes

@Service
class ImageService(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
) {
    @Transactional
    fun postImageUrl(
        type: String,
        typeId: Long,
        imageCount: Int,
    ): List<String> {
        val imageS3Url: MutableList<String> = mutableListOf()
        for (number in 0..imageCount) {
            imageS3Url += generateS3Path(type, typeId, number)
        }
        return imageS3Url
    }

    @Transactional
    fun deleteImageUrl(imageS3Url: List<String>) {
        val bucketName = System.getenv("AWS_S3_BUCKET") ?: "Something went wrong"
        if (bucketName == "Something went wrong") {
            throw IllegalStateException(
                "AWS_S3_BUCKET environment variable is missing. Please configure it.",
            )
        }

        // S3 객체 식별자 리스트 생성
        val objectIdentifiers =
            imageS3Url.map { s3Url ->
                val objectKey = s3Url.removePrefix("s3://$bucketName/")
                ObjectIdentifier.builder().key(objectKey).build()
            }

        // 삭제 요청 생성
        val deleteObjectsRequest =
            DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete { delete ->
                    delete.objects(objectIdentifiers)
                }
                .build()

        // S3에서 객체 삭제
        s3Client.deleteObjects(deleteObjectsRequest)
    }

    fun generateS3Path(
        type: String,
        typeId: Long,
        imageIndex: Int,
    ): String {
        val bucketName = System.getenv("AWS_S3_BUCKET") ?: "Something went wrong"
        if (bucketName == "Something went wrong") {
            throw IllegalStateException(
                "AWS_S3_BUCKET environment variable is missing. Please configure it.",
            )
        }
        val path = "$type/$typeId/image_$imageIndex.jpg"
        return "s3://$bucketName/$path"
    }

    fun generatePresignedUrl(imageS3Url: List<String>): List<String> {
        val bucketName: String = System.getenv("AWS_S3_BUCKET") ?: "Something went wrong"
        if (bucketName == "Something went wrong") {
            throw IllegalStateException(
                "AWS_S3_BUCKET environment variable is missing. Please configure it.",
            )
        }

        return imageS3Url.map { s3Url ->
            val objectKey = s3Url.removePrefix("s3://$bucketName/")

            // Presigned URL을 생성하기 위한 요청 객체 생성
            val getObjectRequest =
                GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build()

            // Presigned URL 요청 객체 생성
            val presignedRequest =
                GetObjectPresignRequest.builder()
                    .signatureDuration(ofMinutes(15))
                    .getObjectRequest(getObjectRequest)
                    .build()

            s3Presigner.presignGetObject(presignedRequest).url().toString()
        }
    }
}
