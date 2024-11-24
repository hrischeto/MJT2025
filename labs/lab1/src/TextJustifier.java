public class TextJustifier {

    private int getWordsForRow(String[] words, int maxWidth){
        int accumulatedLength=0;
        int wordsCount=0;

       for(String word:words)
       {
           if(accumulatedLength+word.length()<maxWidth-wordsCount) {
               accumulatedLength += word.length();
               wordsCount++;
           }

           else if(accumulatedLength+word.length()==maxWidth-wordsCount)
               return ++wordsCount;

           else return wordsCount;

       }

       return -1;
    }

    public static String[] justifyText(String[] words, int maxWidth){
        String[] text=new String[words.length];

        StringBuilder row=new StringBuilder();

        int lastUsedIndex=0;

        for(String word:words)
        {
            lastUsedIndex+=getWordsForRow(words, maxWidth)
            //check how many words we can fit and get their accumulated length
            //see how much space is left
            //divide the spaces
            //construct the row(strbuilder) and add it to the array(str)
        }
        return text;
    }

    public static void main(String[] args) {

    }
}
//stringbuilder