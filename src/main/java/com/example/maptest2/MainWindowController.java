package com.example.maptest2;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.prgState.PrgState;
import model.prgState.fileTable.IMyFileTable;
import model.prgState.heap.IMyHeap;
import model.prgState.out.IMyList;
import model.statement.IStmt;
import model.value.StringValue;
import model.value.Value;
import repository.IRepository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class MainWindowController {
    private IRepository repository;
    private Controller controller;

    @FXML
    private TextField prgStatesField;

    @FXML
    private Button runOneStepButton;

    // heap
    @FXML
    private TableView<HeapEntry> heapTable;
    @FXML
    private TableColumn<HeapEntry, Integer> heapAddressColumn;
    @FXML
    private TableColumn<HeapEntry, String> heapValueColumn;

    private ObservableList<HeapEntry> heapData = FXCollections.observableArrayList();

    //out
    @FXML
    private ListView<String> outListView;

    // filetable
    @FXML
    private ListView<String> fileTableListView;

    //prgstates
    @FXML
    private ListView<Integer> prgStateListView;

    //symtable
    @FXML
    private TableView<SymTableEntry> symTableView;
    @FXML
    private TableColumn<SymTableEntry, String> symVarColumn;
    @FXML
    private TableColumn<SymTableEntry, String> symValueColumn;

    //exestack
    @FXML
    private ListView<String> exeStateListView;

    public void setController(Controller controller) {
        this.controller = controller;
        this.repository = controller.getRepository();
    }
    @FXML
    private void handleRunOneStep() throws InterruptedException {
        //try {
        if (controller.getExecutor() == null) {
            controller.setExecutor(Executors.newFixedThreadPool(2));
        }
        List<PrgState> prgList = controller.removeCompletedPrg(repository.getPrgList());

            if (prgList.isEmpty()) {
                runOneStepButton.setDisable(true);
                controller.getExecutor().shutdownNow();
                return;
            }

            List<Integer> allReachableAddresses = prgList.stream()
                    .flatMap(prg -> controller.getReachableAddresses(
                            prg.getSymTable().getContent().values(),
                            prg.getHeap().getContent()
                    ).stream())
                    .distinct()
                    .collect(Collectors.toList());

            if (!prgList.isEmpty()) {
                IMyHeap sharedHeap = prgList.getFirst().getHeap();
                sharedHeap.setContent(controller.safeGarbageCollector(allReachableAddresses, sharedHeap.getContent()));
            }
            controller.oneStepForAllPrg(prgList);

            refreshAll();

//        }catch (Exception ex){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Error");
//            alert.setContentText(ex.getMessage());
//            alert.showAndWait();
//            System.out.println(ex.getMessage());
//        }
    }

    private void refreshAll() {
        List<PrgState> prgList = repository.getPrgList();

        updatePrgStates(prgList.size());
        updateHeapTable(prgList.getFirst().getHeap());
        updateOutListView(prgList.getFirst().getOut());
        updateFileTableList(prgList.getFirst().getFileTable());
        updatePrgStateIds(prgList);
        updateFileTable(prgList.getFirst().getFileTable());

        Integer selectedId = prgStateListView.getSelectionModel().getSelectedItem();

        if (selectedId != null) {
            prgList.stream().filter(p -> p.getId().equals(selectedId)).findFirst().ifPresent(p -> {
                updateSymTable(p);
                updateExeStack(p);
            });
        }


    }
    public void updatePrgStates(int count) {
        prgStatesField.setText(String.valueOf(count));
    }

    @FXML
    public void initialize() {
        // bind columns to heap entry properties
        heapAddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty().asObject());
        heapValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        symValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        symVarColumn.setCellValueFactory(cellData -> cellData.getValue().varNameProperty());

        heapTable.setItems(heapData);

        prgStateListView.getSelectionModel().selectedItemProperty().
                addListener((observable, oldId, newId) -> {
                    if (newId != null) {
                        PrgState prg = repository.getPrgList().stream()
                                .filter(prgState -> prgState.getId().equals(newId)).findFirst().orElse(null);
                        if (prg != null) {
                            updateExeStack(prg);
                            updateSymTable(prg);
                        }
                    }
        });

    }
    @FXML
    public void updateFileTable(IMyFileTable fileTable) {
        fileTableListView.setItems(
                FXCollections.observableArrayList(
                        fileTable.getContent().keySet().stream().map(Value::toString).toList()
                )
        );
    }

    @FXML
    public void updateHeapTable(IMyHeap heapContent) {
        heapData.clear();
        heapContent.getContent().forEach((key, value) -> {heapData.add(new HeapEntry(key, value));});
    }

    @FXML
    public void updateOutListView(IMyList<Value> out) {
        outListView.setItems(
                FXCollections.observableArrayList(
                        out.getList().stream()
                                .map(Value::toString)
                                .toList()
                )
        );

    }

    @FXML
    public void updateFileTableList(IMyFileTable fileTable) {
        fileTableListView.setItems(
                FXCollections.observableArrayList(
                        fileTable.getContent().keySet().stream().map(StringValue::getValue).toList()
                )
        );
    }
    @FXML
    public void updatePrgStateIds(List<PrgState> prgStates) {
        prgStateListView.setItems(
                FXCollections.observableArrayList(
                        prgStates.stream().map(PrgState::getId).toList()
                )
        );
    }

    @FXML
    public void updateSymTable(PrgState prg) {
        if (prg != null) {
            symTableView.setItems(FXCollections.observableArrayList(
                    prg.getSymTable().getContent().entrySet().stream()
                            .map(e -> new SymTableEntry(e.getKey(), e.getValue())).toList()
            ));
        }
    }

    public void updateExeStack(PrgState prg) {
        List<String> stack = prg.getExeStack().getReversed().stream().map(IStmt::toString).toList();
        exeStateListView.setItems(FXCollections.observableArrayList(stack));
    }
}
