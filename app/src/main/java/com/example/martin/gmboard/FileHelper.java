package com.example.martin.gmboard;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

class FileHelper {

    private final static String LIST = "liststorage.json";
    private final static String UNIT = "unitstorage.json";
    private final static String PC = "playerstorage.json";

    // Prevents from instantiating the class
    private FileHelper(){}

    /******************************************
     *            METHODS ON UNITS             *
     *****************************************/

    // Saves a unit to the unitstorage file
    static void saveUnit(@NonNull Context context, Unit unit) throws IOException {
        String fileName;
        if(unit.isPC())
            fileName = PC;
        else
            fileName = UNIT;

        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);
        units.add(unit);

        if(unit.isPC()){
            addUnitToPCList(context, unit);
        }
        String json = gson.toJson(units, listType);
        writeJsonFile(context, fileName, json);
    }

    // Removes a unit from the storageunit file
    static void removeUnit(@NonNull Context context, Unit unit) throws IOException {
        String fileName;
        if(unit.isPC())
            fileName = PC;
        else
            fileName = UNIT;

        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);


        Unit toRemove = null;
        for(Unit u : units){
            if(u.getName().equals(unit.getName())){
                toRemove = u;
            }
        }
        units.remove(toRemove);


        String json = gson.toJson(units, listType);
        writeJsonFile(context, fileName, json);
    }

    /* Saves a unit to the unitstorage file
     * @param Context context
     * @param Unit newUnit : the modified unit
     */
    static void modifyUnit(Context context, Unit oldUnit, Unit newUnit) throws IOException {

        // Removing unit from old file
        String fileName;
        if(oldUnit.isPC())
            fileName = PC;
        else
            fileName = UNIT;
        String response = readJsonFile(context, fileName);


        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);

        Unit toRemove = null;
        for(Unit u : units){
            if(u.getName().equals(oldUnit.getName())){
                toRemove = u;
            }
        }
        units.remove(toRemove);
        String json = gson.toJson(units, listType);
        writeJsonFile(context, fileName, json);

        // Adding unit to new file
        if(newUnit.isPC())
            fileName = PC;
        else
            fileName = UNIT;
        response = readJsonFile(context, fileName);

        units = gson.fromJson(response, listType);

        units.add(newUnit);
        json = gson.toJson(units, listType);
        writeJsonFile(context, fileName, json);


    }

    /******************************************
     *           METHODS ON UNITLISTS         *
     *****************************************/

    static List<Unit> getAllUnits(Context context, boolean pc){

        String fileName;
        if(pc)
            fileName = PC;
        else
            fileName = UNIT;

        String response = null;
        try {
            response = readJsonFile(context, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);

        return sortUnits(units);
    }

    static Unit getUnitByName(Context context, String name){
        String response = null;
        try {
            response = readJsonFile(context, UNIT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);
        assert units != null;
        for(Unit unit : units){
            if(unit.getName().equals(name))
                return unit;
        }
        return null;
    }

    // Saves a List of unit to the liststorage file
    static void saveUnitList(Context context, UnitList unitList) throws IOException {
        String response = readJsonFile(context, LIST);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);

        unitLists.add(unitList);

        String json = gson.toJson(unitLists, listType);
        writeJsonFile(context, LIST, json);
    }

    // Deletes a Unitlist from the liststorage file
    static void removeUnitList(Context context, UnitList unitList) throws IOException {
        String response = readJsonFile(context, LIST);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);


        UnitList toRemove = null;
        for(UnitList u : unitLists){
            if(u.getName().equals(unitList.getName())){
                toRemove = u;
            }
        }
        unitLists.remove(toRemove);


        String json = gson.toJson(unitLists, listType);
        writeJsonFile(context, LIST, json);
    }

    static List<UnitList> getAllUnitLists(Context context){
        String response = null;
        try {
            response = readJsonFile(context, LIST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);

        // Not returning the list of PCs
        int toRemove = -1;
        for(UnitList u : unitLists){
            if(u.isPC())
                toRemove = unitLists.indexOf(u);
        }

        if(toRemove > -1)
            unitLists.remove(toRemove);

        return sortUnitLists(unitLists);
    }

    public static List<Unit> getUnitsFromList(Context context, UnitList unitList) {

        List<Unit> units = getAllUnits(context, unitList.isPC());
        Hashtable<String, Integer> unitsInList = unitList.getUnits();
        List<Unit> response = new ArrayList<Unit>();
        for (Unit u : units) {
            String name = u.getName();
            if (unitsInList.containsKey(name))
                for (int i = 0; i < unitsInList.get(name); i++)
                    response.add(u);
        }
        return response;
    }

    static void modifyUnitList(Context context, UnitList oldUnitList, UnitList newUnitList) throws IOException {
        // Removing unit from old file
        String fileName;
        if(oldUnitList.isPC())
            fileName = PC;
        else
            fileName = UNIT;
        String response = readJsonFile(context, fileName);


        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);

        UnitList toRemove = null;
        for(UnitList u : unitLists){
            if(u.getName().equals(oldUnitList.getName())){
                toRemove = u;
            }
        }
        unitLists.remove(toRemove);
        String json = gson.toJson(unitLists, listType);
        writeJsonFile(context, fileName, json);

        if(newUnitList.isPC())
            fileName = PC;
        else
            fileName = UNIT;


        response = readJsonFile(context, fileName);

        unitLists = gson.fromJson(response, listType);


        unitLists.add(newUnitList);

        json = gson.toJson(unitLists, listType);
        writeJsonFile(context, fileName, json);
    }

    private static void addUnitToPCList(Context context, Unit unit) throws IOException {
        String response = readJsonFile(context, LIST);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);

        UnitList toAddTo = new UnitList(true);
        for(UnitList u : unitLists){
            if(u.isPC()){
                toAddTo = u;
            }
        }
        toAddTo.addUnit(unit);

        String json = gson.toJson(unitLists, listType);
        writeJsonFile(context, LIST, json);

    }
    public static List<Map> getAllMap(Context context){
        String fileName = "mapstorage.json";

        String response = null;
        try {
            response = readJsonFile(context, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map>>(){}.getType();
        ArrayList<Map> mapLists = gson.fromJson(response, listType);

        return mapLists;
    }
    public static void saveMap(@NonNull Context context, Map map) throws IOException {

        String fileName = "mapstorage.json";
        File file = new File(context.getFilesDir(), fileName);
        FileWriter fileWriter = null;

        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map>>(){}.getType();
        ArrayList<Map> mapLists = gson.fromJson(response, listType);

        mapLists.add(map);
        String json = gson.toJson(mapLists, listType);
        fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(json);
        bw.close();
    }

    /******************************************
     *              PRIVATE METHODS           *
     *****************************************/

    //Reads a Json file (Formatted as a JsonArray) [{...},{...}])
    //Returns the equivalent string
    static String readJsonFile(@NonNull Context context, String fileName) throws IOException {
        File file = new File(context.getFilesDir(), fileName);

        FileReader fileReader = null;
        FileWriter fileWriter;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;

        String response;

        if(!file.exists()) {
            try {
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("[]");
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        StringBuffer output = new StringBuffer();
        try {
            fileReader = new FileReader(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bufferedReader = new BufferedReader(fileReader);

        String line;

        while((line  = bufferedReader.readLine()) != null) {
            output.append(line + "\n");
        }

        response = output.toString();
        bufferedReader.close();
        return response;

    }

    //Writes a Json File
    // The existence of the file is not tested as this method should always be called after readJsonFile who creates the file
    private static void writeJsonFile(@NonNull Context context, String fileName, String json) throws IOException {
        File file = new File(context.getFilesDir(), fileName);
        FileWriter fileWriter;
        fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(json);
        bw.close();
    }

    // Sorts the List of Units by name
    private static List<Unit> sortUnits(List<Unit> units){
        Collections.sort( units, new Comparator<Unit>() {
            @Override
            public int compare(Unit a, Unit b) {
                String valA = a.getName();
                String valB = b.getName();

                return valA.compareTo(valB);
            }
        });

        return units;
    }

    // Sorts the List of UnitList by name
    private static List<UnitList> sortUnitLists(List<UnitList> unitLists){
        Collections.sort( unitLists, new Comparator<UnitList>() {
            @Override
            public int compare(UnitList a, UnitList b) {
                String valA = a.getName();
                String valB = b.getName();

                return valA.compareTo(valB);
            }
        });

        return unitLists;
    }
}
