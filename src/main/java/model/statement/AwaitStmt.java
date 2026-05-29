package model.statement;

import javafx.util.Pair;
import model.prgState.PrgState;
import model.prgState.barrierTable.IMyBarrierTable;
import model.prgState.symTable.IMyDictionary;
import model.value.IntValue;
import model.value.Value;
import model.value.type.IntType;
import model.value.type.Type;

import java.util.List;

public class AwaitStmt implements IStmt{
    private String var;
    public AwaitStmt(String var){
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState prgState) throws StmtException {
        IMyDictionary<String, Value> symTable = prgState.getSymTable();
        IMyBarrierTable barrierTable = prgState.getBarrierTable();

        if (!symTable.isDefined(var)){
            throw new StmtException("Variable "+var+" is not defined");
        }

        if (!symTable.lookup(var).getType().equals(new IntType())){
            throw new StmtException("Variable "+var+" is not a number");
        }

        int index = ((IntValue) symTable.lookup(var)).getValue();

        if (!barrierTable.isDefined(index)){
            throw new StmtException("Variable "+var+" is not a number");
        }
        Pair<Integer, List<Integer>> entry = barrierTable.lookup(index);
        int N1 = entry.getKey();
        List<Integer> List1 = entry.getValue();

        int currId = prgState.getId();
        int NL = List1.size();

        if (N1 > NL) {
            if (!List1.contains(currId)){
                List1.add(currId);
            }

            barrierTable.update(index, new Pair<>(N1, List1));

            prgState.getExeStack().push(this);
        }
        return null;
    }

    @Override
    public String toString(){
        return "await(" + var + ")";
    }

    @Override
    public IStmt deepCopy(){
        return new AwaitStmt(var);
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        if (!typeEnv.isDefined(var)){
            throw new StmtException("Variable "+var+" is not defined");
        }
        if (!typeEnv.lookup(var).equals(new IntType())){
            throw new StmtException("Variable "+var+" is not a number");
        }
        return typeEnv;
    }
}
