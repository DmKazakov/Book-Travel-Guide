package ru.hse.spb.kazakov

import edu.stanford.nlp.pipeline.StanfordCoreNLP
import ru.hse.spb.kazakov.mongo.BookLocationStore
import ru.hse.spb.kazakov.mongo.Datastore
import java.util.*
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import org.apache.log4j.BasicConfigurator


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No databse IP specified.")
        return
    }

    BasicConfigurator.configure()
    val props = Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment")
    val pipeline = StanfordCoreNLP(props)

    val bookLocStore = BookLocationStore(Datastore.getInstance(args[0]))
    bookLocStore.getAllLocations().forEach {
        val annotation = pipeline.process(it.location.sentence)
        val coreMap = annotation.get(CoreAnnotations.SentencesAnnotation::class.java).first()
        val tree = coreMap.get(SentimentCoreAnnotations.SentimentAnnotatedTree::class.java)
        it.sentiment = Math.abs(RNNCoreAnnotations.getPredictedClass(tree) - 2)
        bookLocStore.save(it)
    }
}