package repository;

import model.prgState.PrgState;
import model.statement.IStmt;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Repository implements IRepository {
    List<PrgState> prgStates;
    private String logFilePath = "prgStatesLog.txt";

    public Repository(PrgState initialState, String logFilePath) {
            prgStates = new ArrayList<>();
            prgStates.add(initialState);
            this.logFilePath = logFilePath;
    }

//    @Override
//    public PrgState getCrtPrg() {
//        if (prgStates.isEmpty()) {
//            return null;
//        }
//        return prgStates.getFirst();
//    }

    @Override
    public void logPrgStateExec(PrgState prgState) throws RepoException {

        try(PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println("=== Program State ===");

            logFile.println("Id: " + prgState.getId());
            logFile.println("");

            logFile.println("ExeStack:");
            for (IStmt stmt : prgState.getExeStack().getReversed()) {
                for (String line : stmt.toInfixList()) {
                    logFile.println(line);
                    //logFile.println("a");
                }
            }
            logFile.println("");
            logFile.println("SymTable:");
            for (Map.Entry<String, Value> entry : prgState.getSymTable().getContent().entrySet()) {
                logFile.println(entry.getKey() + " --> " + entry.getValue().toString());
            }

            logFile.println("Out:");
            for (Value v : prgState.getOut().getList()) {
                logFile.println(v.toString());
            }
            logFile.println("");

            logFile.println("HeapTable:");
            for (Map.Entry<Integer, Value> entry : prgState.getHeap().getContent().entrySet()) {
                logFile.println(entry.getKey() + " --> " + entry.getValue().toString());
            }
            logFile.println("");

            logFile.println("FileTable:");
            for (StringValue fname : prgState.getFileTable().getContent().keySet()) {
                logFile.println(fname.getValue());
            }

            logFile.println("==========\n");
        }
        catch (IOException e) {
            throw new RepoException("Error logging program state: " + e.getMessage());
        }
    }

    @Override
    public List<PrgState> getPrgList() {
        return prgStates;
    }

    @Override
    public void setPrgList(List<PrgState> prgList) {
        this.prgStates = prgList;
    }
}
