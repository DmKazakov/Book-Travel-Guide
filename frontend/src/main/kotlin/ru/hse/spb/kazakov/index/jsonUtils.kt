package ru.hse.spb.kazakov.index

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import ru.hse.spb.kazakov.mongo.BookLocation
import ru.hse.spb.kazakov.mongo.IncomingDependency
import ru.hse.spb.kazakov.mongo.OutgoingDependency
import ru.hse.spb.kazakov.mongo.Token

fun toJSON(locations: List<BookLocation>): String {
    val jArray = JSONArray()
    locations.map { it.toJSON() }.forEach { jArray.add(it) }

    val result = JSONObject()
    result["locations"] = jArray

    return result.toJSONString()
}

private fun BookLocation.toJSON() = JSONObject().apply {
    put("title", title)
    put("author", author)
    put("type", location.type)
    put("user rating", userRating)
    put("amod deps", outgoingAmod)
    put("amod neighbors", neighborsAmod)
    put("reviews number", reviewsNumber)
    put("location", location.location)
    put("sentence", location.sentence)
    put("incoming deps", location.inDeps.asJsonArray { toJSON() })
    put("outgoing deps", location.outDeps.asJsonArray { toJSON() })
    put("left neighbors", location.leftNeighbors.asJsonArray { toJSON() })
    put("right neighbors", location.rightNeighbors.asJsonArray { toJSON() })
    put("id", morphiaId.toHexString())
}

private fun <E> List<E>.toJArray(): JSONArray {
    val jarray = JSONArray()
    this.forEach { jarray.add(it) }
    return jarray
}

private fun <E> List<E>.asJsonArray(fn: E.() -> JSONObject): JSONArray {
    return map { it.fn() }.toJArray()
}

private fun IncomingDependency.toJSON() = JSONObject().apply {
    put("token", token.text)
    put("pof", token.partOfSpeech)
    put("dep type", dependencyType)
}

private fun OutgoingDependency.toJSON() = JSONObject().apply {
    put("token", token.text)
    put("pof", token.partOfSpeech)
    put("dep type", dependencyType)
}

private fun Token.toJSON() = JSONObject().apply {
    put("token", text)
    put("pof", partOfSpeech)
}