package ru.hse.spb.kazakov.nlp

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Embedded
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

@Embedded
data class Token(var text: String = "", var partOfSpeech: String = "")

@Embedded
data class IncomingDependency(var token: Token = Token(), var dependencyType: String = "")

@Embedded
data class OutgoingDependency(var token: Token = Token(), var dependencyType: String = "")

@Entity
data class LocationContext(
    var location: String = "", var sentence: String = "",
    var inDeps: List<IncomingDependency> = emptyList(), var outDeps: List<OutgoingDependency> = emptyList(),
    var leftNeighbors: List<Token> = emptyList(), var rightNeighbors: List<Token> = emptyList(),
    @Id var id: ObjectId = ObjectId()
) {

    fun evaluateRating(): Int {
        return outDeps.asSequence().filter { it.dependencyType == "amod" }.count()
    }
}