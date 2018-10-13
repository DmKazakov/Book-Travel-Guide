package ru.hse.spb.kazakov

import com.github.mertakdut.Reader
import com.github.mertakdut.exception.OutOfPagesException


class EpubReader(pathToEpub: String) {
    private var currentSection = 0
    private var isEof = false
    private val reader: Reader = Reader()

    init {
        reader.setIsIncludingTextContent(true)
        reader.setFullContent(pathToEpub)
    }

    fun readNextSection(): String? {
        if (isEof) {
            return null
        }

        var text: String?
        do {
            text = try {
                val section = reader.readSection(currentSection)
                currentSection++
                section.sectionTextContent.trim()
            } catch (exception: OutOfPagesException) {
                isEof = true
                null
            }
        } while (text == "")

        return text
    }
}