package ru.hse.spb.kazakov.mongo

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

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
    var outgoingAmod = location.outDeps.asSequence().filter { it.dependencyType == "amod" }.count()
        get() = location.outDeps.asSequence().filter { it.dependencyType == "amod" }.count()
    var neighborsAmod = location.leftNeighbors.union(location.rightNeighbors).count { it.partOfSpeech.isAdjective() }
        get() = location.leftNeighbors.union(location.rightNeighbors).count { it.partOfSpeech.isAdjective() }
    var sentiment = 2

    @Deprecated("For morphia only")
    private constructor() : this(null, null, -1, LocationContext(), -1)

    fun incUserRating() {
        userRating++
        reviewsNumber++
    }

    fun decUserRating() {
        userRating--
        reviewsNumber++
    }

    private fun String.isAdjective() = this == "JJ" || this == "JJR" || this == "JJS"
}