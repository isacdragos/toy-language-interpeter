package view;

import controller.Controller;
import model.expresion.ArithExp;
import model.expresion.ValueExp;
import model.expresion.VarExp;
import model.prgState.PrgState;
import model.statement.*;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.type.BoolType;
import model.value.type.IntType;
import model.value.type.StringType;
import repository.IRepository;
import repository.Repository;

import java.util.Scanner;

public class Console {
    private boolean displayFlag;
    private Scanner sc;

    public Console(boolean displayFlag) {
        this.displayFlag = displayFlag;
        sc = new Scanner(System.in);
    }

    public void Start() throws StmtException {
        while (true) {
            System.out.println("-Toy Language Interpreter-");
            System.out.println("Program 1: int v; v=2;Print(v)");
            System.out.println("Program 2: int a; int b; a=2+3*5; b=a+1; Print(b)");
            System.out.println("Program 3: bool a; int v; a=true; (IF(a)THEN(v=2)ELSE(v=3)); Print(v)");
            System.out.println("Program 4: int x; x = true; print(x)");
            System.out.println("0. Exit");

            String choice = sc.nextLine();
            if (choice.equals("0")) {
                System.out.println("Exiting...");
                break;
            }

            IStmt selectedProgram;

            switch (choice) {
                case "1":
                    // Program 1: int v; (v=2;Print(v))
                    selectedProgram = new CompStmt(
                            new VarDeclStmt("v", new IntType()),
                            new CompStmt(
                                    new AssignStmt("v", new ValueExp(new IntValue(2))),
                                    new PrintStmt(new VarExp("v"))
                            )
                    );
                    break;
                case "2":
                    // Program 2: int a; int b; a=2+3*5; b=a+1; Print(b)
                    selectedProgram = new CompStmt(
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
                    break;
                case "3":
                    selectedProgram = new CompStmt(
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
                    break;
                case "4":
                    selectedProgram = new CompStmt(
                            new VarDeclStmt("x", new IntType()),
                            new CompStmt(
                                    new AssignStmt("x", new ValueExp(new BoolValue(true))), //wrong type
                                    new PrintStmt(new VarExp("x"))
                            )
                    );
                    break;
                case "5":
                    selectedProgram = new CompStmt(
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
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }
            PrgState initialState = new PrgState(selectedProgram);
            IRepository repo = new Repository(initialState, "log0.txt");
            Controller ctrl = new Controller(repo, displayFlag);

            System.out.println("===Executing===");
            try {
                ctrl.allStep();
            } catch (StmtException | InterruptedException e) {
                System.out.println("Execution error: " + e.getMessage());
            }
            System.out.println("===Finished===\n");
        }
    }
}
