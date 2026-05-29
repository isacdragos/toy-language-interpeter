package com.example.maptest2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.expresion.*;
import model.prgState.symTable.MyDictionary;
import model.statement.*;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.type.BoolType;
import model.value.type.IntType;
import model.value.type.RefType;
import model.value.type.StringType;

import java.util.List;

public class MainGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("select-view.fxml"));
        Parent root = fxmlLoader.load();

        ProgramSelectController controller = fxmlLoader.getController();

        boolean displayFlag = false;
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
        IStmt ex2 = new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a",
                                        new ArithExp(1,
                                                new ValueExp(new IntValue(2)),
                                                new ArithExp(3,
                                                        new ValueExp(new IntValue(3)),
                                                        new ValueExp(new IntValue(5))
                                                )
                                        )
                                ),
                                new CompStmt(
                                        new AssignStmt("b",
                                                new ArithExp(1,
                                                        new VarExp("a"),
                                                        new ValueExp(new IntValue(1))
                                                )
                                        ),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                ));
        IStmt ex3 = new CompStmt(
                new VarDeclStmt("a", new BoolType()),
                new CompStmt(
                        new VarDeclStmt("v", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("a"),
                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
                                                new AssignStmt("v", new ValueExp(new IntValue(3)))
                                        ),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );
        IStmt ex4 = new CompStmt(
                new VarDeclStmt("x", new IntType()),
                new CompStmt(
                        new AssignStmt("x", new ValueExp(new BoolValue(true))), //wrong type
                        new PrintStmt(new VarExp("x"))
                )
        );

        IStmt ex5 = new CompStmt(
                new VarDeclStmt("varf", new StringType()), // string varf;
                new CompStmt(
                        new AssignStmt("varf", new ValueExp(new StringValue("src/main/java/test.in"))), // varf = "test.in"
                        new CompStmt(
                                new OpenRFile(new VarExp("varf")), // openRFile(varf)
                                new CompStmt(
                                        new VarDeclStmt("varc", new IntType()), // int varc;
                                        new CompStmt(
                                                new ReadFile(new VarExp("varf"), "varc"), // readFile(varf,varc)
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("varc")), // print(varc)
                                                        new CompStmt(
                                                                new ReadFile(new VarExp("varf"), "varc"), // readFile(varf,varc)
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("varc")), // print(varc)
                                                                        new CloseRFile(new VarExp("varf")) // closeRFile(varf)
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        IStmt ex6 = new CompStmt(
                new VarDeclStmt("x", new IntType()),
                new CompStmt(
                        new AssignStmt("x", new ValueExp(new IntValue(10))),
                        new IfStmt(
                                new RelExp(">", new VarExp("x"), new ValueExp(new IntValue(5))),
                                new PrintStmt(new ValueExp(new IntValue(1))),
                                new PrintStmt(new ValueExp(new IntValue(0)))
                        )
                )
        );

        IStmt ex7 = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("v")),
                                                new PrintStmt(new VarExp("a"))
                                        )
                                )
                        )
                )
        );

        IStmt ex8 = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(
                                                        new ArithExp(
                                                                1,
                                                                new ReadHeapExp(new ReadHeapExp(new VarExp("a"))),
                                                                new ValueExp(new IntValue(5))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        IStmt ex9 = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new CompStmt(
                                        new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(
                                                new ArithExp(
                                                        1,
                                                        new ReadHeapExp(new VarExp("v")),
                                                        new ValueExp(new IntValue(5))
                                                )
                                        )
                                )
                        )
                )
        );

        IStmt ex10 = new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new NewStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(
                                                        new ReadHeapExp(
                                                                new ReadHeapExp(new VarExp("a"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        IStmt ex11 =  new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(
                                new WhileStmt(
                                        new RelExp(">", new VarExp("v"), new ValueExp(new IntValue(0))),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("v")),
                                                new AssignStmt(
                                                        "v",
                                                        new ArithExp(
                                                                2,
                                                                new VarExp("v"),
                                                                new ValueExp(new IntValue(1))
                                                        )
                                                )
                                        )
                                ),
                                new PrintStmt(new VarExp("v"))
                        )
                )
        );
        IStmt ex12 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(
                                        new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new WriteHeapStmt("a", new ValueExp(new IntValue(30))),
                                                                new CompStmt(
                                                                        new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                                        new CompStmt(
                                                                                new PrintStmt(new VarExp("v")),
                                                                                new PrintStmt(new ReadHeapExp(new VarExp("a")))
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(new ReadHeapExp(new VarExp("a")))
                                                )
                                        )
                                )
                        )
                )
        );

        try {
            ex1.typecheck(new MyDictionary<>());
            ex2.typecheck(new MyDictionary<>());
            ex3.typecheck(new MyDictionary<>());
//            ex4.typecheck(new MyDictionary<>());
            ex5.typecheck(new MyDictionary<>());
            ex6.typecheck(new MyDictionary<>());
            ex8.typecheck(new MyDictionary<>());
            ex9.typecheck(new MyDictionary<>());
            ex10.typecheck(new MyDictionary<>());
            ex11.typecheck(new MyDictionary<>());
            ex12.typecheck(new MyDictionary<>());

            System.out.println("typecheck OK");
        } catch (StmtException e) {
            System.out.println("ERROR: " + e.getMessage());
            return;
        }

        controller.setPrograms(
                List.of(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10, ex11, ex12)
        );

        stage.setTitle("Program Selection");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
