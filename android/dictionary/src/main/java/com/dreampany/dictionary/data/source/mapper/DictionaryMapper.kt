package com.dreampany.dictionary.data.source.mapper

import com.dreampany.common.misc.exts.currentMillis
import com.dreampany.common.misc.exts.utc
import com.dreampany.common.misc.exts.uuid
import com.dreampany.common.misc.func.GsonParser
import com.dreampany.dictionary.data.model.*
import com.dreampany.dictionary.data.source.remote.model.*
import com.dreampany.dictionary.data.source.room.dao.*
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 10/11/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class DictionaryMapper
@Inject constructor(
    private val parser: GsonParser,
    private val languageDao: LanguageDao,
    private val sourceDao: SourceDao,
    private val partOfSpeechDao: PartOfSpeechDao,
    private val wordDao: WordDao,
    private val pronunciationDao: PronunciationDao,
    private val definitionDao: DefinitionDao,
    private val exampleDao: ExampleDao,
    private val relationTypeDao: RelationTypeDao,
    private val relationDao: RelationDao
) {
    companion object {
        const val PATTERN_DATE = "yyyy-MM-dd"
    }

    private val languages: MutableMap<String, Language>
    private val sources: MutableMap<String, Source>
    private val partOfSpeechs: MutableMap<String, PartOfSpeech>
    private val words: MutableMap<String, Word>
    private val pronunciations: MutableMap<String, Pronunciation>
    private val definitions: MutableMap<String, Definition>
    private val examples: MutableMap<String, Example>
    private val relationTypes: MutableMap<String, RelationType>
    private val relations: MutableMap<String, Relation>

    init {
        languages = Maps.newConcurrentMap()
        sources = Maps.newConcurrentMap()
        partOfSpeechs = Maps.newConcurrentMap()
        words = Maps.newConcurrentMap()
        pronunciations = Maps.newConcurrentMap()
        definitions = Maps.newConcurrentMap()
        examples = Maps.newConcurrentMap()
        relationTypes = Maps.newConcurrentMap()
        relations = Maps.newConcurrentMap()
    }

    val String.source: Source?
        get() = sources.values.find { it.source == this }

    val String.partOfSpeech: PartOfSpeech?
        get() = partOfSpeechs.values.find { it.partOfSpeech == this }

    val String.relationType: RelationType?
        get() = relationTypes.values.find { it.relationType == this }

    val Pair<Word, Word>.relation: Relation?
        get() = relations.values.find { it.leftWordId == first.id && it.rightWordId == second.id }

    @Synchronized
    suspend fun createLanguage(input: LanguageObject): Language? {
        var output = languages.get(input.id)
        if (output == null) {
            if (languageDao.count(input.id) <= 0) {
                output = Language(
                    id = uuid,
                    code = input.code,
                    name = input.name,
                    createdAt = currentMillis
                )
                languageDao.insertOrReplace(output)
                languages.put(output.id, output)
            } else {
                output = languageDao.read(input.id)
                if (output != null) languages.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createSource(input: String): Source? {
        var output = input.source
        if (output == null) {
            if (sourceDao.countBySource(input) <= 0) {
                output = Source(id = uuid, source = input, createdAt = currentMillis)
                sourceDao.insertOrReplace(output)
                sources.put(output.id, output)
            } else {
                output = sourceDao.readBySource(input)
                if (output != null) sources.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createPartOfSpeech(input: String): PartOfSpeech? {
        var output = input.partOfSpeech
        if (output == null) {
            if (partOfSpeechDao.countByPartOfSpeech(input) <= 0) {
                output = PartOfSpeech(id = uuid, partOfSpeech = input, createdAt = currentMillis)
                partOfSpeechDao.insertOrReplace(output)
                partOfSpeechs.put(output.id, output)
            } else {
                output = partOfSpeechDao.readByPartOfSpeech(input)
                if (output != null) partOfSpeechs.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createWord(input: WordObject): Word? {
        val language = createLanguage(input.language) ?: return null
        var output = words.get(input.id)
        if (output == null) {
            if (wordDao.count(input.id) <= 0) {
                output = Word(
                    id = input.id,
                    languageId = language.id,
                    word = input.word,
                    origin = input.origin,
                    createdAt = currentMillis
                )
                wordDao.insertOrReplace(output)
                words.put(output.id, output)
            } else {
                output = wordDao.read(input.id)
                if (output != null) words.put(output.id, output)
            }
        }
        output?.language = language
        return output
    }

    @Synchronized
    suspend fun createWord(
        input: Map.Entry<String, String>,
        languageId: String
    ): Word? {
        var output = words.get(input.key)
        if (output == null) {
            if (wordDao.count(input.key) <= 0) {
                output = Word(
                    id = input.key,
                    languageId = languageId,
                    word = input.value,
                    createdAt = currentMillis
                )
                wordDao.insertOrReplace(output)
                words.put(output.id, output)
            } else {
                output = wordDao.read(input.key)
                if (output != null) words.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createPronunciations(
        inputs: List<PronunciationObject>,
        word: Word
    ): MutableList<Pronunciation> {
        return inputs.map { createPronunciation(it, word) }.filterNotNull()
            .toMutableList()
    }

    @Synchronized
    suspend fun createPronunciation(input: PronunciationObject, word: Word): Pronunciation? {
        val source = createSource(input.source) ?: return null
        var output = pronunciations.get(input.id)
        if (output == null) {
            if (pronunciationDao.count(input.id) <= 0) {
                output = Pronunciation(
                    id = input.id,
                    sourceId = source.id,
                    wordId = word.id,
                    pronunciation = input.pronunciation,
                    url = input.url,
                    createdAt = currentMillis
                )
                pronunciationDao.insertOrReplace(output)
                pronunciations.put(output.id, output)
            } else {
                output = pronunciationDao.read(input.id)
                if (output != null) pronunciations.put(output.id, output)
            }
        }
        output?.source = source
        return output
    }

    @Synchronized
    suspend fun createDefinitions(
        inputs: List<DefinitionObject>,
        word: Word
    ): MutableList<Definition> {
        return inputs.map { createDefinition(it, word) }
            .filterNotNull()
            .toMutableList()
    }

    @Synchronized
    suspend fun createDefinition(input: DefinitionObject, word: Word): Definition? {
        val source = createSource(input.source) ?: return null
        val partOfSpeech = createPartOfSpeech(input.partOfSpeech) ?: return null
        var output = definitions.get(input.id)
        if (output == null) {
            if (definitionDao.count(input.id) <= 0) {
                output = Definition(
                    id = input.id,
                    sourceId = source.id,
                    partOfSpeechId = partOfSpeech.id,
                    wordId = word.id,
                    definition = input.definition,
                    createdAt = currentMillis
                )
                definitionDao.insertOrReplace(output)
                definitions.put(output.id, output)
            } else {
                output = definitionDao.read(input.id)
                if (output != null) definitions.put(output.id, output)
            }
        }
        output?.source = source
        output?.partOfSpeech = partOfSpeech
        input.examples?.let { output?.examples = createExamples(it, word, output) }
        return output
    }

    @Synchronized
    suspend fun createExamples(
        inputs: MutableList<ExampleObject>,
        word: Word,
        definition: Definition?
    ): MutableList<Example> {
        return inputs.map { createExample(it, word, definition) }.filterNotNull()
            .toMutableList()
    }

    @Synchronized
    suspend fun createExample(
        input: ExampleObject,
        word: Word,
        definition: Definition?
    ): Example? {
        var output = examples.get(input.id)
        if (output == null) {
            if (exampleDao.count(input.id) <= 0) {
                output = Example(
                    id = input.id,
                    wordId = word.id,
                    definitionId = definition?.id,
                    author = input.author,
                    title = input.title,
                    example = input.example,
                    url = input.url,
                    year = input.year?.utc(PATTERN_DATE) ?: 0L,
                    createdAt = currentMillis
                )
                exampleDao.insertOrReplace(output)
                examples.put(output.id, output)
            } else {
                output = exampleDao.read(input.id)
                if (output != null) examples.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createRelationType(input: String): RelationType? {
        var output = input.relationType
        if (output == null) {
            if (relationTypeDao.countByRelationType(input) <= 0) {
                output = RelationType(id = uuid, relationType = input, createdAt = currentMillis)
                relationTypeDao.insertOrReplace(output)
                relationTypes.put(output.id, output)
            } else {
                output = relationTypeDao.readByRelationType(input)
                if (output != null) relationTypes.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createRelations(
        inputs: MutableMap<String, MutableMap<String, String>>,
        word: Word
    ): MutableMap<RelationType, MutableList<Relation>> {
        val outputs = mutableMapOf<RelationType, MutableList<Relation>>()
        inputs.forEach { entry ->
            val relationType = createRelationType(entry.key)
            if (relationType != null) {
                val output = createRelations(entry.value, relationType, word)
                outputs.put(relationType, output)
            }
        }
        return outputs
    }

    @Synchronized
    suspend fun createRelations(
        inputs: MutableMap<String, String>,
        relationType: RelationType,
        word: Word
    ): MutableList<Relation> {
        return inputs.map { createRelation(it, relationType, word) }.filterNotNull()
            .toMutableList()
    }

    @Synchronized
    suspend fun createRelation(
        input: Map.Entry<String, String>,
        relationType: RelationType,
        word: Word
    ): Relation? {
        val relationWord = createWord(input, word.languageId) ?: return null
        val leftWord = if (word.id.compareTo(relationWord.id) <= 0) word else relationWord
        val rightWord = if (word.id.compareTo(relationWord.id) <= 0) relationWord else word
        var output = Pair(leftWord, rightWord).relation
        if (output == null) {
            if (relationDao.count(leftWord.id, rightWord.id) <= 0) {
                output = Relation(
                    id = uuid,
                    relationTypeId = relationType.id,
                    leftWordId = leftWord.id,
                    rightWordId = rightWord.id,
                    createdAt = currentMillis
                )
                relationDao.insertOrReplace(output)
                relations.put(output.id, output)
            } else {
                output = relationDao.read(leftWord.id, rightWord.id)
                if (output != null) relations.put(output.id, output)
            }
        }
        output?.relationType = relationType.relationType
        output?.leftWord = leftWord.word
        output?.rightWord = rightWord.word
        return output
    }
}