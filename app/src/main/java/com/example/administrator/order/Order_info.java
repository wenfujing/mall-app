package com.example.administrator.order;

public class Order_info {
    private int ID;
    private String Name;
    private boolean DeleteState;
    private boolean EvaluateState;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isDeleteState() {
        return DeleteState;
    }

    public void setDeleteState(boolean deleteState) {
        DeleteState = deleteState;
    }

    public boolean isEvaluateState() {
        return EvaluateState;
    }

    public void setEvaluateState(boolean evaluateState) {
        EvaluateState = evaluateState;
    }
}
