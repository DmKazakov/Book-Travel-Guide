package ru.hse.spb.kazakov

import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.CoreEntityMention
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import org.apache.log4j.BasicConfigurator
import java.util.*
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation


class LocationRecognizer {
    private val pipeline: StanfordCoreNLP

    init {
        BasicConfigurator.configure()
        val props = Properties().apply {
            setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, depparse")
            setProperty("ner.applyFineGrained", "false")
        }
        pipeline = StanfordCoreNLP(props)
    }

    fun extractLocations(text: String): List<LocationContext> {
        val document = CoreDocument(text)
        pipeline.annotate(document)

        return document.entityMentions()
            .asSequence()
            .filter { it.entityType() == "LOCATION" }
            .map {
                val posTaggedSentence = it.getPosTaggedSentence()
                val dependencies = it.getDependencies()
                LocationContext(posTaggedSentence, dependencies)
            }
            .toList()
    }

    private fun CoreEntityMention.getPosTaggedSentence(): String {
        return sentence().tokens().joinToString(" ") {
            val pos = it.get(PartOfSpeechAnnotation::class.java)
            val word = it.get(TextAnnotation::class.java)
            "$word($pos)"
        }
    }

    private fun CoreEntityMention.getDependencies(): List<String> {
        val depGraph = sentence().dependencyParse()
        return tokens()
            .flatMap { it ->
                val node = depGraph.getNodeByIndex(it.index())
                depGraph.childPairs(node)
                    .asSequence()
                    .filter { it.first.shortName == "amod" }
                    .map { it.second.toString() }
                    .toList()
            }
    }

    data class LocationContext(val posTaggedSentence: String, val dependencies: List<String>)
}
