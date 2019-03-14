package com.example.martin.gmboard;

public interface UnitListCreationListener {

    void swapButtons(boolean which);

    void enableEdition();

    boolean getEdit();

    void setOldUnitList(UnitList u);


}
