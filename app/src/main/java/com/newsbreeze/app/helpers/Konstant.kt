package com.newsbreeze.app.helpers

object Konstant {

    init {
        System.loadLibrary("native-lib")
    }

    //constant keys for passing data to another activity
    const val KEY_SOURCE_NAME ="SOURCE_NAME"
    const val KEY_AUTHOR ="AUTHOR"
    const val KEY_TITLE = "TITLE"
    const val KEY_DESCRIPTION = "DESCRIPTION"
    const val KEY_IMAGE_URL = "IMAGE_URL"
    const val KEY_CONTENT_URL = "CONTENT_URL"
    const val KEY_CONTENT = "CONTENT"
    const val KEY_DATE ="DATE"


    //To avoid sql injection use it
    fun escapingString(str: String): String {
        return str.replace("'", "''")
    }

    external fun getAPIKey(): String //Dead end right? go to the cpp folder.
    external fun getRootURL(): String //Dead end right? go to the cpp folder.
}