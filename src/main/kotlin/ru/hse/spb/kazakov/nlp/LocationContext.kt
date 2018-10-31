package ru.hse.spb.kazakov.nlp

import org.mongodb.morphia.annotations.Embedded

@Embedded
data class Token(var text: String = "", var partOfSpeech: String = "")

@Embedded
data class IncomingDependency(var token: Token = Token(), var dependencyType: String = "")

@Embedded
data class OutgoingDependency(var token: Token = Token(), var dependencyType: String = "")

@Embedded
data class LocationContext(
    var location: String = "", var sentence: String = "", var sectionOffset: Int = 0,
    var inDeps: List<IncomingDependency> = emptyList(), var outDeps: List<OutgoingDependency> = emptyList(),
    var leftNeighbors: List<Token> = emptyList(), var rightNeighbors: List<Token> = emptyList()
) {
    fun evaluateRating(): Int {
        return outDeps.asSequence().filter { it.dependencyType == "amod" }.count()
    }
}