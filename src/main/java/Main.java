import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class  Main{
    public static AtomicInteger count1 = new AtomicInteger();
    public static AtomicInteger count2 = new AtomicInteger();
    public static AtomicInteger count3 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindrome = new Thread(()->{
                for(String text : texts){
                    if(isPalindrome(text)&& !sameChar(text)) {
                        increasingAtomic(text);
                    }
            }
        });
        palindrome.start();
        Thread theSameChar = new Thread(()->{
            for(String text : texts){
                if(sameChar(text)) increasingAtomic(text);
            }
        });
        theSameChar.start();

        Thread increasing = new Thread(()->{
            for(String text : texts){
                if(!isPalindrome(text)&& increasingOrder(text)) increasingAtomic(text);
            }
        });
        increasing.start();

        theSameChar.join();
        palindrome.join();
        increasing.join();

        System.out.println("Красивых палиндромных слов :" + count1);
        System.out.println("Красивых слов одной и той же буквы :" + count2);
        System.out.println("Красивых слов с возрастающими буквами :" + count3);

    }
    public static void increasingAtomic(String text){
        if (text.length() == 3) count1.getAndIncrement();
        if (text.length() == 4) count2.getAndIncrement();
        if(text.length() == 5) count3.getAndIncrement();
    }
    public static boolean isPalindrome(String text){
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    public static boolean sameChar(String text){
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i-1)) return false;
        }
        return true;
    }

    public static boolean increasingOrder(String text){
        for (int i = 1; i < text.length(); i++) {
            if(text.charAt(i)<text.charAt(i-1)) return false;
        }
        return true;
    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}