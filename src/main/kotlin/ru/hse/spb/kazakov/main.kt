package ru.hse.spb.kazakov

import ru.hse.spb.kazakov.nlp.LocationRecognizer

/**
 * Prints all sentences from specified epub file with mentions of locations.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No epub file specified.")
        return
    }
    val epubReader = EpubReader(args[0])
    val pipeliner = LocationRecognizer()

    var section = epubReader.readNextSection()
    while (section != null) {
        pipeliner.extractLocations(section).forEach {
            println(it.location)
            println(it.sentence)
            println(it.inDeps)
            println(it.outDeps)
            println(it.leftNeighbors)
            println(it.rightNeighbors)
            println()
        }
        section = epubReader.readNextSection()
    }
}