package ru.hse.spb.kazakov

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import ru.hse.spb.kazakov.nlp.LocationContext

@Entity
data class BookLocation(
    val title: String?,
    val author: String?,
    val section: Int,
    val location: LocationContext,
    val sourceId: Int
) {
    @Id
    val morphiaId: ObjectId = ObjectId()
    var userRating = 0
        private set
    var reviewsNumber = 0
        private set

    @Deprecated("For morphia only")
    constructor() : this(null, null, -1, LocationContext(), -1)

    fun incUserRating() = userRating++

    fun decUserRating() = userRating--

    fun incReviewsNumber() = reviewsNumber++

    fun decReviewsNumber() = reviewsNumber--
}