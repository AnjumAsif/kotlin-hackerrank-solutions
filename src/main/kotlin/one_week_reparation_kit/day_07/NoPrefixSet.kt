package one_week_reparation_kit.day_07

import java.util.*

/*
 * Complete the 'noPrefix' function below.
 *
 * The function accepts STRING_ARRAY words as parameter.
 */


//Version 1: Using Set data structures
fun noPrefix(words: Array<String>): Unit {
    // Write your code here
    val prefixes = mutableSetOf("")
    val prevWords = mutableSetOf("")

    for (word in words) {
        if (prefixes.contains(word)) {
            print("BAD SET")
            print("\n")
            print(word)
            return
        }
        var newPrefix = ""
        for (letter in word) {
            newPrefix += letter
            if (prevWords.contains(newPrefix)) {
                print("BAD SET")
                print("\n")
                print(word)
                return
            }
            prefixes.add(newPrefix)
        }
        prevWords.add(word)
    }
    print("GOOD SET")
}

//Version 2: Using TreeSet data structures
fun noPrefix2(words: Array<String>): Unit {
    val wordsTreeSet = TreeSet<String>()
    for (word in words) {
        val next = wordsTreeSet.ceiling(word);
        val prev = wordsTreeSet.floor(word);

        if ((next != null && next.startsWith(word)) || (prev != null && word.startsWith(prev))) {
            println("BAD SET");
            println(word);
            return;
        }
        wordsTreeSet.add(word);
    }

    println("GOOD SET");
}