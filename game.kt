import kotlinx.coroutines.*
import kotlinx.coroutines.io.writer
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.abs
import java.io.File

val minWordLength: Int = 8
val wordListFile: String = "WordList.txt"
val outWordListFile: String = "Out.txt"

fun main()
{
    runBlocking {
        val wordSet: HashSet<String> = (CoroutineScope(Dispatchers.IO).async {
            val wordList: HashSet<String> = HashSet()
            FileReader(wordListFile).use { fileReader ->
                fileReader.forEachLine { line ->
                    wordList.add(line)
                }
            }
            return@async wordList
        }).await()
        val appropriateWords: List<String> = wordSet.stream().filter { word ->
            return@filter word.length >= minWordLength
        }.collect(Collectors.toList())

        val random = Random()
        val randomWordIndex = abs(random.nextInt()) % appropriateWords.size
        val randomWord: String = appropriateWords.stream().skip(randomWordIndex.toLong()).findFirst().get()

        println(String.format("Вам выпало слово \"%s\". Составляйте из него другие слова.", randomWord))


        val randomWordLetters: HashSet<Char> = randomWord.toHashSet()
        val userWords = ArrayList<String>()
        do {
            val userWord = readLine()?.trim()
            if (userWord != null && userWord.isNotEmpty()) {
                var anyExtraChars = false
                userWord.forEach { char ->
                    if (!randomWordLetters.contains(char)) {
                        anyExtraChars = true
                    }
                }
                if (anyExtraChars) {
                    println("Таких букв нет.")
                }
                else {
                    userWords.add(userWord)
                }
            }
        } while (userWord != null && userWord.isNotEmpty())

        val saveUserWordsTask = CoroutineScope(Dispatchers.IO).launch {
            val newLineEscapeSequence = System.getProperty("line.separator")
            FileWriter(outWordListFile).use { fileWriter ->
                userWords.forEach { userWord ->
                    fileWriter.write(userWord + newLineEscapeSequence)
                }
            }
        }

        val checkResults = CoroutineScope(Dispatchers.Default).launch {
            var score = 0
            userWords.forEach { userWord ->
                if (wordSet.contains(userWord)) {
                    score++
                }

            }
            println(String.format("Вы набрали %d.", score))
        }

        saveUserWordsTask.join()
        checkResults.join()
    }
}




