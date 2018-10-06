package ru.hse.spb.kazakov

import com.github.mertakdut.Reader
import com.github.mertakdut.exception.OutOfPagesException


class EpubReader(file: String) {
    private var currentSection = 0
    private var isEnd = false
    private val reader: Reader = Reader()

    init {
        reader.setIsIncludingTextContent(true)
        reader.setFullContent(file)
    }

    fun readSection(): String? {
        if (isEnd) {
            return null
        }

        var text: String?
        do {
            text = try {
                val section = reader.readSection(currentSection)
                currentSection++
                section.sectionTextContent.trim()
            } catch (exception: OutOfPagesException) {
                isEnd = true
                null
            }
        } while (text == "")

        return text
    }
}