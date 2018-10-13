package ru.hse.spb.kazakov.nlp

data class Token(val text: String, val partOfSpeech: String)

data class IncomingDependency(val token: Token, val dependencyType: String)

data class OutgoingDependency(val token: Token, val dependencyType: String)

data class LocationContext(
    val location: String, val sentence: String,
    val inDeps: List<IncomingDependency>, val outDeps: List<OutgoingDependency>,
    val leftNeighbors: List<Token>, val rightNeighbors: List<Token>) {

    fun evaluateRating(): Int {
        return outDeps.asSequence().filter { it.dependencyType == "amod" }.count()
    }
}