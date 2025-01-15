package com.toyProject7.karrot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class AwsS3Config {
    @Bean
    fun s3Client(): S3Client {
        val regionName = System.getenv("AWS_REGION") ?: "Something went wrong"
        if (regionName == "Something went wrong") {
            throw IllegalStateException(
                "AWS_S3_BUCKET environment variable is missing. Please configure it.",
            )
        }

        return S3Client.builder()
            .region(Region.of(regionName))
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        val regionName = System.getenv("AWS_REGION") ?: "Something went wrong"
        if (regionName == "Something went wrong") {
            throw IllegalStateException(
                "AWS_S3_BUCKET environment variable is missing. Please configure it.",
            )
        }

        return S3Presigner.builder()
            .region(Region.of(regionName))
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build()
    }
}
