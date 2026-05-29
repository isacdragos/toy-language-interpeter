package com.example.maptest2;

import controller.Controller;
import model.expresion.*;
import model.prgState.PrgState;
import model.prgState.symTable.MyDictionary;
import model.statement.*;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.type.BoolType;
import model.value.type.IntType;
import model.value.type.RefType;
import model.value.type.StringType;
import repository.IRepository;
import repository.Repository;
import view.ExitCommand;
import view.RunExample;
import view.TextMenu;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Toy Language Interpreter ===");

        boolean displayFlag = true;

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
                        new AssignStmt("varf", new ValueExp(new StringValue("src/test.in"))), // varf = "test.in"
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
        IStmt ex12WithZeros = new CompStmt(
                ex12, // the original program
                new CompStmt(
                        new PrintStmt(new ValueExp(new IntValue(0))),
                        new CompStmt(
                                new PrintStmt(new ValueExp(new IntValue(0))),
                                new CompStmt(
                                        new PrintStmt(new ValueExp(new IntValue(0))),
                                        new CompStmt(
                                                new PrintStmt(new ValueExp(new IntValue(0))),
                                                new PrintStmt(new ValueExp(new IntValue(0)))
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
        PrgState prg1 = new PrgState(ex1);
        IRepository repo1 = new Repository(prg1, "log1.txt");
        Controller ctr1 = new Controller(repo1, displayFlag);

        PrgState prg2 = new PrgState(ex2);
        IRepository repo2 = new Repository(prg2, "log2.txt");
        Controller ctr2 = new Controller(repo2, displayFlag);

        PrgState prg3 = new PrgState(ex3);
        IRepository repo3 = new Repository(prg3, "log3.txt");
        Controller ctr3 = new Controller(repo3, displayFlag);

        PrgState prg4 = new PrgState(ex4);
        IRepository repo4 = new Repository(prg4, "log4.txt");
        Controller ctr4 = new Controller(repo4, displayFlag);

        PrgState prg5 = new PrgState(ex5);
        IRepository repo5 = new Repository(prg5, "log5.txt");
        Controller ctr5 = new Controller(repo5, displayFlag);

        PrgState prg6 = new PrgState(ex6);
        IRepository repo6 = new Repository(prg6, "log6.txt");
        Controller ctr6 = new Controller(repo6, displayFlag);

        PrgState prg7 = new PrgState(ex7);
        IRepository repo7 = new Repository(prg7, "log7.txt");
        Controller ctr7 = new Controller(repo7, displayFlag);

        PrgState prg8 = new PrgState(ex8);
        IRepository repo8 = new Repository(prg8, "log8.txt");
        Controller ctr8 = new Controller(repo8, displayFlag);

        PrgState prg9 = new PrgState(ex9);
        IRepository repo9 = new Repository(prg9, "log9.txt");
        Controller ctr9 = new Controller(repo9, displayFlag);

        PrgState prg10 = new PrgState(ex10);
        IRepository repo10 = new Repository(prg10, "log10.txt");
        Controller ctr10 = new Controller(repo10, displayFlag);

        PrgState prg11 = new PrgState(ex11);
        IRepository repo11 = new Repository(prg11, "log11.txt");
        Controller ctr11 = new Controller(repo11, displayFlag);

        PrgState prg12 = new PrgState(ex12WithZeros);
        IRepository repo12 = new Repository(prg12, "log12.txt");
        Controller ctr12 = new Controller(repo12, displayFlag);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), ctr1));
        menu.addCommand(new RunExample("2", ex2.toString(), ctr2));
        menu.addCommand(new RunExample("3", ex3.toString(), ctr3));
        menu.addCommand(new RunExample("4", ex4.toString(), ctr4));
        menu.addCommand(new RunExample("5", ex5.toString(), ctr5));
        menu.addCommand(new RunExample("6", ex6.toString(), ctr6));
        menu.addCommand(new RunExample("7", ex7.toString(), ctr7));
        menu.addCommand(new RunExample("8", ex8.toString(), ctr8));
        menu.addCommand(new RunExample("9", ex9.toString(), ctr9));
        menu.addCommand(new RunExample("10", ex10.toString(), ctr10));
        menu.addCommand(new RunExample("11", ex11.toString(), ctr11));
        menu.addCommand(new RunExample("12", ex12.toString(), ctr12));

        menu.show();

        System.out.println("Interpreter closed.");
    }
}