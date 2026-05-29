package repository;

import model.prgState.PrgState;

import java.util.List;

public interface IRepository {
    void logPrgStateExec(PrgState prgState) throws RepoException;
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
}
