package com.markme.attendance.ai

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

/**
 * CameraX ImageAnalysis bridge with ML Kit Face Detection & TFLite Embedding Matcher
 */
class FaceAnalyzer(
    private val onFaceDetected: (Bitmap, FloatArray) -> Unit,
    private val onUnknownTracked: (Bitmap, frameCount: Int) -> Unit
) : ImageAnalysis.Analyzer {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .setMinFaceSize(0.15f)
            .build()
    )

    private var consecutiveUnknownFrames = 0

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)

            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val primaryFace = faces[0]
                        val bitmap = imageProxy.toBitmap()
                        val bounds = primaryFace.boundingBox

                        // Crop face safely
                        val safeLeft = bounds.left.coerceAtLeast(0)
                        val safeTop = bounds.top.coerceAtLeast(0)
                        val safeWidth = bounds.width().coerceAtMost(bitmap.width - safeLeft)
                        val safeHeight = bounds.height().coerceAtMost(bitmap.height - safeTop)

                        if (safeWidth > 40 && safeHeight > 40) {
                            val faceCrop = Bitmap.createBitmap(bitmap, safeLeft, safeTop, safeWidth, safeHeight)
                            consecutiveUnknownFrames++
                            onUnknownTracked(faceCrop, consecutiveUnknownFrames)
                        }
                    } else {
                        consecutiveUnknownFrames = 0
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
