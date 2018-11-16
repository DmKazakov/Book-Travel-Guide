package ru.hse.spb.kazakov.index

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import ru.hse.spb.kazakov.BookLocation
import ru.hse.spb.kazakov.nlp.IncomingDependency
import ru.hse.spb.kazakov.nlp.OutgoingDependency
import ru.hse.spb.kazakov.nlp.Token

fun toJSON(locations: List<BookLocation>): String {
    val jArray = JSONArray()
    locations.map { it.toJSON() }.forEach { jArray.add(it) }

    val result = JSONObject()
    result["locations"] = jArray

    return result.toJSONString()
}

private fun BookLocation.toJSON() = JSONObject().apply {
    put("title", this@toJSON.title)
    put("author", this@toJSON.author)
    put("user rating", this@toJSON.userRating)
    put("reviews number", this@toJSON.reviewsNumber)
    put("location", this@toJSON.location.location)
    put("sentence", this@toJSON.location.sentence)
    put("incoming deps", this@toJSON.location.inDeps.map { it.toJSON() }.toJArray())
    put("outgoing deps", this@toJSON.location.outDeps.map { it.toJSON() }.toJArray())
    put("left neighbors", this@toJSON.location.leftNeighbors.map { it.toJSON() }.toJArray())
    put("right neighbors", this@toJSON.location.rightNeighbors.map { it.toJSON() }.toJArray())
    put("id", this@toJSON.morphiaId.toHexString())
}

private fun <E> List<E>.toJArray(): JSONArray {
    val jarray = JSONArray()
    this.forEach { jarray.add(it) }
    return jarray
}

private fun IncomingDependency.toJSON() = JSONObject().apply {
    put("token", this@toJSON.token.text)
    put("pof", this@toJSON.token.partOfSpeech)
    put("dep type", this@toJSON.dependencyType)
}

private fun OutgoingDependency.toJSON() = JSONObject().apply {
    put("token", this@toJSON.token.text)
    put("pof", this@toJSON.token.partOfSpeech)
    put("dep type", this@toJSON.dependencyType)
}

private fun Token.toJSON() = JSONObject().apply {
    put("token", this@toJSON.text)
    put("pof", this@toJSON.partOfSpeech)
}