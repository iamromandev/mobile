package com.dreampany.word.data.source.mapper

import com.dreampany.common.misc.exts.currentMillis
import com.dreampany.common.misc.exts.utc
import com.dreampany.common.misc.exts.uuid
import com.dreampany.common.misc.func.GsonParser
import com.dreampany.word.data.model.*
import com.dreampany.word.data.source.remote.model.*
import com.dreampany.word.data.source.room.dao.*
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
    private val parser: GsonParser
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
    suspend fun createLanguage(input: LanguageObject, dao: LanguageDao): Language? {
        var output = languages.get(input.id)
        if (output == null) {
            if (dao.count(input.id) <= 0) {
                output = Language(
                    id = uuid,
                    code = input.code,
                    name = input.name,
                    createdAt = currentMillis
                )
                dao.insertOrReplace(output)
                languages.put(output.id, output)
            } else {
                output = dao.read(input.id)
                if (output != null) languages.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createSource(input: String, dao: SourceDao): Source? {
        var output = input.source
        if (output == null) {
            if (dao.countBySource(input) <= 0) {
                output = Source(id = uuid, source = input, createdAt = currentMillis)
                dao.insertOrReplace(output)
                sources.put(output.id, output)
            } else {
                output = dao.readBySource(input)
                if (output != null) sources.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createPartOfSpeech(input: String, dao: PartOfSpeechDao): PartOfSpeech? {
        var output = input.partOfSpeech
        if (output == null) {
            if (dao.countByPartOfSpeech(input) <= 0) {
                output = PartOfSpeech(id = uuid, partOfSpeech = input, createdAt = currentMillis)
                dao.insertOrReplace(output)
                partOfSpeechs.put(output.id, output)
            } else {
                output = dao.readByPartOfSpeech(input)
                if (output != null) partOfSpeechs.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createWord(input: WordObject, dao: WordDao, languageDao: LanguageDao): Word? {
        val language = createLanguage(input.language, languageDao) ?: return null
        var output = words.get(input.id)
        if (output == null) {
            if (dao.count(input.id) <= 0) {
                output = Word(
                    id = input.id,
                    languageId = language.id,
                    word = input.word,
                    origin = input.origin,
                    createdAt = currentMillis
                )
                dao.insertOrReplace(output)
                words.put(output.id, output)
            } else {
                output = dao.read(input.id)
                if (output != null) words.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createWord(
        input: Map.Entry<String, String>,
        languageId: String,
        dao: WordDao
    ): Word? {
        var output = words.get(input.key)
        if (output == null) {
            if (dao.count(input.key) <= 0) {
                output = Word(
                    id = input.key,
                    languageId = languageId,
                    word = input.value,
                    createdAt = currentMillis
                )
                dao.insertOrReplace(output)
                words.put(output.id, output)
            } else {
                output = dao.read(input.key)
                if (output != null) words.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createPronunciations(
        inputs: List<PronunciationObject>,
        word: Word,
        dao: PronunciationDao,
        sourceDao: SourceDao
    ): MutableList<Pronunciation> {
        return inputs.map { createPronunciation(it, word, dao, sourceDao) }.filterNotNull()
            .toMutableList()
    }

    @Synchronized
    suspend fun createPronunciation(
        input: PronunciationObject,
        word: Word,
        dao: PronunciationDao,
        sourceDao: SourceDao
    ): Pronunciation? {
        val source = createSource(input.source, sourceDao) ?: return null
        var output = pronunciations.get(input.id)
        if (output == null) {
            if (dao.count(input.id) <= 0) {
                output = Pronunciation(
                    id = input.id,
                    sourceId = source.id,
                    wordId = word.id,
                    pronunciation = input.pronunciation,
                    url = input.url,
                    createdAt = currentMillis
                )
                dao.insertOrReplace(output)
                pronunciations.put(output.id, output)
            } else {
                output = dao.read(input.id)
                if (output != null) pronunciations.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createDefinitions(
        inputs: List<DefinitionObject>,
        word: Word,
        dao: DefinitionDao,
        sourceDao: SourceDao,
        partOfSpeechDao: PartOfSpeechDao
    ): MutableList<Definition> {
        return inputs.map { createDefinition(it, word, dao, sourceDao, partOfSpeechDao) }
            .filterNotNull()
            .toMutableList()
    }

    @Synchronized
    suspend fun createDefinition(
        input: DefinitionObject,
        word: Word,
        dao: DefinitionDao,
        sourceDao: SourceDao,
        partOfSpeechDao: PartOfSpeechDao
    ): Definition? {
        val source = createSource(input.source, sourceDao) ?: return null
        val partOfSpeech = createPartOfSpeech(input.partOfSpeech, partOfSpeechDao) ?: return null
        var output = definitions.get(input.id)
        if (output == null) {
            if (dao.count(input.id) <= 0) {
                output = Definition(
                    id = input.id,
                    sourceId = source.id,
                    partOfSpeechId = partOfSpeech.id,
                    wordId = word.id,
                    definition = input.definition,
                    createdAt = currentMillis
                )
                dao.insertOrReplace(output)
                definitions.put(output.id, output)
            } else {
                output = dao.read(input.id)
                if (output != null) definitions.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createExamples(
        inputs: List<ExampleObject>,
        word: Word,
        definition: Definition?,
        dao: ExampleDao
    ): MutableList<Example> {
        return inputs.map { createExample(it, word, definition, dao) }.filterNotNull()
            .toMutableList()
    }

    @Synchronized
    suspend fun createExample(
        input: ExampleObject,
        word: Word,
        definition: Definition?,
        dao: ExampleDao
    ): Example? {
        var output = examples.get(input.id)
        if (output == null) {
            if (dao.count(input.id) <= 0) {
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
                dao.insertOrReplace(output)
                examples.put(output.id, output)
            } else {
                output = dao.read(input.id)
                if (output != null) examples.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createRelationType(input: String, dao: RelationTypeDao): RelationType? {
        var output = input.relationType
        if (output == null) {
            if (dao.countByRelationType(input) <= 0) {
                output = RelationType(id = uuid, relationType = input, createdAt = currentMillis)
                dao.insertOrReplace(output)
                relationTypes.put(output.id, output)
            } else {
                output = dao.readByRelationType(input)
                if (output != null) relationTypes.put(output.id, output)
            }
        }
        return output
    }

    @Synchronized
    suspend fun createRelations(
        inputs: MutableMap<String, MutableMap<String, String>>,
        word: Word,
        dao: RelationDao,
        relationTypeDao: RelationTypeDao,
        wordDao: WordDao
    ): MutableList<Relation> {
        val relations = arrayListOf<Relation>()
        inputs.forEach {
            val output = createRelations(it.key, it.value, word, dao, relationTypeDao, wordDao)
            if (output != null) relations.addAll(output)
        }
        return relations
    }

    @Synchronized
    suspend fun createRelations(
        inputRelationType: String,
        inputs: MutableMap<String, String>,
        word: Word,
        dao: RelationDao,
        relationTypeDao: RelationTypeDao,
        wordDao: WordDao
    ): MutableList<Relation>? {
        val relationType = createRelationType(inputRelationType, relationTypeDao) ?: return null
        return inputs.map { createRelation(it, relationType, word, dao, wordDao) }.filterNotNull().toMutableList()
    }

    @Synchronized
    suspend fun createRelation(
        input: Map.Entry<String, String>,
        relationType: RelationType,
        word: Word,
        dao: RelationDao,
        wordDao: WordDao
    ): Relation? {
        val relationWord = createWord(input, word.languageId, wordDao) ?: return null
        val leftWord = if (word.id.compareTo(relationWord.id) <= 0) word else relationWord
        val rightWord = if (word.id.compareTo(relationWord.id) <= 0) relationWord else word
        var output = Pair(leftWord, rightWord).relation
        if (output == null) {
            if (dao.count(leftWord.id, rightWord.id) <= 0) {
                output = Relation(
                    id = uuid,
                    relationTypeId = relationType.id,
                    leftWordId = leftWord.id,
                    rightWordId = rightWord.id,
                    createdAt = currentMillis
                )
                dao.insertOrReplace(output)
                relations.put(output.id, output)
            } else {
                output = dao.read(leftWord.id, rightWord.id)
                if (output != null) relations.put(output.id, output)
            }
        }
        output?.relationType = relationType
        output?.leftWord = leftWord
        output?.rightWord = rightWord
        return output
    }
}