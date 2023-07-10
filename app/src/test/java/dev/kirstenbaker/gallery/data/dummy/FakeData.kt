package dev.kirstenbaker.gallery.data.dummy

// test list with increasing id values
val artworkList = (0 until 200).map { num ->
    testArtwork1.copy(id = num)
}