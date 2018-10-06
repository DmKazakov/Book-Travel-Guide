package ru.hse.spb.kazakov

import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import org.apache.log4j.BasicConfigurator
import java.util.*

class LocationRecognizer {
    private val pipeline: StanfordCoreNLP
    private val locations = mutableListOf<String>()

    init {
        BasicConfigurator.configure();
        val props = Properties()
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner")
        props.setProperty("ner.applyFineGrained", "false")
        this.pipeline = StanfordCoreNLP(props)
    }

    /**
     * Saves sentences with recognized locations.
     */
    fun extractLocations(text: String) {
        val document = CoreDocument(text)
        pipeline.annotate(document)

        for (entityMention in document.entityMentions()) {
            if (entityMention.entityType() == "LOCATION") {
                locations.add("${entityMention.text()}: ${entityMention.sentence()}")
            }
        }
    }

    fun getLocations(): List<String> = locations
}
