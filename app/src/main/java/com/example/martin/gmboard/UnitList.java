package com.example.martin.gmboard;

import java.util.List;

public class UnitList {

    private List<Unit> units;

    public UnitList (List<Unit> pUnits){
        units = pUnits;
    }

    public List<Unit> getUnits(){ return units; }

    //Retourne une unité si elle est dans la liste
    // retourne null sinon
    public Unit getUnitByName(String unitName) {
        for(Unit unit : units){
            if(unit.getName() == unitName)
                return unit;
        }
        return null;
    }

    public boolean addUnit(Unit unit){
        return units.add(unit);
    }

    public boolean removeUnit(Unit unit){
        return units.remove(unit);
    }
}
