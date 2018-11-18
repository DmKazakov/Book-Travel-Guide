package ru.hse.spb.kazakov.nlp

import org.mongodb.morphia.annotations.Embedded

@Embedded
data class Token(val text: String, val partOfSpeech: String) {
    @Deprecated("For morphia use only")
    constructor() : this("", "")
}

@Embedded
data class IncomingDependency(val token: Token, val dependencyType: String) {
    @Deprecated("For morphia use only")
    constructor() : this(Token(), "")
}

@Embedded
data class OutgoingDependency(val token: Token, val dependencyType: String) {
    @Deprecated("For morphia use only")
    constructor() : this(Token(), "")
}

@Embedded
data class LocationContext(
        val location: String,
        val sentence: String,
        val type: String,
        val sectionOffset: Int,
        val inDeps: List<IncomingDependency>,
        val outDeps: List<OutgoingDependency>,
        val leftNeighbors: List<Token>,
        val rightNeighbors: List<Token>
) {
    @Deprecated("For morphia use only")
    constructor() : this(
            "",
            "",
            "",
            -1,
            emptyList<IncomingDependency>(),
            emptyList<OutgoingDependency>(),
            emptyList<Token>(),
            emptyList<Token>()
    )

    fun evaluateRating(): Int {
        return outDeps.asSequence().filter { it.dependencyType == "amod" }.count()
    }
}