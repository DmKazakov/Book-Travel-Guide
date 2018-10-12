package ru.hse.spb.kazakov

/**
 * Prints all sentences from specified epub file with mentions of locations.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No epub file specified.")
        return
    }
    val epubReader = EpubReader(args[0])
    val pipeliner = Pipeliner()

    var section = epubReader.readSection()
    while (section != null) {
        pipeliner.extractLocations(section).forEach {
            println(it.posTaggedSentence)
            println(it.dependencies.joinToString(", "))
            println()
        }
        section = epubReader.readSection()
    }
}