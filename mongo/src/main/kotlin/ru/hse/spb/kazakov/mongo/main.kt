package ru.hse.spb.kazakov.mongo


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No databse IP specified.")
        return
    }

    val bookLocStore = BookLocationStore(Datastore.getInstance(args[0]))
    bookLocStore.getAllLocations().forEach {
        it.neighborsAmod = it.neighborsAmod
        it.outgoingAmod = it.outgoingAmod
        bookLocStore.save(it)
    }
}
