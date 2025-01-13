package com.toyProject7.karrot.image.service

import com.toyProject7.karrot.article.ArticleNotFoundException
import com.toyProject7.karrot.article.persistence.ArticleRepository
import com.toyProject7.karrot.image.ImageDeleteException
import com.toyProject7.karrot.image.ImagePresignedUrlCreateException
import com.toyProject7.karrot.image.ImageS3UrlCreateException
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import com.toyProject7.karrot.image.persistence.ImageUrlRepository
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
    private val articleRepository: ArticleRepository,
    private val imageUrlRepository: ImageUrlRepository,
) {
    @Transactional
    fun postImageUrl(
        type: String,
        typeId: Long,
        imageIndex: Int,
    ): ImageUrlEntity {
        val imageUrlEntity =
            ImageUrlEntity(
                article = articleRepository.findById(typeId).orElseThrow { ArticleNotFoundException() },
                url = generateS3Path(type, typeId, imageIndex),
            )
        imageUrlRepository.save(imageUrlEntity)

        return imageUrlEntity
    }

    @Transactional
    fun deleteImageUrl(imageS3Urls: MutableList<ImageUrlEntity>) {
        val bucketName = System.getenv("AWS_S3_BUCKET") ?: "Something went wrong"
        if (bucketName == "Something went wrong") {
            throw IllegalStateException(
                "AWS_S3_BUCKET environment variable is missing. Please configure it.",
            )
        }

        try {
            // S3 객체 식별자 리스트 생성
            val objectIdentifiers =
                imageS3Urls.map { s3Url ->
                    val objectKey = s3Url.url.removePrefix("s3://$bucketName/")
                    ObjectIdentifier.builder().key(objectKey).versionId("2008-10-17").build()
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

            // 엔티티 삭제
            imageS3Urls.map { imageUrlEntity -> imageUrlRepository.delete(imageUrlEntity) }
        } catch (e: Exception) {
            throw ImageDeleteException()
        }
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
        try {
            val path = "$type/$typeId/image_$imageIndex.jpg"
            return "s3://$bucketName/$path"
        } catch (e: Exception) {
            throw ImageS3UrlCreateException()
        }
    }

    @Transactional
    fun generatePresignedUrl(
        imageS3Url: String,
        articleId: Long,
        imageIndex: Int,
    ): ImageUrlEntity {
        val bucketName: String = System.getenv("AWS_S3_BUCKET") ?: "Something went wrong"
        if (bucketName == "Something went wrong") {
            throw IllegalStateException(
                "AWS_S3_BUCKET environment variable is missing. Please configure it.",
            )
        }
        try {
            val objectKey = imageS3Url.removePrefix("s3://$bucketName/")

            // Presigned URL을 생성하기 위한 요청 객체 생성
            val getObjectRequest =
                GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .versionId("2008-10-17")
                    .build()

            // Presigned URL 요청 객체 생성
            val presignedRequest =
                GetObjectPresignRequest.builder()
                    .signatureDuration(ofMinutes(15))
                    .getObjectRequest(getObjectRequest)
                    .build()

            val imageUrlEntity =
                ImageUrlEntity(
                    article = articleRepository.findById(articleId).orElseThrow { ArticleNotFoundException() },
                    url = s3Presigner.presignGetObject(presignedRequest).url().toString(),
                )

            imageUrlRepository.save(imageUrlEntity)
            return imageUrlEntity
        } catch (e: Exception) {
            throw ImagePresignedUrlCreateException()
        }
    }

    @Transactional
    fun updatePresignedUrl(
        imageUrlEntity: ImageUrlEntity,
        imageS3Url: String,
    ) {
        val bucketName: String = System.getenv("AWS_S3_BUCKET") ?: "Something went wrong"
        if (bucketName == "Something went wrong") {
            throw IllegalStateException(
                "AWS_S3_BUCKET environment variable is missing. Please configure it.",
            )
        }
        try {
            val objectKey = imageS3Url.removePrefix("s3://$bucketName/")

            // Presigned URL을 생성하기 위한 요청 객체 생성
            val getObjectRequest =
                GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .versionId("2008-10-17")
                    .build()

            // Presigned URL 요청 객체 생성
            val presignedRequest =
                GetObjectPresignRequest.builder()
                    .signatureDuration(ofMinutes(15))
                    .getObjectRequest(getObjectRequest)
                    .build()

            imageUrlEntity.url = s3Presigner.presignGetObject(presignedRequest).url().toString()
            imageUrlRepository.save(imageUrlEntity)
        } catch (e: Exception) {
            throw ImagePresignedUrlCreateException()
        }
    }
}
