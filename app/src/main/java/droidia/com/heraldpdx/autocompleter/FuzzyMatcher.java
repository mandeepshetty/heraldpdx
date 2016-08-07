package droidia.com.heraldpdx.autocompleter;

/**
 * Created by mandeep on 8/3/16.
 */

/*
 * Ported directly from https://github.com/forrestthewoods/lib_fts
 * Some pretty neat work by forrestthewoods.
 */
public class FuzzyMatcher {

    private FuzzyMatcher() {}
    static boolean isPatternSequentiallyPresentInString(String pattern, String string) {

        char[] patternChars = pattern.toLowerCase().toCharArray();
        char[] stringChars = string.toLowerCase().toCharArray();

        int p = 0;
        int s = 0;

        while (p != pattern.length() && s != string.length()) {

            if (patternChars[p] == stringChars[s])
                p++;
            s++;
        }
        return p == pattern.length();
    }

    static int fuzzyMatch(String pattern, String str) {

        int adjacency_bonus = 5;              // bonus for adjacent matches
        int number_of_adjacent_matches = 1;
        int separator_bonus = 10;             // bonus if match occurs after a separator
        int camel_bonus = 10;                 // bonus if match is uppercase and prev is lower

        int leading_letter_penalty = -3;      // penalty applied for every letter in str before the first match
        int max_leading_letter_penalty = -9;  // maximum penalty for leading letters
        int unmatched_letter_penalty = -1;    // penalty for every letter that doesn't matter


        // Loop variables
        int score = 0;
        char[] patternArr = pattern.toCharArray();
        int patternMarker = 0;

        char[] stringArr = str.toCharArray();
        int stringMarker = 0;

        boolean prevMatched = false;
        boolean prevLower = false;
        boolean prevSeparator = true;         // true so if first letter match gets separator bonus

        char bestLetter = '~';
        int bestLetterMarker = -1;
        int bestLetterScore = 0;

        for (int i = 0; i < stringArr.length; i++) {


            boolean nextMatch = patternMarker != patternArr.length &&
                    Character.toLowerCase(patternArr[patternMarker]) == Character.toLowerCase(stringArr[stringMarker]);
            boolean rematch = bestLetterMarker < 0 &&
                    Character.toLowerCase(bestLetter) == Character.toLowerCase(stringArr[stringMarker]);

            boolean advanced = nextMatch && bestLetter != '~';
            boolean patternRepeat = bestLetter != '~' && patternMarker < pattern.length()
                    && Character.toLowerCase(bestLetter) == Character.toLowerCase(patternArr[patternMarker]);

            if (advanced || patternRepeat)
            {
                score += bestLetterScore;
                bestLetter = '~';
                bestLetterScore = 0;
            }

            if (nextMatch || rematch)
            {
                int newScore = 0;

                // Apply penalty for each letter before the first pattern match
                // Note: std::max because penalties are negative values. So max is smallest penalty.
                if (patternMarker == 0)
                {
                    int penalty = leading_letter_penalty * stringMarker;
                    if (penalty < max_leading_letter_penalty)
                        penalty = max_leading_letter_penalty;

                    score += penalty;
                }

                // Apply increasing bonus for consecutive bonuses.
                if (prevMatched) {
                    newScore += adjacency_bonus * number_of_adjacent_matches;
                    ++number_of_adjacent_matches;
                } else {
                    number_of_adjacent_matches = 5;
                }

                // Apply bonus for matches after a separator
                if (prevSeparator)
                    newScore += separator_bonus;

                // Apply bonus across camel case boundaries
                if (prevLower && Character.isUpperCase(stringArr[stringMarker]))
                    newScore += camel_bonus;

                // Update pattern iter IFF the next pattern letter was matched
                if (nextMatch)
                    ++patternMarker;

                // Update best letter in str which may be for a "next" letter or a rematch
                if (newScore >= bestLetterScore)
                {
                    // Apply penalty for now skipped letter
                    if (bestLetter != '~')
                        score += unmatched_letter_penalty;

                    bestLetter = stringArr[stringMarker];
                    bestLetterScore = newScore;
                }

                prevMatched = true;
            }
            else
            {
                score += unmatched_letter_penalty;
                prevMatched = false;
            }

            // Separators should be more easily defined
            prevLower = Character.isLowerCase(stringArr[stringMarker]);
            prevSeparator = stringArr[stringMarker] == ' ' ;

            ++stringMarker;
        }

        // Apply score for last match
        if (bestLetter != '~')
            score += bestLetterScore;

        // Did not match full pattern
        if (patternMarker != patternArr.length)
            return -10000;

        else return score;

    }

}
