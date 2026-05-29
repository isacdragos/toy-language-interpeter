package model.statement;

import model.expresion.Exp;
import model.prgState.PrgState;
import model.prgState.symTable.IMyDictionary;
import model.value.IntValue;
import model.value.StringValue;
import model.value.Value;
import model.value.type.IntType;
import model.value.type.StringType;
import model.value.type.Type;

import java.io.BufferedReader;

public class ReadFile implements IStmt{
    private Exp exp;
    private String varName;

    public ReadFile(Exp exp, String varName){
        this.exp = exp;
        this.varName = varName;
    }
    @Override
    public PrgState execute(PrgState prgState) throws StmtException{
        //System.out.println(varName);
        if (!prgState.getSymTable().isDefined(varName)){
            throw new StmtException("readFile: Variable "+varName+" is not defined");
        }

        Value var = prgState.getSymTable().lookup(varName);
        if (!var.getType().equals(new IntType())) {
            throw new StmtException("readFile: Variable "+varName+" is not of type Int");
        }

        Value vExp = exp.eval(prgState.getSymTable(), prgState.getHeap());
        if (!vExp.getType().equals(new StringType())) {
            throw new StmtException("readFile: expression is not of type String");
        }

        BufferedReader br = prgState.getFileTable().lookup((StringValue) vExp);
        if (br == null){
            throw new StmtException("readFile: file not found");
        }

        try{
            String line = br.readLine();;
            int number = (line == null) ? 0 : (Integer.parseInt(line));
            prgState.getSymTable().put(varName, new IntValue(number));
        }
        catch(Exception e){
            throw new StmtException("readFile: error reading: " + e.getMessage());
        }
        return null;
    }
    @Override
    public String toString(){
        return "ReadFile(" +exp.toString()+")";
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFile(exp.deepCopy(), varName);
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        if (!typeEnv.isDefined(varName)){
            throw new StmtException("readFile: Variable "+varName+" is not defined");
        }
        Type varType = typeEnv.lookup(varName);
        if  (!(varType instanceof IntType)){
            throw new StmtException("readFile: type is not of type Int");
        }

        Type expType = exp.typecheck(typeEnv);
        if (!(expType instanceof StringType)){
            throw new StmtException("readFile: type is not of type String");
        }

        return typeEnv;



    }
}
