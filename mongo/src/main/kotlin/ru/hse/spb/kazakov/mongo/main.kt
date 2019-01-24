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

    val bookLocs = HashMap<String, BookLocation>()
    bookLocStore.getReviewedLocations().forEach {
        val duplicate = bookLocs[it.location.sentence]
        if(duplicate != null && duplicate.location.location == it.location.location) {
            println(duplicate.location.sentence)
            println(duplicate.location.location)
            println(it.location.sentence)
            println(it.location.location)
            println()
            bookLocStore.deleteQuote(it.morphiaId)
        } else {
            bookLocs[it.location.sentence] = it
        }
    }
}
