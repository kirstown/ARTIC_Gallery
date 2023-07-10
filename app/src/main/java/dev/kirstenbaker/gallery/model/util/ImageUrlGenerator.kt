package dev.kirstenbaker.gallery.model.util

/**
    The ARTIC API gives us a UUID for each art piece that we then use to
    construct the image URL. Note that this base URL is different from that of the mainAPI.

    See docs: https://api.artic.edu/docs/#iiif-image-api
 */
object ImageUrlGenerator {
    private const val articBaseUrl = "https://www.artic.edu/"

    fun generateThumbnailUrl(imageId: String) =
        "${articBaseUrl}iiif/2/${imageId}/full/200,/0/default.jpg"

    fun generateDefaultSizeImageUrl(imageId: String) =
        "${articBaseUrl}iiif/2/${imageId}/full/843,/0/default.jpg"
}
