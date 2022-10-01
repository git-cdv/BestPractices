package com.chkan.bestpractices.dropdown_list

import android.content.Context
import com.chkan.bestpractices.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import javax.inject.Inject

/**
 * @author Dmytro Chkan on 04.09.2022.
 */
interface Repository {

    fun data(): DropDownList

    class Base @Inject constructor(
        private val cacheDataSource: CacheDataSource,
        private val mapper: DropDownMapper
    ) : Repository {

        override fun data(): DropDownList {
            val data = cacheDataSource.data()
            return mapper.map(data)
        }
    }
}


interface Mapper<R, S> {
    fun map(source: S): R
}

interface CacheDataSource {
    fun data(): List<DropDownRaw>

    class Base @Inject constructor(
        private val readRawResource: ReadRawResource,
        //private val gson: Gson
    ) : CacheDataSource {

        override fun data(): List<DropDownRaw> {
            val string = readRawResource.read(R.raw.data)
            //return gson.fromJson(string, DropDownRaw.Wrapper::class.java).data
            return Json.decodeFromString<Wrapper>(string).data
        }
    }
}

interface ReadRawResource {

    fun read(id: Int): String

    class Base @Inject constructor(
        @ApplicationContext val context: Context
    ) : ReadRawResource {
        override fun read(id: Int) = context.resources.openRawResource(id)
            .bufferedReader().use(BufferedReader::readText)
    }
}