//package Classes;
//
//import lombok.AllArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.Optional;
//
//@AllArgsConstructor
//public class CodeGeneration {
//
//    Optional<NodeExit> root;
//
//    public String generate() {
//        StringBuilder stringStream = new StringBuilder(".global _start\n");
//        stringStream.append("_start:\n");
//        stringStream.append(String.format("    mov x0, #%s\n",  root.get().exitNode.intLit.value.get()));
//        stringStream.append("    mov x16, #1\n");
//        stringStream.append("    svc #0");
//        return stringStream.toString();
//    }
//}
