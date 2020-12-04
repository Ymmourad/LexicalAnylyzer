import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    final private static String filename = "input.txt";

    public static void main(String[] args) {
        String input = readInput(filename);
        if (input == null)
        {
            System.out.println("Nothing in file or no file to read!");
            return;
        }
        System.out.println("Contents of input.txt: " +"\n" +"\n" + input);

        // Creating a lexer object
        Lexer lexer = new Lexer();
        // Scanning the input, using the lexer object
        lexer.analyze(input);

        // printing all found tokens: index, code, lexeme
        System.out.println("Tokens found: \n");
        ArrayList<Token> tokens = lexer.getTokens();
        for (int i = 0; i < tokens.size(); i++){
            System.out.format("%d: lexeme: %s:  %s\n",
                    i + 1,
                    tokens.get(i).getCode().name(),
                    tokens.get(i).getLexeme());
        }
    }

    /**
     * String builder to read input.txt
     */
    public static String readInput(String filename)
    {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to read from input file!");
            return null;
        }
        return stringBuilder.toString();
    }
}
