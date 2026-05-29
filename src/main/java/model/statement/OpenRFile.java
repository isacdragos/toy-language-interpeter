package model.statement;

import model.expresion.Exp;
import model.prgState.PrgState;
import model.prgState.fileTable.IMyFileTable;
import model.prgState.symTable.IMyDictionary;
import model.value.StringValue;
import model.value.Value;
import model.value.type.StringType;
import model.value.type.Type;

import java.io.BufferedReader;
import java.io.FileReader;

public class OpenRFile implements IStmt{
    private Exp exp;

    public OpenRFile(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState prgState) throws StmtException{
        Value v = exp.eval(prgState.getSymTable(), prgState.getHeap());
        if (!(v instanceof StringValue)) {
            throw new StmtException("openRFile: expression is not a string");
        }

        StringValue fileNameValue = (StringValue) v;
        IMyFileTable fileTable = prgState.getFileTable();

        if (fileTable.isDefined(fileNameValue)) {
            System.out.println("Error");
            throw new StmtException("openRFile: file already opened");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileNameValue.getValue()));
            fileTable.put(fileNameValue, br);
            System.out.println("openRFile: " + fileNameValue.getValue());
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            throw new StmtException("openRFile: could not open file");
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new OpenRFile(exp.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        Type expType = exp.typecheck(typeEnv);

        if (!expType.equals(new StringType())) {
            throw new StmtException("OpenRFile: Expression must be of type String.");
        }

        return typeEnv;
    }

}
