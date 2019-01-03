package ru.hse.spb.kazakov

import edu.stanford.nlp.pipeline.CoreDocument
import edu.stanford.nlp.pipeline.CoreEntityMention
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import org.apache.log4j.BasicConfigurator
import java.util.*
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation
import edu.stanford.nlp.ling.IndexedWord
import ru.hse.spb.kazakov.mongo.IncomingDependency
import ru.hse.spb.kazakov.mongo.LocationContext
import ru.hse.spb.kazakov.mongo.OutgoingDependency
import ru.hse.spb.kazakov.mongo.Token
import kotlin.math.max

private const val NEIGHBORS_NUM = 3

class LocationRecognizer {
    private val pipeline: StanfordCoreNLP

    init {
        BasicConfigurator.configure()
        val props = Properties().apply {
            setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, depparse")
            setProperty("ner.applyNumericClassifiers", "false")
            setProperty("ner.useSUTime", "false")
            setProperty("ner.markTimeRanges", "false")
            setProperty("ner.includeRange", "false")
            setProperty("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz")
        }
        pipeline = StanfordCoreNLP(props)
    }

    fun extractLocations(text: String): List<LocationContext> {
        val document = CoreDocument(text)
        pipeline.annotate(document)

        return document.entityMentions()
            .asSequence()
            .filter { it.entityType().isLocation() }
            .map {
                val inDeps = it.getIncomingDependencies()
                val outDeps = it.getOutgoingDependencies()
                val lNeighbors = it.getLeftNeighbors()
                val rNeighbors = it.getRightNeighbors()
                val sectionOffset = it.tokens().first().beginPosition()
                LocationContext(
                        it.text(), it.sentence().text(), it.entityType(), sectionOffset,
                        inDeps, outDeps, lNeighbors, rNeighbors
                )
            }
            .toList()
    }

    private fun CoreEntityMention.getIncomingDependencies(): List<IncomingDependency> {
        val depGraph = sentence().dependencyParse()
        return tokens()
            .flatMap { token ->
                val node = depGraph.getNodeByIndex(token.index())
                depGraph.parentPairs(node)
                    .map { IncomingDependency(it.second.toToken(), it.first.shortName) }
            }
    }

    private fun CoreEntityMention.getOutgoingDependencies(): List<OutgoingDependency> {
        val depGraph = sentence().dependencyParse()
        return tokens()
            .flatMap { token ->
                val node = depGraph.getNodeByIndex(token.index())
                depGraph.childPairs(node)
                    .map { OutgoingDependency(it.second.toToken(), it.first.shortName) }
            }
    }

    private fun CoreEntityMention.getRightNeighbors(): List<Token> {
        val tokens = sentence().tokens()
        val lastIndex = tokens().last().index()
        return tokens
            .asSequence()
            .drop(lastIndex)
            .take(NEIGHBORS_NUM)
            .map { Token(it.originalText(), it.get(PartOfSpeechAnnotation::class.java)) }
            .toList()
    }

    private fun CoreEntityMention.getLeftNeighbors(): List<Token> {
        val tokens = sentence().tokens()
        val firsIndex = tokens().first().index()
        return tokens
            .asSequence()
            .drop(max(0, firsIndex - NEIGHBORS_NUM - 1))
            .take(NEIGHBORS_NUM)
            .map { Token(it.originalText(), it.get(PartOfSpeechAnnotation::class.java)) }
            .toList()
    }

    private fun IndexedWord.toToken(): Token {
        return Token(toString(), get(PartOfSpeechAnnotation::class.java))
    }

    private fun String.isLocation() = this == "CITY" || this == "COUNTRY" || this == "STATE_OR_PROVINCE" || this == "LOCATION"
}




