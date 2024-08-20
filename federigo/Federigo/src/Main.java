// take in file as command line argument
// read from file
// tokenize said file
import Classes.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Classes.Parser;

public class Main {
    public static void main(String[] args) {
        String content;
        if (args.length > 0) {
            Path sourceCodePath = Paths.get(args[0]);
            try {
                content = Files.readString(sourceCodePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("You need to add atleast one file to run this program");
        }
        Lexer lexer = new Lexer(content);
        List<TokenType.Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens, 0);

       Node parseTree =  parser.parseProgram();

       parseTree.print("");


    }


}





