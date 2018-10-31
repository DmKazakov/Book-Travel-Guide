package ru.hse.spb.kazakov

import com.github.mertakdut.Reader
import com.github.mertakdut.exception.OutOfPagesException

class EpubReader(pathToEpub: String) {
    private var currentSection = 0
    private var isEof = false
    private val reader: Reader = Reader()
    val creator
        get() = reader.infoPackage.metadata.creator
    val title
        get() = reader.infoPackage.metadata.title

    init {
        reader.setIsIncludingTextContent(true)
        reader.setFullContent(pathToEpub)
    }

    fun isValid(): Boolean {
        val metadata = reader.infoPackage.metadata
        return title != "" && creator != "" && metadata.language == "en"
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