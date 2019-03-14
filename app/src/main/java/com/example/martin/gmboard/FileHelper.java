package com.example.martin.gmboard;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

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
    private final static String PC_LIST = "pcliststorage.json";
    private final static String UNIT = "unitstorage.json";
    private final static String PC = "playerstorage.json";
    private final static String MAP = "mapstorage.json";
    private final static String BOT_PLAYLIST = "botstorage.json";
    private final static String TOP_PLAYLIST = "topstorage.json";

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
            fileName = PC_LIST;
        else
            fileName = LIST;
        String response = readJsonFile(context, fileName);

        Log.d("halp", "oldname "+oldUnitList.getName());
        Log.d("halp", "new name "+newUnitList.getName());

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
            fileName = PC_LIST;
        else
            fileName = LIST;


        response = readJsonFile(context, fileName);

        unitLists = gson.fromJson(response, listType);


        unitLists.add(newUnitList);

        json = gson.toJson(unitLists, listType);
        writeJsonFile(context, fileName, json);
    }

    private static void addUnitToPCList(Context context, Unit unit) throws IOException {
        String response = readJsonFile(context, PC_LIST);
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
        writeJsonFile(context, PC_LIST, json);

    }

    private static void removeUnitFromPCList(Context context, Unit unit) throws IOException {
        String response = readJsonFile(context, PC_LIST);
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
        writeJsonFile(context, PC_LIST, json);

    }

    static List<Map> getAllMaps(Context context){
        String fileName = MAP;

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

    static void saveMap(@NonNull Context context, Map map) throws IOException {
        String response = readJsonFile(context, MAP);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map>>(){}.getType();
        ArrayList<Map> maps = gson.fromJson(response, listType);

        maps.add(map);

        String json = gson.toJson(maps, listType);
        writeJsonFile(context, MAP, json);
    }
    static void updateMap(@NonNull Context context, Map map) throws IOException {
        Log.d("passage","Enter Update Map");
        String response = readJsonFile(context, MAP);

        Gson gson = new Gson();

        Type listType = new TypeToken<List<Map>>(){}.getType();
        ArrayList<Map> maps = gson.fromJson(response, listType);

        for(Map m : maps){
            if (m.getName().equals(map.getName())){
                maps.remove(m);
            }
        }
        maps.add(map);

        String json = gson.toJson(maps, listType);
//        Log.d("jsonperso",json);
        writeJsonFile(context, MAP, json);
    }

    /******************************************
     *              METHODS ON SONGS          *
     *****************************************/
    static List<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File sf : files){
            if(sf.isDirectory() && !sf.isHidden()){
                al.addAll(findSongs(sf));
            } else {
                if(sf.getName().endsWith(".mp3")){
                    al.add(sf);
                }
            }
        }
        return al;
    }

    static void savePlaylist(Context context, int which, SongInfo s) throws IOException {

        String fileName;
        if(which == SongAdapter.RV_TOP)
            fileName = TOP_PLAYLIST;
        else
            fileName = BOT_PLAYLIST;

        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<SongInfo>>(){}.getType();
        ArrayList<SongInfo> si = gson.fromJson(response, listType);
        si.add(s);

        String json = gson.toJson(si, listType);
        writeJsonFile(context, fileName, json);
    }

    static List<SongInfo> getPlaylist(Context context, int which){

        String fileName;
        if(which == SongAdapter.RV_TOP)
            fileName = TOP_PLAYLIST;
        else
            fileName = BOT_PLAYLIST;


        String response = null;
        try {
            response = readJsonFile(context, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<SongInfo>>(){}.getType();
        ArrayList<SongInfo> si = gson.fromJson(response, listType);

        return si;
    }
    static void removeSong (Context context, int which, SongInfo song) throws IOException {
        String fileName;
        if(which == SongAdapter.RV_TOP)
            fileName = TOP_PLAYLIST;
        else
            fileName = BOT_PLAYLIST;

        String response = readJsonFile(context, fileName);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<SongInfo>>(){}.getType();
        ArrayList<SongInfo> si = gson.fromJson(response, listType);

        SongInfo toRemove = null;
        for(SongInfo s : si){
            if(s.equals(song)){
                toRemove = s;
            }
        }

        si.remove(toRemove);

        String json = gson.toJson(si, listType);
        writeJsonFile(context, fileName, json);
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

    static void wipeJsonFile(@NonNull Context context) throws IOException {
        File file = new File(context.getFilesDir(), LIST);

        FileWriter fileWriter;
        BufferedWriter bufferedWriter;

                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("[]");
                bufferedWriter.close();

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
    private static void writeMapJson(@NonNull Context context,ArrayList<Map> maps) throws IOException {
        String json = "[";
        for(Map map : maps) {
            json += "{'dX':" + map.dX + ",'dY':" + map.dY + ",'pinType':" + map.pinType + ",'xmap':" + map.xmap + ",'ymap':" + map.ymap + ",'wmap':" + map.wmap + ",'hmap':" + map.hmap + ",'note':'" + map.note + "','name':'" + map.name + "','image':" + map.image + ",";


            map.pointJson = "'circlePoints':[";
            for (Point p : map.circlePoints) {
                map.pointJson += ("{'x':" + String.valueOf(p.x) + ",'y':" + String.valueOf(p.y) + "},");
            }
            map.pointJson = map.pointJson.substring(0, map.pointJson.length() - 1);
            map.pointJson += "],";

            map.pinJson = "'listPins':[";
            for (Pin p : map.listPins) {
                map.pinJson += ("{'start':" + String.valueOf(p.start) + ",'xCurr':" + String.valueOf(p.xCurr) + ",'yCurr':" + String.valueOf(p.yCurr) + ",'type':" + String.valueOf(p.type) + ",'name':'" + String.valueOf(p.name) + "'},");
            }
            map.pinJson = map.pinJson.substring(0, map.pinJson.length() - 1);
            map.pinJson += "],";
            String typejson = "'listType':[";
            for (int p : map.listType) {
                map.pinJson += (String.valueOf(p) + ',');
            }
            typejson = typejson.substring(0, typejson.length() - 1);
            json += (map.pointJson + map.pinJson + typejson + "]},");
        }
        json = json.substring(0, json.length() - 1);
        json +="]";
        Log.d("json",json);
        File file = new File(context.getFilesDir(), "mapstorage.json");
        FileWriter fileWriter;
        fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(json);
        bw.close();
    }
}
