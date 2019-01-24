package ru.hse.spb.kazakov.mongo


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No databse IP specified.")
        return
    }

    val bookLocStore = BookLocationStore(Datastore.getInstance(args[0]))
    bookLocStore.getReviewedLocations().forEach {
        it.neighborsAmod = it.neighborsAmod
        it.outgoingAmod = it.outgoingAmod
        bookLocStore.save(it)
    }

    val bookLocs = HashMap<Pair<String, String>, BookLocation>()
    bookLocStore.getReviewedLocations().forEach {
        val pair = Pair(it.location.location, it.location.sentence)
        val duplicate = bookLocs[pair]
        if(duplicate != null) {
            if (it.userRating < 0) {
                bookLocStore.deleteQuote(it.morphiaId)
            } else {
                bookLocStore.deleteQuote(duplicate.morphiaId)
                bookLocs[pair] = it
            }
        } else {
            bookLocs[pair] = it
        }
    }
}
