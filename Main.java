/*
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
*/
public class Main {
public static void main(String[] args) {
    String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};
    int n= (int) (Math.random()* 1_000_000);
    int result= n*3;
    result += 0b10101;
    result += 0xFF;
    result *= 6;
    int s;
    do {
        s=0;
        int aux=result;
        while (aux!=0) {
            s += aux%10;
            aux /= 10;
        }
        result=s;
    } while (s>=10);
    System.out.println("Willy-nilly, this language is " + languages[result]);
    }
}