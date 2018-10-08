package ru.hse.spb.kazakov

import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.CoreEntityMention
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import org.apache.log4j.BasicConfigurator
import java.lang.StringBuilder
import java.util.*
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation


class LocationRecognizer {
    private val pipeline: StanfordCoreNLP
    private val locations = mutableListOf<LocationContext>()

    init {
        BasicConfigurator.configure()
        val props = Properties()
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, depparse")
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
                val posTaggedSentence = entityMention.getPosTaggedSentence()
                val dependencies = entityMention.getDependencies()
                locations.add(LocationContext(posTaggedSentence, dependencies))
            }
        }
    }

    private fun CoreEntityMention.getPosTaggedSentence(): String {
        val sentence = StringBuilder()

        sentence.append("${this.text()}: ")
        for (token in this.sentence().tokens()) {
            val pos = token.get(PartOfSpeechAnnotation::class.java)
            val word = token.get(TextAnnotation::class.java)
            sentence.append("$word($pos) ")
        }

        return sentence.toString()
    }

    private fun CoreEntityMention.getDependencies(): List<String> {
        val dependencies = mutableListOf<String>()
        val depGraph = this.sentence().dependencyParse()

        for (token in this.tokens()) {
            val node = depGraph.getNodeByIndex(token.index())
            for (dependence in depGraph.childPairs(node)) {
                if (dependence.first.shortName == "amod") {
                    dependencies.add(dependence.second.toString())
                }
            }
        }

        return dependencies
    }

    fun getLocations(): List<LocationContext> = locations

    data class LocationContext(val posTaggedSentence: String, val dependencies: List<String>)
}
