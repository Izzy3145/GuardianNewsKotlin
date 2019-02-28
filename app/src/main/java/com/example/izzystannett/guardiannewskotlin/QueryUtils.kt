package com.example.izzystannett.guardiannewskotlin

import android.icu.util.BuddhistCalendar
import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.Integer.parseInt
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class QueryUtils {
    val LOG_TAG: String = "QUERY UTILS CLASS"
    val API_URL: String =
        "https://content.guardianapis.com/search?q=climatechange&api-key=9d3f96ad-2cc7-4f8d-9026-9f6f442bd691"

    fun fetchNewsArticles(): List<NewsArticle>? {

       val guardianAPI = createUrl(API_URL)

        var jsonResponse: String = try {
            makeHttpRequest(guardianAPI)
        } catch (e: IOException) {
            fail("IOException in fetchNewsArticles()")
        }
        return extractFeatureFromJson(jsonResponse)
    }

    private fun fail(message: String): Nothing {
        throw Exception(message)
    }

    private fun createUrl(stringUrl: String): URL {
        val url = try {URL(stringUrl)} catch(e: MalformedURLException){fail("MalformedURLException in createUrl()")}
        return url
    }

    private fun makeHttpRequest(url: URL): String {
        lateinit var jsonResponse: String
        lateinit var urlConnection: HttpURLConnection
        lateinit var inputStream: InputStream
        try {
            //try to connect
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            if (urlConnection.responseCode == 200) {
                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e)
        } finally {
            urlConnection?.disconnect()
            inputStream?.close()
        }
        return jsonResponse;
    }

    private fun readFromStream(inputStream: InputStream?):String{
        var output = StringBuilder()
        var inputStreamReader:InputStreamReader? = InputStreamReader(inputStream,Charset.forName("UTF-8"))
        var reader:BufferedReader? = BufferedReader(inputStreamReader)
        var line:String? = reader?.readLine()
        while(line!=null) {
            output.append(line)
            line = reader?.readLine()
        }

        return output.toString()
    }


    private fun extractFeatureFromJson(jsonResponse: String): List<NewsArticle>?{
            var newsArticles: MutableList<NewsArticle> = ArrayList<NewsArticle>()
            if (TextUtils.isEmpty(jsonResponse)) return null

            try {
                var baseJsonResponse = JSONObject(jsonResponse)
                var baseObject = baseJsonResponse.getJSONObject("response")
                var resultsArray = baseObject.getJSONArray("results")


                var articleSection: String?
                var articleName: String?
                var articleUrl: String?

                for (i in 0..(resultsArray.length() - 1)) {
                    var articleObject:JSONObject?= resultsArray.getJSONObject(i)
                    articleSection = articleObject?.getString("sectionName")
                    articleName = articleObject?.getString("webTitle")
                    articleUrl = articleObject?.getString("webUrl")
                    var foundArticle = NewsArticle(articleSection, articleName, articleUrl)
                    newsArticles.add(foundArticle)
                }


                return newsArticles;

            } catch (e: JSONException) {
                Log.e(LOG_TAG, "Problem parsing JSON results", e)
            }
            return null
        }
    }


