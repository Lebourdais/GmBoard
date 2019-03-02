package com.example.martin.gmboard;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    // Prevents from instantiating the class
    private FileHelper(){}

    // Saves a unit to the unitstorage file
    public static void saveUnit(@NonNull Context context, Unit unit) throws IOException, JSONException {
        String fileName = "unitstorage.json";
        File file = new File(context.getFilesDir(), fileName);
        FileWriter fileWriter = null;

        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);
        units.add(unit);
        String json = gson.toJson(units, listType);

        fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(json);
        bw.close();
    }

    // Removes a unit from the storageunit file
    public static void removeUnit(@NonNull Context context, Unit unit) throws IOException, JSONException {
        String fileName = "unitstorage.json";
        File file = new File(context.getFilesDir(), fileName);
        FileWriter fileWriter = null;

        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);
        units.remove(unit);
        String json = gson.toJson(units, listType);

        fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(json);
        bw.close();
    }

    /* Saves a unit to the unitstorage file
     * @param Context context
     * @param Unit newUnit : the modified unit
     */
    public static void modifyUnit(Context context, Unit newUnit) throws IOException, JSONException {
        String fileName = "unitstorage.json";
        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);
        for(Unit oldUnit : units ){
            if(oldUnit.getName().equals(newUnit.getName())){
                units.remove(oldUnit);
            }
        }
        units.add(newUnit);

        String json = gson.toJson(units, listType);
        writeJsonFile(context, fileName, json);
    }

    // Saves a List of unit to the liststorage file
    public static void saveUnitList(Context context, UnitList unitList) throws IOException, JSONException {
        String fileName = "liststorage.json";
        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);

        unitLists.add(unitList);

        String json = gson.toJson(unitLists, listType);
        writeJsonFile(context, fileName, json);
    }

    // Deletes a Unitlist from the liststorage file
    public static void removeUnitList(Context context, UnitList unitList) throws IOException, JSONException {
        String fileName = "liststorage.json";
        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);
        unitLists.remove(unitList);

        String json = gson.toJson(unitLists, listType);
        writeJsonFile(context, fileName, json);
    }

    /* Deletes a unit from a unit list
     * @param Context context
     * @param UnitList unitList : the UnitList object still containing the unit to be removed
     * Unit unit : The unit to be removed
     */
    public static void removeUnitFromUnitList(Context context, UnitList unitList, Unit unit) throws IOException, JSONException {
        String fileName = "liststorage.json";
        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);
        unitLists.get(unitLists.indexOf(unitList)).removeUnit(unit);

        String json = gson.toJson(unitLists, listType);
        writeJsonFile(context, fileName, json);
    }

    //Reads a Json file (Formatted as a JsonArray) [{...},{...}])
    //Returns the equivalent string
    private static String readJsonFile(@NonNull Context context, String fileName) throws IOException {
        File file = new File(context.getFilesDir(), fileName);

        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        String response = null;

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

        String line = "";

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
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(json);
        bw.close();
    }
}
