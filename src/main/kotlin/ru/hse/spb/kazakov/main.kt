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
    val locRecognizer = LocationRecognizer()

    var section = epubReader.readSection()
    while (section != null) {
        locRecognizer.extractLocations(section)
        section = epubReader.readSection()
    }

    for (location in locRecognizer.getLocations()) {
        println(location.posTaggedSentence)
        println(location.dependencies.joinToString(", "))
        println()
    }
}