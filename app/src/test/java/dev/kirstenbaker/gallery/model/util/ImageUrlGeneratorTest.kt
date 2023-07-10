package dev.kirstenbaker.gallery.model.util

import org.junit.Assert
import org.junit.Test

class ImageUrlGeneratorTest {
    private val imageId = "02cd860f-126b-5e55-080c-cf9c507b8dfb"

    @Test
    fun `thumbnail url is generated correctly`() {
        val generatedThumbnailUrl = ImageUrlGenerator.generateThumbnailUrl(imageId)
        val expectedThumbnailUrl =
            "https://www.artic.edu/iiif/2/02cd860f-126b-5e55-080c-cf9c507b8dfb/full/200,/0/default.jpg"
        Assert.assertEquals(generatedThumbnailUrl, expectedThumbnailUrl)
    }

    @Test
    fun `full size image url is generated correctly`() {
        val generatedThumbnailUrl = ImageUrlGenerator.generateDefaultSizeImageUrl(imageId)
        val expectedThumbnailUrl =
            "https://www.artic.edu/iiif/2/02cd860f-126b-5e55-080c-cf9c507b8dfb/full/843,/0/default.jpg"
        Assert.assertEquals(generatedThumbnailUrl, expectedThumbnailUrl)
    }
}